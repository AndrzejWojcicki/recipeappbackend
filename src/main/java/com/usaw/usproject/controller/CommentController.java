package com.usaw.usproject.controller;

import com.usaw.usproject.model.Comment;
import com.usaw.usproject.model.RecipeSteps;
import com.usaw.usproject.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @CrossOrigin(origins = {"https://recipe-app-us.herokuapp.com","http://localhost:4200"})
    @GetMapping("/comments/{id}")
    public Comment getComments(@Valid @PathVariable("id") Long id) {
        try {
            Optional<Comment> comment = commentRepository.findById(id);
            Comment _comment = new Comment();
            if(comment.isPresent()) {
                _comment.setAuthor(comment.get().getAuthor());
                _comment.setComment_id(comment.get().getComment_id());
                _comment.setDateCreated(comment.get().getDateCreated());
                _comment.setMessage(comment.get().getMessage());
            }
            return (_comment);
        } catch (Exception e) {
            return (null);
        }
    }

    @CrossOrigin(origins = {"https://recipe-app-us.herokuapp.com","http://localhost:4200"})
    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) {
        System.out.println(comment);
        try {
            Comment temp = new Comment( comment.getAuthor(), comment.getRecipe(),
                    comment.getMessage() );
            temp.setDateCreated(new Date());
            Comment _comment = commentRepository.save(temp);

            return new ResponseEntity<>(_comment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = {"https://recipe-app-us.herokuapp.com","http://localhost:4200"})
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") long id) {
        try {
            commentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
