package active.engine.gui.swing.action;

import active.engine.command.CommandHandler;
import active.engine.gui.business.IOActions;
import active.engine.gui.config.Directories;
import active.engine.gui.swing.GUIController;
import active.engine.gui.swing.ImportFileBuilder;
import active.engine.gui.swing.SwingConfigProvider;
import active.engine.gui.swing.fight.FightPane;
import active.engine.internal.cat.DecoratedDescription;
import active.engine.internal.fight.DefaultFight;
import active.io.xml.XMLInput;
import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.event.FightData;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static active.engine.gui.swing.ImportFileBuilder.selectFile;
import static active.engine.gui.swing.action.FightDumpUtil.dump;
import static active.engine.gui.swing.support.KeySupport.ctrl;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class SwingFightActions {

    private static FightController resumeFight(GUIController gui, FightController fight, Consumer<FightController> initializer){
        CommandHandler handler = gui.getCommandHandler();

        DecoratedDescription.Builder log = FightDumpUtil.log();
        fight.on().action()
                .forEach(action -> {
                    System.out.println(log.newDescription().append(action.getAction()).toString());
                });

        initializer.accept(fight);

        dump("Actors", fight.getState().getActors());
        dump("Targets", fight.getState().getTargets());
        dump("Start", fight);

        handler.registerContext(FightController.class, fight);

        gui.setContent(new FightPane(fight.getData(), gui));

        return fight;
    }

    public static Action newFight(GUIController gui) {
        return ActionBuilder.action("New Fight")
                .withMnemonic('N')
                .withAccelerator(ctrl('N'))
                .doing(() -> resumeFight(gui, gui.getBattleField().startFight(), fight -> {}));
    }

    public static Action endFight(GUIController gui){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.clearContent(null);
            }
        };
    }

    public static Action loadFight(GUIController gui){
        return ActionBuilder.action("Open Fight")
                .withMnemonic('O')
                .withAccelerator(ctrl('O'))
                .doing(() -> {
                    selectFile()
                            .forInput()
                            .forWindow(gui.getAncestorWindow())
                            .withTitle("Select Fight File")
                            .withButton("Open")
                            .withStartDirectory(SwingConfigProvider.getConfig().get(Directories.LOAD_FIGHT_DIRECTORY, Directories.SAVE_FIGHT_DIRECTORY))
                            .andThen(file -> {
                                SwingConfigProvider.getConfig().set(Directories.LOAD_FIGHT_DIRECTORY, file.getParentFile().toPath());
                                DefaultFight fight = IOActions.importFight(file);
                                resumeFight(gui, gui.getBattleField().resumeFight(fight, f -> {
                                }), f -> {
                                });
                            });
                });
    }

    public static Action exportFight(Supplier<Component> parent, Supplier<Fight> fight){
        Action exportAs = exportFightAs(parent, fight);
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Optional<File> current = SwingConfigProvider.getLocalProperty(SwingConfigProvider.Local.CURRENT_FIGHT_FILE);
                if (current.isPresent()){
                    IOActions.exportFight(current.get(), fight.get());
                } else {
                    exportAs.actionPerformed(e);
                }
            }
        };
    }


    public static Action exportFightAs(Supplier<Component> parent, Supplier<Fight> fight) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile()
                        .forOutput()
                        .forWindow(parent.get())
                        .withTitle("Save Fight")
                        .withButton("Save")
                        .withStartDirectory(SwingConfigProvider.getConfig().get(Directories.SAVE_FIGHT_DIRECTORY))
                        .andThen(file -> {
                            SwingConfigProvider.getConfig().set(Directories.SAVE_FIGHT_DIRECTORY, file.getParentFile().toPath());
                            IOActions.exportFight(file, fight.get());
                            SwingConfigProvider.setLocalProperty(SwingConfigProvider.Local.CURRENT_FIGHT_FILE, file);
                        });
            }
        };
    }

    public static Action importPartyFile(Supplier<Component> parent, Supplier<CommandHandler> handler) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile()
                        .forInput()
                        .forWindow(parent.get())
                        .withTitle("Select Party File")
                        .withButton("Import")
                        .withStartDirectory(SwingConfigProvider.getConfig().get(Directories.IMPORT_PARTY_DIRECTORY))
                        .andThen(file -> {
                            SwingConfigProvider.getConfig().set(Directories.IMPORT_PARTY_DIRECTORY, file.getParentFile().toPath());
                            IOActions.importParties(file, handler.get());
                        } );
            }
        };
    }

    public static final KeyListener KEY_LISTENER = new KeyListener() {
        private boolean debug = false;
        @Override
        public void keyTyped(KeyEvent e) {
            dump("T>", e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            dump("D>", e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            dump("U>", e);
        }

        private void dump(String prefix, KeyEvent e){
            if (debug){
                System.out.println(prefix + " " + toString(e));
            }
        }

        private String toString(KeyEvent e){
            return (e.isControlDown()?"ctrl ":"") + (e.isShiftDown()?"shift ":"") + ((char) e.getKeyCode()) + " ("+e.getKeyCode()+")" + " | " + e.getKeyChar() + " (" + ((int) e.getKeyChar()) + ")" ;
        }
    };



}
