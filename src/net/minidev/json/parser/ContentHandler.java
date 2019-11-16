package net.minidev.json.parser;

import java.io.IOException;

public abstract interface ContentHandler
{
  public abstract void startJSON()
    throws ParseException, IOException;
  
  public abstract void endJSON()
    throws ParseException, IOException;
  
  public abstract boolean startObject()
    throws ParseException, IOException;
  
  public abstract boolean endObject()
    throws ParseException, IOException;
  
  public abstract boolean startObjectEntry(String paramString)
    throws ParseException, IOException;
  
  public abstract boolean endObjectEntry()
    throws ParseException, IOException;
  
  public abstract boolean startArray()
    throws ParseException, IOException;
  
  public abstract boolean endArray()
    throws ParseException, IOException;
  
  public abstract boolean primitive(Object paramObject)
    throws ParseException, IOException;
}
