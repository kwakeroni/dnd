package active.engine.channel;

class SkipOp<T> extends Pipeline<T, T> {

    long toSkip;

    public SkipOp(long count) {
        this.toSkip = count;
    }

    @Override
    public void accept(T t) {
        if (toSkip > 0){
            toSkip--;
        } else {
            forward(t);
        }
    }
    
    

}
