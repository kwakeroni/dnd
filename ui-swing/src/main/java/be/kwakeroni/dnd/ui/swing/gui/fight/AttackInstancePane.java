package be.kwakeroni.dnd.ui.swing.gui.fight;

import be.kwakeroni.dnd.engine.api.InteractionHandler;
import be.kwakeroni.dnd.model.effect.Attack;
import be.kwakeroni.dnd.model.fight.AttackActionType;
import be.kwakeroni.dnd.model.target.Hittable;
import be.kwakeroni.dnd.ui.swing.gui.support.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class AttackInstancePane {

    JPanel panel;
    List<AttackLine> attacks;

    public AttackInstancePane(AttackActionType attackType, Hittable selectedTarget) {
        this.panel = new JPanel();
        TableLayout layout = new TableLayout(this.panel, false, 5, 0);
        this.panel.setLayout(layout);

        this.attacks = new ArrayList<>(attackType.getAttacks().size());

        TableLayout.addRow(this.panel, AttackLine.ofRollAll(this::rollAllAttack, this::rollAllDamage));

        for (Attack<?> attack : attackType.getAttacks()) {
            AttackLine attackLine = AttackLine.ofAttack(attack, selectedTarget);
            this.attacks.add(attackLine);
            TableLayout.addRow(this.panel, attackLine);
        }

    }

    private void rollAllAttack() {
        for (AttackLine attack : attacks) {
            attack.rollAttack();
        }
    }

    private void rollAllDamage() {
        for (AttackLine attack : attacks) {
            attack.rollDamage();
        }
    }

    void addToAttackData(InteractionHandler.InteractiveAttackData attack) {
        for (AttackLine single : attacks) {
            single.getDamage().ifPresent(
                    dmg -> attack.addHit(attack.getTarget(), dmg)
            );

        }
    }

    public Component component() {
        return this.panel;
    }

}
