package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.service.CommentAdminService;

import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static constant.Constants.PATTERN_FOR_DATETIME;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/comments")
public class CommentAdminController {
    private final CommentAdminService commentAdminService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> findAllByAdmin(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                           @Positive @RequestParam(defaultValue = "10") Integer size,
                                                           @RequestParam(required = false) @Past @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime rangeStart,
                                                           @RequestParam(required = false) @PastOrPresent @DateTimeFormat(pattern = PATTERN_FOR_DATETIME) LocalDateTime rangeEnd) {
        List<CommentDto> list = commentAdminService.findAllByAdmin(rangeStart, rangeEnd, from, size);
        return ResponseEntity.ok().body(list);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentByAdmin(@PathVariable Long commentId) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
