package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.service.CommentPrivateService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/comments")
public class CommentPublicController {
    private final CommentPrivateService commentPrivateService;

    @GetMapping

    public ResponseEntity<List<CommentDto>> findAllByEventId(@RequestParam Long eventId,
                                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<CommentDto> list = commentPrivateService.findAllByEventId(eventId, from, size);
        return ResponseEntity.ok().body(list);
    }
}
