package com.example.deviceinfo.infoGetter;


import lombok.Data;

@Data
public class CPUInfo {
    public int mHz;//CPU频率
    public String vendor;//CPU厂商
    public String model;//CPU型号

}
