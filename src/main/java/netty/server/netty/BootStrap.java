package netty.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

@Service
public class BootStrap implements RemotingServer {

    private final Integer SERVER_NUMBER = 1;

    private final Integer CLIENT_NUMBER = 5;

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup serverGroup;

    private EventLoopGroup clientGroup;

    private Channel channel;

    @Autowired
    private SimpleChannelHandler simpleChannelHandler;


    @Override
    public void start() throws InterruptedException {
        clientGroup = new NioEventLoopGroup(CLIENT_NUMBER);
        serverGroup = new NioEventLoopGroup(SERVER_NUMBER);
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(serverGroup,clientGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG,1024)
                .option(ChannelOption.SO_REUSEADDR,true).childOption(ChannelOption.TCP_NODELAY,true).
                childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.SO_SNDBUF,153600).
                childOption(ChannelOption.SO_RCVBUF,153600).
                childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,new WriteBufferWaterMark(1048576,67108864))
                .localAddress(new InetSocketAddress(8081)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ProtocolDecoder())
                        .addLast(new IdleStateHandler(10,0,0))
                        .addLast(new IdleStateTriggerHandler())
                        .addLast(new ProtocolEncoder())
                        .addLast(simpleChannelHandler);
            }
        });
        channel = serverBootstrap.bind().sync().channel();
    }

    @Override
    public void shutdown() {

    }
}
