package ru.dverkask.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Сущность оптического устройства")
public class OpticDevice {
    @Schema(description = "Уникальный ID устройства", example = "3a3c4efc-d7d9-48a0-8d76-207df95ea4c4")
    private UUID   uuid;
    @Schema(description = "Оптическая сила", example = "15")
    private double opticPower;
    @Schema(description = "Фокусное расстояние", example = "1/15")
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
                "uuid=" + this.uuid +
                ", opticPower=" + this.opticPower +
                ", focalDistance=" + this.focalDistance +
                "}";
    }
}