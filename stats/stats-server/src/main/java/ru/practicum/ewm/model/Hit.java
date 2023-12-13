package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // Идентификатор записи

    @Column(name = "app", nullable = false)
    private String app; // Идентификатор сервиса для которого записывается информация

    @Column(name = "uri", nullable = false)
    private String uri;  // URI для которого был осуществлен запрос

    @Column(name = "ip", nullable = false)
    private String ip; // IP-адрес пользователя, осуществившего запрос

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp; //  Дата и время, когда был совершен запрос к эндпоинту

}