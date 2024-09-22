package com.example.prac05.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private List<Long> postsId = new ArrayList<>();

    private List<Long> commentsIds = new ArrayList<>();

}
