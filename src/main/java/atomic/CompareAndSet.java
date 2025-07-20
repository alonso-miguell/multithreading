package atomic;

import java.util.concurrent.atomic.AtomicReference;

public class CompareAndSet {
    public static void main(String[] args){
        String oldValue="old value";
        String newValue="new value";


        AtomicReference<String> atomicReference=new AtomicReference<>(oldValue);
//        atomicReference.set("other value");

        //compares atomicReference value to oldValue, if true then sets newValue
        if(atomicReference.compareAndSet(oldValue, newValue)){
            System.out.println("atomicReference value = " + atomicReference.get());
        }
        else{
            System.out.println("Value didn't change");
        }

    }

}
