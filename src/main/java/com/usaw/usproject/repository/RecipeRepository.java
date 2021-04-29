package com.usaw.usproject.repository;


import com.usaw.usproject.model.Recipe;
import com.usaw.usproject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @RestResource(path = "category")
    Page<Recipe> findByCategoryId(@Param("id") Long id, Pageable pageable);

    Optional<Recipe> findById(Long id);

    @RestResource(path = "search")
    Page<Recipe> findByNameContaining(@Param("name") String keyword, Pageable pageable);

    @RestResource(path = "filter")
    Page<Recipe> findByNameContainingAndDifficulty(@Param("name") String keyword, @Param("difficulty") int difficulty, Pageable pageable);
}
