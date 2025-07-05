package threads;

public class UncaughtExceptionHandler {

    public static void main(String[] args) {
        Thread exceptionThread = new Thread(() -> {
            throw new RuntimeException("Intentional exception");
        });

        exceptionThread.setName("ExceptionThread");

        // Traditional way
        //        exceptionThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
        //            @Override
        //            public void uncaughtException(Thread t, Throwable e) {
        //                System.out.println("A new exception happened in "+t.getName()+" with error: "+e.getMessage());
        //            }
        //        });

        // using lambdas
        //You can set a general UncaughtExceptionHandler for the thread if any exception is thrown and not caught anywhere
        exceptionThread.setUncaughtExceptionHandler((t, e) ->
                System.out.println("A new exception happened in " + t.getName() + " with error: " + e.getMessage()));

        exceptionThread.start();
    }
}
