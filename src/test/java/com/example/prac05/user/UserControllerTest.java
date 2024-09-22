package com.example.prac05.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User mockUser;

    @BeforeEach
    public void setUp(){
        mockUser = User.builder()
                .email("john@rdrmail.com")
                .username("john")
                .build();

        Mockito.when(userService.findUser(1L)).thenReturn(EntityModel.of(new UserModel(mockUser)));
    }

    @Test
    void createUserTest() throws Exception{

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("dutch");
        userDTO.setEmail("dutch@rdrmail.com");

        Mockito.when(userService.createUser(userDTO))
                .thenReturn(EntityModel.of(new UserModel(userDTO)));

        this.mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("dutch"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("dutch@rdrmail.com"))
                ;
    }

    @DisplayName("Find All User")
    @Test
    void getAllUsersTest() throws Exception {
        this.mockMvc.perform(get("/api/v1/user/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("john"))
                ;
    }

    @DisplayName("Find User by Id")
    @Test
    void getUserByIdTest() throws Exception{
        this.mockMvc.perform(get("/api/v1/user")
                .param("id", String.valueOf(1L))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("john"));
    }

    @DisplayName("Fix User")
    @Test
    void fixUserTest() throws Exception{
        UserDTO userDTO = UserDTO.builder()
                .id(mockUser.getId())
                .username("jack")
                .email("jack@rdrmail.com")
                .postsId(new ArrayList<>())
                .commentsIds(new ArrayList<>())
                .build();

        Mockito.when(userService.fixUser(userDTO)).thenReturn(EntityModel.of(new UserModel(userDTO)));

        this.mockMvc.perform(put("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("jack"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jack@rdrmail.com"));
    }

    @DisplayName("Delete user")
    @Test
    void deleteUserByIdTest() throws Exception{
        Mockito.when(userService.deleteUser(mockUser.getId())).thenReturn(EntityModel.of(true));

        this.mockMvc.perform(delete("/api/v1/user")
                .param("id", String.valueOf(1L)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}