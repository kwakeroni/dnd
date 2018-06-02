package be.kwakeroni.dnd.event.impl;

import be.kwakeroni.dnd.event.Channel;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class ChannelAdapter<T> extends Pipeline<T,T> implements Channel.Entry<T> {

            @Override
            public void accept(T t) {
                    forward(t);
            }

}
