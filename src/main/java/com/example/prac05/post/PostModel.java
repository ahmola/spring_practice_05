package com.example.prac05.post;

import com.example.prac05.comment.Comment;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
@Builder
public class PostModel extends RepresentationModel<PostModel> {
    private Long id;

    private String title;

    private String body;

    private List<Long> commentsId = new ArrayList<>();

    private Long userId;

    public PostModel(Post post){
        this.title = post.getTitle();
        this.body = post.getBody();
        this.commentsId = post.getComments().stream().map(Comment::getId).collect(Collectors.toList());
        this.userId = post.getId();
    }
}
