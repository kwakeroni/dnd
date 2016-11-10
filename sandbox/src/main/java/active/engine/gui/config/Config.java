package active.engine.gui.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Config {

    private Properties properties;

    Config(Properties properties) {
        this.properties = properties;
        System.out.println("Config of type " + this.getClass().getName());
    }

    public <T> T get(Setting<T> setting){
        return setting.fromNullableString(this.properties.getProperty(setting.getKey()));
    }

    public <T> T get(Setting<T>... settings){
        return Stream.of(settings)
                .filter(setting -> this.properties.containsKey(setting.getKey()))
                .map(this::get)
                .findFirst()
                .orElse(null);
    }


    public <T> void set(Setting<T> setting, T value){
        if (value == null){
            this.properties.remove(setting.getKey());
        } else {
            this.properties.setProperty(setting.getKey(), setting.toString(value));
        }
    }

    protected Properties getProperties(){
        return this.properties;
    }
}
