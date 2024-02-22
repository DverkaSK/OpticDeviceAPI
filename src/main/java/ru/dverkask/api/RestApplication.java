package ru.dverkask.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestApplication {

    public static void main(String[] args) {
        OpticDevice device = new OpticDevice(0.5);
        device.zoomIn(2);

        device.zoomOut(2);

//        DevicesList.getDevices().add(device);
//        DevicesList.getDevices().add(device);
//        DevicesList.getDevices().add(device);

        System.out.println(OpticDeviceService.getDevices());

        OpticDeviceService.writeToYAML();

        OpticDeviceService.getDevices().clear();
        OpticDeviceService.readFromYAML();
        SpringApplication.run(RestApplication.class, args);
    }
}
