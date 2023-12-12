package ru.practicum.ewm.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class ViewStats {

    private String app; // Название сервиса

    private String uri; // URI сервиса

    private Long hits; //  Количество просмотров

}