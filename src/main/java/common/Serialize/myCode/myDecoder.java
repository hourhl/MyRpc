package common.Serialize.myCode;

import common.Message.MessageType;
import common.Serialize.mySerialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.java.Log;

import java.util.List;

@Log
public class myDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 数据包格式
        // 包类型 + 序列化器类型 + 数据长度 + 数据
        // 优点：防止粘包，格式清晰
        short messageType = in.readShort();
        if(messageType != MessageType.REQUEST.getCode() && messageType != MessageType.RESPONSE.getCode()){
            log.info("Do not support this message type");
            return;
        }

        short serializeType = in.readShort();
        Serializer serializer = Serializer.getSerializerByType(serializeType);
        if(serializer == null){
            log.info("Get Serializer failed");
            throw new RuntimeException("Get Serializer failed");
        }

        int length = in.readInt();
        byte[] data = new byte[length];
        in.readBytes(data);
        Object deserializeData = serializer.deserialize(data, messageType);
        out.add(deserializeData);
    }
}
