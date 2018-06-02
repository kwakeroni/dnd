package be.kwakeroni.dnd.engine.api.command.io;

import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.util.function.Pair;

import java.io.File;
import java.util.Collection;

public interface IOCommandFactory {

    public Command.WithOptionalResult<File> selectFile(FileSelection selection);

    public Command.WithResult<Collection<Party>> importParties(File file);

    public Command.WithResult<Fight> importFight(File file);

    public Command exportFight(Fight fight, File file);

    public default Command exportFight(Pair<Fight, File> pair) {
        return exportFight(pair.getA(), pair.getB());
    }

    public interface FileSelection {
        public String getKind();

        public String getTitle();

        public String getActionText();

        public static FileSelection of(String kind, String title, String actionText){
            return new FileSelection() {
                @Override
                public String getKind() {
                    return kind;
                }

                @Override
                public String getTitle() {
                    return title;
                }

                @Override
                public String getActionText() {
                    return actionText;
                }
            };
        }
    }

}
