package Client.cache;

import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log
public class serviceCache {
    private static HashMap<String, List<String>> cache = new HashMap<>();

    public void addAddressToCache(String serviceName, String serviceAddress) {
        if(cache.containsKey(serviceName)){
            List<String> addresses = cache.get(serviceName);
            addresses.add(serviceAddress);
        } else {
            List<String> addresses = new ArrayList<>();
            addresses.add(serviceAddress);
            cache.put(serviceName, addresses);
        }
        log.info("Add address : " + serviceName + "(" + serviceAddress + ")");
    }

    public void replaceAddress(String serviceName, String oldAddress, String newAddress) {
        if(cache.containsKey(serviceName)){
            List<String> addresses = cache.get(serviceName);
            addresses.remove(oldAddress);
            addresses.add(newAddress);
            log.info("Replace serviceAddress : " + serviceName + "(" + newAddress + ")");
        } else {
            log.info("Replace fail : the service is not exist");
        }

    }

    public List<String> getAddressFromCache(String serviceName) {
        if(!cache.containsKey(serviceName)){
            return null;
        }
        List<String> addresss = cache.get(serviceName);
        log.info("Get serviceAddress:" + serviceName + "(" + addresss + ")");
        return addresss;
    }

    public void deleteAddress(String serviceName, String serviceAddress) {
        List<String> addresses = cache.get(serviceName);
        addresses.remove(serviceAddress);
        log.info("Delete address : " + serviceName + "(" + serviceAddress + ")");
    }
}
