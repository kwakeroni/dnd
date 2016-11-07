package active.engine.gui.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class ConfigProvider {




    public static Config fromDefaultConfigFileOrInMemoryFallback() {
        try {
            return fromDefaultConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
            return new Config(defaultProperties());
        }
    }

    public static Config fromDefaultConfigFile() throws IOException {
        Path configFile = getDefaultConfigFile();

        try {
            return new FileBackedConfig(configFile, load(configFile));

        } catch (IOException exc) {
            exc.printStackTrace();

            return new FileBackedConfig(configFile, createDefaultPropertiesFile(configFile));
        }
    }

    private static Path getDefaultConfigFile() {
        Path userDir = Paths.get(System.getProperty("user.dir"));
        return userDir.resolve(".dnd-engine.cfg");
    }

    private static Properties load(Path path) throws IOException {
        try (InputStream stream = Files.newInputStream(path)) {
            Properties properties = new Properties();
            properties.load(stream);
            return properties;
        }
    }

    static void store(Path path, Properties properties) throws IOException {
        try (OutputStream stream = Files.newOutputStream(path)) {
            properties.store(stream, "DND Engine Config");
        }
    }

    private static Properties createDefaultPropertiesFile(Path path) throws IOException {
        Properties properties = defaultProperties();
        store(path, properties);
        return properties;
    }

    private static Properties defaultProperties(){
        Properties properties = new Properties();
        return properties;
    }


}
