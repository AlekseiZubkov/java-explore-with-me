package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;

    private final UserCategoryService userCategoryService;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size) {
        Pageable page = (ids == null) ?
                PageRequest.of(from, size) :
                PageRequest.of(from, size, Sort.by("id"));

        return UserMapper.INSTANCE.convertUserListToUserDTOList(
                (ids == null) ?
                        userRepository.findAll(page).getContent() :
                        userRepository.findAllByIdIn(ids, page));
    }

    @Override
    public UserDto saveUser(NewUserRequest newUserRequest) {
        User user = UserMapper.INSTANCE.toUserFromNewDto(newUserRequest);
        user = userRepository.save(user);
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        userCategoryService.returnUser(userId);
        userRepository.deleteById(userId);
    }

}