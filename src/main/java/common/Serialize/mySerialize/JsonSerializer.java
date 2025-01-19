package common.Serialize.mySerialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Message.RpcRequest;

public class JsonSerializer implements Serializer{

    @Override
    public byte[] serialize(Object obj){
        byte[] bytes = JSONObject.toJSONBytes(obj);
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType){
        Object obj = null;
        // messageType - 0: request ; 1: response
        switch (messageType){
            case 0:
                RpcRequest request = JSON.parseObject(bytes, RpcRequest.class);
                Object[] objects = new Object[request.getParameters().length];
        }

        return obj;
    }

    @Override
    public int getType(){
        return 1;
    }
}
