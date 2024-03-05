package ru.dverkask.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dverkask.api.service.opticdevice.OpticDeviceService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class RestApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpticDeviceService opticDeviceService;

    @BeforeEach public void setup() {
        OpticDevice device1 = new OpticDevice(2);
        OpticDevice device2 = new OpticDevice(3);
        Mockito.when(
                opticDeviceService.findAll()
        ).thenReturn(Arrays.asList(device1, device2));
    }

    @Test void getDevicesTest() throws Exception {
        mockMvc.perform(get("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(opticDeviceService, Mockito.times(1)).findAll();
    }

    @Test void getDeviceTest() throws Exception {
        UUID        uuid   = UUID.randomUUID();
        OpticDevice device = new OpticDevice(2);
        Mockito.when(
                opticDeviceService.find(uuid)
        ).thenReturn(device);

        mockMvc.perform(get("/api/devices/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(opticDeviceService, Mockito.times(1)).find(uuid);
    }

    @Test void zoomInTest() throws Exception {
        UUID        uuid     = UUID.randomUUID();
        int         increase = 5;
        OpticDevice device   = new OpticDevice(2);

        doAnswer((invocation) -> {
            device.zoomIn(increase);
            return null;
        }).when(opticDeviceService).zoomIn(uuid, increase);

        mockMvc.perform(patch("/api/devices/" + uuid + "/zoomIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"zoom\":\"" + increase + "\"}"))
                .andExpect(status().isOk());

        Mockito.verify(opticDeviceService, Mockito.times(1)).zoomIn(uuid, increase);

        assertEquals(device.getOpticPower(), 10);
    }

    @Test void zoomOutTest() throws Exception {
        UUID        uuid     = UUID.randomUUID();
        int         decrease = 2;
        OpticDevice device   = new OpticDevice(2);

        doAnswer((invocation) -> {
            device.zoomOut(decrease);
            return null;
        }).when(opticDeviceService).zoomOut(uuid, decrease);

        mockMvc.perform(patch("/api/devices/" + uuid + "/zoomOut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"zoom\":\"" + decrease + "\"}"))
                .andExpect(status().isOk());

        Mockito.verify(opticDeviceService, Mockito.times(1)).zoomOut(uuid, decrease);

        assertEquals(device.getOpticPower(), 1);
    }

    @Test void deleteDeviceTest() throws Exception {
        UUID uuid = UUID.randomUUID();

        Mockito.doNothing().when(opticDeviceService).delete(uuid);

        mockMvc.perform(delete("/api/devices/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(opticDeviceService, Mockito.times(1)).delete(uuid);
    }

    @Test void createDeviceTest() throws Exception {
        OpticDevice device = new OpticDevice(2);

        ArgumentCaptor<OpticDevice> captor = ArgumentCaptor.forClass(OpticDevice.class);
        doAnswer(invocation -> null).when(opticDeviceService).save(captor.capture());

        String deviceJson = "{\"opticPower\": 2}";

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceJson))
                .andExpect(status().isCreated());

        Mockito.verify(opticDeviceService, Mockito.times(1)).save(any(OpticDevice.class));

        OpticDevice actualDevice = captor.getValue();
        assertEquals(device.getOpticPower(), actualDevice.getOpticPower());
        assertEquals(device.getFocalDistance(), actualDevice.getFocalDistance());
    }
}