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
                // 注意：json.parseObject得到的所有值都是对象类型，不会是基本类型
                RpcRequest request = JSON.parseObject(bytes, RpcRequest.class);
                log.info("request :" + request);
                Object[] objects = new Object[request.getParameters().length];
                // fastjson的解析可能和预期的参数类型不匹配，需要额外处理
                for(int i = 0; i < objects.length; i++){
                    Class<?> paramsType = request.getParamTypes()[i];
                    Object param = request.getParameters()[i];
                    // 这一个if的作用是为了排除fastjson解析错误的情况
                    // 因为fastjson解析的时候，会把所有的数字按照自己的标准来解析，和原定的request的参数类型可能不一致
                    if(paramsType.isPrimitive()) { // 判断是否基本类型（int , double, float等）
                        // 如果参数是基本类型，找它的包装类型
                        Class<?> wrapperType = getWrapperType(paramsType);
                        if (wrapperType.isInstance(param)) { // 验证参数是否是包装类型的实例
                            objects[i] = param;
                        } else {
                            throw new IllegalArgumentException("ParameterType miss match: expected : " + paramsType.getName() + ", found : " + param.getClass().getName());
                        }
                    }
                    else if(paramsType.isAssignableFrom(param.getClass())){ // 验证paramsType是否与param类型相同，或是其父类接口
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
                log.info("response :" + response);
                Class<?> dataType = response.getDataType();
                log.info("dataType is " + dataType + " response.getData.getClass() : " + response.getData().getClass());
                if(!dataType.isAssignableFrom(response.getData().getClass())) {
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
