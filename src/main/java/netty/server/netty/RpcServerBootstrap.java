package netty.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class RpcServerBootstrap implements RemotingServer {
    private static final Logger logger = LoggerFactory.getLogger(RpcServerBootstrap.class);
    private final ServerBootstrap serverBootstrap = new ServerBootstrap();
 /*   private final EventLoopGroup eventLoopWorker;
    private final EventLoopGroup eventLoopBoss;*/
    private ChannelHandler[] channelHandlers;
    private int listenerPort;
    private AtomicBoolean initialized = new AtomicBoolean(false);



    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
