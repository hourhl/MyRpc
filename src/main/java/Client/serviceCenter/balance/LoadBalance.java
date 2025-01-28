package Client.serviceCenter.balance;

import java.util.List;

public interface LoadBalance {
    // 从服务器列表中获取一个合适的服务器地址
    String balance(List<String> addressList);
    // 添加一个真实节点和虚拟节点到哈希环中
    void addNode(String node);
    // 删除一个真实节点和对应的虚拟节点
    void deleteNode(String node);
}
