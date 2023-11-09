package com.example.deviceinfo;


import com.example.deviceinfo.util.IPUtil;
import org.hyperic.sigar.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

@SpringBootTest
class DeviceInfoApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void systempathget(){
        String l = System.getProperty("java.library.path");
        l.replace(";","\n");
        System.out.println(l);
    }



    /*@Test
    public void cpuAndMemTest() throws SigarException, UnknownHostException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        JSONObject object = new JSONObject();

        double memoryLoad = (double) mem.getUsed() / mem.getTotal();
        memoryLoad *= 100;
        memoryLoad = new BigDecimal(memoryLoad).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        object.put("memoryLoad",memoryLoad);

        CpuPerc cpu = sigar.getCpuPerc();
        double cpuLoad = cpu.getCombined();
        cpuLoad *= 100;
        cpuLoad = new BigDecimal(cpuLoad).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        object.put("cpuLoad",cpuLoad);

        InetAddress inetAddress = InetAddress.getLocalHost();
        String ip = inetAddress.getHostAddress();
*//*
        String ifNames[] = sigar.getNetInterfaceList();
        for (int i = 0; i < ifNames.length; i++) {
            String name = ifNames[i];
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            System.out.println("网络设备名:    " + name);// 网络设备名
            System.out.println("IP地址:    " + ifconfig.getAddress());// IP地址
            System.out.println("子网掩码:    " + ifconfig.getNetmask());// 子网掩码
            if ((ifconfig.getFlags() & 1L) <= 0L) {
                System.out.println("!IFF_UP...skipping getNetInterfaceStat");
                continue;
            }
            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets());// 接收的总包裹数
            System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets());// 发送的总包裹数
            System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
            System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes());// 发送的总字节数
            System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors());// 接收到的错误包数
            System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors());// 发送数据包时的错误数
            System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped());// 接收时丢弃的包数
            System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped());// 发送时丢弃的包数
            System.out.println("******************************************************");
        }*//*

        String address = InetAddress.getLocalHost().getHostAddress();
        System.out.println("address is " + address);


        System.out.println("cpuload is " + cpuLoad);
        System.out.println("memoryLoad is " + memoryLoad);
        *//*System.out.println("ip is " + ip);*//*
        try {
            String addressIPUtil = IPUtil.getLocalIp4Address().get().toString().replace("/", "");
            System.out.println("address is " + address);
            System.out.println("addressIPUtil is " + addressIPUtil);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }
*/
    @Test
    public void IPUtilTest() throws SocketException {
//        System.out.println(IPUtil.getLocalIp4Address().get().toString().replace("/", ""));
        System.out.println("ip is " + IPUtil.getV4IP());
    }

/*
    @Test
    public void netTest() throws SigarException {
        Sigar sigar = new Sigar();
        String ifNames[] = sigar.getNetInterfaceList();
        Long sendCount = 0L;
        Long receiveCount = 0L;
        Long lossCountofSend = 0L;
        Long lossCountofReceive = 0L;
        for (int i = 0; i < ifNames.length; i++) {
            String name = ifNames[i];
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            System.out.println("网络设备名:    " + name);// 网络设备名
            System.out.println("IP地址:    " + ifconfig.getAddress());// IP地址
            System.out.println("子网掩码:    " + ifconfig.getNetmask());// 子网掩码
            if ((ifconfig.getFlags() & 1L) <= 0L) {
                System.out.println("!IFF_UP...skipping getNetInterfaceStat");
                continue;
            }
            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets());// 接收的总包裹数
            System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets());// 发送的总包裹数
            System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
            Long receiveNum = ifstat.getRxPackets();
            Long sendNum = ifstat.getTxPackets();
            sendCount += sendNum;
            receiveCount += receiveNum;

            Long lossNumofSend = ifstat.getTxDropped();
            Long lossNumofReceive = ifstat.getRxDropped();
            lossCountofReceive += lossNumofReceive;
            lossCountofSend += lossNumofSend;


            System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes());// 发送的总字节数
            System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors());// 接收到的错误包数
            System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors());// 发送数据包时的错误数
            System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped());// 接收时丢弃的包数
            System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped());// 发送时丢弃的包数
            System.out.println("******************************************************");
        }
        double lossRateOfSend = (double) lossCountofSend / sendCount;
        double lossRateOfReceive = (double) lossCountofReceive / receiveCount;

        System.out.println("receiveCount = " + receiveCount);
        System.out.println("sendCount = " + sendCount);
        System.out.println("lossCountofReceive = " + lossCountofReceive);
        System.out.println("lossCountofSend = " + lossCountofSend);
        System.out.println("lossRateOfReceive = " + lossRateOfReceive);
        System.out.println("lossRateOfSend = " + lossRateOfSend);

        double testData = (double) 5/3;
        System.out.println("testData = " + testData);

    }*/
}
