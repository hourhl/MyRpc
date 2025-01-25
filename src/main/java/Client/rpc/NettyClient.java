package Client.rpc;


import Client.netty.Initializer;
import Client.serviceCenter.ServiceCenter;
import Client.serviceCenter.ZKServiceCenter;
import common.Message.RpcRequest;
import common.Message.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;

import java.net.InetSocketAddress;

@AllArgsConstructor
@Data
@Log
public class NettyClient implements BaseClient{
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    private ServiceCenter serviceCenter;
    public NettyClient() throws InterruptedException {
        this.serviceCenter = new ZKServiceCenter();
    }

    // 客户端初始化
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new Initializer());
    }

    @Override
    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        // 获取服务的ip和端口
        log.info("interfaceName : " + rpcRequest.getInterfaceName());
        InetSocketAddress address = this.serviceCenter.serviceDiscovery(rpcRequest.getInterfaceName());
        log.info("get address :" + address);
        String host  = address.getHostName();
        int port = address.getPort();

        Channel channel = null;
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channel = channelFuture.channel();

            channel.writeAndFlush(rpcRequest);
            channel.closeFuture().sync();

            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RpcResponse");
            RpcResponse rpcResponse = channel.attr(key).get();

            log.info("NettyClient sendRequest get rpcResponse : " + rpcResponse);
            return rpcResponse;
        } catch (InterruptedException e) {
            e.printStackTrace();
//        } finally {
//            shutdown();
        }
        return null;
    }

//    public void shutdown() {
//        eventLoopGroup.shutdownGracefully();
//        log.info("NettyClient shutdown");
//    }
}
