package net.minidev.json.parser;

import java.io.IOException;

abstract class JSONParserMemory extends JSONParserBase {
    protected int len;

    public JSONParserMemory(int permissiveMode) {
        super(permissiveMode);
    }

    protected void readNQString(boolean[] stop) throws IOException {
        int start = this.pos;
        this.skipNQString(stop);
        this.extractStringTrim(start, this.pos);
    }

    protected Object readNumber(boolean[] stop) throws ParseException, IOException {
        int start = this.pos;
        this.read();
        this.skipDigits();
        if (this.c != '.' && this.c != 'E' && this.c != 'e') {
            this.skipSpace();
            if (this.c >= 0 && this.c < '~' && !stop[this.c] && this.c != 26) {
                this.skipNQString(stop);
                this.extractStringTrim(start, this.pos);
                if (!this.acceptNonQuote) {
                    throw new ParseException(this.pos, 1, this.xs);
                } else {
                    return this.xs;
                }
            } else {
                this.extractStringTrim(start, this.pos);
                return this.parseNumber(this.xs);
            }
        } else {
            if (this.c == '.') {
                this.read();
                this.skipDigits();
            }

            if (this.c != 'E' && this.c != 'e') {
                this.skipSpace();
                if (this.c >= 0 && this.c < '~' && !stop[this.c] && this.c != 26) {
                    this.skipNQString(stop);
                    this.extractStringTrim(start, this.pos);
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    } else {
                        return this.xs;
                    }
                } else {
                    this.extractStringTrim(start, this.pos);
                    return this.extractFloat();
                }
            } else {
                this.sb.append('E');
                this.read();
                if (this.c != '+' && this.c != '-' && (this.c < '0' || this.c > '9')) {
                    this.skipNQString(stop);
                    this.extractStringTrim(start, this.pos);
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    } else {
                        if (!this.acceptLeadinZero) {
                            this.checkLeadinZero();
                        }

                        return this.xs;
                    }
                } else {
                    this.sb.append(this.c);
                    this.read();
                    this.skipDigits();
                    this.skipSpace();
                    if (this.c >= 0 && this.c < '~' && !stop[this.c] && this.c != 26) {
                        this.skipNQString(stop);
                        this.extractStringTrim(start, this.pos);
                        if (!this.acceptNonQuote) {
                            throw new ParseException(this.pos, 1, this.xs);
                        } else {
                            return this.xs;
                        }
                    } else {
                        this.extractStringTrim(start, this.pos);
                        return this.extractFloat();
                    }
                }
            }
        }
    }

    protected void readString() throws ParseException, IOException {
        if (!this.acceptSimpleQuote && this.c == '\'') {
            if (this.acceptNonQuote) {
                this.readNQString(stopAll);
            } else {
                throw new ParseException(this.pos, 0, this.c);
            }
        } else {
            int tmpP = this.indexOf(this.c, this.pos + 1);
            if (tmpP == -1) {
                throw new ParseException(this.len, 3, (Object)null);
            } else {
                this.extractString(this.pos + 1, tmpP);
                if (this.xs.indexOf(92) == -1) {
                    this.checkControleChar();
                    this.pos = tmpP;
                    this.read();
                } else {
                    this.sb.clear();
                    this.readString2();
                }
            }
        }
    }

    protected abstract void extractString(int var1, int var2);

    protected abstract int indexOf(char var1, int var2);

    protected void extractStringTrim(int start, int stop) {
        this.extractString(start, stop);
        this.xs = this.xs.trim();
    }
}
