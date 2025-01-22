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
                    Object param = request.getParameters()[i];
                    if(paramsType.isPrimitive()) {
                        // 如果参数是基本类型，找它的包装类型
                        Class<?> wrapperType = getWrapperType(paramsType);
                        if (wrapperType.isInstance(param)) {
                            objects[i] = param;
                        } else {
                            throw new IllegalArgumentException("ParameterType miss match: expected : " + paramsType.getName() + ", found : " + param.getClass().getName());
                        }
                    }
                    else if(paramsType.isAssignableFrom(param.getClass())){
                        objects[i] = request.getParameters()[i];
                    } else {
                        throw new IllegalArgumentException("ParameterType miss match: expected : " + paramsType.getName() + ", found : " + param.getClass().getName());
                    }
                }
                request.setParameters(objects);
                obj = request;
                break;

            case 1:
                RpcResponse response = JSON.parseObject(bytes, RpcResponse.class);
                Object data = response.getData();
                Class<?> dataType = response.getData().getClass();
//                if(!dataType.isAssignableFrom(response.getData().getClass())){
//                    response.setData(JSONObject.toJavaObject((JSONObject) response.getData(), dataType));
//                }
                if(dataType.isPrimitive()) {
                    // 如果参数是基本类型，找它的包装类型
                    Class<?> wrapperType = getWrapperType(dataType);
                    if (wrapperType.isInstance(data)) {
                        response.setData(data);
                    } else {
                        throw new IllegalArgumentException("ParameterType miss match: expected : " + dataType.getName() + ", found : " + data.getClass().getName());
                    }
                }
                else if(dataType.isAssignableFrom(data.getClass())){
                    response.setData(data);
                } else {
                    throw new IllegalArgumentException("ParameterType miss match: expected : " + dataType.getName() + ", found : " + data.getClass().getName());
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

    private Class<?> getWrapperType(Class<?> paramType){
        if(paramType == int.class) return Integer.class;
        if(paramType == long.class) return Long.class;
        if(paramType == float.class) return Float.class;
        if(paramType == double.class) return Double.class;
        if(paramType == boolean.class) return Boolean.class;
        if(paramType == byte.class) return Byte.class;
        if(paramType == char.class) return Character.class;
        if(paramType == short.class) return Short.class;
        return paramType;
    }
}
