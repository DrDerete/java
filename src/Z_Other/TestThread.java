package Z_Other;

public class TestThread implements Runnable {
    Thread t;
    boolean flag;

    public TestThread() {
        t = new Thread(this);
        flag = false;
        t.start();
    }

    public synchronized void notify_() {
        flag = false;
        notify();
    }

    public void run() {
        try {
            for (int i = 0; i < 20; i++) {
                System.out.println(t + " " + i);
                Thread.sleep(200);
                while (flag) wait();
            }
        } catch (Exception e) {
            e.toString();
        }
    }
}