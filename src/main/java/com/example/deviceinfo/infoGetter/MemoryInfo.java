package com.example.deviceinfo.infoGetter;

import lombok.Data;

@Data
public class MemoryInfo {
    //内存
    public long memTotal;
    public long memUsed;
    public long memFree;
    //交换区
    public long swapTotal;
    public long swapUsed;
    public long swapFree;
}
