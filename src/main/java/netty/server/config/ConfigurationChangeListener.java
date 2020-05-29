package netty.server.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public interface ConfigurationChangeListener {
    int CORE_LISTENER_THREAD = 1;

    int MAX_LISTENER_THREAD = 1;

    ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(CORE_LISTENER_THREAD,MAX_LISTENER_THREAD,Integer.MAX_VALUE,
            TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(),new NamedTh);

}
