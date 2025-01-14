package Client.serviceCenter;

import org.apache.curator.framework.CuratorFramework;

public class ZKServiceCenter implements ServiceCenter{
    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRPC";
}
