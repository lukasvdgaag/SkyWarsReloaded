package net.minidev.json;

import java.io.IOException;

public abstract interface JSONStreamAware
{
  public abstract void writeJSONString(Appendable paramAppendable)
    throws IOException;
}
