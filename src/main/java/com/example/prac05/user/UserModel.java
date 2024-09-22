package com.example.prac05.user;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
@Builder
public class UserModel extends RepresentationModel<UserModel> {
    private Long id;

    private String username;

    private String email;

    private List<Long> postsId = new ArrayList<>();

    private List<Long> commentsIds = new ArrayList<>();

    public UserModel(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public UserModel(UserDTO userDTO){
        this.id = userDTO.getId();
        this.username = userDTO.getUsername();
        this.email = userDTO.getEmail();
        this.commentsIds.addAll(userDTO.getCommentsIds());
        this.postsId.addAll(userDTO.getPostsId());
    }
}
