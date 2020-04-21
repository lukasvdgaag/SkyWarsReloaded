package net.minidev.json.parser;

import net.minidev.json.parser.ContainerFactory;
import net.minidev.json.parser.ContentHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

abstract class JSONParserBase {
    protected char c;
    public static final byte EOI = 26;
    protected static final char MAX_STOP = '~';
    protected static boolean[] stopAll = new boolean[126];
    protected static boolean[] stopArray = new boolean[126];
    protected static boolean[] stopKey = new boolean[126];
    protected static boolean[] stopValue = new boolean[126];
    protected static boolean[] stopX = new boolean[126];
    protected ContainerFactory containerFactory;
    protected ContentHandler handler;
    protected final JSONParserBase.MSB sb = new JSONParserBase.MSB(15);
    protected Object xo;
    protected String xs;
    protected int pos;
    protected final boolean acceptLeadinZero;
    protected final boolean acceptNaN;
    protected final boolean acceptNonQuote;
    protected final boolean acceptSimpleQuote;
    protected final boolean acceptUselessComma;
    protected final boolean checkTaillingData;
    protected final boolean ignoreControlChar;
    protected final boolean useHiPrecisionFloat;
    protected final boolean useIntegerStorage;

    public JSONParserBase(int permissiveMode) {
        this.acceptNaN = (permissiveMode & 4) > 0;
        this.acceptNonQuote = (permissiveMode & 2) > 0;
        this.acceptSimpleQuote = (permissiveMode & 1) > 0;
        this.ignoreControlChar = (permissiveMode & 8) > 0;
        this.useIntegerStorage = (permissiveMode & 16) > 0;
        this.acceptLeadinZero = (permissiveMode & 32) > 0;
        this.acceptUselessComma = (permissiveMode & 64) > 0;
        this.useHiPrecisionFloat = (permissiveMode & 128) > 0;
        this.checkTaillingData = (permissiveMode & 256) == 0;
    }

    public void checkControleChar() throws ParseException {
        if (!this.ignoreControlChar) {
            int l = this.xs.length();

            for(int i = 0; i < l; ++i) {
                char c = this.xs.charAt(i);
                if (c >= 0) {
                    if (c <= 31) {
                        throw new ParseException(this.pos + i, 0, c);
                    }

                    if (c == 127) {
                        throw new ParseException(this.pos + i, 0, c);
                    }
                }
            }

        }
    }

    public void checkLeadinZero() throws ParseException {
        int len = this.xs.length();
        if (len != 1) {
            if (len == 2) {
                if (this.xs.equals("00")) {
                    throw new ParseException(this.pos, 6, this.xs);
                }
            } else {
                char c1 = this.xs.charAt(0);
                char c2 = this.xs.charAt(1);
                if (c1 == '-') {
                    char c3 = this.xs.charAt(2);
                    if (c2 == '0' && c3 >= '0' && c3 <= '9') {
                        throw new ParseException(this.pos, 6, this.xs);
                    }
                } else if (c1 == '0' && c2 >= '0' && c2 <= '9') {
                    throw new ParseException(this.pos, 6, this.xs);
                }
            }
        }
    }

    protected Number extractFloat() throws ParseException {
        if (!this.acceptLeadinZero) {
            this.checkLeadinZero();
        }

        if (!this.useHiPrecisionFloat) {
            return Float.parseFloat(this.xs);
        } else {
            return (Number)(this.xs.length() > 18 ? new BigDecimal(this.xs) : Double.parseDouble(this.xs));
        }
    }

    public Object parse(ContainerFactory containerFactory, ContentHandler handler) throws ParseException {
        this.containerFactory = containerFactory;
        this.handler = handler;
        this.pos = -1;

        Object result;
        try {
            this.read();
            handler.startJSON();
            result = this.readMain(stopX);
            handler.endJSON();
            if (this.checkTaillingData) {
                this.skipSpace();
                if (this.c != 26) {
                    throw new ParseException(this.pos - 1, 1, this.c);
                }
            }
        } catch (IOException var5) {
            throw new ParseException(this.pos, var5);
        }

        this.xs = null;
        this.xo = null;
        return result;
    }

