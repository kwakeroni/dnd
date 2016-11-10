package active.engine.gui.config;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public enum Directories implements Setting<Path> {
    IMPORT_PARTY_DIRECTORY("directory.in.party"),
    LOAD_FIGHT_DIRECTORY("directory.in.fight"),
    SAVE_FIGHT_DIRECTORY("directory.out.fight")
    ;

    private final String key;


    Directories(String key) {
        this.key = key;
    }


    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public Path fromString(String string) {
        return Paths.get(string);
    }

    @Override
    public String toString(Path value) {
        return value.toString();
    }
}
