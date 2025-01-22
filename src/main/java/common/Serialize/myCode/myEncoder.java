package common.Serialize.myCode;

import common.Message.MessageType;
import common.Message.RpcRequest;
import common.Message.RpcResponse;
import common.Serialize.mySerialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;

@Log
@AllArgsConstructor
public class myEncoder extends MessageToByteEncoder {
    private Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        log.info(msg.toString());
        // 数据包格式
        // 包类型 + 序列化器类型 + 数据长度 + 数据
        // 优点：防止粘包，格式清晰

        if(msg instanceof RpcRequest) {
            out.writeShort(MessageType.REQUEST.getCode());
        } else if(msg instanceof RpcResponse) {
            out.writeShort(MessageType.RESPONSE.getCode());
        }

        out.writeShort(serializer.getType());
        byte[] data = serializer.serialize(msg);
        out.writeInt(data.length);
        out.writeBytes(data);
        log.info(out.toString());
    }

}
