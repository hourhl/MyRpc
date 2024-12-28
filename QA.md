# Version1

## basic

* 客户端为什么要通过动态代理来请求服务
* 什么是interface，什么是service
  * interface：服务端对客户端暴露的接口，例如UserService
  * service：服务端对接口的具体实现，例如UserServiceImpl
  * 在客户端发起对服务端的调用的时候，它实际只知道服务端暴露的接口，不知道具体的实现
  * 服务端需要根据客户端的接口名来找到对应的服务实现，本阶段将接口名和实现的映射关系存储到HashMap中
* 服务端中ServerSocket和Socket有什么区别
  * ServerSocket用于监听端口，返回一个socket
  * socket用于通信