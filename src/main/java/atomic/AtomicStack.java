package atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class AtomicStack<T> {
    AtomicReference<StandardNode<T>> atomicHead = new AtomicReference<>();
    private AtomicInteger operationsCounter = new AtomicInteger(0);

    public int getCounter() {
        return operationsCounter.get();
    }

    public void push(T value) {
        StandardNode<T> newHead = new StandardNode<>(value);

        while (true) {
            StandardNode<T> currentHeadNode = atomicHead.get();
            newHead.next=currentHeadNode;

            if (atomicHead.compareAndSet(currentHeadNode, newHead)) {
                break;
            }
            else{
                LockSupport.parkNanos(1);
            }
        }

        operationsCounter.incrementAndGet();

    }

    public T pop() {
        StandardNode<T> currentHeadNode = atomicHead.get();
        StandardNode<T> newHead;

        while(currentHeadNode!=null){
            newHead=currentHeadNode.next;
            if(atomicHead.compareAndSet(currentHeadNode,newHead)){
                break;
            }
            else {
                LockSupport.parkNanos(1);
            }
        }

        operationsCounter.incrementAndGet();
        return currentHeadNode!=null? currentHeadNode.value : null;
    }

    private static class StandardNode<T> {
        private T value;
        public StandardNode<T> next;

        StandardNode(T value) {
            this.value = value;
            this.next = next;
        }
    }
}
