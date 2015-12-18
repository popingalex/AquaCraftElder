package org.aqua.thread.event;

import org.aqua.thread.event.impl.EventPool.Event;
import org.aqua.thread.event.impl.EventPool.Schedule;

public interface EventManager {
    Event fetchEvent(String key);
    Schedule fetchSchedule(String key, int delay);
    void store(Event event);
}
