package org.aqua.thread.event;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class EventUtil implements Runnable {
    private HashSet<LoopEventCounter> counterMap;
    private Long                      increment;
    private Long                      interval;
    private Handler                   handler;

    public EventUtil(Handler handler) {
        this(handler, 1L);
    }

    public EventUtil(Handler handler, Long interval) {
        this(handler, 1L, 1000L);
    }

    public EventUtil(Handler handler, Long interval, Long increment) {
        this.increment = increment;
        this.interval = interval;
        this.handler = handler;
        this.counterMap = new HashSet<LoopEventCounter>();
    }

    public void register(Enum<?>... tags) {
        register(1, tags);
    }

    public void register(Integer interval, Enum<?>... tags) {
        if (tags.length == 0) {
            // throw Need At least one Tag
        } else {
            counterMap.add(new LoopEventCounter(Arrays.asList(tags), interval));
        }
    }

    @Override
    public void run() {
        long preTime;
        long sufTime;
        StringBuffer debugString;
        while (true) {
            {// debuging
                debugString = new StringBuffer("===== Event Looping =====");
                debugString.append(System.getProperty("line.separator"));
            }
            preTime = System.currentTimeMillis();
            for (LoopEventCounter counter : counterMap) {
                if (counter.latest == 0L || counter.latest + counter.interval * increment <= preTime) {
                    counter.times++;
                    TypedEvent event = new TypedEvent(counter.tagList);
                    {// debuging
                        debugString.append("----- Event -----");
                        debugString.append(System.getProperty("line.separator"));
                        debugString.append(counter.tagList.toString());
                        debugString.append(System.getProperty("line.separator"));
                    }
                    new Thread(new EventRunner(handler, event)).start();
                    counter.latest = preTime;
                }
            }
            {// debuging
             // System.out.print(debugString);
            }
            sufTime = System.currentTimeMillis();
            long oddTime = (sufTime - preTime) % interval;
            try {
                Thread.sleep(interval * increment - oddTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void power() {
        new Thread(this).start();
    }

    private class LoopEventCounter {
        Long          times;
        Long          latest;
        List<Enum<?>> tagList;
        Integer       interval;
        public LoopEventCounter(List<Enum<?>> tagList, Integer interval) {
            this.times = 0L;
            this.latest = 0L;
            this.tagList = tagList;
            this.interval = interval;
        }
    }

    public static class TagNotFoundException extends Exception {
        private static final long serialVersionUID = 1L;
        public TagNotFoundException(String message) {
            super(message);
        }
    }

    public static class TypedEvent {
        private List<Enum<?>> tagList;
        public Object[]       sources;
        protected TypedEvent(List<Enum<?>> tagList, Object... sources) {
            this.tagList = tagList;
            this.sources = sources;
        }
        public <K extends Enum<?>> K getTag(Class<K> typeClass) throws TagNotFoundException {
            for (Enum<?> tag : tagList) {
                if (typeClass.isInstance(tag)) {
                    return typeClass.cast(tag);
                }
            }
            throw new TagNotFoundException("Tag of certain Type Not Found");
        }
    }
    
    public static class TypedLoopEvent extends TypedEvent {

        protected TypedLoopEvent(List<Enum<?>> tagList, Object[] sources) {
            super(tagList, sources);
        }
        
    }

    private class EventRunner implements Runnable {
        private Handler    handler;
        private TypedEvent event;
        private EventRunner(Handler handler, TypedEvent event) {
            this.handler = handler;
            this.event = event;
        }
        @Override
        public void run() {
            handler.onLoopEvent(event);
        }
    }

    public interface Handler {
        public void onLoopEvent(TypedEvent event);
        public void onActionEvent(TypedLoopEvent event);
    }
}
