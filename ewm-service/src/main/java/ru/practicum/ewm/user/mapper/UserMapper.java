package ru.practicum.ewm.user.mapper;

import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);

    User toUserFromNewDto(NewUserRequest newUserRequest);

    List<UserDto> convertUserListToUserDTOList(List<User> list);

}