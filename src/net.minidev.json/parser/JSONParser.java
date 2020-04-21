package net.minidev.json.parser;

import java.io.InputStream;
import java.io.Reader;


public class JSONParser {
    public static final int ACCEPT_SIMPLE_QUOTE = 1;
    public static final int ACCEPT_NON_QUOTE = 2;
    public static final int ACCEPT_NAN = 4;
    public static final int IGNORE_CONTROL_CHAR = 8;
    public static final int USE_INTEGER_STORAGE = 16;
    public static final int ACCEPT_LEADING_ZERO = 32;
    public static final int ACCEPT_USELESS_COMMA = 64;
    public static final int USE_HI_PRECISION_FLOAT = 128;
    public static final int ACCEPT_TAILLING_DATA = 256;
    public static final int MODE_PERMISSIVE = -1;
    public static final int MODE_RFC4627 = 400;
    public static final int MODE_JSON_SIMPLE = 448;
    public static final int MODE_STRICTEST = 144;
    public static int DEFAULT_PERMISSIVE_MODE = System.getProperty("JSON_SMART_SIMPLE") != null ? 448 : -1;

    private int mode;

    private JSONParserReader pStream;

    private JSONParserInputStream pSBintream;

    private JSONParserString pString;
    private JSONParserByteArray pBytes;

    /**
     * @deprecated
     */
    public JSONParser() {
        mode = DEFAULT_PERMISSIVE_MODE;
    }

    public JSONParser(int permissifMode) {
        mode = permissifMode;
    }


    public Object parse(String in)
            throws ParseException {
        if (pString == null)
            pString = new JSONParserString(mode);
        return pString.parse(in);
    }


    public Object parse(String in, ContainerFactory containerFactory)
            throws ParseException {
        if (pString == null)
            pString = new JSONParserString(mode);
        return pString.parse(in, containerFactory);
    }

    public Object parse(String in, ContainerFactory containerFactory, ContentHandler handler) throws ParseException {
        if (pString == null)
            pString = new JSONParserString(mode);
        return pString.parse(in, containerFactory, handler);
    }


    public Object parse(byte[] in)
            throws ParseException {
        if (pBytes == null)
            pBytes = new JSONParserByteArray(mode);
        return pBytes.parse(in);
    }


    public Object parse(byte[] in, ContainerFactory containerFactory)
            throws ParseException {
        if (pBytes == null)
            pBytes = new JSONParserByteArray(mode);
        return pBytes.parse(in, containerFactory);
    }

    public Object parse(byte[] in, ContainerFactory containerFactory, ContentHandler handler) throws ParseException {
        if (pBytes == null)
            pBytes = new JSONParserByteArray(mode);
        return pBytes.parse(in, containerFactory, handler);
    }


    public Object parse(Reader in)
            throws ParseException {
        if (pStream == null)
            pStream = new JSONParserReader(mode);
        return pStream.parse(in);
    }


    public Object parse(Reader in, ContainerFactory containerFactory)
            throws ParseException {
        if (pStream == null)
            pStream = new JSONParserReader(mode);
        return pStream.parse(in, containerFactory);
    }


    public Object parse(Reader in, ContainerFactory containerFactory, ContentHandler handler)
            throws ParseException {
        if (pStream == null)
            pStream = new JSONParserReader(mode);
        return pStream.parse(in, containerFactory);
    }


    public Object parse(InputStream in)
            throws ParseException {
        if (pSBintream == null)
            pSBintream = new JSONParserInputStream(mode);
        return pSBintream.parse(in);
    }


    public Object parse(InputStream in, ContainerFactory containerFactory)
            throws ParseException {
        if (pSBintream == null)
            pSBintream = new JSONParserInputStream(mode);
        return pSBintream.parse(in, containerFactory);
    }


    public Object parse(InputStream in, ContainerFactory containerFactory, ContentHandler handler)
            throws ParseException {
        if (pSBintream == null)
            pSBintream = new JSONParserInputStream(mode);
        return pSBintream.parse(in, containerFactory);
    }
}
