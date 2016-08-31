package active.engine.gui.swing.support.builder;

import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class BorderLayoutBuilder implements LayoutBuilder {

    private final Container container;

    private BorderLayoutBuilder(Container container){
        this.container = container;
        this.container.setLayout(new BorderLayout());
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public BorderLayoutBuilder west(Component component){
        container.add(component, BorderLayout.WEST);
        return this;
    }

    public BorderLayoutBuilder west(LayoutBuilder content){
        return west(content.getContainer());
    }

    public BorderLayoutBuilder east(Component component){
        container.add(component, BorderLayout.EAST);
        return this;
    }

    public BorderLayoutBuilder east(LayoutBuilder content){
        return east(content.getContainer());
    }

    public BorderLayoutBuilder north(Component component){
        container.add(component, BorderLayout.NORTH);
        return this;
    }

    public BorderLayoutBuilder north(LayoutBuilder content){
        return north(content.getContainer());
    }

    public BorderLayoutBuilder south(Component component){
        container.add(component, BorderLayout.SOUTH);
        return this;
    }

    public BorderLayoutBuilder south(LayoutBuilder content){
        return south(content.getContainer());
    }

    public BorderLayoutBuilder center(Component component){
        container.add(component, BorderLayout.CENTER);
        return this;
    }

    public BorderLayoutBuilder center(LayoutBuilder content){
        return center(content.getContainer());
    }

    public static BorderLayoutBuilder borderLayout(Container container){
        return new BorderLayoutBuilder(container);
    }
}
