package net.minidev.json;


public class JSONStyle {
    public static final int FLAG_PROTECT_KEYS = 1;
    public static final int FLAG_PROTECT_4WEB = 2;
    public static final int FLAG_PROTECT_VALUES = 4;
    public static final int FLAG_AGRESSIVE = 8;
    public static final JSONStyle NO_COMPRESS = new JSONStyle();
    public static final JSONStyle MAX_COMPRESS = new JSONStyle(-1);
    public static final JSONStyle LT_COMPRESS = new JSONStyle(2);
    private boolean _protectKeys;
    private boolean _protect4Web;
    private boolean _protectValues;
    private JStylerObj.MustProtect mpKey;
    private JStylerObj.MustProtect mpValue;
    private JStylerObj.StringProtector esc;

    public JSONStyle(int FLAG) {
        this._protectKeys = (FLAG & 1) == 0;
        this._protectValues = (FLAG & 4) == 0;
        this._protect4Web = (FLAG & 2) == 0;
        Object mp;
        if ((FLAG & 8) > 0) {
            mp = JStylerObj.MP_AGGRESIVE;
        } else {
            mp = JStylerObj.MP_SIMPLE;
        }

        if (this._protectValues) {
            this.mpValue = JStylerObj.MP_TRUE;
        } else {
            this.mpValue = (JStylerObj.MustProtect)mp;
        }

        if (this._protectKeys) {
            this.mpKey = JStylerObj.MP_TRUE;
        } else {
            this.mpKey = (JStylerObj.MustProtect)mp;
        }

        if (this._protect4Web) {
            this.esc = JStylerObj.ESCAPE4Web;
        } else {
            this.esc = JStylerObj.ESCAPE_LT;
        }

    }

    public JSONStyle() {
        this(0);
    }

    public boolean protectKeys() {
        return this._protectKeys;
    }

    public boolean protectValues() {
        return this._protectValues;
    }

    public boolean protect4Web() {
        return this._protect4Web;
    }

    public boolean indent() {
        return false;
    }

    public boolean mustProtectKey(String s) {
        return this.mpKey.mustBeProtect(s);
    }

    public boolean mustProtectValue(String s) {
        return this.mpValue.mustBeProtect(s);
    }

    public void escape(String s, Appendable out) {
        this.esc.escape(s, out);
    }
}
