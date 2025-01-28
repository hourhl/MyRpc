package Client.serviceCenter.balance.impl;

import Client.serviceCenter.balance.LoadBalance;
import lombok.extern.java.Log;

import java.util.*;

@Log
public class HashBalance implements LoadBalance {
    // 虚拟节点格式
    private static final int VIRTUAL_NUM = 5;
    // key:hash value:VirtualNode
    private SortedMap<Integer, String> shards = new TreeMap<Integer, String>();
    // 真实节点列表
    private List<String> realNodes = new LinkedList<>();
    // 初始服务器
    private String[] servers = null;

    // 初始化：将真实节点和对应的虚拟节点添加到哈希环中
    private void init(List<String> serviceLists){
        for(String service : serviceLists){
            realNodes.add(service);
            log.info("RealNode add : " + service);
            // 每个真实节点生成VIRTUAL_NUM个虚拟节点
            for (int i = 0; i < VIRTUAL_NUM; i++){
                String virNode = service + "&&VN" + i;
                int hash = getHash(virNode);
                shards.put(hash, virNode);
                log.info("VirtualNode add : hash(" + hash + "); virNode(" + virNode + ")");
            }
        }
    }

    // 采用FNV-1a哈希算法的变体
    // 它的基本工作方式如下
    // 1. 从一个大的基准值开始
    // 2. 对输入数据的每个字节进行异或运算
    // 3. 将结果乘以一个质数
    private static int getHash(String str){
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for(int i = 0; i < str.length(); i++){
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        return hash < 0 ? Math.abs(hash) : hash;
    }

    @Override
    public String balance(List<String> addressList){
        String random = UUID.randomUUID().toString();
        return getServer(random, addressList);
    }

    public String getServer(String node, List<String> serviceList){
        init(serviceList);
        int hash = getHash(node);
        Integer key = null;
        // 在哈希环上顺时针找到第一个服务器节点
        SortedMap<Integer, String> subMap = shards.tailMap(hash);
        if(subMap.isEmpty()){
            key = shards.lastKey();
        } else {
            key = subMap.firstKey();
        }
        String virtualNode = shards.get(key);
        log.info("virtualNode : " + virtualNode);
        return virtualNode.substring(0, virtualNode.indexOf("&&"));
    }

    @Override
    public void addNode(String node){
        if(!realNodes.contains(node)){
            realNodes.add(node);
            log.info("RealNode add : " + node);
            for (int i = 0; i < VIRTUAL_NUM; i++){
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash, virtualNode);
                log.info("VirtualNode add : " + virtualNode);
            }
        }
    }

    @Override
    public void deleteNode(String node){
        if(realNodes.contains(node)){
            realNodes.remove(node);
            log.info("RealNode delete : " + realNodes);
            for(int i = 0; i < VIRTUAL_NUM; i++){
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.remove(hash);
                log.info("VirtualNode delete : " + virtualNode);
            }
        }
    }
}
