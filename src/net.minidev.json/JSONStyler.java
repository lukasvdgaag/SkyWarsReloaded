package net.minidev.json;

import net.minidev.json.parser.ContentHandler;
import net.minidev.json.parser.ParseException;


















public class JSONStyler
  extends JSONStyle
  implements ContentHandler
{
  int deep = 0;
  Appendable out;
  String[] indent;
  
  public JSONStyler(int FLAG) {
    super(FLAG);
    setIdentLevel(2);
  }
  
  public JSONStyler(int FLAG, int nbLevel) {
    super(FLAG);
    setIdentLevel(nbLevel);
  }
  
  public void setOutput(Appendable out) {
    this.out = out;
  }
  
  public void setIdentLevel(int nbLevel) {
    String[] indent = new String[nbLevel];
    StringBuilder sb = new StringBuilder("\n");
    for (int i = 0; i < nbLevel; i++) {
      indent[i] = sb.toString();
      sb.append(' ');
    }
    this.indent = indent;
  }
  

  public JSONStyler() {}
  
  public boolean indent()
  {
    return true;
  }
  
  public String getNewLine() {
    if (deep <= 0)
      return "";
    if (deep < indent.length)
      return indent[deep];
    return indent[(deep - 1)];
  }
  
  public JSONStyler getStyler() {
    return this;
  }
  
  public void startJSON()
    throws ParseException
  {}
  
  public void endJSON()
    throws ParseException
  {}
  
  public boolean startObject() throws ParseException
  {
    deep += 1;
    return false;
  }
  
  public boolean endObject() throws ParseException
  {
    deep -= 1;
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
    deep += 1;
    return false;
  }
  
  public boolean endArray() throws ParseException
  {
    deep -= 1;
    return false;
  }
  
  public boolean primitive(Object value) throws ParseException
  {
    return false;
  }
}
