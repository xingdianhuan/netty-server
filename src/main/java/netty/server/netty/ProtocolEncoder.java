package netty.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.server.common.KryoSerializer;
import netty.server.common.RpcKyroFactory;
import netty.server.context.ProtocolConstants;
import netty.server.context.RpcMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.rmi.server.ExportException;
import java.util.Map;
/**
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
 * </p>
 * https://github.com/seata/seata/issues/893
 *
 * @author Geng Zhang
 * @see ProtocolV1Decoder
 * @since 0.7.0
 */
/**
 * 将本地数据传送到网络
 */
@Component
public class ProtocolEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        try {
            if (msg instanceof RpcMessage) {
                RpcMessage rpcMessage = (RpcMessage) msg;
                int fullLength = 16;
                int headLength = 16;
                byte messageType = rpcMessage.getMessageType();
                out.writeBytes(ProtocolConstants.MAGIC_CODE_BYTES);
                out.writeByte(ProtocolConstants.VERSION);
                out.writerIndex(out.writerIndex() + 6);
                out.writeByte(messageType);
                out.writeByte(rpcMessage.getCodec());
                out.writeByte(rpcMessage.getCompressor());
                out.writeInt(rpcMessage.getId());
                Map<String, String> headMap = rpcMessage.getHeadMap();
                if (headMap != null && !headMap.isEmpty()) {
                    int length = encode(headMap, out);
                    fullLength += length;
                    headLength += length;
                }
                byte[] bodyBytes = null;
                if (messageType != ProtocolConstants.MSGTYPE_HEARTBEAT_REQUEST &&
                        messageType != ProtocolConstants.MSGTYPE_HEARTBEAT_RESPONSE) {
                    KryoSerializer serializer = RpcKyroFactory.getInstance().get();
                    bodyBytes = serializer.serialize(rpcMessage.getBody());
                    fullLength += bodyBytes.length;
                }
                if (bodyBytes != null) {
                    out.writeBytes(bodyBytes);
                }
                int writerIndex = out.writerIndex();
                out.writerIndex(writerIndex - fullLength + 3);
                out.writeInt(fullLength);
                out.writeShort(headLength);
                out.writerIndex(writerIndex);

            }else{
                throw new UnsupportedOperationException("Not support this class:" + msg.getClass());
            }
        }catch (ExportException e){

        }

    }

    private int encode(Map<String, String> map, ByteBuf out) {
        if (map == null || out == null || map.isEmpty()) {
            return 0;
        }
        int start = out.writerIndex();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null) {
                writeKey(key, out);
                writeValue(value, out);
            }
        }

        return out.writerIndex() - start;

    }

    private void writeKey(String key, ByteBuf out) {
        if (key == null) {
            out.writeShort(-1);
            return;
        }
        if (key.isEmpty()) {
            out.writeShort(0);
            return;
        }
        byte[] bytes = key.getBytes(Charset.forName("UTF-8"));
        out.writeBytes(bytes);
        out.writeShort(bytes.length);
    }

    private void writeValue(String value, ByteBuf out) {
        if (value == null) {
            out.writeShort(-1);
            return;
        }
        if (value.isEmpty()) {
            out.writeShort(0);
            return;
        }
        byte[] bytes = value.getBytes(Charset.forName("UTF-8"));
        out.writeBytes(bytes);
        out.writeShort(bytes.length);
    }


}
