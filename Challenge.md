1. Fastjson反序列化

   将获得的response字节反序列化为对应的类时，出现Exception in thread "main" java.lang.ClassCastException: class com.alibaba.fastjson.JSONObject cannot be cast to class common.pojo.User (com.alibaba.fastjson.JSONObject and common.pojo.User are in unnamed module of loader 'app')
   	at jdk.proxy2/jdk.proxy2.$Proxy5.getUserById(Unknown Source)
   	at Client.Client.main(Client.java:16)、

   * 原因

     RPCResponse的data字段，无法直接反序列化为对应的类型

   * 解决方法

     RPCResponse添加dataType字段，保存data类型，用于在反序列化时将data解析为原有的类型
