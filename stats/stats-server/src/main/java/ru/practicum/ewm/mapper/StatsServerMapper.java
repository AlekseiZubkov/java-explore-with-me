package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.model.Hit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static constant.Constants.PATTERN_FOR_DATETIME;

@Mapper
public interface StatsServerMapper {

    StatsServerMapper INSTANCE = Mappers.getMapper(StatsServerMapper.class);

    @Mapping(target = "timestamp", source = "hit.timestamp", dateFormat = PATTERN_FOR_DATETIME)
    EndpointHit toEndpointHit(Hit hit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", source = "endpointHit.timestamp", dateFormat = PATTERN_FOR_DATETIME)
    Hit toHit(EndpointHit endpointHit);

}