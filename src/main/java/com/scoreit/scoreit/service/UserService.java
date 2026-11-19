package com.scoreit.scoreit.service;

import com.scoreit.scoreit.dto.CreateUserRequest;
import com.scoreit.scoreit.dto.UserResponse;
import com.scoreit.scoreit.model.User;
import com.scoreit.scoreit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(CreateUserRequest request){
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }
        if (userRepository.existsByEmail(email)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already taken");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }

    public List<UserResponse> getUsers(){
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }


}
