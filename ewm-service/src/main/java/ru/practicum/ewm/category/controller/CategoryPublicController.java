package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryPublicController {

    private final CategoryPublicService publicService;

    @GetMapping

    public ResponseEntity<List<CategoryDto>> getAllCategories(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<CategoryDto> categoryDtos = publicService.getAllCategories(from, size);
        log.info("Получен список категорий from = {}, size = {}.", from, size);
        return ResponseEntity.ok().body(categoryDtos);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long catId) {  // Получение информации о категории
        // по её идентификатору.
        CategoryDto categoryDto = publicService.getCategoryById(catId);
        log.info("Получена категория с id = {}.", catId);
        return ResponseEntity.ok(categoryDto);
    }

}