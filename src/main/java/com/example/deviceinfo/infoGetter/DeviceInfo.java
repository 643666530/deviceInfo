package com.example.deviceinfo.infoGetter;


import lombok.Data;

import java.util.List;

@Data
public class DeviceInfo {
    public List<NetInfo> netInfoList;
    public List<CPUInfo> cpuInfoList;
    public MemoryInfo memoryInfo;
    public OSInfo osInfo;
}

