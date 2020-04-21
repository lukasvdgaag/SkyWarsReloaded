package net.minidev.json.parser;

import java.io.IOException;
import java.io.InputStream;





















class JSONParserInputStream
  extends JSONParserStream
{
  private InputStream in;
  
  public JSONParserInputStream(int permissiveMode)
  {
    super(permissiveMode);
  }
  


  public Object parse(InputStream in)
    throws ParseException
  {
    return parse(in, ContainerFactory.FACTORY_SIMPLE, ContentHandlerDumy.HANDLER);
  }
  


  public Object parse(InputStream in, ContainerFactory containerFactory)
    throws ParseException
  {
    return parse(in, containerFactory, ContentHandlerDumy.HANDLER);
  }
  




  public Object parse(InputStream in, ContainerFactory containerFactory, ContentHandler handler)
    throws ParseException
  {
    this.in = in;
    return super.parse(containerFactory, handler);
  }
  
  protected void read() throws IOException {
    int i = in.read();
    c = (i == -1 ? '\032' : (char)i);
    pos += 1;
  }
  
  protected void readS() throws IOException
  {
    sb.append(c);
    int i = in.read();
    if (i == -1) {
      c = '\032';
    } else {
      c = ((char)i);
      pos += 1;
    }
  }
  
  protected void readNoEnd() throws ParseException, IOException {
    int i = in.read();
    if (i == -1)
      throw new ParseException(pos - 1, 3, "EOF");
    c = ((char)i);
  }
}
