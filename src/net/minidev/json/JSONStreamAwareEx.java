package net.minidev.json;

import java.io.IOException;

public abstract interface JSONStreamAwareEx
  extends JSONStreamAware
{
  public abstract void writeJSONString(Appendable paramAppendable, JSONStyle paramJSONStyle)
    throws IOException;
}
