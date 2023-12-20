package ru.practicum.ewm.exception;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        List<String> stList = Arrays.stream(e.getStackTrace()).map(x -> x.toString()).collect(Collectors.toList());
        final List<ErrorResponse> errorResponses = e.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    String mes = "Field: " + error.getField() + ". Error: " +
                            error.getDefaultMessage() + " Value: " + error.getRejectedValue() + " ";
                    return new ErrorResponse(mes);
                })
                .collect(Collectors.toList());
        StringBuilder stringBuilderErrors = new StringBuilder();
        for (ErrorResponse errorResponse : errorResponses) {
            stringBuilderErrors.append(errorResponse.getError());
        }

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setReason("Incorrectly made request.");
        apiError.setMessage(stringBuilderErrors.toString());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(stList);

        log.warn(apiError.toString());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNotSave(final SaveException e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT);
        apiError.setReason("Ошибка сохранения");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(Arrays.stream(e.getStackTrace()).map(x -> x.toString()).collect(Collectors.toList()));

        log.warn(apiError.toString());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotSave(final NotFoundException e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND);
        apiError.setReason("Требуемый объект не был найден.");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(Arrays.stream(e.getStackTrace()).map(x -> x.toString()).collect(Collectors.toList()));

        log.warn(apiError.toString());
        return apiError;
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNotSave(final ConflictException e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT);
        apiError.setReason("Для запрошенной операции условия не выполнены.");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(Arrays.stream(e.getStackTrace()).map(x -> x.toString()).collect(Collectors.toList()));

        log.warn(apiError.toString());
        return apiError;
    }

    @ExceptionHandler({RuntimeException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotSave(final RuntimeException e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setReason("Неправильно сделан запрос..");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(Arrays.stream(e.getStackTrace()).map(x -> x.toString()).collect(Collectors.toList()));

        log.warn(apiError.toString());
        return apiError;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException( Exception e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setReason("Произошла непредвиденная ошибка.");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(Arrays.stream(e.getStackTrace()).map(x -> x.toString()).collect(Collectors.toList()));

        log.warn(apiError.toString());
        return apiError;
    }

}