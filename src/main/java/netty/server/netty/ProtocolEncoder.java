package netty.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.server.context.ProtocolConstants;
import netty.server.context.RpcMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 将本地数据传送到网络
 */
@Component
public class ProtocolEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            if(msg instanceof RpcMessage){
               RpcMessage rpcMessage = (RpcMessage) msg;
               int fullLength = 16;
               int headLength = 16;
               byte messageType = rpcMessage.getMessageType();
               out.writeBytes(ProtocolConstants.MAGIC_CODE_BYTES);
               out.writeByte(ProtocolConstants.VERSION);
               out.writerIndex(out.writerIndex()+6);
               out.writeByte(messageType);
               out.writeByte(rpcMessage.getCodec());
               out.writeByte(rpcMessage.getCompressor());
               out.writeInt(rpcMessage.getId());
                Map<String, String> headMap = rpcMessage.getHeadMap();
                if (headMap!=null && !headMap.isEmpty()){
                    int length= encode(headMap,out);
                    fullLength +=length;
                    headLength += length;
                }





            }
    }

    private int encode(Map<String, String> map, ByteBuf out) {
        if (map == null || out ==null || map.isEmpty()){
            return 0;
        }
        int start = out.writerIndex();
        for(Map.Entry<String, String> entry :map.entrySet()){
              String key = entry.getKey();
              String value = entry.getValue();
              if(key!=null) {
                 writeKey(key,out);
                 writeValue(value,out);
              }
        }

        return out.writerIndex()-start;

    }

    private void writeKey(String key,ByteBuf out){
        byte[] bytes = key.getBytes();
        out.writeBytes(bytes);
        out.writeShort(bytes.length);
    }

    private void writeValue(String value,ByteBuf out){
        byte[] bytes = value.getBytes();
        out.writeBytes(bytes);
        out.writeShort(bytes.length);
    }


}
