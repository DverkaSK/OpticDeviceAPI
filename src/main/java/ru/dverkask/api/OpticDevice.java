package ru.dverkask.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OpticDevice {
    private UUID   uuid;
    private double opticPower;
    private double focalDistance;

    @JsonCreator
    public OpticDevice(@JsonProperty("opticPower") double opticPower) {
        this.uuid = UUID.randomUUID();
        this.opticPower = opticPower;
        this.focalDistance = 1 / opticPower;
    }

    public void zoomIn(int increase) {
        opticPower *= increase;
        focalDistance = 1 / opticPower;
    }

    public void zoomOut(int decrease) {
        opticPower /= decrease;
        focalDistance = 1 / opticPower;
    }

    @Override public String toString() {
        return "OpticDevice{" +
                "opticPower=" + this.opticPower +
                ", focalDistance=" + this.focalDistance +
                "}";
    }
}