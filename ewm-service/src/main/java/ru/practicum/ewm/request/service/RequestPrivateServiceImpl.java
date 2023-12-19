package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.StateEvent;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.StateRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestPrivateServiceImpl implements RequestPrivateService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getAllRequestsByUser(Long userId) {
        returnUser(userId);
        return ParticipationRequestMapper.INSTANCE.convertParticipationRequestToDtoList(
                requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public ParticipationRequestDto saveRequest(Long userId, Long eventId) {
        User requester = returnUser(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с ID " + eventId + " не найдено."));

        if (requestRepository.countByRequesterIdAndEventId(userId, eventId) != 0) {
            throw new ConflictException(String.format("Нельзя добавить повторный запрос, " +
                    "userId = %s, eventId = %s.", userId, eventId));
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException(String.format("Инициатор события не может добавить запрос на участие " +
                    "в своём событии, userId = %s, eventId = %s.", userId, eventId));
        }
        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictException(String.format("Нельзя участвовать в неопубликованном событии, " +
                    "userId = %s, eventId = %s.", userId, eventId));
        }
        if (event.getParticipantLimit() > 0) {
            if (event.getParticipantLimit() <= requestRepository.countByEventIdAndStatus(eventId, StateRequest.CONFIRMED)) {
                throw new ConflictException(String.format("У события достигнут лимит запросов на участие, " +
                        "userId = %s, eventId = %s.", userId, eventId));
            }
        }

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(requester);
        participationRequest.setEvent(event);
        participationRequest.setCreated(LocalDateTime.now());

        if (event.getRequestModeration() && !event.getParticipantLimit().equals(0)) {
            participationRequest.setStatus(StateRequest.PENDING);
        } else {
            participationRequest.setStatus(StateRequest.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.saveAndFlush(event);
        }

        return ParticipationRequestMapper.INSTANCE.toParticipationRequestDto(
                requestRepository.save(participationRequest));
    }


    @Override
    public ParticipationRequestDto updateRequest(Long userId, Long requestId) {
        returnUser(userId);
        ParticipationRequest participationRequest = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос на участие с ID " + requestId + " не найден."));

        if (!participationRequest.getRequester().getId().equals(userId)) {
            throw new NotFoundException(String.format("Можно отменить только свой запрос на участие, " +
                    "userId = %s, requestId = %s.", userId, requestId));
        }
        participationRequest.setStatus(StateRequest.CANCELED);

        return ParticipationRequestMapper.INSTANCE.toParticipationRequestDto(
                requestRepository.saveAndFlush(participationRequest));
    }

    private User returnUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден."));
    }

}