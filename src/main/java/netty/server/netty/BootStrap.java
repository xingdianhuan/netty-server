package netty.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class BootStrap implements InitializingBean {

    private final Integer SERVER_NUMBER = 1;

    private final Integer CLIENT_NUMBER = 5;

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup serverGroup;

    private EventLoopGroup clientGroup;

    @Override
    public void afterPropertiesSet() throws Exception {
        clientGroup = new NioEventLoopGroup(CLIENT_NUMBER);
        serverGroup = new NioEventLoopGroup(SERVER_NUMBER);
        serverBootstrap = new ServerBootstrap();

    }
}
