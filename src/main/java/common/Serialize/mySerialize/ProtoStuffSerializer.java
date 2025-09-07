package common.Serialize.mySerialize;

import common.Exception.SerializeException;
import common.Message.RpcRequest;
import common.Message.RpcResponse;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.extern.java.Log;

@Log
public class ProtoStuffSerializer implements Serializer{
    @Override
    public byte[] serialize(Object obj){
        if(obj == null){
            log.info("ProtoStuffSerializer serialize fail: obj is null");
            throw new IllegalArgumentException("obj is null");
        }

        // 获取对象schema
        Schema schema = RuntimeSchema.getSchema(obj.getClass());

        // 使用LinkedBuffer来创建缓冲区
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

        // 序列化对象为字节数组
        byte[] bytes = null;
        try {
            bytes = ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType){
        if(bytes == null || bytes.length == 0){
            log.info("ProtoStuffSerializer deserialize fail: bytes is null");
            throw new IllegalArgumentException("bytes is null");
        }

        Class<?> clazz = getMessageClassByType(messageType);

        // 获取对象schema
        Schema schema = RuntimeSchema.getSchema(clazz);

        // 创建一个空对象实例
        Object obj = null;
        try{
            obj = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            log.info("ProtoStuffSerializer deserialize fail: " + e);
            throw new SerializeException("Deserialize fail:" + e.getMessage());
        }

        ProtobufIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    @Override
    public int getType() {
        return 2;
    }

    private Class<?> getMessageClassByType(int messageType){
        if(messageType == 0){
            return RpcRequest.class;
        } else if(messageType == 1){
            return RpcResponse.class;
        } else {
            log.info("ProtoStuffSerializer getMessageClassByType fail: unknown messageType " + messageType);
            throw new IllegalArgumentException("unknown messageType " + messageType);
        }
    }
}
