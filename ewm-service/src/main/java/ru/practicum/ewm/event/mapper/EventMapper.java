package ru.practicum.ewm.event.mapper;

import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static constant.Constants.PATTERN_FOR_DATETIME;

@Mapper(uses = {UserMapper.class, CategoryMapper.class, LocationMapper.class})
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "initiator", source = "user")
    Event toEventFromNewDto(NewEventDto newEventDto, User user, Category category, Location location);

    @Mapping(target = "createdOn", source = "event.createdOn", dateFormat = PATTERN_FOR_DATETIME)
    @Mapping(target = "eventDate", source = "event.eventDate", dateFormat = PATTERN_FOR_DATETIME)
    @Mapping(target = "publishedOn", source = "event.publishedOn", dateFormat = PATTERN_FOR_DATETIME)
    EventFullDto toEventFullDto(Event event);

    @Mapping(target = "eventDate", source = "event.eventDate", dateFormat = PATTERN_FOR_DATETIME)
    EventShortDto toEventShortDto(Event event);

    List<EventFullDto> convertEventListToEventFullDtoList(List<Event> list);

    List<EventShortDto> convertEventListToEventShortDtoList(List<Event> list);

}