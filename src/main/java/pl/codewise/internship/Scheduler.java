package pl.codewise.internship;

import java.util.concurrent.ConcurrentHashMap;

public class Scheduler {
    private ConcurrentHashMap<Integer, Timer> callbacks = new ConcurrentHashMap<>();
    private Integer currentId = 0;

    public synchronized Integer callbackSize() {
        return callbacks.size();
    }

    public synchronized Integer start(Runnable callback, long delay) {
        currentId++;
        Integer newId = currentId;
        callbacks.put(newId, new Timer(newId, callback, System.currentTimeMillis() + delay));
        return currentId;
    }

    public synchronized void stop(Integer taskId) {
        callbacks.remove(taskId);
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
