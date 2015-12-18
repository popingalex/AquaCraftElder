package org.aqua.thread.event.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.aqua.thread.event.EventHandler;
import org.aqua.thread.event.IEventAdapter;

public class EventAdapter<Case extends Enum<?> & EventCase<? extends Enum<?>>> implements IEventAdapter, Runnable {
    private Logger                   logger = Logger.getLogger(getClass());
    private Thread                   worker;
    private boolean                  working;
    private int                      tickDelay;
    private long                     tickCount;
    private Class<Case>              caseClass;
    private static EventPool         eventPool;
    private List<EventHandler>       handlerList;
    private List<EventPool.Schedule> scheduleList;

    static {
        eventPool = new EventPool();
    }

    public EventAdapter(Class<Case> caseClass) {
        this.caseClass = caseClass;
        this.handlerList = new LinkedList<EventHandler>();
        this.scheduleList = Collections.synchronizedList(new LinkedList<EventPool.Schedule>());
    }

    public void power(int delay) {
        tickDelay = delay;
        working = true;
        worker = new Thread(this);
        worker.start();
    }

    public boolean working() {
        return working;
    }

    public void shutdown(boolean clear) {
        working = false;
        for (; worker.isAlive();) {
        }
        if (clear) {
            scheduleList.clear();
        }
    }

    @Override
    public void run() {
        String name = caseClass.getSimpleName();
        long timestamp = System.currentTimeMillis();
        for (; working; tickCount++) {
            {   // work
                logger.debug(name + " loopin " + tickCount);
                List<EventPool.Schedule> iterator;
                synchronized (scheduleList) {
                    iterator = new ArrayList<EventPool.Schedule>(scheduleList.size());
                    iterator.addAll(scheduleList);
                }
                for (EventPool.Schedule schedule : iterator) {
                    if (schedule.jointime + schedule.delay == tickCount) {
                        logger.debug(name + " key " + schedule.key);
                        schedule.runs += 1;
                        scheduleList.remove(schedule);
                        boolean keep = false;
                        for (EventHandler handler : handlerList) {
                            if (handler.handleSchedule(schedule.key, schedule)) {
                                keep = true;
                            }
                        }
                        if (keep) {
                            offer(schedule);
                        } else {
                            eventPool.store(schedule);
                        }
                    }
                }
                logger.debug(name + "next after " + tickDelay);
            }
            {   // tick
                try {
                    Thread.sleep(tickDelay - (System.currentTimeMillis() - timestamp) % tickDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void addListener(EventHandler handler) {
        handlerList.add(handler);
    }

    @Override
    public void removeListener(EventHandler handler) {
        handlerList.remove(handler);
    }

    public void offer(EventPool.Event event) {
        if (event instanceof EventPool.Schedule) {
            EventPool.Schedule schedule = (EventPool.Schedule) event;
            schedule.jointime = tickCount;
            scheduleList.add(schedule);
        } else {
            for (EventHandler handler : handlerList) {
                handler.handleEvent(event.key, event);
                eventPool.store(event);
            }
        }
    }

    public Case getEnum(String key) {
        return getEnum(caseClass, key);
    }

    public static <SCase> SCase getEnum(Class<SCase> caseClass, String key) {
        for (SCase c : caseClass.getEnumConstants()) {
            if (c.toString().equals(key)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public EventPool.Event fetchEvent(String key) {
        return eventPool.fetchEvent(key);
    }

    @Override
    public EventPool.Schedule fetchSchedule(String key, int delay) {
        return eventPool.fetchSchedule(key, delay);
    }

    @Override
    public void store(EventPool.Event event) {
        eventPool.store(event);
    }
}
