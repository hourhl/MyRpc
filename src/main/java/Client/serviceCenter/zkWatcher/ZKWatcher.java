package Client.serviceCenter.zkWatcher;

import Client.cache.serviceCache;
import lombok.extern.java.Log;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

@Log
public class ZKWatcher {

    private CuratorFramework client;
    private serviceCache cache;

    public ZKWatcher(CuratorFramework client, serviceCache cache){
        this.client = client;
        this.cache = cache;
    }

    // 监听节点
    public void watchToUpdate(String path) throws InterruptedException {
        CuratorCache curatorCache = CuratorCache.build(client, path);
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData childData, ChildData childData1) {
                // childData : /[root]/[serviceName]/[serviceAddress]
                switch (type.name()) {
                    case "NODE_CREATED":
                        String[] paths = parsePath(childData1);
                        if(paths.length <= 2) break;
                        else {
                            String serviceName = paths[1];
                            String serviceAddress = paths[2];
                            // 将新注册的服务加入本地缓存
                            cache.addAddressToCache(serviceName, serviceAddress);
                        }
                        break;
                    case "NODE_CHANGED" :
                        if(childData.getData() != null){
                            log.info("Before Update：" + new String(childData.getData()));
                        } else {
                            log.info("First add address");
                        }
                        String[] oldPaths = parsePath(childData);
                        String[] newPaths = parsePath(childData1);
                        cache.replaceAddress(oldPaths[1], oldPaths[2], newPaths[2]);
                        log.info("After update: " + new String(childData1.getData()));
                        break;
                    case "NODE_DELETED":
                        String[] paths_d = parsePath(childData);
                        if (paths_d.length <= 2) break;
                        else{
                            String serviceName = paths_d[1];
                            String serviceAddress = paths_d[2];
                            cache.deleteAddress(serviceName, serviceAddress);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        // start watcher
        curatorCache.start();
    }


    public String[] parsePath(ChildData childData){
        // 获取更新的节点路径
        String path = new String(childData.getPath());
        return path.split("/");
    }
}
