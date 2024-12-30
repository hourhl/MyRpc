package Server.server.impl;

import Server.netty.Initializer;
import Server.provider.ServiceProvider;
import Server.server.RPCServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;


@Log
@AllArgsConstructor
public class NettyRPCServer implements RPCServer {
    private ServiceProvider serviceProvider;

    @Override
    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        log.info("Netty RPC Server started on port " + port);

        try {
            ServerBootstrap serverbootstrap = new ServerBootstrap();
            serverbootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new Initializer(serviceProvider));

            // 同步阻塞
            ChannelFuture channelFuture = serverbootstrap.bind(port).sync();
            // 死循环监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop(){}
}
