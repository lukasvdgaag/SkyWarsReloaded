package net.minidev.json.parser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;


































public abstract interface ContainerFactory
{
  public static final ContainerFactory FACTORY_SIMPLE = new ContainerFactory()
  {
    public Map<String, Object> createObjectContainer()
    {
      return new JSONObject();
    }
    
    public List<Object> createArrayContainer()
    {
      return new JSONArray();
    }
  };
  
  public static final ContainerFactory FACTORY_ORDERED = new ContainerFactory()
  {
    public Map<String, Object> createObjectContainer()
    {
      return new LinkedHashMap();
    }
    
    public List<Object> createArrayContainer()
    {
      return new JSONArray();
    }
  };
  
  public abstract Map<String, Object> createObjectContainer();
  
  public abstract List<Object> createArrayContainer();
}
