package com.dumptruckman.bukkit.configuration.util;

import com.dumptruckman.bukkit.configuration.SerializableSet;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SerializationHelper {
    private static final Logger LOG = Logger.getLogger(SerializationHelper.class.getName());

    public SerializationHelper() {
    }

    public static Object serialize( Object value) {
        if ((value instanceof Object[])) {
            value = new ArrayList(Arrays.asList((Object[]) value));
        }
        if (((value instanceof Set)) && (!(value instanceof SerializableSet))) {
            value = new SerializableSet((Set) value);
        }
        if ((value instanceof ConfigurationSection))
            return buildMap(((ConfigurationSection) value).getValues(false));
        if ((value instanceof Map))
            return buildMap((Map) value);
        if ((value instanceof List))
            return buildList((List) value);
        if ((value instanceof ConfigurationSerializable)) {
            ConfigurationSerializable serializable = (ConfigurationSerializable) value;
            Map<String, Object> values = new LinkedHashMap();
            values.put("==", ConfigurationSerialization.getAlias(serializable.getClass()));
            values.putAll(serializable.serialize());
            return buildMap(values);
        }
        return value;
    }



    private static Map<String, Object> buildMap( Map<?, ?> map) {
        Map<String, Object> result = new LinkedHashMap(map.size());
        try {
            for (Entry<?, ?> entry : map.entrySet()) {
                result.put(entry.getKey().toString(), serialize(entry.getValue()));
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error while building configuration map.", e);
        }
        return result;
    }


    private static List<Object> buildList( Collection<?> collection) {
        List<Object> result = new ArrayList(collection.size());
        try {
            for (Object o : collection) {
                result.add(serialize(o));
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error while building configuration list.", e);
        }
        return result;
    }


    public static Object deserialize( Map<?, ?> input) {
        Map<String, Object> output = new LinkedHashMap(input.size());
        for (Entry<?, ?> e : input.entrySet()) {
            if ((e.getValue() instanceof Map)) {
                output.put(e.getKey().toString(), deserialize((Map) e.getValue()));
            } else if ((e.getValue() instanceof List)) {
                output.put(e.getKey().toString(), deserialize((List) e.getValue()));
            } else {
                output.put(e.getKey().toString(), e.getValue());
            }
        }
        if (output.containsKey("==")) {
            try {
                return ConfigurationSerialization.deserializeObject(output);
            } catch (IllegalArgumentException ex) {
                throw new YAMLException("Could not deserialize object", ex);
            }
        }
        return output;
    }


    private static Object deserialize( List<?> input) {
        List<Object> output = new ArrayList(input.size());
        for (Object o : input) {
            if ((o instanceof Map)) {
                output.add(deserialize((Map) o));
            } else if ((o instanceof List)) {
                output.add(deserialize((List) o));
            } else {
                output.add(o);
            }
        }
        return output;
    }
}
