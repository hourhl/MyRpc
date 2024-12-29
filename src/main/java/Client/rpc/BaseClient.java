package Client.rpc;

import common.Message.RpcRequest;
import common.Message.RpcResponse;

public interface BaseClient {
    RpcResponse sendRequest(RpcRequest rpcRequest);
}
