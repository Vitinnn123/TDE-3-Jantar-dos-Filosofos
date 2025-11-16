// DeadlockRepro.java
// Compilar e executar; o programa vai travar (deadlock) exibindo as mensagens de log
public class DeadlockRepro {
    static final Object LOCK_A = new Object();
    static final Object LOCK_B = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println("T1: tentando LOCK_A");
            synchronized (LOCK_A) {
                System.out.println("T1: obteve LOCK_A");
                dormir(50);
                System.out.println("T1: tentando LOCK_B");
                synchronized (LOCK_B) {
                    System.out.println("T1: obteve LOCK_B");
                }
            }
            System.out.println("T1: terminou");
        });

        Thread t2 = new Thread(() -> {
            System.out.println("T2: tentando LOCK_B");
            synchronized (LOCK_B) {
                System.out.println("T2: obteve LOCK_B");
                dormir(50);
                System.out.println("T2: tentando LOCK_A");
                synchronized (LOCK_A) {
                    System.out.println("T2: obteve LOCK_A");
                }
            }
            System.out.println("T2: terminou");
        });

        t1.start();
        t2.start();
    }

    static void dormir(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
