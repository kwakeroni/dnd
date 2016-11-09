package active.engine.gui.swing.fight;

import static active.engine.gui.swing.support.builder.SwingBuilders.*;
import static java.util.stream.Collectors.*;

import active.engine.gui.swing.comp.JRollField;
import active.engine.gui.swing.support.ContainerAdapter;
import active.engine.gui.swing.support.builder.EventListenerBuilder;
import active.model.cat.Hittable;
import active.model.die.D20;
import active.model.die.Dice;
import active.model.die.Die;
import active.model.effect.Attack;
import active.model.effect.Damage;
import active.model.event.Reaction;
import active.model.value.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.*;
import java.util.function.Supplier;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class AttackLine implements ContainerAdapter {

    final Reaction atkRoll;
    final Reaction dmgRoll;
    final Supplier<Optional<Damage>> dmg;
    final Collection<Component> components;

    private AttackLine(Reaction atkRoll, Reaction dmgRoll, Supplier<Optional<Damage>> dmg, Supplier<? extends Component>... components) {
        this.atkRoll = atkRoll;
        this.dmgRoll = dmgRoll;
        this.dmg = dmg;
        this.components = Arrays.stream(components).map(cb -> (Component) cb.get()).collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    private AttackLine(Reaction atkRoll, Reaction dmgRoll, Supplier<Optional<Damage>> dmg, Component... components) {
        this.atkRoll = atkRoll;
        this.dmgRoll = dmgRoll;
        this.dmg = dmg;
        this.components = Collections.unmodifiableList(Arrays.asList(components));
    }

    public Optional<Damage> getDamage(){
        return this.dmg.get();
    }

    public void rollAttack() {
        this.atkRoll.react();
    }

    public void rollDamage(){
        this.dmgRoll.react();
    }

    @Override
    public Collection<? extends Component> components() {
        return this.components;
    }

    public static <D extends Die> AttackLine ofAttack(Attack<D> attack, Hittable target) {

        JRollField<D20> rollField = new JRollField<>(Dice.D20);
        JRollField<D> damageField = new JRollField<>(attack.getDamageDie());
        CalculationChain<D> chain = new CalculationChain<>(
                attack, target,
                rollField, damageField,
                textField().columns(3).bg(brighter(rollField.component().getBackground(), 1.05f)).build()
        );



        return new AttackLine(
                chain.rollField()::roll,
                chain.damageField()::roll,
                () -> chain.damageField().getRoll().map(attack::getDamage),
                label(attack.getName()),
                chain.rollField()::component,
                label(attack.getAttackBonus() + "="),
                chain::attackField,
                label("<=>"),
                label(target.getAC()),
                label("(" + target.getName() + ")"),
                chain::hitLabel,
                label(" ==> "),
                chain.damageField()::component
        );
    }

    public static AttackLine ofRollAll(Reaction rollAllATK, Reaction rollAllDMG) {
        JButton atkButton = new JButton("+");
        atkButton.setMargin(new Insets(2, 2, 2, 2));
        JButton dmgButton = new JButton("+");
        dmgButton.setMargin(new Insets(2, 2, 2, 2));

        EventListenerBuilder
                .of(atkButton)
                .onAction(rollAllATK);

        EventListenerBuilder
                .of(dmgButton)
                .onAction(rollAllDMG);

        return new AttackLine(
                rollAllATK,
                rollAllDMG,
                Optional::empty,
                null,
                atkButton,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                dmgButton
        );
    }

    private static Color brighter(Color color, float factor) {
        return new Color(
                multiply(color.getRed(), factor),
                multiply(color.getGreen(), factor),
                multiply(color.getBlue(), factor),
                color.getAlpha()
        );
    }

    private static int multiply(int value, float factor) {
        return Math.min(255, (int) (((float) value) * factor));
    }

    private static class CalculationChain<D extends Die> {

        private final JRollField<D20> rollField;
        private final TextField attackField;
        private final JLabel hitLabel;
        private final JRollField<D> damageField;

        public CalculationChain(Attack<?> attack, Hittable target, JRollField<D20> rollField, JRollField<D> dmgField, TextField attackField) {
            JLabel hitLabel = new JLabel("miss");

            this.attackField = attackField;
            this.rollField = rollField;
            this.hitLabel = hitLabel;
            this.damageField = dmgField;

            rollField.onRoll(() ->
                    attackField.setText(
                            rollField.getRoll()
                                    .map(score -> score.modify(attack.getAttackBonus()))
                                    .map(Score::toString)
                                    .orElse(""))
            );
            attackField.addTextListener(new TextListener() {
                @Override
                public void textValueChanged(TextEvent e) {
                    System.out.println("text value changed");

                    boolean hit = parseSafe(attackField.getText())
                                    .map(i -> i >= target.getAC().toInt())
                                    .orElse(false);

                    hitLabel.setText(hit? "HIT" : "miss");

                    if (hit){
                        damageField.roll();
                    } else {
                        damageField.clear();
                    }
                }
            });
            EventListenerBuilder.of(attackField);
        }

        public JRollField<D20> rollField() {
            return this.rollField;
        }

        public TextField attackField() {
            return this.attackField;
        }

        public JLabel hitLabel(){
            return this.hitLabel;
        }

        public JRollField<D> damageField(){
            return this.damageField;
        }
    }

    private static Optional<Integer> parseSafe(String text) {
        try {
            if (text == null || text.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(Integer.valueOf(text));
        } catch (NumberFormatException exc){
            return Optional.empty();
        }
    }
}
