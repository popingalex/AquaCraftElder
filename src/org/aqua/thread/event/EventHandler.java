package org.aqua.thread.event;

import org.aqua.thread.event.impl.EventPool;

public interface EventHandler {
    public void handleEvent(String key, EventPool.Event event);
    public boolean handleSchedule(String key, EventPool.Schedule schedule);
}