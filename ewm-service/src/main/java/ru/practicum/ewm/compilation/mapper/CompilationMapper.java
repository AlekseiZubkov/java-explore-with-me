package ru.practicum.ewm.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.Set;

@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {

    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    @Mapping(target = "events", source = "events")
    Compilation toCompilationFromNewDto(NewCompilationDto newCompilationDto, Set<Event> events);

    CompilationDto toCompilationDto(Compilation compilation);

    List<CompilationDto> convertCompilationListToCompilationDTOList(List<Compilation> list);

}