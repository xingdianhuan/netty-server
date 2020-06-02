package netty.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.server.context.RpcMessage;
import org.springframework.stereotype.Component;

@Component
public class SimpleChannelHandler extends SimpleChannelInboundHandler<RpcMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext cxt, RpcMessage mess) throws Exception {

    }
}
