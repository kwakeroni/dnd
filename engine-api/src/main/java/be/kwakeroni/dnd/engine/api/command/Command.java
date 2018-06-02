package be.kwakeroni.dnd.engine.api.command;

import be.kwakeroni.dnd.util.function.Pair;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface Command {

    public void execute(CommandContext context);

    public default Command andThen(Supplier<? extends Command> nextCommandSupplier) {
        return context -> {
            this.execute(context);
            Command nextCommand = nextCommandSupplier.get();
            nextCommand.execute(context);
        };
    }

    public default <CombinedCommand extends Command> CombinedCommand andThen(Combiner<? extends CombinedCommand> combiner) {
        return combiner.executeAfter(this::execute);
    }

    public static Command combine(List<? extends Command> commands) {
        return context -> {
            for (Command command : commands) {
                command.execute(context);
            }
        };
    }

    public static <Input, C extends Command> Function<Input, C> execute(Function<Input, C> function) {
        return function;
    }

    @FunctionalInterface
    public interface Combiner<CombinedCommand extends Command> {
        public CombinedCommand executeAfter(Consumer<CommandContext> firstCommand);
    }

    @FunctionalInterface
    public interface WithResultCombiner<FirstResult, CombinedCommand extends Command> {
        public CombinedCommand executeAfter(Function<CommandContext, FirstResult> firstCommand);
    }

//    public interface SpecialCombiner<FirstResult, CombinedCommand extends Command> {
//        CombinedCommand createPlaceHolder(Consumer<CommandContext> run);
//        CombinedCommand getNextCommand(FirstResult firstResult);
//    }

    @FunctionalInterface
    public interface WithFollowUp<FutureResult> extends Command {

        @Override
        default void execute(CommandContext context) {
            executeWithFollowUp(context, __ -> {});
        }

        public void executeWithFollowUp(CommandContext context, Consumer<? super FutureResult> followUpAction);

        public default Command andThen(Consumer<? super FutureResult> action){
            return context -> executeWithFollowUp(context, action);
        }

        public default Command andThen(Function<? super FutureResult, Command> nextCommandSupplier) {
            return context ->
                executeWithFollowUp(context, firstResult -> {
                    Command nextCommand = nextCommandSupplier.apply(firstResult);
                    nextCommand.execute(context);
                });
        }

//        public default <CombinedCommand extends Command> CombinedCommand andThenWithSpecial(SpecialCombiner<FutureResult, CombinedCommand> combiner) {
//            return combiner.createPlaceHolder(context -> {
//                executeWithFollowUp(context, firstResult -> {
//                    CombinedCommand nextCommand = combiner.getNextCommand(firstResult);
//                    nextCommand.execute(context);
//                });
//            });
//        }


        public static <FirstResult, NextResult> WithResultCombiner<FirstResult, WithFollowUp<NextResult>> followUpWith(Function<? super FirstResult, ? extends WithFollowUp<NextResult>> nextCommandSupplier) {
            return firstCommand ->
                    (context, followUpAction) -> {
                        FirstResult firstResult = firstCommand.apply(context);
                        nextCommandSupplier.apply(firstResult).executeWithFollowUp(context, followUpAction);
                    };
        }
    }


    @FunctionalInterface
    public interface WithResult<Result> extends Command {

        @Override
        default void execute(CommandContext context) {
            getResult(context);
        }

        public Result getResult(CommandContext context);

        public default Command andThen(Consumer<? super Result> action) {
            return context -> {
                Result firstResult = this.getResult(context);
                action.accept(firstResult);
            };
        }

        public default Command andThen(Function<? super Result, Command> nextCommandSupplier) {
            return context -> {
                Result firstResult = this.getResult(context);
                Command nextCommand = nextCommandSupplier.apply(firstResult);
                nextCommand.execute(context);
            };
        }

        public default <CombinedCommand extends Command> CombinedCommand andThen(WithResultCombiner<? super Result, ? extends CombinedCommand> combiner) {
            return combiner.executeAfter(this::getResult);
        }

        public static <FirstResult, NextResult> WithResultCombiner<FirstResult, WithResult<NextResult>> get(Function<? super FirstResult, ? extends WithResult<NextResult>> nextCommandSupplier) {
            return firstCommand ->
                    context -> {
                        FirstResult firstResult = firstCommand.apply(context);
                        WithResult<? extends NextResult> nextCommand = nextCommandSupplier.apply(firstResult);
                        return nextCommand.getResult(context);
                    };
        }

        public static <FirstResult, MappedResult> WithResultCombiner<FirstResult, WithResult<MappedResult>> map(Function<? super FirstResult, ? extends MappedResult> mapper) {
            return firstCommand ->
                    context -> {
                        FirstResult firstResult = firstCommand.apply(context);
                        return mapper.apply(firstResult);
                    };
        }

        public static <FirstResult> WithResultCombiner<FirstResult, WithResult<FirstResult>> peek(Consumer<? super FirstResult> action) {
            return firstCommand ->
                    context -> {
                        FirstResult firstResult = firstCommand.apply(context);
                        action.accept(firstResult);
                        return firstResult;
                    };
        }

        public static <FirstResult> WithResultCombiner<FirstResult, WithResult<FirstResult>> also(Supplier<Command> sideCommand) {
            return also(__ -> sideCommand.get());
        }
        public static <FirstResult> WithResultCombiner<FirstResult, WithResult<FirstResult>> also(Function<? super FirstResult, Command> sideCommand) {
            return firstCommand ->
                    context -> {
                        FirstResult firstResult = firstCommand.apply(context);
                        sideCommand.apply(firstResult).execute(context);
                        return firstResult;
                    };
        }

        public static <FirstResult, NextResult> WithResultCombiner<FirstResult, WithResult<Pair<FirstResult, NextResult>>> pairWith(Supplier<? extends WithResult<NextResult>> nextCommandSupplier) {
            return firstCommand ->
                    context -> {
                        FirstResult firstResult = firstCommand.apply(context);
                        NextResult nextResult = nextCommandSupplier.get().getResult(context);
                        return new Pair<>(firstResult, nextResult);
                    };
        }

    }

    @FunctionalInterface
    public interface WithOptionalResult<Result> extends WithResult<Optional<Result>> {

        public default Command andIfPresent(Function<? super Result, Command> nextCommandSupplier) {
            return context ->
                    this.getResult(context)
                            .ifPresent(result -> nextCommandSupplier.apply(result).execute(context));
        }

        public default <NextResult> WithOptionalResult<NextResult> _andIfPresent(WithResultCombiner<? super Result, ? extends WithResult<NextResult>> combiner) {
            return context -> {
                Optional<Result> firstResult = this.getResult(context);
                if (firstResult.isPresent()){
                    NextResult nextResult = combiner.executeAfter(ctx -> firstResult.get()).getResult(context);
                    return Optional.ofNullable(nextResult);
                } else {
                    return Optional.empty();
                }
            };
        }

        public default <NextResult> WithOptionalResult<NextResult> andIfPresent(WithResultCombiner<? super Result, ? extends WithResult<NextResult>> combiner) {
            return context ->
                    this.getResult(context)
                        .map(firstResult -> combiner.executeAfter(ctx -> firstResult).getResult(context));
        }

        public default WithOptionalResult<Result> or(Supplier<WithOptionalResult<Result>> supplier) {
            return context ->
                    this.getResult(context)
                        .or(() -> supplier.get().getResult(context));
        }

        public static <FirstResult, NextResult> WithResultCombiner<FirstResult, WithOptionalResult<NextResult>> getOptional(Function<? super FirstResult, ? extends WithOptionalResult<NextResult>> nextCommandSupplier) {
            return firstCommand ->
                    context -> {
                        FirstResult firstResult = firstCommand.apply(context);
                        WithOptionalResult<? extends NextResult> nextCommand = nextCommandSupplier.apply(firstResult);
                        return (Optional<NextResult>) nextCommand.getResult(context);
                    };
        }

        public static <FirstResult, MappedResult> WithResultCombiner<FirstResult, WithOptionalResult<MappedResult>> mapOptional(Function<? super FirstResult, ? extends MappedResult> mapper) {
            return firstCommand ->
                    context -> {
                        FirstResult firstResult = firstCommand.apply(context);
                        return Optional.ofNullable(mapper.apply(firstResult));
                    };
        }

    }

}
