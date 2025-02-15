package common.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Request消息格式
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    // 客户端调用的接口名和服务名
    private String interfaceName;
    private String methodName;
    // 传递的参数和参数类型
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
