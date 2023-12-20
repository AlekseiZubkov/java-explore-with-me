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
@RequiredArgsConstructor
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.INSTANCE.toCategoryFromNewDto(newCategoryDto);
        try {
             category = categoryRepository.save(category);

        } catch (RuntimeException e) {
            throw new SaveException("Категория не была создана: " + newCategoryDto);
        }
        return CategoryMapper.INSTANCE.toCategoryDto(category);
    }

    @Transactional
    @Override
    public Boolean deleteCategoryById(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + catId + " не найден."));

        try {
            return categoryRepository.deleteByIdWithReturnedLines(catId) >= 0;
        } catch (RuntimeException e) {
            throw new ConflictException("Категория с id = " + catId + " не может быть удалена, " +
                    "существуют события, связанные с категорией.");
        }
    }


    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + catId + " не найден."));
        category.setName(categoryDto.getName());

        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException  e) {
            throw new ConflictException("Категория с id = " + catId + " не была обновлена: " + categoryDto);
        }
        return CategoryMapper.INSTANCE.toCategoryDto(category);
    }

}