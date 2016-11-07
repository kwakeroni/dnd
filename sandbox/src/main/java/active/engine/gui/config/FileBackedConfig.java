package active.engine.gui.config;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
class FileBackedConfig extends Config {

    private final Path configFile;

    FileBackedConfig(Path configFile, Properties configProperties) {
        super(configProperties);
        this.configFile = configFile;
    }

    @Override
    public <T> void set(Setting<T> setting, T value) {
        super.set(setting, value);
        try {
            ConfigProvider.store(this.configFile, getProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
