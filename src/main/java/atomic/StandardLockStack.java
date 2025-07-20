package atomic;

public class StandardLockStack<T> {

    StandardNode<T> head;
    private int operationsCounter =0;

    public int getCounter() {
        return operationsCounter;
    }

    public synchronized void push(T value){
        StandardNode<T> newHead=new StandardNode<>(value);
        newHead.next=head;
        head=newHead;
        operationsCounter++;
    }

    public synchronized T pop(){
        if(head==null){
            operationsCounter++;
            return null;
        }

        operationsCounter++;
        T value= head.value;
        head=head.next;
        return value;
    }

    private static class StandardNode<T>{
        private T value;
        public StandardNode<T> next;

        StandardNode(T value){
            this.value=value;
            this.next= next;
        }
    }
}
