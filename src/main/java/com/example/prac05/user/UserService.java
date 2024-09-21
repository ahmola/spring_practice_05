package com.example.prac05.user;

import com.example.prac05.comment.Comment;
import com.example.prac05.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

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
        if(!userRepository.existsById(id))
            throw new RuntimeException("There is no such user");
        User user = userRepository.findById(id).get();
        EntityModel<UserModel> resource = EntityModel.of(toModel(user));
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(id)).withSelfRel());
        return resource;
    }
}
