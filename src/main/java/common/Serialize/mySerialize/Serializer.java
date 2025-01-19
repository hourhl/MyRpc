package common.Serialize.mySerialize;

public interface Serializer {
    byte[] serialize(Object obj);
    Object deserialize(byte[] bytes, int messageType);


    // type - 0 : java io原生序列器
    // type - 1 : fastjson
    int getType();
    static Serializer getSerializerByCode(int code){
        switch (code) {
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