    protected Number parseNumber(String s) throws ParseException {
        int p = 0;
        int l = s.length();
        int max = 19;
        boolean neg;
        if (s.charAt(0) == '-') {
            ++p;
            ++max;
            neg = true;
            if (!this.acceptLeadinZero && l >= 3 && s.charAt(1) == '0') {
                throw new ParseException(this.pos, 6, s);
            }
        } else {
            neg = false;
            if (!this.acceptLeadinZero && l >= 2 && s.charAt(0) == '0') {
                throw new ParseException(this.pos, 6, s);
            }
        }

        boolean mustCheck;
        if (l < max) {
            max = l;
            mustCheck = false;
        } else {
            if (l > max) {
                return new BigInteger(s, 10);
            }

            max = l - 1;
            mustCheck = true;
        }

        long r;
        for(r = 0L; p < max; r = r * 10L + (long)(48 - s.charAt(p++))) {
        }

        if (mustCheck) {
            boolean isBig;
            if (r > -922337203685477580L) {
                isBig = false;
            } else if (r < -922337203685477580L) {
                isBig = true;
            } else if (neg) {
                isBig = s.charAt(p) > '8';
            } else {
                isBig = s.charAt(p) > '7';
            }

            if (isBig) {
                return new BigInteger(s, 10);
            }

            r = r * 10L + (long)(48 - s.charAt(p));
        }

        if (neg) {
            return (Number)(this.useIntegerStorage && r >= -2147483648L ? (int)r : r);
        } else {
            r = -r;
            return (Number)(this.useIntegerStorage && r <= 2147483647L ? (int)r : r);
        }
    }

    protected abstract void read() throws IOException;

    protected List<Object> readArray() throws ParseException, IOException {
        List<Object> obj = this.containerFactory.createArrayContainer();
        if (this.c != '[') {
            throw new RuntimeException("Internal Error");
        } else {
            this.read();
            boolean needData = false;
            this.handler.startArray();

            while(true) {
                switch(this.c) {
                    case '\t':
                    case '\n':
                    case '\r':
                    case ' ':
                        this.read();
                        break;
                    case '\u001a':
                        throw new ParseException(this.pos - 1, 3, "EOF");
                    case ',':
                        if (needData && !this.acceptUselessComma) {
                            throw new ParseException(this.pos, 0, this.c);
                        }

                        this.read();
                        needData = true;
                        break;
                    case ':':
                    case '}':
                        throw new ParseException(this.pos, 0, this.c);
                    case ']':
                        if (needData && !this.acceptUselessComma) {
                            throw new ParseException(this.pos, 0, this.c);
                        }

                        this.read();
                        this.handler.endArray();
                        return obj;
                    default:
                        obj.add(this.readMain(stopArray));
                        needData = false;
                }
            }
        }
    }

    protected Object readMain(boolean[] stop) throws ParseException, IOException {
        while(true) {
            switch(this.c) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    this.read();
                    break;
                case '\u000b':
                case '\f':
                case '\u000e':
                case '\u000f':
                case '\u0010':
                case '\u0011':
                case '\u0012':
                case '\u0013':
                case '\u0014':
                case '\u0015':
                case '\u0016':
                case '\u0017':
                case '\u0018':
                case '\u0019':
                case '\u001a':
                case '\u001b':
                case '\u001c':
                case '\u001d':
                case '\u001e':
                case '\u001f':
                case '!':
                case '#':
                case '$':
                case '%':
                case '&':
                case '(':
                case ')':
                case '*':
                case '+':
                case ',':
                case '.':
                case '/':
                case ';':
                case '<':
                case '=':
                case '>':
                case '?':
                case '@':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case '\\':
                case '^':
                case '_':
                case '`':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                case '|':
                default:
                    this.readNQString(stop);
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }

                    this.handler.primitive(this.xs);
                    return this.xs;
                case '"':
                case '\'':
                    this.readString();
                    this.handler.primitive(this.xs);
                    return this.xs;
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    this.xo = this.readNumber(stop);
                    this.handler.primitive(this.xo);
                    return this.xo;
                case ':':
                case ']':
                case '}':
                    throw new ParseException(this.pos, 0, this.c);
                case 'N':
                    this.readNQString(stop);
                    if (!this.acceptNaN) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }

