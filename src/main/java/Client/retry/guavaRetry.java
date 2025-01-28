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
                .retryIfException()
                .retryIfResult(response -> Objects.equals(response.getCode(), 500))
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
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
