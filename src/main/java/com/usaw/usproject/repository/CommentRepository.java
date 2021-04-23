package com.usaw.usproject.repository;

import com.usaw.usproject.model.Comment;
import com.usaw.usproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;


@RepositoryRestResource(collectionResourceRel = "commentsOfRecipe", path = "recipe-comments")
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);
}
