/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.bukkit.configuration.json;

import com.dumptruckman.bukkit.configuration.SerializableSet;
import com.dumptruckman.bukkit.configuration.util.SerializationHelper;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A JSON Configuration for Bukkit based on {@link FileConfiguration}.
 * <p>
 * Able to store all the things you'd expect from a Bukkit configuration.
 */
public class JsonConfiguration extends FileConfiguration {

    protected static final String BLANK_CONFIG = "{}\n";

    private static final Logger LOG = Logger.getLogger(JsonConfiguration.class.getName());

    public JsonConfiguration() {
        ConfigurationSerialization.registerClass(SerializableSet.class);
    }

    private static JsonConfiguration loadConfiguration( final JsonConfiguration config,  final File file) {
        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "Cannot find file " + file, ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (InvalidConfigurationException ex) {
            LOG.log(Level.SEVERE, "Cannot load " + file, ex);
        }
        return config;
    }

    /**
     * Loads up a configuration from a json formatted file.
     * <p>
     * If the file does not exist, it will be created.  This will attempt to use UTF-8 encoding for the file, if it fails
     * to do so, the system default will be used instead.
     *
     * @param file The file to load the configuration from.
     * @return The configuration loaded from the file contents.
     */
    public static JsonConfiguration loadConfiguration( final File file) {
        return loadConfiguration(new JsonConfiguration(), file);
    }


    @Override
    public String saveToString() {
        String dump = JSONValue.toJSONString(SerializationHelper.serialize(getValues(false)));

        if (dump.equals(BLANK_CONFIG)) {
            dump = "";
        }

        return dump;
    }

    @Override
    public void loadFromString( final String contents) throws InvalidConfigurationException {
        if (contents.isEmpty()) {
            return;
        }

        Map<?, ?> input;
        try {
            input = (Map<?, ?>) new JSONParser(JSONParser.USE_INTEGER_STORAGE).parse(contents);
        } catch (ParseException e) {
            throw new InvalidConfigurationException("Invalid JSON detected.", e);
        } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.", e);
        }

        if (input != null) {
            convertMapsToSections(input, this);
        } else {
            throw new InvalidConfigurationException("An unknown error occurred while attempting to parse the json.");
        }
    }

    private void convertMapsToSections( Map<?, ?> input,  final ConfigurationSection section) {
        final Object result = SerializationHelper.deserialize(input);
        if (result instanceof Map) {
            input = (Map<?, ?>) result;
            for (Map.Entry<?, ?> entry : input.entrySet()) {
                String key = entry.getKey().toString();
                Object value = entry.getValue();

                if (value instanceof Map) {
                    convertMapsToSections((Map<?, ?>) value, section.createSection(key));
                } else {
                    section.set(key, value);
                }
            }
        } else {
            section.set("", result);
        }
    }

    @Override
    protected String buildHeader() {
        // json does not support comments of any kind.
        return "";
    }

    @Override
    public JsonConfigurationOptions options() {
        if (options == null) {
            options = new JsonConfigurationOptions(this);
        }

        return (JsonConfigurationOptions) options;
    }
}