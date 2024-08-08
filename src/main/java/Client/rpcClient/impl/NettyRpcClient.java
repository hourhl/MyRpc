package Client.rpcClient.impl;

import Client.netty.nettyInitializer.NettyClientInitializer;
import Client.rpcClient.RpcClient;
import Client.serviceCenter.ServiceCenter;
import Client.serviceCenter.ZKServiceCenter;
import Common.Message.RpcRequest;
import Common.Message.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class NettyRpcClient implements RpcClient {

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    private ServiceCenter serviceCenter;
    public NettyRpcClient() throws InterruptedException{
        this.serviceCenter = new ZKServiceCenter();
    }

    // netty客户端初始化
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        // 从注册中心获取host，post
        InetSocketAddress address = serviceCenter.serviceDiscovery(request.getInterfaceName());
        String host = address.getHostName();
        int port = address.getPort();
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(request);
            // sync()堵塞获取结果
            channel.closeFuture().sync();
            // 则色的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在handler中设置）
            // AttributeKey是线程隔离的，不会有线程安全问题
            // 当前场景下选择堵塞获取结果
            // 其他场景也可以选择添加监听器的方式来异步获取结果channelFuture.addListener...
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = channel.attr(key).get();

            System.out.println(response);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
