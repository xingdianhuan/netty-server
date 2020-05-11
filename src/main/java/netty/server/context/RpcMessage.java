package netty.server.context;

import java.util.HashMap;
import java.util.Map;

public class RpcMessage {
    private int id;
    private byte messageType;
    private byte codec;
    private byte compressor;
    private Map<String, String> headMap = new HashMap<>();
    private Object body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public byte getCodec() {
        return codec;
    }

    public void setCodec(byte codec) {
        this.codec = codec;
    }

    public byte getCompressor() {
        return compressor;
    }

    public void setCompressor(byte compressor) {
        this.compressor = compressor;
    }

    public Map<String, String> getHeadMap() {
        return headMap;
    }

    public void setHeadMap(Map<String, String> headMap) {
        this.headMap = headMap;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
