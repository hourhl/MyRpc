package common.Serialize.mySerialize;

public interface Serializer {
    // 将对象序列化为字节数组
    byte[] serialize(Object obj);
    // 将字节数组反序列化为对象
    Object deserialize(byte[] bytes, int messageType);


    // type - 0 : java io原生序列器
    // type - 1 : fastjson
    int getType();
    // 静态工厂方法
    static Serializer getSerializerByType(int type){
        switch (type) {
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
