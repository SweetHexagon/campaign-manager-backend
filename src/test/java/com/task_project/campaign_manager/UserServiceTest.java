package com.task_project.campaign_manager;

import com.task_project.campaign_manager.data.User;
import com.task_project.campaign_manager.dtos.UserDto;
import com.task_project.campaign_manager.exceptions.UsernameAlreadyExistsException;
import com.task_project.campaign_manager.repositories.UserRepository;
import com.task_project.campaign_manager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;



    @Test
    void testRegisterNewUser_Success() {
        UserDto userDto = new UserDto("newUser", "password");
        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        UserDto result = userService.registerNewUser(userDto);

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterNewUser_UsernameAlreadyExists() {
        UserDto userDto = new UserDto("existingUser", "password");
        when(userRepository.findByUsername("existingUser")).thenReturn(new User());

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.registerNewUser(userDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        UserDto userDto = new UserDto("nonexistentUser", "password");
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.authenticateUser(userDto);
        });

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        UserDto userDto = new UserDto("existingUser", "wrongPassword");
        User user = new User("existingUser", "encodedPassword");
        when(userRepository.findByUsername("existingUser")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            userService.authenticateUser(userDto);
        });
    }
}
