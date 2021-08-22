package com.liftoff.recipeapp.data;

import com.liftoff.recipeapp.models.RecipeCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeCategoryRepository extends CrudRepository<RecipeCategory, Integer> {
}
