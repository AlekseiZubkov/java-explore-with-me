package ru.practicum.ewm.event.mapper;

import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "id", ignore = true)
    Location toLocation(LocationDto locationDto);

    LocationDto toLocationDto(Location location);

}