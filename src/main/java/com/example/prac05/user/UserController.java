package com.example.prac05.user;

import com.example.prac05.comment.Comment;
import com.example.prac05.post.Post;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserModel>> getAllUsers(){
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users.stream()
                .map(UserService::toModel)
                .toList(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserModel>> creatUser(@RequestBody UserDTO userDTO){
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<EntityModel<UserModel>> getUser(@RequestParam(name = "id") Long id){
        return new ResponseEntity<>(userService.findUser(id), HttpStatus.OK);
    }
}
