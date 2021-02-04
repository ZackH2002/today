package com.example.today.base;

public class ContentUrl {
    public static String getConstellationUrl(String consName,String type){
        String url="http://web.juhe.cn:8080/constellation/getAll?consName="+consName+"&type="+type+"&key=300ae7250536d3de2a5c9050da825e67";
        return url;
    }
}
