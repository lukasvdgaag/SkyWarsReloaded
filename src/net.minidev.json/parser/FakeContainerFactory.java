package net.minidev.json.parser;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


















public class FakeContainerFactory
  implements ContainerFactory
{
  public FackList list;
  public FackMap map;
  
  public FakeContainerFactory() {}
  
  public List<Object> createArrayContainer()
  {
    if (list == null)
      list = new FackList();
    return list;
  }
  
  public Map<String, Object> createObjectContainer()
  {
    if (map == null)
      map = new FackMap();
    return map;
  }
  
  static class FackMap extends AbstractMap<String, Object>
  {
    FackMap() {}
    
    public Object put(String key, Object value) {
      return null;
    }
    
    public Set<Entry<String, Object>> entrySet()
    {
      return null;
    }
  }
  
  static class FackList extends AbstractList<Object>
  {
    FackList() {}
    
    public boolean add(Object e) {
      return false;
    }
    
    public Object get(int index)
    {
      return null;
    }
    
    public int size()
    {
      return 0;
    }
  }
}
