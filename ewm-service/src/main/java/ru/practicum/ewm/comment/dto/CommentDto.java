package ru.practicum.ewm.comment.dto;

import lombok.*;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;

    @NotBlank
    @Size(max = 1024)
    private String text;

    private UserShortDto author;

    private Long eventId;

    private LocalDateTime created;

    private Boolean edited;
}
