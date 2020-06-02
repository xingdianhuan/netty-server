package netty.server.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private final static Map<String, AtomicInteger> PREFIX_COUNTER = new ConcurrentHashMap<>();
    private final String prefix;
    private final int totalSize;
    private final Boolean makeDaemons;

    public NamedThreadFactory(String prefix, int totalSize) {
        this(prefix, totalSize, true);
    }

    public NamedThreadFactory(String prefix, int totalSize, Boolean makeDaemons) {
        PREFIX_COUNTER.putIfAbsent(prefix,new AtomicInteger(0));
        int prefixCounter = PREFIX_COUNTER.get(prefix).incrementAndGet();
        this.prefix = prefix+"_"+prefixCounter;
        this.totalSize = totalSize;
        this.makeDaemons = makeDaemons;
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = prefix;
        if (totalSize>1) {
            name += "_" + totalSize;
        }
            Thread thread = new Thread(r,name);
            thread.setDaemon(makeDaemons);
            if (thread.getPriority()!=Thread.NORM_PRIORITY){
                thread.setPriority(Thread.NORM_PRIORITY);
            }

        return thread;
    }
}
