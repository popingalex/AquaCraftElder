package org.aqua.thread.event;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class EventManager implements Runnable {
    LinkedHashSet<EventTrigger> triggerSet = new LinkedHashSet<EventTrigger>();

    private Long           increment  = 1000L;
    private Long           interval   = 1L;

    @Override
    public void run() {
        while (true) {
            long timePre = System.currentTimeMillis();

            // TODO do your work

            long timeSuf = System.currentTimeMillis();
            long timeOdd = (timePre - timeSuf) % interval;

            try {
                Thread.sleep(increment * interval - timeOdd);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        // TODO use thread pool
        // ThreadPoolExecutor pool = new
        new Thread(this);
    }

    public void shutdown() {

    }

    public interface EventHandler {
        public void handle(EventContext context);
    }

    public interface DigiHandler extends EventHandler {
        public void handle(Integer flag, EventContext context);
    }

    public interface EnumHandler<T extends Enum<?>> extends EventHandler {
        public void handle(T flag, EventContext context);
    }

    private static class Event {
        EventHandler handler;
        public Event(EventHandler handler) {
            super();
            this.handler = handler;
        }
    }

    private static class DigiEvent extends Event {
        Integer digiFlag;
        public DigiEvent(Integer digiFlag, DigiHandler handler) {
            super(handler);
            this.digiFlag = digiFlag;
        }
    }

    private static class EnumEvent extends Event {
        Enum<?> enumFlag;
        public <T extends Enum<?>> EnumEvent(T enumFlag, EnumHandler<T> handler) {
            super(handler);
            this.enumFlag = enumFlag;
        }
    }

    public static class EventTrigger {
        public Long              dotimes;
        public Long              interval;
        public Long              schedule;
        public LinkedList<Event> eventList;

        public EventTrigger(Long dotimes, Long interval, Long schedule) {
            this.dotimes = dotimes;
            this.interval = interval;
            this.schedule = schedule;
            this.eventList = new LinkedList<Event>();
        }

        public EventTrigger(Long dotimes, Long interval, EventManager manager, Date schedule) {

        }
    }

    public static class EventContext {

    }

    public void register(Event event, EventTrigger trigger) {
        if (triggerSet.contains(trigger)) {
            
        }
    }
    public void register(Integer digiFlag, EventTrigger trigger, DigiHandler handler) {
        register(new DigiEvent(digiFlag, handler), trigger);
    }

    public <K extends Enum<?>> void register(K enumFlag, EventTrigger trigger, EnumHandler<K> handler) {
        register(new EnumEvent(enumFlag, handler), trigger);
    }

    public void register(EventHandler handler, EventTrigger trigger) {
        register(new Event(handler), trigger);
    }
}
