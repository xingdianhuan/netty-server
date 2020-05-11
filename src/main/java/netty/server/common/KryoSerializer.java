package netty.server.common;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;

public class KryoSerializer {

    private final Kryo kryo;

    public KryoSerializer(Kryo kryo){this.kryo = kryo;}

    public Kryo getKryo(){return this.kryo;}

    public <T> byte[] serialize(T t) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = new Output(baos);
        kryo.writeClassAndObject((Output) out,t);
         out.close();
         return baos.toByteArray();
        }

     public <T> T deserialize(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
         InputStream in = new Input(bis);
         in.close();
         return (T) kryo.readClassAndObject((Input) in);
     }




}
