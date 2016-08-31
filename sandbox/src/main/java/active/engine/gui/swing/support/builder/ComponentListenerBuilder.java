package active.engine.gui.swing.support.builder;

import active.model.event.Reaction;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface ComponentListenerBuilder<C extends Component, B extends ComponentListenerBuilder<C, B>> {

    C component();

    B self();

    default B onKeyPressed(Reaction reaction){
        return onKeyPressed(reaction.asConsumer());
    }

    default B onKeyPressed(Consumer<? super KeyEvent> action){
        component().addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                action.accept(e);
            }
        });
        return self();
    }

    default B onKeyReleased(Reaction reaction){
        return onKeyReleased(reaction.asConsumer());
    }

    default B onKeyReleased(Consumer<? super KeyEvent> action){
        component().addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                action.accept(e);
            }
        });
        return self();
    }

    default B onKeyTyped(Reaction reaction){
        return onKeyTyped(reaction.asConsumer());
    }

    default B onKeyTyped(Consumer<? super KeyEvent> action){
        component().addKeyListener(new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent e) {
                action.accept(e);
            }
        });
        return self();
    }

}
