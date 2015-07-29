package org.aqua.thread.event;

import org.aqua.thread.event.EventUtil.Handler;
import org.aqua.thread.event.EventUtil.TagNotFoundException;
import org.aqua.thread.event.EventUtil.TypedEvent;
import org.aqua.thread.event.EventUtil.TypedLoopEvent;

import test.Tester;

public class EventUtilTest extends Tester implements Handler {
    enum MainEvent {
        EventA, EventB, EventC
    }

    enum EventA {
        AA, AB, AC
    }

    enum EventB {
        BA, BB, BC
    }

    enum EventC {
        CA, CB, CC
    }
    @Override
    public void test(String[] args) {
        EventUtil util = new EventUtil(this);
        // util.register(MainEvent.EventA, EventA.AA, 1);
        // util.register(MainEvent.EventA, EventA.AB, 2);
        util.register(1, MainEvent.EventA, EventA.AA);
        util.register(2, MainEvent.EventB);
        util.power();
    }
    @Override
    public void onLoopEvent(TypedEvent event) {
        try {
            switch (event.getTag(MainEvent.class)) {
            case EventA:
                break;
            case EventB:
                break;
            case EventC:
                break;
            }
        } catch (TagNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActionEvent(TypedLoopEvent event) {
        
    }
}
