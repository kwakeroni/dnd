package active.engine.gui.swing.support;

import javax.swing.*;
import java.awt.*;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class TableLayout implements LayoutManager2 {

    private final GridBagLayout delegate;
    private final Container container;
    private int nbRows;
    private int nbColumns;
    private Dimension minSize = new Dimension(0,0);
    private GridBagConstraints defaults = new GridBagConstraints();

    public TableLayout(Container container) {
        this.container = container;
        this.delegate = new GridBagLayout();
        this.nbRows = 0;
        this.nbColumns = 0;
    }

    public TableLayout(Container container, boolean inner, int padX, int padY) {
        this(container);
        if (inner){
           this.defaults.ipadx = padX;
            this.defaults.ipady = padY;
        } else {
            this.defaults.insets = new Insets(padY, padX, padY, padX);
        }
    }

    private void requireMinHeight(int height){
        if (this.minSize.height < height){
            this.minSize.height = height;
        }
//        System.out.println("Require min " + this.minSize);
    }

    private void requireMinWidth(int width){
        if (this.minSize.width < width){
            this.minSize.width = width;
        }
//        System.out.println("Require min " + this.minSize);
    }

    public void addRow(Iterable<? extends Component> components){
        int row = nbRows++;
        int col=0;
        int width = 0;
        for (Component component : components){
            if (component != null) {
                width = width + component.getMinimumSize().width;
                requireMinHeight(this.minSize.height + component.getMinimumSize().height);
                this.container.add(component, atCell(row, col));
            }
            col++;
        }
        requireMinWidth(width);
    }

    public void addFillerRow(){
        this.container.add(new JLabel(""), spacer(this.nbRows, this.nbColumns+1));
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        TableCellConstraint constraint = (TableCellConstraint) constraints;
        this.nbRows = Math.max(this.nbRows, constraint.getRow());
        this.nbColumns = Math.max(this.nbColumns, constraint.getColumn());
        delegate.addLayoutComponent(comp, toGridBagConstraint(constraint));
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        delegate.addLayoutComponent(name, comp);
    }

//    public GridBagConstraints getConstraints(Component comp) {
//        return delegate.getConstraints(comp);
//    }

    @Override
    public float getLayoutAlignmentX(Container parent) {
        return delegate.getLayoutAlignmentX(parent);
    }

    @Override
    public float getLayoutAlignmentY(Container parent) {
        return delegate.getLayoutAlignmentY(parent);
    }

//    public int[][] getLayoutDimensions() {
//        return delegate.getLayoutDimensions();
//    }
//
//    public Point getLayoutOrigin() {
//        return delegate.getLayoutOrigin();
//    }
//
//    public double[][] getLayoutWeights() {
//        return delegate.getLayoutWeights();
//    }

    @Override
    public void invalidateLayout(Container target) {
        delegate.invalidateLayout(target);
    }

    @Override
    public void layoutContainer(Container parent) {
        delegate.layoutContainer(parent);
    }

//    public Point location(int x, int y) {
//        return delegate.location(x, y);
//    }
//
    @Override
    public Dimension maximumLayoutSize(Container target) {
        return delegate.maximumLayoutSize(target);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return delegate.minimumLayoutSize(parent);
//        Dimension dSize = delegate.minimumLayoutSize(parent);
//        if (this.minSize.width < dSize.width){
//            this.minSize.width = dSize.width;
//        }
//        if (this.minSize.height < dSize.height){
//            this.minSize.height = dSize.height;
//        }
//
//
//        return this.minSize;
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return delegate.preferredLayoutSize(parent);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        delegate.removeLayoutComponent(comp);
    }



//    public void setConstraints(Component comp, GridBagConstraints constraints) {
//        delegate.setConstraints(comp, constraints);
//    }
//

    private GridBagConstraints toGridBagConstraint(TableCellConstraint constraint){
        if(constraint.constraints.fill == -1){
            constraint.constraints.fill = GridBagConstraints.HORIZONTAL;
            constraint.constraints.insets = defaults.insets;
            constraint.constraints.ipadx = defaults.ipadx;
            constraint.constraints.ipady = defaults.ipady;
        }
        return constraint.constraints;
    }

    public static class TableCellConstraint {
        private final GridBagConstraints constraints;

        public TableCellConstraint(int row, int column){
            this.constraints = new GridBagConstraints();
            this.constraints.gridx = column;
            this.constraints.gridy = row;
            this.constraints.fill = -1;

        }

        private TableCellConstraint(GridBagConstraints constraints){
            this.constraints = constraints;
        }

        public int getRow(){
            return this.constraints.gridx;
        }

        public int getColumn(){
            return this.constraints.gridy;
        }
    }

    public static TableCellConstraint atCell(int row, int column){
        return new TableCellConstraint(row, column);
    }

    public static void addRow(Container container, Iterable<? extends Component> components){
        ((TableLayout) container.getLayout()).addRow(components);
    }

    public static void addFillerRow(Container container){
        ((TableLayout) container.getLayout()).addFillerRow();
    }

    private TableCellConstraint spacer(int row, int column) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = column;
        c.gridy = row;
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.insets = INSETS;
        c.weightx = 1.0;
        c.weighty = 1.0;
        return new TableCellConstraint(c);
    }
}
