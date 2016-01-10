package active.engine.channel;

class LimitOp<T> extends Pipeline<T, T> {

    long toAccept;

    public LimitOp(long count) {
        super();
        this.toAccept = count;
    }
    
    @Override
    public void accept(T t) {
        if (toAccept > 0){
            toAccept--;
            forward(t);
        }
    } 
    
}
