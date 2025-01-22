1. Fastjson反序列化

    java.lang.IllegalArgumentException: ParameterType miss match: expected : int, found : java.lang.Integer
   	at io.netty.handler.codec.ByteToMessageDecoder.callDecode(ByteToMessageDecoder.java:471)
   	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:276)
   	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
   	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
   	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
   	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410)
   	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
   	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
   	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919)
   	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:163)
   	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:714)
   	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:650)
   	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:576)
   	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493)
   	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
   	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
   	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
   	at java.base/java.lang.Thread.run(Thread.java:1575)
   Caused by: java.lang.IllegalArgumentException: ParameterType miss match: expected : int, found : java.lang.Integer



2. Exception in thread "main" java.lang.ClassCastException: class com.alibaba.fastjson.JSONObject cannot be cast to class common.pojo.User (com.alibaba.fastjson.JSONObject and common.pojo.User are in unnamed module of loader 'app')
   	at jdk.proxy2/jdk.proxy2.$Proxy5.getUserById(Unknown Source)
   	at Client.Client.main(Client.java:14)