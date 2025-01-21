package common.Serialize.mySerialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Message.RpcRequest;
import common.Message.RpcResponse;
import lombok.extern.java.Log;

// 基于fastjson的序列化器
@Log
public class JsonSerializer implements Serializer{

    // 将对象序列化为json格式的字节数组
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
                for(int i = 0; i < objects.length; i++){
                    Class<?> paramsType = request.getParamTypes()[i];
                    if(!paramsType.isAssignableFrom(request.getParameters()[i].getClass())){
                        objects[i] = JSONObject.toJavaObject((JSONObject) request.getParameters()[i], request.getParamTypes()[i]);
                    } else {
                        objects[i] = request.getParameters()[i];
                    }
                }
                request.setParameters(objects);
                obj = request;
                break;

            case 1:
                RpcResponse response = JSON.parseObject(bytes, RpcResponse.class);
                Class<?> dataType = response.getData().getClass();
                if(!dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject) response.getData(), dataType));
                }
                obj = response;
                break;
            default:
                log.info("Do not support this message type");
                throw new RuntimeException("Do not support this message type");
        }

        return obj;
    }

    @Override
    public int getType(){
        return 1;
    }
}
