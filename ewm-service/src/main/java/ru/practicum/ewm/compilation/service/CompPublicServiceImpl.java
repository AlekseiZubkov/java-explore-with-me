package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompPublicServiceImpl implements CompPublicService {

    private final CompilationRepository compilationRepository;


    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));

        if (pinned == null) {
            return CompilationMapper.INSTANCE.convertCompilationListToCompilationDTOList(
                    compilationRepository.findAll(page).getContent());
        } else {
            return CompilationMapper.INSTANCE.convertCompilationListToCompilationDTOList(
                    compilationRepository.findAllByPinned(pinned, page));
        }
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return CompilationMapper.INSTANCE.toCompilationDto(returnCompilation(compId));
    }
    private Compilation returnCompilation(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка событий с ID " + compId + " не найдена."));
    }

}