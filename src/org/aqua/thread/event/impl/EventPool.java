package org.aqua.thread.event.impl;

import org.aqua.resource.TypedPool;
import org.aqua.thread.event.EventManager;

public class EventPool implements EventManager {
    TypedPool<Event> typedPool;

    public EventPool() {
        typedPool = new TypedPool<Event>() {
            @Override
            protected Event create(Class<? extends Event> key) throws Exception {
                if (key.equals(Schedule.class)) {
                    return new Schedule();
                } else {
                    return new Event();
                }
            }
            @Override
            protected Class<? extends Event> typeof(Event object) {
                if (object instanceof Schedule) {
                    return Schedule.class;
                } else {
                    return Event.class;
                }
            }
        };
    }

    @Override
    public Event fetchEvent(String key) {
        Event event = typedPool.fetch(Event.class);
        event.key = key;
        return event;
    }

    @Override
    public Schedule fetchSchedule(String key, int delay) {
        Schedule schedule = typedPool.fetch(Schedule.class);
        schedule.key = key;
        schedule.runs = 0;
        schedule.delay = delay;
        return schedule;
    }

    @Override
    public void store(Event event) {
        typedPool.store(event);
    }

    public class Event {
        String   key;
        Object[] sources;
        protected Event() {
        }

        public void setSource(Object... sources) {
            this.sources = sources;
        }

        public Object[] getSource() {
            return sources;
        }
    }

    public class Schedule extends Event {
        int  runs;
        int  delay;
        long jointime;
    }
}