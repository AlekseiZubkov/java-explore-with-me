package ru.practicum.ewm.event.model;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "locations", schema = "public")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lat", nullable = false)
    private Float lat;
    @Column(name = "lon", nullable = false)
    private Float lon;

}