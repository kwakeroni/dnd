package be.kwakeroni.dnd.ui.swing.gui;

import be.kwakeroni.dnd.ui.base.config.Config;
import be.kwakeroni.dnd.ui.base.config.ConfigProvider;

import java.util.*;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class SwingConfigProvider {



    private static final Map<Local, Object> LOCAL = Collections.synchronizedMap(new HashMap<>());

    private static final Config CONFIG = ConfigProvider.fromDefaultConfigFileOrInMemoryFallback();

    public static Config getConfig(){
        return CONFIG;
    }

    public static <T> Optional<T> getLocalProperty(Local name){
        return Optional.ofNullable((T) LOCAL.get(name));
    }

    public static void setLocalProperty(Local name, Object value){
        LOCAL.put(name, value);
    }

    public static void clearLocalProperty(Local name){
        LOCAL.remove(name);
    }

    public enum Local {
    }

}
