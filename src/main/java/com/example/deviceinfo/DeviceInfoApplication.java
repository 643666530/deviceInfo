package com.example.deviceinfo;

import com.alibaba.fastjson.JSONObject;
import com.example.deviceinfo.infoGetter.DevInfoGetter;
import com.example.deviceinfo.infoGetter.DeviceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class DeviceInfoApplication implements CommandLineRunner {

    @Autowired
    DevInfoGetter devInfoGetter;

    public static void main(String[] args) {

        System.out.println(System.getProperty("java.library.path"));
        SpringApplication app = new SpringApplication(DeviceInfoApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception{


//        DeviceInfo info = devInfoGetter.getDevInfo();
        log.info("aa");
    }
}
