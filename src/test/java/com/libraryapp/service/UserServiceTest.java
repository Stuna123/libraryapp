package com.libraryapp.service;

import com.libraryapp.entity.AppUser;
import com.libraryapp.entity.Role;
import com.libraryapp.form.RegisterForm;
import com.libraryapp.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserWhenEmailDoesNotExist() {
        // Arrange
        RegisterForm form = new RegisterForm();
        form.setFirstName("Francis");
        form.setLastName("Tabora");
        form.setEmail("francis@test.com");
        form.setPassword("password123");

        Mockito.doReturn(false)
                .when(appUserRepository)
                .existsByEmail("francis@test.com");

        Mockito.doReturn("encoded-password")
                .when(passwordEncoder)
                .encode("password123");
        // Act
        userService.registerUser(form);

        // Assert
        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);

        verify(appUserRepository).save(userCaptor.capture());

        AppUser savedUser = userCaptor.getValue();

        assertThat(savedUser.getFirstName()).isEqualTo("Francis");
        assertThat(savedUser.getLastName()).isEqualTo("Tabora");
        assertThat(savedUser.getEmail()).isEqualTo("francis@test.com");
        assertThat(savedUser.getPassword()).isEqualTo("encoded-password");
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);

        verify(passwordEncoder).encode("password123");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        RegisterForm form = new RegisterForm();
        form.setFirstName("Francis");
        form.setLastName("Tabora");
        form.setEmail("francis@test.com");
        form.setPassword("password123");

        Mockito.doReturn(true)
                .when(appUserRepository)
                .existsByEmail("francis@test.com");

        // Act & Assert
        assertThatThrownBy(() -> userService.registerUser(form))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Un compte existe déjà avec cet email.");

        verify(appUserRepository, never()).save(any(AppUser.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
