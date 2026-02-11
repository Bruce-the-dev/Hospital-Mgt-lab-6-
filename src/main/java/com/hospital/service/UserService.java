package com.hospital.service;


import com.hospital.exceptions.ResourceNotFoundException;
import com.hospital.model.DTO.UserInput;
import com.hospital.model.DTO.UserResponse;
import com.hospital.model.User;
import com.hospital.model.mapper.UserMapper;
import com.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserInput input) {

        if (userRepository.existsByUsername(input.getUsername())) {
            throw new IllegalArgumentException(
                    "Username already exists: " + input.getUsername()
            );
        }

        User user = UserMapper.toEntity(input);

        // Hashing password BEFORE saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userRepository.save(user);
        return UserMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id)
                );
        return UserMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with username " + username)
                );
        return UserMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(int page,
                                          int size,
                                          String sortBy,
                                          String direction) {
        Pageable pageable = PageRequest.of(page, size, direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        return userRepository.findAll(pageable)
                .map(UserMapper::toResponse);

    }

    public UserResponse updateUser(Long id, UserInput input) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id)
                );

        // Prevent username collision
        if (input.getUsername() != null &&
                !input.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(input.getUsername())) {

            throw new IllegalArgumentException(
                    "Username already exists: " + input.getUsername()
            );
        }

        UserMapper.updateEntity(input, user);

        // Hash only if password was updated
        if (input.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return UserMapper.toResponse(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id)
                );
        userRepository.delete(user);
    }

    public void enableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id)
                );
        user.setStatus(true);
    }

    public void disableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id)
                );
        user.setStatus(false);
    }

    // Authenticate user (Login)
    public String authenticateUser(String username, String password) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return "User successfully authenticated";
        } // User authenticated
        return "Wrong password";
    }


}