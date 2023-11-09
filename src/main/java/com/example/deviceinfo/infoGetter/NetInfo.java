package com.example.deviceinfo.infoGetter;

import lombok.Data;

@Data
public class NetInfo {
    //网络信息
    public String name;//网络名
    public String address;//ip地址
    public String netMask;//子网掩码
    public String hwAddr;//MAC地址
    public String gateway;//默认网关
    public String ethernetDescription;//网卡描述
}


