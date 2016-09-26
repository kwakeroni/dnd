package active.engine.gui.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import active.engine.command.CommandHandler;
import active.engine.event.EventBroker;
import active.engine.gui.InteractionHandler;
import active.engine.gui.swing.support.ContainerAdapter;
import active.engine.util.gui.swing.WindowAdapter;
import active.model.cat.Hittable;
import active.model.fight.Participant;
import active.model.fight.command.Attack;
import active.model.fight.event.FightData;

public class EngineWindow implements InteractionHandler, ContainerAdapter {

    private final JFrame frame;
    private final JCharacterList characterList;

    public EngineWindow(CommandHandler appCommandHandler, FightData fightData, EventBroker<?> events){
        CommandHandler context = appCommandHandler;

        this.frame = new JFrame();
        this.frame.setMinimumSize(new Dimension(640,480));
        this.frame.setTitle("Dungeons and Dragons 3.5 Fight Assistant");
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frame.addWindowListener(WindowAdapter.closing(event -> showCloseDialog()));

        this.characterList = new JCharacterList(fightData);
        this.frame.getContentPane().add(this.characterList.component(), BorderLayout.CENTER);

        JPanel rightPane = new JPanel();
        this.frame.getContentPane().add(rightPane, BorderLayout.EAST);

        rightPane.add(new JFightActionPane(context, fightData).component(), BorderLayout.WEST);
//        rightPane.add(new LogPane(events).component(), BorderLayout.EAST);

        context.registerContext(InteractionHandler.class, this);

        this.frame.setJMenuBar(new FightMenu(appCommandHandler).component());
    }
    
    public void show(){
        this.frame.pack();
        this.frame.setVisible(true);
    }
    
    private void close(){
        this.frame.setVisible(false);
        this.frame.dispose();
    }
    
    private void showCloseDialog(){
        int choice = JOptionPane.showOptionDialog(this.frame, "Do you really want to quit ?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (choice == JOptionPane.YES_OPTION){
            this.close();
        }
    }

    @Override
    public Stream<? extends Component> componentStream() {
        return Arrays.stream(this.frame.getContentPane().getComponents());
    }

    @Override
    public Collection<? extends Component> components() {
        return Arrays.asList(this.frame.getContentPane().getComponents());
    }

    @Override
    public void withParticipant(Consumer<Participant> action) {
        Snapshot snapshot = modify(c -> (c == this.characterList.component()), Component::isEnabled, Component::setEnabled);
        this.characterList.withParticipant(action.andThen(p -> snapshot.restore()));
    }

    @Override
    public void requestAttackData(Attack.AttackData attack) {
        new AttackWindow(this.frame, attack);
    }

    public void click(String name) {
        SwingUtilities.invokeLater(() -> {
        click(name, this.frame);
    });

    }

    private void click(String name, Window window){

        boolean found = false;
        List<Component> components = getComponents(window);
        for (int i=0; !found && i<components.size(); i++){
            found = click(components.get(i), name);
        }


        if (!found) {
            System.out.println("not found " + name);
            throw new IllegalStateException("Not found component with name " + name);
        }

    }

    private List<Component> getComponents(Window window){
        List<Component> list = new ArrayList<>(3);
        if (window instanceof RootPaneContainer){
            list.add(((RootPaneContainer) window).getContentPane());
        }
        list.add(window);
        Component modal = getModal(window);
        if (modal != null){
            list.add(modal);
        }
        return list;
    }


    private Component getModal(Window window){
        try {
            Field field = Window.class.getDeclaredField("modalBlocker");
            field.setAccessible(true);
            Component c = (Component) field.get(window);
            return c;
        } catch (Exception exc) {
            return null;
        }
    }

    private boolean click(Component component, String name){
        if (component == null){
            return false;
        }
        String cname = getName(component);

        if (name.equals(cname)){
            click(component);
            return true;
        } else {
            return clickChild(component, name);
        }
    }

    private boolean clickChild(Component parent, String name){
        if (parent instanceof JList<?>){
            JList<Object> list = (JList<Object>) parent;
            ListModel<?> model = list.getModel();
            int index=-1;
            for (int i=0; index<0 && i<model.getSize(); i++){
                if (name.equals(String.valueOf(model.getElementAt(i)))){
                    index = i;
                }
            }
            list.ensureIndexIsVisible(index);
            Point p = list.indexToLocation(index);
            if (p != null) {
                click(list, list.indexToLocation(index));
                return true;
            } else {
                System.out.println("Could not click on " + model.getElementAt(index));
            }
        }
        if (parent instanceof Container){
            for (Component child : ((Container) parent).getComponents()){
                if(click(child, name)){
                    return true;
                }
            }
        }
        return false;
    }

    private String getName(Component component){
            if (component instanceof AbstractButton) return ((AbstractButton) component).getText();
            if (component instanceof JLabel) return ((JLabel) component).getText();
            if (component instanceof JTextComponent) return ((JTextComponent) component).getText();
            if (component instanceof TextComponent) return ((TextComponent) component).getText();
        return component.getClass().getName() + "@" + System.identityHashCode(component);
    }

    private static final Point POINT_ZERO = new Point(0,0);

    private void click(Component component){
        click(component, POINT_ZERO);
    }
    private void click(Component component, Point point){
        MouseEvent onPress = mouseEvent(component, MouseEvent.MOUSE_PRESSED, point);
        MouseEvent onRelease = mouseEvent(component, MouseEvent.MOUSE_RELEASED, point);
        MouseEvent onClick = mouseEvent(component, MouseEvent.MOUSE_CLICKED, point);
        Stream.of(component.getMouseListeners()).forEach(l -> l.mousePressed(onPress));
        Stream.of(component.getMouseListeners()).forEach(l -> l.mouseReleased(onRelease));
        Stream.of(component.getMouseListeners()).forEach(l -> l.mouseClicked(onClick));
    }



    private MouseEvent mouseEvent(Component component, int type, Point point){
        return new MouseEvent(component, type, System.currentTimeMillis(), MouseEvent.BUTTON1_MASK, (int) point.getX(), (int) point.getY(), 1, false);

    }
}
