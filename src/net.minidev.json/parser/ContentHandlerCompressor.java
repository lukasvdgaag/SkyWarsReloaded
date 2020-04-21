package net.minidev.json.parser;

import java.io.IOException;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
















public class ContentHandlerCompressor
  implements ContentHandler
{
  Appendable out;
  JSONStyle compression;
  int[] stack = new int[10];
  
  int pos;
  
  private void push(int type)
  {
    pos += 2;
    if (pos >= stack.length) {
      int[] tmp = new int[stack.length * 2];
      System.arraycopy(stack, 0, tmp, 0, stack.length);
      stack = tmp;
    }
    stack[pos] = type;
    stack[(pos + 1)] = 0;
  }
  
  private boolean isInObject() {
    return stack[pos] == 0;
  }
  
  private boolean isInArray() {
    return stack[pos] == 1;
  }
  
  public ContentHandlerCompressor(Appendable out, JSONStyle compression) {
    this.out = out;
    this.compression = compression;
  }
  
  public void startJSON()
    throws ParseException, IOException
  {}
  
  public void endJSON()
    throws ParseException, IOException
  {}
  
  public boolean startObject() throws ParseException, IOException
  {
    if (isInArray()) { int tmp17_16 = (pos + 1); int[] tmp17_8 = stack; int tmp19_18 = tmp17_8[tmp17_16];tmp17_8[tmp17_16] = (tmp19_18 + 1); if (tmp19_18 > 0)
        out.append(','); }
    out.append('{');
    push(0);
    
    return false;
  }
  
  public boolean endObject() throws ParseException, IOException
  {
    out.append('}');
    pos -= 2;
    
    return false;
  }
  
  public boolean startObjectEntry(String key) throws ParseException, IOException
  {
    int tmp10_9 = (pos + 1); int[] tmp10_1 = stack; int tmp12_11 = tmp10_1[tmp10_9];tmp10_1[tmp10_9] = (tmp12_11 + 1); if (tmp12_11 > 0)
      out.append(',');
    if (key == null) {
      out.append("null");
    } else if (!compression.mustProtectKey(key)) {
      out.append(key);
    } else {
      out.append('"');
      JSONValue.escape(key, out, compression);
      out.append('"');
    }
    out.append(':');
    return false;
  }
  
  public boolean endObjectEntry() throws ParseException, IOException
  {
    return false;
  }
  
  public boolean startArray() throws ParseException, IOException
  {
    if (isInArray()) { int tmp17_16 = (pos + 1); int[] tmp17_8 = stack; int tmp19_18 = tmp17_8[tmp17_16];tmp17_8[tmp17_16] = (tmp19_18 + 1); if (tmp19_18 > 0)
        out.append(','); }
    out.append('[');
    push(1);
    return false;
  }
  
  public boolean endArray() throws ParseException, IOException
  {
    out.append(']');
    pos -= 2;
    return false;
  }
  
  public boolean primitive(Object value) throws ParseException, IOException
  {
    if (!isInObject()) { int tmp17_16 = (pos + 1); int[] tmp17_8 = stack; int tmp19_18 = tmp17_8[tmp17_16];tmp17_8[tmp17_16] = (tmp19_18 + 1); if (tmp19_18 > 0)
        out.append(',');
    }
    if ((value instanceof String)) {
      if (!compression.mustProtectValue((String)value)) {
        out.append((String)value);
      } else {
        out.append('"');
        JSONValue.escape((String)value, out, compression);
        out.append('"');
      }
    } else
      JSONValue.writeJSONString(value, out, compression);
    return false;
  }
}
