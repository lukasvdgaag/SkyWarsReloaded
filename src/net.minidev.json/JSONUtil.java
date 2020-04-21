package net.minidev.json;









public class JSONUtil
{
  public JSONUtil() {}
  







  public static String getSetterName(String key)
  {
    int len = key.length();
    char[] b = new char[len + 3];
    b[0] = 's';
    b[1] = 'e';
    b[2] = 't';
    char c = key.charAt(0);
    if ((c >= 'a') && (c <= 'z'))
      c = (char)(c - ' ');
    b[3] = c;
    for (int i = 1; i < len; i++) {
      b[(i + 3)] = key.charAt(i);
    }
    return new String(b);
  }
  
  public static String getGetterName(String key) {
    int len = key.length();
    char[] b = new char[len + 3];
    b[0] = 'g';
    b[1] = 'e';
    b[2] = 't';
    char c = key.charAt(0);
    if ((c >= 'a') && (c <= 'z'))
      c = (char)(c - ' ');
    b[3] = c;
    for (int i = 1; i < len; i++) {
      b[(i + 3)] = key.charAt(i);
    }
    return new String(b);
  }
  
  public static String getIsName(String key) {
    int len = key.length();
    char[] b = new char[len + 2];
    b[0] = 'i';
    b[1] = 's';
    char c = key.charAt(0);
    if ((c >= 'a') && (c <= 'z'))
      c = (char)(c - ' ');
    b[2] = c;
    for (int i = 1; i < len; i++) {
      b[(i + 2)] = key.charAt(i);
    }
    return new String(b);
  }
}
