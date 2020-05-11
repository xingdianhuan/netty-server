package netty.server.common;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import org.springframework.stereotype.Component;


public class RpcKyroFactory implements KryoFactory {

    public static final RpcKyroFactory KRYOFACTORY = new RpcKyroFactory();

    private KryoPool pool = new KryoPool.Builder(this).softReferences().build();

    public static RpcKyroFactory getInstance(){
        return KRYOFACTORY;
    }

    public KryoSerializer get(){
        return new Kryo
    }
    @Override
    public Kryo create() {
        return null;
    }
}
