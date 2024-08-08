package Client.serviceCenter.ZkWatcher;

import Client.cache.serviceCache;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

public class watchZK {
    // curator提供的zookeeper客户端
    private CuratorFramework client;
    // 本地缓存
    serviceCache cache;

    public watchZK(CuratorFramework client, serviceCache cache) {
        this.client = client;
        this.cache = cache;
    }

    /**
     * 监听当前节点和子节点的更新，创建，删除
     */
    public void watchToUpdate(String path) throws InterruptedException {
        CuratorCache curatorCache = CuratorCache.build(client, "/");
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData childData, ChildData childData1) {
                // 第一个参数：事件类型（枚举）
                // 第二个参数：节点更新前的状态、数据
                // 第三个参数：节点更新后的状态、数据
                // 创建节点时，节点刚被创建，不存在，更新前节点，所以第二个参数为null
                // 删除节点时，节点被删除，不存在，更新后节点，所以第三个参数为null
                // 节点创建时没有赋予值create / curator / app1只创建节点，在这种情况下，更新前节点的data为null，获取不到更新前节点的数据
                switch (type.name()) {
                    case "NODE_CREATED" : // 监听器第一次执行时节点存在也会触发次事件
                        String[] pathList = parsePath(childData1);
                        if(pathList.length <= 2) break;
                        else {
                            String serviceName = pathList[1];
                            String address = pathList[2];
                            // 将新注册的服务加入到本地缓存中
                            cache.addServiceToCache(serviceName, address);
                        }
                        break;
                    case "NODE_CHANGED": // 节点更新
                        if (childData.getData() != null) {
                            System.out.println("修改前的数据: " + new String(childData.getData()));
                        } else {
                            System.out.println("节点第一次赋值！");
                        }
                        String[] oldPathList = parsePath(childData);
                        String[] newPathList = parsePath(childData1);
                        cache.replaceServiceAddress(oldPathList[1], oldPathList[2], newPathList[2]);
                        System.out.println("修改后的数据： " + new String(childData1.getData()));
                        break;
                    case "NODE_DELETED" : // 节点删除
                        String[] pathList_d = parsePath(childData);
                        if(pathList_d.length <= 2) break;
                        else{
                            String serviceName = pathList_d[1];
                            String address = pathList_d[2];
                            // 将新注册的服务加入到本地缓存中
                            cache.delete(serviceName, address);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        // 开启监听
        curatorCache.start();
    }

    // 解析节点对应地址
    public String[] parsePath(ChildData childData) {
        // 获取更新的节点路径
        String path = new String(childData.getData());
        // 按照格式，读取
        return path.split("/");
    }
}
