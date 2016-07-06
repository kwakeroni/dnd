package active.model.fight.command;

import active.engine.command.Command;
import active.engine.command.CommandContext;
import active.engine.gui.InteractionHandler;
import active.model.cat.Hittable;
import active.model.fight.Participant;

import java.util.function.Function;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class SelectParticipant<T extends Participant> implements Command {

    private Function<? super T, ? extends Command> followUpAction;

    public SelectParticipant(Function<? super T, ? extends Command> followUpAction) {
        this.followUpAction = followUpAction;
    }

    @Override
    public void execute(CommandContext context) {
        context.getContext(InteractionHandler.class).withParticipant(participant -> {
            followUpAction.apply((T) participant).execute(context);
        });


    }

    public static <PH extends Hittable & Participant> SelectParticipant<PH> hittable(Function<? super Hittable, ? extends Command> folluwUpAction){
        return new SelectParticipant<>(folluwUpAction);
    }


}
