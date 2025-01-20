package Client.netty;

import common.Serialize.myCode.myDecoder;
import common.Serialize.myCode.myEncoder;
import common.Serialize.mySerialize.JsonSerializer;
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

        // 定义消息格式，解决粘包问题
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        // 计算消息长度，写入前4个字节
        pipeline.addLast(new LengthFieldPrepender(4));
        // 定义编码器
        pipeline.addLast(new myDecoder());
        // 定义解码器
        pipeline.addLast(new myEncoder(new JsonSerializer()));

        pipeline.addLast(new Handler());
    }
}
