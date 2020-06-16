package com.zbj;

import java.io.IOException;

public class FindGirlServlet extends MyServlet{
    @Override
    public void doGet(MyRequest myRequest, MyResponse myResponse) {
        try {
            myResponse.write("hello girl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(MyRequest myRequest, MyResponse myResponse) {
        try {
            myResponse.write("hello girl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
