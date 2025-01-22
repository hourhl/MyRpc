package Client.netty;

import common.Serialize.myCode.myDecoder;
import common.Serialize.myCode.myEncoder;
import common.Serialize.mySerialize.JsonSerializer;
import common.Serialize.mySerialize.Serializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


public class Initializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 入站定义解码器
        pipeline.addLast(new myDecoder());
//        pipeline.addLast(new myEncoder(Serializer.getSerializerByType(1)));
        pipeline.addLast(new Handler());
        // 出站定义编码器
        pipeline.addLast(new myEncoder(new JsonSerializer()));
    }
}
