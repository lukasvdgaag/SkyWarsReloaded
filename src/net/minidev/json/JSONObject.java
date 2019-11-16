package net.minidev.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSONObject extends HashMap<String, Object> implements JSONAware, JSONAwareEx, JSONStreamAwareEx
{
  private static final long serialVersionUID = -503443796854799292L;
  
  public JSONObject() {}
  
  public static String escape(String s)
  {
    return JSONValue.escape(s);
  }
  
  public static String toJSONString(Map<String, ? extends Object> map) {
    return toJSONString(map, JSONValue.COMPRESSION);
  }
  









  public static String toJSONString(Map<String, ? extends Object> map, JSONStyle compression)
  {
    StringBuilder sb = new StringBuilder();
    try {
      writeJSON(map, sb, compression);
    }
    catch (IOException e) {}
    
    return sb.toString();
  }
  

  public static void writeJSONKV(String key, Object value, Appendable out, JSONStyle compression)
    throws IOException
  {
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
    if ((value instanceof String)) {
      if (!compression.mustProtectValue((String)value)) {
        out.append((String)value);
      } else {
        out.append('"');
        JSONValue.escape((String)value, out, compression);
        out.append('"');
      }
    } else {
      JSONValue.writeJSONString(value, out, compression);
    }
  }
  























  public JSONObject(Map<String, ?> map)
  {
    super(map);
  }
  
  public static void writeJSON(Map<String, Object> map, Appendable out) throws IOException {
    writeJSON(map, out, JSONValue.COMPRESSION);
  }
  






  public static void writeJSON(Map<String, ? extends Object> map, Appendable out, JSONStyle compression)
    throws IOException
  {
    if (map == null) {
      out.append("null");
      return;
    }
    

    boolean first = true;
    



    out.append('{');
    


    for (Entry<?, ?> entry : map.entrySet()) {
      if (first) {
        first = false;
      } else {
        out.append(',');
      }
      
      writeJSONKV(entry.getKey().toString(), entry.getValue(), out, compression);
    }
    


    out.append('}');
  }
  




  public void writeJSONString(Appendable out)
    throws IOException
  {
    writeJSON(this, out, JSONValue.COMPRESSION);
  }
  

  public void writeJSONString(Appendable out, JSONStyle compression)
    throws IOException
  {
    writeJSON(this, out, compression);
  }
  
  public void merge(Object o2) {
    merge(this, o2);
  }
  
  protected static JSONObject merge(JSONObject o1, Object o2) {
    if (o2 == null)
      return o1;
    if ((o2 instanceof JSONObject))
      return merge(o1, (JSONObject)o2);
    throw new RuntimeException("JSON megre can not merge JSONObject with " + o2.getClass());
  }
  
  private static JSONObject merge(JSONObject o1, JSONObject o2) {
    if (o2 == null)
      return o1;
    for (String key : o1.keySet()) {
      Object value1 = o1.get(key);
      Object value2 = o2.get(key);
      if (value2 != null)
      {
        if ((value1 instanceof JSONArray)) {
          o1.put(key, merge((JSONArray)value1, value2));

        }
        else if ((value1 instanceof JSONObject)) {
          o1.put(key, merge((JSONObject)value1, value2));

        }
        else if (!value1.equals(value2))
        {
          throw new RuntimeException("JSON megre can not merge " + value1.getClass() + " with " + value2.getClass()); } }
    }
    for (String key : o2.keySet()) {
      if (!o1.containsKey(key))
      {
        o1.put(key, o2.get(key)); }
    }
    return o1;
  }
  
  protected static JSONArray merge(JSONArray o1, Object o2) {
    if (o2 == null)
      return o1;
    if ((o1 instanceof JSONArray))
      return merge(o1, (JSONArray)o2);
    o1.add(o2);
    return o1;
  }
  
  private static JSONArray merge(JSONArray o1, JSONArray o2) {
    o1.addAll(o2);
    return o1;
  }
  
  public String toJSONString() {
    return toJSONString(this, JSONValue.COMPRESSION);
  }
  
  public String toJSONString(JSONStyle compression) {
    return toJSONString(this, compression);
  }
  
  public String toString(JSONStyle compression) {
    return toJSONString(this, compression);
  }
  
  public String toString() {
    return toJSONString(this, JSONValue.COMPRESSION);
  }
}
