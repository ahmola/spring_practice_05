package com.example.prac05.post;

import com.example.prac05.comment.CommentRepository;
import com.example.prac05.exception.PostNotFoundException;
import com.example.prac05.exception.UserNotFoundException;
import com.example.prac05.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    public List<PostModel> findAll() {
        return postRepository.findAll().stream()
                .map(PostModel::new).toList();
    }

    public EntityModel<PostModel> createPost(PostDTO postDTO) {
        Post post = new Post(postDTO);
        post.setUser(userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("There is no such User : " + postDTO.getUserId())));
        post.setComments(new ArrayList<>()); // there is no way comment was created before post!

        postRepository.save(post);

        return EntityModel.of(new PostModel(post));
    }

    public EntityModel<PostModel> findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new PostNotFoundException("There is no such Post : " + id));
        return EntityModel.of(new PostModel(post));
    }

    public EntityModel<PostModel> fixPost(PostDTO postDTO) {
        Post post = postRepository.findById(postDTO.getId())
                .orElseThrow(() -> new PostNotFoundException("There is no such post : " + postDTO.getId()));
        post.setBody(postDTO.getBody());
        post.setTitle(postDTO.getTitle());
        post.setUser(userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new PostNotFoundException("There is no such User : " + postDTO.getUserId())));
        post.setComments(postDTO.getCommentsId().stream()
                .map(commentRepository::findById)
                .map(Optional::get)
                .toList());

        try {
            postRepository.save(post);
        }catch (Exception e){
            throw new RuntimeException("Something goes wrong during Fixing Post : " + e.getMessage());
        }

        return EntityModel.of(new PostModel(post));
    }

    public EntityModel<Boolean> deletePostById(Long id) {
        try {
            postRepository.findById(id);
        }catch (Exception e){
            throw new RuntimeException("Something goes wrong during Deleting Post : " + e.getMessage());
        }

        return EntityModel.of(true);
    }
}
