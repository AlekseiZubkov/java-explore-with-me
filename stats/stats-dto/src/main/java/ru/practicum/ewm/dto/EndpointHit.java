package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import java.time.LocalDateTime;

import static constant.Constants.PATTERN_FOR_DATETIME;

@Data
@Validated
public class EndpointHit {
    @Positive
    private Long id; // Идентификатор записи
    @NotBlank
    private String app; // Идентификатор сервиса для которого записывается информация
    @NotBlank
    private String uri; // URI для которого был осуществлен запрос
    @NotBlank
    private String ip; // IP-адрес пользователя, осуществившего запрос
    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime timestamp; //  Дата и время, когда был совершен запрос к эндпоинту

}