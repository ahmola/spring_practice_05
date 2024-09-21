package com.example.prac05.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@DataJpaTest
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Create User")
    @Test
    public void testSaving(){
        // given
        User user = User.builder()
                .email("john@rdrmail.com")
                .username("john")
                .build();

        // when & then
        assertEquals(user, userRepository.save(user));
    }

    @DisplayName("Find User by Name")
    @Test
    public void testFindByUser(){
        User user1 = User.builder()
                .username("john")
                .email("john@rdrmail.com")
                .build();

        User user2= User.builder()
                .username("dutch")
                .email("dutch@rdrmail.com")
                .build();

        User user3 = User.builder()
                .username("dutch")
                .email("vanderlinde@rdrmail.com")
                .build();

        userRepository.saveAll(List.of(user1,user2,user3));

        assertEquals(List.of(user2, user3), userRepository.findByUsername(user2.getUsername()));
    }

    @DisplayName("Find User by Email")
    @Test
    public void testFindUserByEmail(){
        User user2= User.builder()
                .username("dutch")
                .email("dutch@rdrmail.com")
                .build();

        User user3 = User.builder()
                .username("dutch")
                .email("vanderlinde@rdrmail.com")
                .build();

        userRepository.saveAll(List.of(user2, user3));

        assertEquals(user2, userRepository.findByEmail("dutch@rdrmail.com").get());
        assertEquals(user3, userRepository.findByEmail("vanderlinde@rdrmail.com").get());
    }

}