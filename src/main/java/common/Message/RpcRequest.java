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
    // Serializable 说明该类可以被序列化
    private static final long serialVersionUID = 1111111L;

    // 客户端调用的接口名和服务名
    private String interfaceName;
    private String methodName;
    // 传递的参数和参数类型
    private Object[] parameters;
    // Class<?>标识任意类型的class对象
    // eg: paramTypes[0] = String.class
    private Class<?>[] paramTypes;
}
