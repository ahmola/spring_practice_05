package com.example.prac05.user;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
@Builder
public class UserModel extends RepresentationModel<UserModel> {
    private Long id;

    private String username;

    private String email;

    private List<Long> postsId;

    private List<Long> commentsIds;
}
