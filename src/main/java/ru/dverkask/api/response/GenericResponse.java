package ru.dverkask.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Класс для предоставления ответа")
public class GenericResponse<T> {
    @Schema(description = "Статус ответа")
    private int status;
    @Schema(description = "Сообщение ответа")
    private String message;
    @Schema(description = "Вся информация ответа")
    private T data;
}
