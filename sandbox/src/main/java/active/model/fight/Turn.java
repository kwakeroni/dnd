package active.model.fight;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Turn {

    public <AP extends Participant & IsActor> AP getActor();

}
