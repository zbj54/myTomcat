package com.zbj;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 自制tomcat步骤及注意事项
 * 首先真实的tomcat启动类bootstrap,这里就用ZbjTomcat代替作为ZbjTomcat的启动类
 * 在启动类中，首先进行了servletMapping 的初始化（涉及到了ServletMapping和SerbletMappingConfig两个类），此操作类似于tomcat加载配置在web.xml中的servlet-mapping
 * 接着启动一个serverSocket服务，监听某端口
 * 当获得请求之后，通过socket拿到输入输出流，自行封装处理一下，包装成自己的MyRequest,MyResponse
 * 接着就是请求的转发dispatch()，转发到相应的servlet中去，真实的tomcat是满足servlet容器规范的，这里自定义一个MyServlet，相当于做一个自己的servlet规范，然后业务类
 * servlet必须要继承此标准规范servlet，就如同tomcat的servlet必须实现httpServlet一样的道理
 * 至此一个简单的自制tomcat就成型了
 */
public class ZbjTomcat {
    private int port = 8080;
    private Map<String,String> urlServletMap = new HashMap<>();

    public ZbjTomcat(int port){
        this.port = port;
    }

    public void start(){
        initServletMapping();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("zbjTomcat is starting...");
            while(true){
                    Socket socket = serverSocket.accept();
                    /*
                    这种写法是假nio式写法，避免了bio模式请求过多导致线程过多引起的服务器崩溃
                    但是请求过多的时候访问还是会变的非常的缓慢,java nio 三大核心 Channel,Buffer,Selector
                    */
                    Executors.newFixedThreadPool(200).execute(()->{
                        try {
                            InputStream inputStream = socket.getInputStream();
                            OutputStream outputStream = socket.getOutputStream();
                            MyRequest myRequest = new MyRequest();
                            if (inputStream.available() > 0) {
                                myRequest = new MyRequest(inputStream);
                            }
                            MyResponse myResponse = new MyResponse(outputStream);
                            dispatch(myRequest, myResponse);
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }catch (IOException e) {
                e.printStackTrace();
        }finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initServletMapping() {
        for(ServletMapping servletMapping : ServletMappingConfig.servletMappingList){
            urlServletMap.put(servletMapping.getUrl(),servletMapping.getClazz());
        }
    }

    private void dispatch(MyRequest myRequest, MyResponse myResponse) {
        String clazz = urlServletMap.get(myRequest.getUrl());
        if(null != clazz){
            try {
                Class<MyServlet> myServletClass = (Class<MyServlet>) Class.forName(clazz);
                MyServlet myServlet = myServletClass.newInstance();
                myServlet.service(myRequest,myResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        new ZbjTomcat(8080).start();
    }
}
