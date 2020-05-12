package netty.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import netty.server.common.ProtocolConstants;
import netty.server.context.RpcMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
/*
        * <pre>
 * 0     1     2     3     4     5     6     7     8     9    10     11    12    13    14    15    16
         * +-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+
         * |   magic   |Proto|     Full length       |    Head   | Msg |Seria|Compr|     RequestId         |
         * |   code    |colVer|    (head+body)      |   Length  |Type |lizer|ess  |                       |
         * +-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+
         * |                                                                                               |
         * |                                   Head Map [Optional]                                         |
         * +-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+
         * |                                                                                               |
         * |                                         body                                                  |
         * |                                                                                               |
         * |                                        ... ...                                                |
         * +-----------------------------------------------------------------------------------------------+
         * </pre>
        * <p>
 * <li>Full Length: include all data </li>
        * <li>Head Length: include head data from magic code to head map. </li>
        * <li>Body Length: Full Length - Head Length</li>
        * </p>*/
@Component
public class ProtocolDecoder extends LengthFieldBasedFrameDecoder {
    public ProtocolDecoder() {
        this(ProtocolConstants.MAX_FRAME_LENGTH);
    }
    public ProtocolDecoder(int maxFrameLength) {
        super(maxFrameLength,3,4,-7,0);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in){
        try {
            Object decoded = super.decode(ctx,in);
            if (decoded instanceof ByteBuf){
                ByteBuf buf = (ByteBuf) decoded;
                return decodeFrame(buf);
            }
        }catch (Exception e){

        }

        return null;
    }

    public Object decodeFrame(ByteBuf frame){
         byte b0 =frame.readByte();
         byte b1 = frame.readByte();
         if (ProtocolConstants.MAGIC_CODE_BYTES[0]!=b0 || ProtocolConstants.MAGIC_CODE_BYTES[1]!=b1){
             throw new UnsupportedOperationException();
         }
         byte version = frame.readByte();
         int fullLength = frame.readInt();
         short headLength = frame.readShort();
         byte messageType = frame.readByte();
         byte codec = frame.readByte();
         byte compressor = frame.readByte();
         int requestId = frame.readInt();
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setCodec(codec);
        rpcMessage.setId(requestId);
        rpcMessage.setCompressor(compressor);
        rpcMessage.setMessageType(messageType);





    }

}
