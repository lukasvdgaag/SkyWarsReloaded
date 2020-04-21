package net.minidev.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import net.minidev.json.parser.ContainerFactory;
import net.minidev.json.parser.ContentHandler;
import net.minidev.json.parser.ContentHandlerCompressor;
import net.minidev.json.parser.FakeContainerFactory;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;



































public class JSONValue
{
  public static JSONStyle COMPRESSION = JSONStyle.NO_COMPRESS;
  



  private static final FakeContainerFactory FACTORY_FAKE_COINTAINER = new FakeContainerFactory();
  





  public JSONValue() {}
  





  public static Object parse(byte[] in)
  {
    try
    {
      return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in);
    } catch (Exception e) {}
    return null;
  }
  













  public static Object parse(InputStream in)
  {
    try
    {
      return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in);
    } catch (Exception e) {}
    return null;
  }
  











  public static Object parse(Reader in)
  {
    try
    {
      return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in);
    } catch (Exception e) {}
    return null;
  }
  











  public static Object parse(String s)
  {
    try
    {
      return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(s);
    } catch (Exception e) {}
    return null;
  }
  




  public static Object parseKeepingOrder(byte[] in)
  {
    try
    {
      return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, ContainerFactory.FACTORY_ORDERED);
    } catch (Exception e) {}
    return null;
  }
  




  public static Object parseKeepingOrder(InputStream in)
  {
    try
    {
      return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, ContainerFactory.FACTORY_ORDERED);
    } catch (Exception e) {}
    return null;
  }
  




  public static Object parseKeepingOrder(Reader in)
  {
    try
    {
      return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, ContainerFactory.FACTORY_ORDERED);
    } catch (Exception e) {}
    return null;
  }
  




  public static Object parseKeepingOrder(String in)
  {
    try
    {
      return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, ContainerFactory.FACTORY_ORDERED);
    } catch (Exception e) {}
    return null;
  }
  




  public static void SAXParse(InputStream input, ContentHandler handler)
    throws ParseException, IOException
  {
    JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
    p.parse(input, FACTORY_FAKE_COINTAINER, handler);
  }
  



  public static void SAXParse(Reader input, ContentHandler handler)
    throws ParseException, IOException
  {
    JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
    p.parse(input, FACTORY_FAKE_COINTAINER, handler);
  }
  



  public static void SAXParse(String input, ContentHandler handler)
    throws ParseException
  {
    JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
    p.parse(input, FACTORY_FAKE_COINTAINER, handler);
  }
  



  public static String compress(String input, JSONStyle style)
  {
    try
    {
      StringBuilder sb = new StringBuilder();
      ContentHandlerCompressor comp = new ContentHandlerCompressor(sb, style);
      JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
      p.parse(input, FACTORY_FAKE_COINTAINER, comp);
      return sb.toString();
    } catch (Exception e) {}
    return input;
  }
  





  public static String compress(String s)
  {
    return compress(s, JSONStyle.MAX_COMPRESS);
  }
  




  public static String uncompress(String s)
  {
    return compress(s, JSONStyle.NO_COMPRESS);
  }
  








  public static Object parseWithException(byte[] in)
    throws IOException, ParseException
  {
    return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, ContainerFactory.FACTORY_SIMPLE);
  }
  








  public static Object parseWithException(InputStream in)
    throws IOException, ParseException
  {
    return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, ContainerFactory.FACTORY_SIMPLE);
  }
  






  public static Object parseWithException(Reader in)
    throws IOException, ParseException
  {
    return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, ContainerFactory.FACTORY_SIMPLE);
  }
  






  public static Object parseWithException(String s)
    throws ParseException
  {
    return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(s, ContainerFactory.FACTORY_SIMPLE);
  }
  








  public static Object parseStrict(InputStream in)
    throws IOException, ParseException
  {
    return new JSONParser(400).parse(in, ContainerFactory.FACTORY_SIMPLE);
  }
  






  public static Object parseStrict(Reader in)
    throws IOException, ParseException
  {
    return new JSONParser(400).parse(in, ContainerFactory.FACTORY_SIMPLE);
  }
  






  public static Object parseStrict(String s)
    throws ParseException
  {
    return new JSONParser(400).parse(s, ContainerFactory.FACTORY_SIMPLE);
  }
  






  public static Object parseStrict(byte[] s)
    throws ParseException
  {
    return new JSONParser(400).parse(s, ContainerFactory.FACTORY_SIMPLE);
  }
  


  public static boolean isValidJsonStrict(Reader in)
    throws IOException
  {
    try
    {
      new JSONParser(400).parse(in, FACTORY_FAKE_COINTAINER);
      return true;
    } catch (ParseException e) {}
    return false;
  }
  




  public static boolean isValidJsonStrict(String s)
  {
    try
    {
      new JSONParser(400).parse(s, FACTORY_FAKE_COINTAINER);
      return true;
    } catch (ParseException e) {}
    return false;
  }
  



  public static boolean isValidJson(Reader in)
    throws IOException
  {
    try
    {
      new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, FACTORY_FAKE_COINTAINER);
      return true;
    } catch (ParseException e) {}
    return false;
  }
  




  public static boolean isValidJson(String s)
  {
    try
    {
      new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(s, FACTORY_FAKE_COINTAINER);
      return true;
    } catch (ParseException e) {}
    return false;
  }
  









  public static void writeJSONString(Object value, Appendable out)
    throws IOException
  {
    writeJSONString(value, out, COMPRESSION);
  }
  









  public static void writeJSONString(Object value, Appendable out, JSONStyle compression)
    throws IOException
  {
    if (value == null) {
      out.append("null");
      return;
    }
    
    if ((value instanceof String)) {
      if (!compression.mustProtectValue((String)value)) {
        out.append((String)value);
      } else {
        out.append('"');
        escape((String)value, out, compression);
        out.append('"');
      }
      return;
    }
    
    if ((value instanceof Number)) {
      if ((value instanceof Double)) {
        if (((Double)value).isInfinite()) {
          out.append("null");
        } else
          out.append(value.toString());
      } else if ((value instanceof Float)) {
        if (((Float)value).isInfinite()) {
          out.append("null");
        } else
          out.append(value.toString());
      } else {
        out.append(value.toString());
      }
      return;
    }
    
    if ((value instanceof Boolean)) {
      out.append(value.toString());
    } else if ((value instanceof JSONStreamAware)) {
      if ((value instanceof JSONStreamAwareEx)) {
        ((JSONStreamAwareEx)value).writeJSONString(out, compression);
      } else
        ((JSONStreamAware)value).writeJSONString(out);
    } else if ((value instanceof JSONAware)) {
      if ((value instanceof JSONAwareEx)) {
        out.append(((JSONAwareEx)value).toJSONString(compression));
      } else
        out.append(((JSONAware)value).toJSONString());
    } else if ((value instanceof Map)) {
      JSONObject.writeJSON((Map)value, out, compression);
    } else if ((value instanceof Iterable)) {
      JSONArray.writeJSONString((Iterable)value, out, compression);
    } else if ((value instanceof Date)) {
      writeJSONString(value.toString(), out, compression);
    } else { if ((value instanceof Enum))
      {
        String s = ((Enum)value).name();
        if (!compression.mustProtectValue(s)) {
          out.append(s);
        } else {
          out.append('"');
          escape(s, out, compression);
          out.append('"');
        }
        return; }
      if (value.getClass().isArray()) {
        Class<?> arrayClz = value.getClass();
        Class<?> c = arrayClz.getComponentType();
        
        out.append('[');
        boolean needSep = false;
        
        if (c.isPrimitive()) {
          if (c == Integer.TYPE) {
            for (int b : (int[])value) {
              if (needSep) {
                out.append(',');
              } else
                needSep = true;
              out.append(Integer.toString(b));
            }
          } else if (c == Short.TYPE) {
            for (short b : (short[])value) {
              if (needSep) {
                out.append(',');
              } else
                needSep = true;
              out.append(Short.toString(b));
            }
          } else if (c == Byte.TYPE) {
            for (byte b : (byte[])value) {
              if (needSep) {
                out.append(',');
              } else
                needSep = true;
              out.append(Byte.toString(b));
            }
          } else if (c == Long.TYPE) {
            for (long b : (long[])value) {
              if (needSep) {
                out.append(',');
              } else
                needSep = true;
              out.append(Long.toString(b));
            }
          } else if (c == Float.TYPE) {
            for (float b : (float[])value) {
              if (needSep) {
                out.append(',');
              } else
                needSep = true;
              out.append(Float.toString(b));
            }
          } else if (c == Double.TYPE) {
            for (double b : (double[])value) {
              if (needSep) {
                out.append(',');
              } else
                needSep = true;
              out.append(Double.toString(b));
            }
          } else if (c == Boolean.TYPE) {
            for (boolean b : (boolean[])value) {
              if (needSep) {
                out.append(',');
              } else
                needSep = true;
              if (b) {
                out.append("true");
              } else
                out.append("false");
            }
          }
        } else {
          for (Object o : (Object[])value) {
            if (needSep) {
              out.append(',');
            } else
              needSep = true;
            writeJSONString(o, out, compression);
          }
        }
        out.append(']');
      } else {
        try {
          Class<?> cls = value.getClass();
          boolean needSep = false;
          Field[] fields = cls.getDeclaredFields();
          out.append('{');
          for (Field field : fields) {
            int m = field.getModifiers();
            if ((m & 0x98) <= 0)
            {
              Object v = null;
              if ((m & 0x1) > 0) {
                v = field.get(value);
              } else {
                String g = JSONUtil.getGetterName(field.getName());
                Method mtd = null;
                try
                {
                  mtd = cls.getDeclaredMethod(g, new Class[0]);
                }
                catch (Exception e) {}
                if (mtd == null) {
                  Class<?> c2 = field.getType();
                  if ((c2 == Boolean.TYPE) || (c2 == Boolean.class)) {
                    g = JSONUtil.getIsName(field.getName());
                    mtd = cls.getDeclaredMethod(g, new Class[0]);
                  }
                }
                if (mtd == null)
                  continue;
                v = mtd.invoke(value, new Object[0]);
              }
              if (needSep) {
                out.append(',');
              } else
                needSep = true;
              JSONObject.writeJSONKV(field.getName(), v, out, compression);
            } }
          out.append('}');
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
  








  public static String toJSONString(Object value)
  {
    return toJSONString(value, COMPRESSION);
  }
  















  public static String toJSONString(Object value, JSONStyle compression)
  {
    StringBuilder sb = new StringBuilder();
    try {
      writeJSONString(value, sb, compression);
    }
    catch (IOException e) {}
    
    return sb.toString();
  }
  
  public static String escape(String s) {
    return escape(s, COMPRESSION);
  }
  



  public static String escape(String s, JSONStyle compression)
  {
    if (s == null)
      return null;
    StringBuilder sb = new StringBuilder();
    compression.escape(s, sb);
    return sb.toString();
  }
  
  public static void escape(String s, Appendable ap) {
    escape(s, ap, COMPRESSION);
  }
  
  public static void escape(String s, Appendable ap, JSONStyle compression) {
    if (s == null)
      return;
    compression.escape(s, ap);
  }
}
