package ru.practicum.ewm.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toCategoryDto(Category category);

    Category toCategoryFromNewDto(NewCategoryDto newCategoryDto);

    List<CategoryDto> convertCategoryListToCategoryDTOList(List<Category> list);

}