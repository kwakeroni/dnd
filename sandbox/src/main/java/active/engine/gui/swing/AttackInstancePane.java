package active.engine.gui.swing;

import active.engine.gui.swing.support.TableLayout;
import active.engine.internal.action.type.AttackActionType;
import active.model.cat.Hittable;
import active.model.effect.Attack;
import active.model.fight.command.Attack.AttackData;

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

    public AttackInstancePane(AttackActionType attackType, Hittable selectedTarget){
        this.panel = new JPanel();
        TableLayout layout = new TableLayout(this.panel, false, 5, 0);
        this.panel.setLayout(layout);

        this.attacks = new ArrayList<>(attackType.getAttacks().size());

        TableLayout.addRow(this.panel, AttackLine.ofRollAll(this::rollAllAttack, this::rollAllDamage));

        for (Attack<?> attack : attackType.getAttacks()){
            AttackLine attackLine = AttackLine.ofAttack(attack, selectedTarget);
            this.attacks.add(attackLine);
            TableLayout.addRow(this.panel, attackLine);
        }

    }

    private void rollAllAttack(){
        for (AttackLine attack : attacks){
            attack.rollAttack();
        }
    }

    private void rollAllDamage(){
        for (AttackLine attack : attacks){
            attack.rollDamage();
        }
    }

    void addToAttackData(AttackData attack){
        for (AttackLine single : attacks){
            single.getDamage().ifPresent(
              dmg -> attack.addHit(attack.getTarget(), dmg)
            );

        }
    }

    public Component component(){
        return this.panel;
    }

}
