package common.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {
    private int code;
    private String msg;
    private Object data;
    private Class<?> dataType;

    // success 和 fail方法不需要依赖实例，因此将其定义为static类型
    public static RpcResponse success(Object data) {
        return RpcResponse.builder()
                .code(200)
                .msg("Success")
                .data(data)
                .dataType(data.getClass()).build();
    }

    public static RpcResponse fail() {
        return RpcResponse.builder()
                .code(500)
                .msg("Fail")
                .data(null).build();
    }
}
