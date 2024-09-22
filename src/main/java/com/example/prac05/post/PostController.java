package com.example.prac05.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/all")
    public ResponseEntity<List<PostModel>> getAllPosts(){
        return new ResponseEntity<>(postService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EntityModel<PostModel>> createPost(@RequestBody PostDTO postDTO){
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<EntityModel<PostModel>> getPostById(@RequestParam(name = "id")Long id){
        return new ResponseEntity<>(postService.findById(id), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<EntityModel<PostModel>> fixPost(@RequestBody PostDTO postDTO){
        return new ResponseEntity<>(postService.fixPost(postDTO), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<EntityModel<Boolean>> deletePostById(@RequestParam(name = "id")Long id){
        return new ResponseEntity<>(postService.deletePostById(id), HttpStatus.OK);
    }
}
