package netty.server.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private final static Map<String, AtomicInteger> PREFIX_COUNTER = new ConcurrentHashMap<>();

    @Override
    public Thread newThread(Runnable r) {
        return null;
    }
}
