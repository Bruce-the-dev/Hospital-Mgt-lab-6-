package com.hospital.graphql.controllers;

import com.hospital.model.DTO.UserInput;
import com.hospital.model.DTO.UserLogin;
import com.hospital.model.DTO.UserResponse;
import com.hospital.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserGraphQLController {

    private final UserService userService;

    @QueryMapping
    public String login(
          @Valid  @Argument UserLogin user
    ) {
        System.out.print(user.getUserName());
        return userService.authenticateUser(user.getUserName(), user.getPassword());
    }


    @QueryMapping
    public UserResponse userById(@Argument Long id) {

        return userService.getUserById(id);
    }

    @QueryMapping
    public UserResponse userByUsername(@Argument String username) {
        return userService.getUserByUsername(username);
    }

    @QueryMapping
    public Page<UserResponse> users(
            @Argument Integer page,
            @Argument Integer size,
            @Argument String sortBy,
            @Argument String direction) {

        return userService.getAllUsers(page, size, sortBy, direction);
    }

    @MutationMapping
    public UserResponse createUser(@Valid @Argument UserInput input) {

        return userService.createUser(input);
    }

    @MutationMapping
    public UserResponse updateUser(
            @Argument Long id,
           @Valid @Argument UserInput input) {

        return userService.updateUser(id, input);
    }

    @MutationMapping
    public Boolean enableUser(@Argument Long id) {
        userService.enableUser(id);
        return true;
    }

    @MutationMapping
    public Boolean disableUser(@Argument Long id) {
        userService.disableUser(id);
        return true;
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userService.deleteUser(id);
        return true;
    }
}
