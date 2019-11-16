package net.minidev.json.parser;











class JSONParserByteArray
  extends JSONParserMemory
{
  private byte[] in;
  









  public JSONParserByteArray(int permissiveMode)
  {
    super(permissiveMode);
  }
  


  public Object parse(byte[] in)
    throws ParseException
  {
    return parse(in, ContainerFactory.FACTORY_SIMPLE, ContentHandlerDumy.HANDLER);
  }
  


  public Object parse(byte[] in, ContainerFactory containerFactory)
    throws ParseException
  {
    return parse(in, containerFactory, ContentHandlerDumy.HANDLER);
  }
  


  public Object parse(byte[] in, ContainerFactory containerFactory, ContentHandler handler)
    throws ParseException
  {
    this.in = in;
    len = in.length;
    return parse(containerFactory, handler);
  }
  
  protected void extractString(int beginIndex, int endIndex) {
    xs = new String(in, beginIndex, endIndex - beginIndex);
  }
  
  protected int indexOf(char c, int pos) {
    for (int i = pos; pos < len; i++)
      if (in[i] == (byte)c)
        return i;
    return -1;
  }
  
  protected void read() {
    if (++pos >= len) {
      c = '\032';
    } else {
      c = ((char)in[pos]);
    }
  }
  

  protected void readS()
  {
    if (++pos >= len) {
      c = '\032';
    } else
      c = ((char)in[pos]);
  }
  
  protected void readNoEnd() throws ParseException {
    if (++pos >= len) {
      c = '\032';
      throw new ParseException(pos - 1, 3, "EOF");
    }
    c = ((char)in[pos]);
  }
}
