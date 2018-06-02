package be.kwakeroni.dnd.ui.base.config;

import java.util.Properties;

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
