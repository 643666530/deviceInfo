package com.example.deviceinfo.infoGetter;

import lombok.Data;

@Data
public class OSInfo {
    public String vendor;//厂商
    public String arch;//内核类型
    public String name;//系统名
    public String version;//版本号
    public String description; // 系统描述

}
