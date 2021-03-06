package be.kwakeroni.dnd.event;

import be.kwakeroni.dnd.event.impl.ChannelEntryOp;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

// Kind of push-driven Stream
public interface Channel<T> {

    /**
     * Returns a stream consisting of the elements of this stream that match
     * the given predicate.
     * <p>
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param predicate a <a href="package-summary.html#NonInterference">
     *                  non-interfering, stateless</a> predicate to apply to
     *                  each element to determine if it should be included
     * @return the new stream
     */
    Channel<T> filter(Predicate<? super T> predicate);

    /**
     * Returns a stream consisting of the results of applying the given
     * function to the elements of this stream.
     * <p>
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param <R>    The element type of the new stream
     * @param mapper a <a href="package-summary.html#NonInterference">
     *               non-interfering, stateless</a> function to apply to each
     *               element
     * @return the new stream
     */
    <R> Channel<R> map(Function<? super T, ? extends R> mapper);

    /*
     * Returns a stream consisting of the results of replacing each element of
     * this stream with the contents of the stream produced by applying the
     * provided mapping function to each element.  (If the result of the mapping
     * function is {@code null}, this is treated as if the result was an empty
     * stream.)
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @apiNote
     * The {@code flatMap()} operation has the effect of applying a one-to-many
     * tranformation to the elements of the stream, and then flattening the
     * resulting elements into a new stream. For example, if {@code orders}
     * is a stream of purchase orders, and each purchase order contains a
     * collection of line items, then the following produces a stream of line
     * items:
     * <pre>{@code
     *     orderStream.flatMap(order -> order.getLineItems().stream())...
     * }</pre>
     *
     * @param <R> The element type of the new stream
     * @param mapper a <a href="package-summary.html#NonInterference">
     *               non-interfering, stateless</a> function to apply to each
     *               element which produces a stream of new values
     * @return the new stream
     */

//    <R> Channel<R> flatMap(Function<? super T, ? extends Channel<? extends R>> mapper);

    /*
     * Returns a stream consisting of the distinct elements (according to
     * {@link Object#equals(Object)}) of this stream.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">stateful
     * intermediate operation</a>.
     *
     * @return the new stream
     */
    // Events are per definition distinct ?
//    Stream<T> distinct();

    /**
     * Returns a stream consisting of the elements of this stream, truncated
     * to be no longer than {@code maxSize} in length.
     * <p>
     * <p>This is a <a href="package-summary.html#StreamOps">short-circuiting
     * stateful intermediate operation</a>.
     *
     * @param maxSize the number of elements the stream should be limited to
     * @return the new stream
     * @throws IllegalArgumentException if {@code maxSize} is negative
     */
    Channel<T> limit(long maxSize);

    /**
     * Returns a stream consisting of the remaining elements of this stream
     * after discarding the first {@code n} elements of the stream.
     * If this stream contains fewer than {@code n} elements then an
     * empty stream will be returned.
     * <p>
     * <p>This is a <a href="package-summary.html#StreamOps">stateful
     * intermediate operation</a>.
     *
     * @param n the number of leading elements to skip
     * @return the new stream
     * @throws IllegalArgumentException if {@code n} is negative
     */
    Channel<T> skip(long n);

    Channel<T> peek(Consumer<? super T> action);

    /**
     * Performs an action for each element of this stream.
     * <p>
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     * <p>
     * <p>For parallel stream pipelines, this operation does <em>not</em>
     * guarantee to respect the encounter order of the stream, as doing so
     * would sacrifice the benefit of parallelism.  For any given element, the
     * action may be performed at whatever time and in whatever thread the
     * library chooses.  If the action accesses shared state, it is
     * responsible for providing the required synchronization.
     *
     * @param action a <a href="package-summary.html#NonInterference">
     *               non-interfering</a> action to perform on the elements
     */
    void forEach(Consumer<? super T> action);

    Switch<T> choice();

    static <T> Channel.Entry<T> newInstance() {
        return new ChannelEntryOp<T>();
    }

    public static interface Switch<T> {
        public Channel<T> when(Predicate<? super T> predicate);

        public Channel<T> otherwise();

        public Channel<T> afterwards();
    }

    public static interface Entry<T> extends Channel<T>, Consumer<T> {

    }
}
