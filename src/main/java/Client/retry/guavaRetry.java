package Client.retry;

import Client.rpc.BaseClient;
import Client.rpc.NettyClient;
import com.github.rholder.retry.*;
import common.Message.RpcRequest;
import common.Message.RpcResponse;
import lombok.extern.java.Log;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Log
public class guavaRetry {
    private BaseClient client;

    public RpcResponse sendServiceWithRetry(RpcRequest rpcRequest, BaseClient client) {
        this.client = client;
        // 配置重试逻辑
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                // 异常重试
                .retryIfException()
                // 结果重试
                .retryIfResult(response -> Objects.equals(response.getCode(), 500))
                // 等待策略：指数等待，初始为2s，最多等待32s，超过最大值后，一直保持最大值
                .withWaitStrategy(WaitStrategies.exponentialWait(2, 32, TimeUnit.SECONDS))
                // 最多重试次数（包含初始发送）
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("RetryListener:第" + attempt.getAttemptNumber() + "次调用");
                    }
                })
                .build();
        try {
            return retryer.call(() -> client.sendRequest(rpcRequest));
        } catch (Exception e){
            e.printStackTrace();
        }
        return RpcResponse.fail();
    }
}
