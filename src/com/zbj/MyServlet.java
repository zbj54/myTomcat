package com.zbj;

public abstract class MyServlet {

    public abstract void doGet(MyRequest myRequest,MyResponse myResponse);
    public abstract void doPost(MyRequest myRequest,MyResponse myResponse);
    public void service(MyRequest myRequest,MyResponse myResponse){
        if ("GET".equals(myRequest.getMethod())){
            doGet(myRequest,myResponse);
        }else if("POST".equals(myRequest.getMethod())){
            doPost(myRequest,myResponse);
        }
    }
}
