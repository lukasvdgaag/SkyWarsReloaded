package net.minidev.json.parser;

import java.io.IOException;














public class ContentHandlerDumy
  implements ContentHandler
{
  public static ContentHandlerDumy HANDLER = new ContentHandlerDumy();
  
  public ContentHandlerDumy() {}
  
  public void startJSON() throws ParseException
  {}
  
  public void endJSON() throws ParseException
  {}
  
  public boolean startObject() throws ParseException, IOException
  {
    return false;
  }
  
  public boolean endObject() throws ParseException
  {
    return false;
  }
  
  public boolean startObjectEntry(String key) throws ParseException
  {
    return false;
  }
  
  public boolean endObjectEntry() throws ParseException
  {
    return false;
  }
  
  public boolean startArray() throws ParseException
  {
    return false;
  }
  
  public boolean endArray() throws ParseException
  {
    return false;
  }
  
  public boolean primitive(Object value) throws ParseException
  {
    return false;
  }
}
