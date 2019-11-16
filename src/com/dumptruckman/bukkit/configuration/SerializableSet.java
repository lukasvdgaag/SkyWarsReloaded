package com.dumptruckman.bukkit.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

@SerializableAs("set")
public class SerializableSet implements Set, ConfigurationSerializable
{
  @NotNull
  private final Set backingSet;
  
  public SerializableSet(@NotNull Set backingSet)
  {
    this.backingSet = backingSet;
  }
  
  public SerializableSet(@NotNull Map<String, Object> serializedForm)
  {
    Object o = serializedForm.get("contents");
    if ((o instanceof List)) {
      backingSet = new HashSet((List)o);
    } else {
      backingSet = Collections.emptySet();
    }
  }
  

  public Map<String, Object> serialize()
  {
    Map<String, Object> serializedForm = new HashMap(backingSet.size());
    List<Object> contents = new ArrayList(backingSet);
    serializedForm.put("contents", contents);
    return serializedForm;
  }
  
  public int size()
  {
    return backingSet.size();
  }
  
  public boolean isEmpty()
  {
    return backingSet.isEmpty();
  }
  
  public boolean contains(Object o)
  {
    return backingSet.contains(o);
  }
  
  @NotNull
  public Iterator iterator()
  {
    return backingSet.iterator();
  }
  
  @NotNull
  public Object[] toArray()
  {
    return backingSet.toArray();
  }
  
  @NotNull
  public Object[] toArray(@NotNull Object[] a)
  {
    return backingSet.toArray(a);
  }
  

  public boolean add(Object o)
  {
    return backingSet.add(o);
  }
  
  public boolean remove(Object o)
  {
    return backingSet.remove(o);
  }
  

  public boolean containsAll(@NotNull Collection c)
  {
    return backingSet.containsAll(c);
  }
  

  public boolean addAll(@NotNull Collection c)
  {
    return backingSet.addAll(c);
  }
  

  public boolean retainAll(@NotNull Collection c)
  {
    return backingSet.retainAll(c);
  }
  

  public boolean removeAll(@NotNull Collection c)
  {
    return backingSet.removeAll(c);
  }
  
  public void clear()
  {
    backingSet.clear();
  }
  

  public boolean equals(Object o)
  {
    return backingSet.equals(o);
  }
  
  public int hashCode()
  {
    return backingSet.hashCode();
  }
  
  public Spliterator spliterator()
  {
    return backingSet.spliterator();
  }
  

  public boolean removeIf(Predicate filter)
  {
    return backingSet.removeIf(filter);
  }
  
  public Stream stream()
  {
    return backingSet.stream();
  }
  
  public Stream parallelStream()
  {
    return backingSet.parallelStream();
  }
  

  public void forEach(Consumer action)
  {
    backingSet.forEach(action);
  }
}
