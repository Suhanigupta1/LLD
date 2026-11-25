
class Logger {

    private static volatile Logger logger;

    private Logger(){
    }

    public static Logger getInstance() {
        if (logger == null) {
            synchronized (Logger.class) {
                if (logger == null) {
                    logger = new Logger();
                }
            }
        }
        return logger;
    }

    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}

class Main {
    public static void main(String[] args) {
        Runnable task = () -> {
            Logger logger = Logger.getInstance();

            System.out.println(
                Thread.currentThread().getName()
                + ": "
                + System.identityHashCode(logger)
            );
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();
    }
}
