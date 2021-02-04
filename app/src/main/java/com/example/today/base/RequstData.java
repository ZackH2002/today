package com.example.today.base;

import java.util.ArrayList;
import java.util.List;

public class RequstData {
    public String msg;
    List<BaseData> lesssonDataList =new ArrayList<>();

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<BaseData> getLesssonDataList() {
        return lesssonDataList;
    }

    public void setLesssonDataList(List<BaseData> lesssonDataList) {
        this.lesssonDataList = lesssonDataList;
    }
}
