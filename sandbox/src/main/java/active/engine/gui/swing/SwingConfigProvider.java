package active.engine.gui.swing;

import active.engine.gui.config.Config;
import active.engine.gui.config.ConfigProvider;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class SwingConfigProvider {

    private static final Config CONFIG = ConfigProvider.fromDefaultConfigFileOrInMemoryFallback();

    public static Config getConfig(){
        return CONFIG;
    }

}
