package pl.codewise.internship;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SchedulerTest {
    Scheduler scheduler = new Scheduler();

    @Test
    public void startStopTest() throws Exception {
        Integer id1 = scheduler.start(() -> System.out.println("new task"), 1000);
        Integer id2 = scheduler.start(() -> System.out.println("new task"), 1000);
        assertEquals(new Integer(2), scheduler.callbackSize());
        scheduler.stop(id1);
        assertEquals(new Integer(1), scheduler.callbackSize());
        scheduler.stop(id2);
        assertEquals(new Integer(0), scheduler.callbackSize());
        scheduler.stopScheduler();
    }

    @Test
    public void callbackTest() throws Exception {
        scheduler.start(() -> System.out.println("Callback"), 500);
        Integer id = scheduler.start(() -> System.out.println("Callback2"), 1500);
        Thread.sleep(2000);
        scheduler.stop(id);
        Thread.sleep(1000);
        scheduler.stopScheduler();
    }

    @Test
    public void stressTest() throws Exception {
        Random random = new Random();
        for(int i=0;i<1000;i++){
            final int a=i;
            scheduler.start(()->System.out.println(a), random.nextLong()%10000);
        }
        Thread.sleep(15000);
    }
}
