package com.example.deviceinfo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.deviceinfo.util.IPUtil;
import com.example.deviceinfo.util.SigarUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hyperic.sigar.*;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.SocketException;
import java.util.Optional;

/**
 * @title:MyController
 * @description:
 * @author:huyao
 * @date:2022/12/21
 **/
@RestController
public class MyController {

    private static String osName = System.getProperty("os.name");

    @GetMapping("/getDeviceInfo")
    JSONObject getDeviceInfo(HttpServletRequest request) throws SigarException, IOException {
        Sigar sigar = SigarUtil.getInstance();
        Mem mem = sigar.getMem();
        JSONObject object = new JSONObject();

        //获得memoryLoad
        double memoryLoad = (double) mem.getUsed() / mem.getTotal();
        memoryLoad *= 100;
        memoryLoad = new BigDecimal(memoryLoad).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        object.put("memoryLoad", memoryLoad);


        //获取cpuLoad
        CpuPerc cpu = sigar.getCpuPerc();
        double cpuLoad = cpu.getCombined();
        cpuLoad *= 100;
        cpuLoad = new BigDecimal(cpuLoad).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        object.put("cpuLoad", cpuLoad);


        //获取ip地址
        //String ip = IPUtil.getIPUsable();

        Optional<Inet4Address> localIp4Address = IPUtil.getLocalIp4Address();
        Inet4Address inet4Address = localIp4Address.get();
        String ip = inet4Address.toString();
        ip = ip.substring(1);
        object.put("IPAddress", ip);

//        获取网络信息
        String ifNames[] = sigar.getNetInterfaceList();
        Long sendCount = 0L;
        Long receiveCount = 0L;
        Long lossCountofSend = 0L;
        Long lossCountofReceive = 0L;
        for (int i = 0; i < ifNames.length; i++) {
            String name = ifNames[i];
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            //System.out.println("网络设备名:    " + name);// 网络设备名
            //System.out.println("IP地址:    " + ifconfig.getAddress());// IP地址
            //System.out.println("子网掩码:    " + ifconfig.getNetmask());// 子网掩码
            if ((ifconfig.getFlags() & 1L) <= 0L) {
                //System.out.println("!IFF_UP...skipping getNetInterfaceStat");
                continue;
            }
            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            //System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets());// 接收的总包裹数
            //System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets());// 发送的总包裹数
            //System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
            Long receiveNum = ifstat.getRxPackets();
            Long sendNum = ifstat.getTxPackets();
            sendCount += sendNum;
            receiveCount += receiveNum;

            Long lossNumofSend = ifstat.getTxDropped();
            Long lossNumofReceive = ifstat.getRxDropped();
            lossCountofReceive += lossNumofReceive;
            lossCountofSend += lossNumofSend;


            //System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes());// 发送的总字节数
            //System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors());// 接收到的错误包数
            //System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors());// 发送数据包时的错误数
            //System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped());// 接收时丢弃的包数
            //System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped());// 发送时丢弃的包数
            //System.out.println("******************************************************");
        }
        double lossRateOfSend = (double) lossCountofSend / sendCount;
        double lossRateOfReceive = (double) lossCountofReceive / receiveCount;

        System.out.println("=======================================");
        System.out.println("receiveCount = " + receiveCount);
        System.out.println("sendCount = " + sendCount);
        System.out.println("lossCountofReceive = " + lossCountofReceive);
        System.out.println("lossCountofSend = " + lossCountofSend);
        System.out.println("lossRateOfReceive = " + lossRateOfReceive);
        System.out.println("lossRateOfSend = " + lossRateOfSend);


        //获取发送时丢包率
        lossRateOfSend = new BigDecimal(lossRateOfSend).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        object.put("lossRateOfSend", lossRateOfSend);

        //获取接收时丢包率
        lossRateOfReceive = new BigDecimal(lossRateOfReceive).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        object.put("lossRateOfReceive", lossRateOfReceive);


        return object;
    }

    /**
     * Window 和Linux 得到磁盘的使用率
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getDeviceDisc")
    public static double getDeviceDisc(HttpServletRequest request) throws Exception {
        double totalHD = 0;
        double usedHD = 0;

        if (osName.toLowerCase().contains("windows") ||
                osName.toLowerCase().contains("win")) {
            long allTotal = 0;
            long allFree = 0;

            for (char c = 'A'; c <= 'Z'; c++) {
                String dirName = c + ":/";
                File win = new File(dirName);

                if (win.exists()) {
                    long total = (long) win.getTotalSpace();
                    long free = (long) win.getFreeSpace();
                    allTotal = allTotal + total;
                    allFree = allFree + free;
                }
            }

            Double precent = (Double) (1 - ((allFree * 1.0) / allTotal)) * 100;
            BigDecimal b1 = new BigDecimal(precent);
            precent = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            return precent;
        } else {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("df -hl"); // df -hl 查看硬盘空间
            BufferedReader in = null;

            try {
                in = new BufferedReader(new InputStreamReader(
                        p.getInputStream()));

                String str = null;
                String[] strArray = null;

                while ((str = in.readLine()) != null) {
                    int m = 0;
                    strArray = str.split(" ");

                    for (String tmp : strArray) {
                        if (tmp.trim().length() == 0) {
                            continue;
                        }

                        ++m;

                        if (tmp.indexOf("G") != -1) {
                            if (m == 2) {
                                if (!tmp.equals("") && !tmp.equals("0")) {
                                    totalHD += (Double.parseDouble(tmp.substring(
                                            0, tmp.length() - 1)) * 1024);
                                }
                            }

                            if (m == 3) {
                                if (!tmp.equals("none") && !tmp.equals("0")) {
                                    usedHD += (Double.parseDouble(tmp.substring(
                                            0, tmp.length() - 1)) * 1024);
                                }
                            }
                        }

                        if (tmp.indexOf("M") != -1) {
                            if (m == 2) {
                                if (!tmp.equals("") && !tmp.equals("0")) {
                                    totalHD += Double.parseDouble(tmp.substring(
                                            0, tmp.length() - 1));
                                }
                            }

                            if (m == 3) {
                                if (!tmp.equals("none") && !tmp.equals("0")) {
                                    usedHD += Double.parseDouble(tmp.substring(
                                            0, tmp.length() - 1));
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                //logger.debug(e);
            } finally {
                in.close();
            }

            // 保留2位小数
            double precent = (usedHD / totalHD) * 100;
            BigDecimal b1 = new BigDecimal(precent);
            precent = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            System.out.println(precent);
            return precent;
        }
    }
}
