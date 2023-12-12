package ru.practicum.ewm.model;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hits", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hits_id")
    private Long id; // Идентификатор записи

    @Column(name = "hits_app", nullable = false)
    private String app; // Идентификатор сервиса для которого записывается информация

    @Column(name = "hits_uri", nullable = false)
    private String uri;  // URI для которого был осуществлен запрос

    @Column(name = "hits_ip", nullable = false)
    private String ip; // IP-адрес пользователя, осуществившего запрос

    @Column(name = "hits_timestamp", nullable = false)
    private LocalDateTime timestamp; //  Дата и время, когда был совершен запрос к эндпоинту

}