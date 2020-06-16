package com.zbj;

import java.util.ArrayList;
import java.util.List;

public class ServletMappingConfig {

    public static List<ServletMapping> servletMappingList = new ArrayList<>();

    static {
        servletMappingList.add(new ServletMapping("findGirl","/girl","com.zbj.FindGirlServlet"));
        servletMappingList.add(new ServletMapping("helloWorld","/world","com.zbj.HelloWorldServlet"));
    }
}
