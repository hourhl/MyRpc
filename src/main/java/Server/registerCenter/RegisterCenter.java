package Server.registerCenter;

import java.net.InetSocketAddress;

public interface RegisterCenter {
    void serviceRegister(String serviceName, InetSocketAddress serviceAddress, boolean canRetry);
}
