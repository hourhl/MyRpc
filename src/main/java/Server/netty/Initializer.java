package Server.netty;

import Server.provider.ServiceProvider;
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
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Initializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 定义编解码器
        pipeline.addLast(new myEncoder(new JsonSerializer()));
        pipeline.addLast(new myDecoder());



        pipeline.addLast(new Handler(serviceProvider));
    }
}
