package Server.netty.nettyInitializer;

import Server.netty.handler.NettyRPCServerHandler;
import Server.provider.ServiceProvider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 消息格式 [长度][消息体],解决沾包问题
        pipeline.addLast(new LengthFieldPrepender(4));

        // 使用java序列化方式，netty的自带的解码编码支持传输这种结构
        pipeline.addLast(new ObjectEncoder());
        // 使用了Netty中的ObjectDecoder,它用于将字节流解码为java对象
        // 在ObjectDecoder的构造函数中传入了一个ClassResolver对象，用于u解析类名并加载相应的类
        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
            @Override
            public Class<?> resolve(String className) throws ClassNotFoundException {
                return Class.forName(className);
            }
        }));

        pipeline.addLast(new NettyRPCServerHandler(serviceProvider));
    }
}
