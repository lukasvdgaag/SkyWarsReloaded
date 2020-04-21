package net.minidev.json.parser;











class JSONParserString
  extends JSONParserMemory
{
  private String in;
  









  public JSONParserString(int permissiveMode)
  {
    super(permissiveMode);
  }
  


  public Object parse(String in)
    throws ParseException
  {
    return parse(in, ContainerFactory.FACTORY_SIMPLE, ContentHandlerDumy.HANDLER);
  }
  


  public Object parse(String in, ContainerFactory containerFactory)
    throws ParseException
  {
    return parse(in, containerFactory, ContentHandlerDumy.HANDLER);
  }
  


  public Object parse(String in, ContainerFactory containerFactory, ContentHandler handler)
    throws ParseException
  {
    this.in = in;
    len = in.length();
    return parse(containerFactory, handler);
  }
  
  protected void extractString(int beginIndex, int endIndex) {
    xs = in.substring(beginIndex, endIndex);
  }
  
  protected int indexOf(char c, int pos) {
    return in.indexOf(c, pos);
  }
  
  protected void read() {
    if (++pos >= len) {
      c = '\032';
    } else {
      c = in.charAt(pos);
    }
  }
  

  protected void readS()
  {
    if (++pos >= len) {
      c = '\032';
    } else {
      c = in.charAt(pos);
    }
  }
  
  protected void readNoEnd() throws ParseException
  {
    if (++pos >= len) {
      c = '\032';
      throw new ParseException(pos - 1, 3, "EOF");
    }
    c = in.charAt(pos);
  }
}
