package ru.practicum.ewm.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import static constant.Constants.PATTERN_FOR_DATETIME;

@Getter
@Setter
@ToString
public class ApiError {

    private HttpStatus status;

    private String reason;

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATETIME)
    private LocalDateTime timestamp;

    private List<String> errors;

}