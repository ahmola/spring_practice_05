package com.example.prac05.post;

import com.example.prac05.comment.Comment;
import com.example.prac05.user.User;
import com.example.prac05.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    private Post mockPost;

    @BeforeEach
    public void setUp(){
        PostDTO postDTO = PostDTO.builder()
                .body("hi everyone")
                .title("hello")
                .build();
        mockPost = new Post(postDTO);
        Mockito.when(postService.createPost(postDTO))
                .thenReturn(EntityModel.of(new PostModel(mockPost)));
    }

    @Test
    void getAllPosts() throws Exception {
        Mockito.when(postService.findAll()).thenReturn(List.of(new PostModel(mockPost)));
        this.mockMvc.perform(get("/api/v1/post/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("hello"))
        ;
    }

    @Test
    void createPost() {
    }

    @Test
    void getPostById() {
    }

    @Test
    void fixPost() {
    }

    @Test
    void deletePostById() {
    }
}