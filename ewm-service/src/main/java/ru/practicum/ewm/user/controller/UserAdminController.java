package ru.practicum.ewm.user.controller;

import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserAdminService;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {

    private final UserAdminService adminService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<UserDto> userDtos = adminService.getAllUsers(ids, from, size);
        log.info("Получен список пользователей, ids = {}, from = {}, size = {}.", ids, from, size);
        return ResponseEntity.ok().body(userDtos);
    }

    @PostMapping
    @Validated
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        UserDto userDto = adminService.saveUser(newUserRequest);
        log.info("Добавлен новый пользователь: {}", userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        adminService.deleteUserById(userId);
        log.info("Удалён пользователь с id = {}", userId);
    }

}