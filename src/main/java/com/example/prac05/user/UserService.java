package com.example.prac05.user;

import com.example.prac05.comment.Comment;
import com.example.prac05.comment.CommentRepository;
import com.example.prac05.exception.UserNotFoundException;
import com.example.prac05.post.Post;
import com.example.prac05.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public static UserModel toModel(User user){
        UserModel userModel = UserModel.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
        if(user.getPosts() == null){
            userModel.setPostsId(null);
            userModel.setCommentsIds(null);
        }
        else{
            userModel.setPostsId(user.getPosts().stream().map(Post::getId).collect(Collectors.toList()));
            userModel.setCommentsIds(user.getComments().stream().map(Comment::getId).collect(Collectors.toList()));
        }
        return userModel;
    }

    public EntityModel<UserModel> createUser(UserDTO userDTO) {
        User user = User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .build();

        userRepository.save(user);

        return EntityModel.of(toModel(user));
    }

    public EntityModel<UserModel> findUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("There is no such User with id : " + id));
        EntityModel<UserModel> resource = EntityModel.of(toModel(user));
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(id)).withSelfRel());
        return resource;
    }

    public EntityModel<UserModel> fixUser(UserDTO userDTO) {

        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new UserNotFoundException("There is no such user"));
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());

        List<Post> newPosts = userDTO.getPostsId().stream()
                        .map(postRepository::findById)
                        .map(Optional::get)
                        .toList();
        user.setPosts(newPosts);

        List<Comment> newComments = userDTO.getCommentsIds().stream()
                .map(commentRepository::findById)
                .map(Optional::get)
                .toList();
        user.setComments(newComments);

        userRepository.save(user);

        return EntityModel.of(UserModel.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .commentsIds(user.getComments().stream().map(Comment::getId).toList())
                .postsId(user.getPosts().stream().map(Post::getId).toList())
                .build()
        );
    }

    public EntityModel<Boolean> deleteUser(Long id) throws Exception {
        try {
            userRepository.deleteById(id);
            return EntityModel.of(true);
        }catch (Exception e){
            throw new Exception("Something goes wrong during deleting");
        }
    }
}
