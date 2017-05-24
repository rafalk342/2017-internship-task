package pl.codewise.internship;

import java.util.concurrent.ConcurrentHashMap;

public class Scheduler {
    private ConcurrentHashMap<Integer, Timer> callbacks = new ConcurrentHashMap<>();
    private Integer currentId = 0;
    private boolean stopScheduler = false;
    private Thread timerThread;

    public Scheduler() {
        this.timerThread = new Thread(this::checkDelays);
        this.timerThread.start();
    }

    public void stopScheduler() {
        this.stopScheduler = true;
    }

    private long minDelay() {
        long min = Long.MAX_VALUE;
        for (Timer t : callbacks.values()) {
            min = Math.min(min, t.timeCall);
        }
        return min;
    }

    private void checkRunRemove() {
        callbacks.entrySet().removeIf(entry -> {
            Timer t = entry.getValue();
            if (System.currentTimeMillis() >= t.timeCall) {
                t.callback.run();
                return true;
            }
            return false;
        });
    }

    private void checkDelays() {
        while (!(stopScheduler && callbacks.isEmpty())) {
            checkRunRemove();
            long timeWait = minDelay() - System.currentTimeMillis();
            try {
                synchronized (this) {
                    wait(timeWait);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized Integer callbackSize() {
        return callbacks.size();
    }

    public synchronized Integer start(Runnable callback, long delay) {
        currentId++;
        Integer newId = currentId;
        callbacks.put(newId, new Timer(newId, callback, System.currentTimeMillis() + delay));
        timerThread.interrupt();
        return currentId;
    }

    public synchronized void stop(Integer taskId) {
        callbacks.remove(taskId);
        timerThread.interrupt();
    }

    class Timer {
        Integer timerId;
        Runnable callback;
        long timeCall;

        public Timer(Integer timerId, Runnable callback, long timeCall) {
            this.timerId = timerId;
            this.callback = callback;
            this.timeCall = timeCall;
        }
    }

}
