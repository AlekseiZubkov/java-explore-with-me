package ru.practicum.ewm.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import static constant.Constants.PATTERN_FOR_DATETIME;

@Data
public class EndpointHit {

    private Long id; // Идентификатор записи

    private String app; // Идентификатор сервиса для которого записывается информация

    private String uri; // URI для которого был осуществлен запрос

    private String ip; // IP-адрес пользователя, осуществившего запрос

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATETIME)
    private String timestamp; //  Дата и время, когда был совершен запрос к эндпоинту

}