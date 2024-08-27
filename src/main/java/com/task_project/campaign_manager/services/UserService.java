package com.task_project.campaign_manager.services;

import com.task_project.campaign_manager.exceptions.UsernameAlreadyExistsException;
import com.task_project.campaign_manager.data.User;
import com.task_project.campaign_manager.dtos.UserDto;
import com.task_project.campaign_manager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //better to add pepper. ToDo: add pepper
    @Transactional
    public UserDto registerNewUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("A user with this username already exists.");
        }

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = new User(userDto.getUsername(), encodedPassword);
        userRepository.save(user);

        return new UserDto(user.getUsername());
    }

    //Considering authentication via JWT token
    public void authenticateUser(UserDto userDto) {

    }


    public BigDecimal getUserBalance(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user.getBalance();
    }
}
