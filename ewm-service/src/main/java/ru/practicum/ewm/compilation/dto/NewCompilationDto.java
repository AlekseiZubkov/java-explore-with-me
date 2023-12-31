package ru.practicum.ewm.compilation.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class NewCompilationDto {
    //Подборка событий

    private Set<Long> events;

    private Boolean pinned = false; // Закреп

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

}