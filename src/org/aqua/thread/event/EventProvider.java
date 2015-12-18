package org.aqua.thread.event;

public interface EventProvider {
    public void addListener(EventHandler handler);
    public void removeListener(EventHandler handler);
}
