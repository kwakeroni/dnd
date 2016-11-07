package active.engine.gui.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Config {

    private Properties properties;

    Config(Properties properties) {
        this.properties = properties;
    }

    public <T> T get(Setting<T> setting){
        return setting.fromNullableString(this.properties.getProperty(setting.getKey()));
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
