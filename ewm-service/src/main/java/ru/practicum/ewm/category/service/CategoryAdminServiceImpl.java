package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.SaveException;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryRepository categoryRepository;
    @Transactional
    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        try {
            Category category = categoryRepository.save(
                    CategoryMapper.INSTANCE.toCategoryFromNewDto(newCategoryDto));
            return CategoryMapper.INSTANCE.toCategoryDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new SaveException("Категория не была создана: " + newCategoryDto);
        }
    }
    @Transactional
    @Override
    public Boolean deleteCategoryById(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + catId + " не найден."));

        try {
            return categoryRepository.deleteByIdWithReturnedLines(catId) >= 0;
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Категория с id = " + catId + " не может быть удалена, " +
                    "существуют события, связанные с категорией.");
        }
    }
    @Transactional
    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + catId + " не найден."));
        category.setName(categoryDto.getName());

        try {
            return CategoryMapper.INSTANCE.toCategoryDto(categoryRepository.saveAndFlush(category));
        } catch (DataIntegrityViolationException e) {
            throw new SaveException("Категория с id = " + catId + " не была обновлена: " + categoryDto);
        }
    }

}