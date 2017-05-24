package pl.codewise.internship;

import org.junit.Test;
import static org.junit.Assert.*;
public class SchedulerTest {
    @Test
    public void startStopTest() throws Exception {
        Scheduler scheduler = new Scheduler();
        Integer id1 =scheduler.start(()-> System.out.println("new task"), 1000);
        Integer id2 =scheduler.start(()-> System.out.println("new task"), 1000);
        assertEquals(new Integer(2), scheduler.callbackSize());
        scheduler.stop(id1);
        assertEquals(new Integer(1), scheduler.callbackSize());
        scheduler.stop(id2);
        assertEquals(new Integer(0), scheduler.callbackSize());
    }
}
