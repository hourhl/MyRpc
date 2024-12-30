package Server.netty;

import Server.provider.ServiceProvider;
import common.Message.RpcRequest;
import common.Message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Log
@AllArgsConstructor
@Data
public class Handler extends ChannelInboundHandlerAdapter {

    private ServiceProvider serviceProvider;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest) msg;
        log.info("rpcRequest: " + rpcRequest);
        RpcResponse rpcResponse = getResponse(rpcRequest);
        ctx.writeAndFlush(rpcResponse);
        ReferenceCountUtil.release(msg);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private RpcResponse getResponse(RpcRequest rpcRequest) {
        // 向服务端提供的接口名
        String interfaceName = rpcRequest.getInterfaceName();
        Object service = serviceProvider.getService(interfaceName);
        Method method = null;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object result = method.invoke(service, rpcRequest.getParameters());
            return RpcResponse.success(result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return RpcResponse.fail();
        }
    }
}
