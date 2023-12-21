package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestPrivateService {

    List<ParticipationRequestDto> getAllRequestsByUser(Long userId);

    ParticipationRequestDto saveRequest(Long userId, Long eventId);

    ParticipationRequestDto updateRequest(Long userId, Long requestId);

}