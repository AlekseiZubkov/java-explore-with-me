package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.SaveException;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompAdminServiceImpl implements CompAdminService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events.addAll(eventRepository.findAllById(newCompilationDto.getEvents()));
        }

        Compilation compilation = compilationRepository.save(
                CompilationMapper.INSTANCE.toCompilationFromNewDto(newCompilationDto, events));

        return CompilationMapper.INSTANCE.toCompilationDto(compilation);
    }

    @Transactional
    @Override
    public void deleteCompilationById(Long compId) {
        returnCompilation(compId);
        try {
            compilationRepository.deleteById(compId);
        } catch (RuntimeException e) {
            throw new ConflictException("Подборка с id = " + compId + " не может быть удалена.");
        }
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = returnCompilation(compId);

        if (updateCompilationRequest.getEvents() != null) {
            Set<Event> events = new HashSet<>(eventRepository.findAllById(updateCompilationRequest.getEvents()));
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        try {
            compilation = compilationRepository.save(compilation);
        } catch (RuntimeException e) {
            throw new SaveException("Подборка событий с id = " + compId +
                    " не была обновлена: " + updateCompilationRequest);
        }
        return CompilationMapper.INSTANCE.toCompilationDto(compilation);
    }

    private Compilation returnCompilation(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка событий с ID " + compId + " не найдена."));
    }
}