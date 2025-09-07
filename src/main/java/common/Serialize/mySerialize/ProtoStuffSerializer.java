package common.Serialize.mySerialize;

import common.Message.RpcRequest;
import common.Message.RpcResponse;
import lombok.extern.java.Log;

@Log
public class ProtoStuffSerializer implements Serializer{
    @Override
    public byte[] serialize(Object obj){
        if(obj == null){
            log.info("ProtoStuffSerializer serialize fail: obj is null");
            throw new IllegalArgumentException("obj is null");
        }

    }

    @Override
    Object deserialize(byte[] bytes, int messageType){
        if(bytes == null || bytes.length == 0){
            log.info("ProtoStuffSerializer deserialize fail: bytes is null");
            throw new IllegalArgumentException("bytes is null");
        }

        Class<?> clazz = getMessageClassByType(messageType);

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
