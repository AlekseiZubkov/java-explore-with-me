package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.service.CommentPrivateService;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/events/{eventId}/comments")
public class CommentPrivateController {
    private final CommentPrivateService commentPrivateService;

    @PostMapping()
    public ResponseEntity<CommentDto> addComment(@PathVariable Long userId, @PathVariable Long eventId,
                                                 @Validated @RequestBody CommentDto commentDto) {
        CommentDto addedComment = commentPrivateService.addComment(userId, eventId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedComment);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long userId, @PathVariable Long commentId,
                                                    @PathVariable Long eventId, @Validated @RequestBody CommentDto commentDto) {
        CommentDto updatedComment = commentPrivateService.updateComment(userId, eventId, commentId, commentDto);
        return ResponseEntity.ok().body(updatedComment);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> findCommentById(@PathVariable Long userId, @PathVariable Long eventId,
                                                      @PathVariable Long commentId) {
        CommentDto foundComment = commentPrivateService.findCommentById(userId, eventId, commentId);
        return ResponseEntity.ok().body(foundComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentByUser(@PathVariable Long userId, @PathVariable Long eventId,
                                                    @PathVariable Long commentId) {
        commentPrivateService.deleteCommentByUser(userId, eventId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
