package ru.practicum.ewm.exception;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class ValidationErrorResponse {

    private final List<ApiError> errorResponses;
}