                    if ("NaN".equals(this.xs)) {
                        this.handler.primitive(0.0F / 0.0);
                        return 0.0F / 0.0;
                    }

                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }

                    this.handler.primitive(this.xs);
                    return this.xs;
                case '[':
                    return this.readArray();
                case 'f':
                    this.readNQString(stop);
                    if ("false".equals(this.xs)) {
                        this.handler.primitive(Boolean.FALSE);
                        return Boolean.FALSE;
                    }

                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }

                    this.handler.primitive(this.xs);
                    return this.xs;
                case 'n':
                    this.readNQString(stop);
                    if ("null".equals(this.xs)) {
                        this.handler.primitive((Object)null);
                        return null;
                    }

                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }

                    this.handler.primitive(this.xs);
                    return this.xs;
                case 't':
                    this.readNQString(stop);
                    if ("true".equals(this.xs)) {
                        this.handler.primitive(Boolean.TRUE);
                        return Boolean.TRUE;
                    }

                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }

                    this.handler.primitive(this.xs);
                    return this.xs;
                case '{':
                    return this.readObject();
            }
        }
    }

    protected abstract void readNoEnd() throws ParseException, IOException;

    protected abstract void readNQString(boolean[] var1) throws IOException;

    protected abstract Object readNumber(boolean[] var1) throws ParseException, IOException;

    protected Map<String, Object> readObject() throws ParseException, IOException {
        Map<String, Object> obj = this.containerFactory.createObjectContainer();
        if (this.c != '{') {
            throw new RuntimeException("Internal Error");
        } else {
            this.handler.startObject();
            boolean needData = false;
            boolean acceptData = true;

            while(true) {
                this.read();
                switch(this.c) {
                    case '\t':
                    case '\n':
                    case '\r':
                    case ' ':
                        break;
                    case '"':
                    case '\'':
                    default:
                        int keyStart = this.pos;
                        if (this.c != '"' && this.c != '\'') {
                            this.readNQString(stopKey);
                            if (!this.acceptNonQuote) {
                                throw new ParseException(this.pos, 1, this.xs);
                            }
                        } else {
                            this.readString();
                        }

                        String key = this.xs;
                        if (!acceptData) {
                            throw new ParseException(this.pos, 1, key);
                        }

                        this.handler.startObjectEntry(key);

                        while(this.c != ':' && this.c != 26) {
                            this.read();
                        }

                        if (this.c == 26) {
                            throw new ParseException(this.pos - 1, 3, (Object)null);
                        }

                        this.readNoEnd();
                        Object duplicate = obj.put(key, this.readMain(stopValue));
                        if (duplicate != null) {
                            throw new ParseException(keyStart, 5, key);
                        }

                        this.handler.endObjectEntry();
                        if (this.c == '}') {
                            this.read();
                            this.handler.endObject();
                            return obj;
                        }

                        if (this.c == 26) {
                            throw new ParseException(this.pos - 1, 3, (Object)null);
                        }

                        if (this.c == ',') {
                            needData = true;
                            acceptData = true;
                        } else {
                            needData = false;
                            acceptData = false;
                        }
                        break;
                    case ',':
                        if (needData && !this.acceptUselessComma) {
                            throw new ParseException(this.pos, 0, this.c);
                        }

                        needData = true;
                        acceptData = true;
                        break;
                    case ':':
                    case '[':
                    case ']':
                    case '{':
                        throw new ParseException(this.pos, 0, this.c);
                    case '}':
                        if (needData && !this.acceptUselessComma) {
                            throw new ParseException(this.pos, 0, this.c);
                        }

                        this.read();
                        this.handler.endObject();
                        return obj;
                }
            }
        }
    }

    abstract void readS() throws IOException;

    protected abstract void readString() throws ParseException, IOException;

    protected void readString2() throws ParseException, IOException {
        char sep = this.c;

        while(true) {
            this.read();
            switch(this.c) {
                case '\u0000':
                case '\u0001':
                case '\u0002':
                case '\u0003':
                case '\u0004':
                case '\u0005':
                case '\u0006':
                case '\u0007':
                case '\b':
                case '\t':
                case '\n':
                case '\u000b':
                case '\f':
                case '\r':
                case '\u000e':
                case '\u000f':
                case '\u0010':
                case '\u0011':
                case '\u0012':
                case '\u0013':
                case '\u0014':
                case '\u0015':
                case '\u0016':
                case '\u0017':
                case '\u0018':
                case '\u0019':
                case '\u001b':
                case '\u001c':
                case '\u001d':
                case '\u001e':
                case '\u001f':
                case '\u007f':
                    if (!this.ignoreControlChar) {
                        throw new ParseException(this.pos, 0, this.c);
                    }
                    break;
                case '\u001a':
                    throw new ParseException(this.pos - 1, 3, (Object)null);
                case ' ':
                case '!':
                case '#':
                case '$':
                case '%':
                case '&':
                case '(':
                case ')':
                case '*':
                case '+':
                case ',':
                case '-':
                case '.':
                case '/':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case ':':
                case ';':
                case '<':
                case '=':
                case '>':
                case '?':
                case '@':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case '[':
                case ']':
                case '^':
                case '_':
                case '`':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                case '{':
                case '|':
                case '}':
                case '~':
                default:
                    this.sb.append(this.c);
                    break;
                case '"':
                case '\'':
                    if (sep == this.c) {
                        this.read();
                        this.xs = this.sb.toString();
                        return;
                    }

                    this.sb.append(this.c);
                    break;
                case '\\':
                    this.read();
                    switch(this.c) {
                        case '"':
                            this.sb.append('"');
                            break;
                        case '\'':
                            this.sb.append('\'');
                            break;
                        case '/':
                            this.sb.append('/');
                            break;
                        case '\\':
                            this.sb.append('\\');
                            break;
                        case 'b':
                            this.sb.append('\b');
                            break;
                        case 'f':
                            this.sb.append('\f');
                            break;
                        case 'n':
                            this.sb.append('\n');
                            break;
                        case 'r':
                            this.sb.append('\r');
                            break;
                        case 't':
                            this.sb.append('\t');
                            break;
                        case 'u':
                            this.sb.append(this.readUnicode());
                    }
            }
        }
    }

    protected char readUnicode() throws ParseException, IOException {
        int value = 0;

        for(int i = 0; i < 4; ++i) {
            value *= 16;
            this.read();
            if (this.c <= '9' && this.c >= '0') {
                value += this.c - 48;
            } else if (this.c <= 'F' && this.c >= 'A') {
                value += this.c - 65 + 10;
            } else {
                if (this.c < 'a' || this.c > 'f') {
                    if (this.c == 26) {
                        throw new ParseException(this.pos, 3, "EOF");
                    } else {
                        throw new ParseException(this.pos, 4, this.c);
                    }
                }

                value += this.c - 97 + 10;
            }
        }

        return (char)value;
    }

    protected void skipDigits() throws IOException {
        while(this.c >= '0' && this.c <= '9') {
            this.readS();
        }

    }

    protected void skipNQString(boolean[] stop) throws IOException {
        while(this.c != 26 && (this.c < 0 || this.c >= '~' || !stop[this.c])) {
            this.readS();
        }

    }

    protected void skipSpace() throws IOException {
        while(this.c <= ' ' && this.c != 26) {
            this.readS();
        }

    }

    static {
        stopKey[58] = stopKey[26] = true;
        stopValue[44] = stopValue[125] = stopValue[26] = true;
        stopArray[44] = stopArray[93] = stopArray[26] = true;
        stopX[26] = true;
        stopAll[44] = stopAll[58] = true;
        stopAll[93] = stopAll[125] = stopAll[26] = true;
    }

    public static class MSB {
        char[] b;
        int p;

        public MSB(int size) {
            this.b = new char[size];
            this.p = -1;
        }

        public void append(char c) {
            ++this.p;
            if (this.b.length <= this.p) {
                char[] t = new char[this.b.length * 2 + 1];
                System.arraycopy(this.b, 0, t, 0, this.b.length);
                this.b = t;
            }

            this.b[this.p] = c;
        }

        public void append(int c) {
            ++this.p;
            if (this.b.length <= this.p) {
                char[] t = new char[this.b.length * 2 + 1];
                System.arraycopy(this.b, 0, t, 0, this.b.length);
                this.b = t;
            }

            this.b[this.p] = (char)c;
        }

        public String toString() {
            return new String(this.b, 0, this.p + 1);
        }

        public void clear() {
            this.p = -1;
        }
    }
}