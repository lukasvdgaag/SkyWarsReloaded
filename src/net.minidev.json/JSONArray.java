package net.minidev.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
















public class JSONArray
  extends ArrayList<Object>
  implements List<Object>, JSONAwareEx, JSONStreamAwareEx
{
  private static final long serialVersionUID = 9106884089231309568L;
  
  public JSONArray() {}
  
  public static String toJSONString(List<? extends Object> list)
  {
    return toJSONString(list, JSONValue.COMPRESSION);
  }
  











  public static String toJSONString(List<? extends Object> list, JSONStyle compression)
  {
    StringBuilder sb = new StringBuilder();
    try {
      writeJSONString(list, sb, compression);
    }
    catch (IOException e) {}
    
    return sb.toString();
  }
  









  public static void writeJSONString(Iterable<? extends Object> list, Appendable out, JSONStyle compression)
    throws IOException
  {
    if (list == null) {
      out.append("null");
      return;
    }
    




    boolean first = true;
    out.append('[');
    for (Object value : list) {
      if (first) {
        first = false;
      } else
        out.append(',');
      if (value == null) {
        out.append("null");
      } else
        JSONValue.writeJSONString(value, out, compression);
    }
    out.append(']');
  }
  


  public static void writeJSONString(List<? extends Object> list, Appendable out)
    throws IOException
  {
    writeJSONString(list, out, JSONValue.COMPRESSION);
  }
  
  public void merge(Object o2) {
    JSONObject.merge(this, o2);
  }
  


  public String toJSONString()
  {
    return toJSONString(this, JSONValue.COMPRESSION);
  }
  
  public String toJSONString(JSONStyle compression) {
    return toJSONString(this, compression);
  }
  


  public String toString()
  {
    return toJSONString();
  }
  





  public String toString(JSONStyle compression)
  {
    return toJSONString(compression);
  }
  
  public void writeJSONString(Appendable out) throws IOException {
    writeJSONString(this, out, JSONValue.COMPRESSION);
  }
  
  public void writeJSONString(Appendable out, JSONStyle compression) throws IOException {
    writeJSONString(this, out, compression);
  }
}
