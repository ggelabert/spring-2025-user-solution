package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.exception.UserNotFoundException;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DigitalSessionServiceUnitTest {

    @Mock
    private DigitalSessionRepository digitalSessionRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void findDigitalSessionByUserIdReturnsDigitalSessions() {
        List<DigitalSession> digitalSessions = List.of(
                DigitalSession.builder()
                        .id(1L)
                        .build(),
                DigitalSession.builder()
                        .id(2L)
                        .build(),
                DigitalSession.builder()
                        .id(3L)
                        .build()
        );
        when(userRepository.findUserById(longThat(i -> i == 1L))).thenReturn(Optional.of(User.builder().build()));
        when(digitalSessionRepository.findDigitalSessionByUser(longThat(i -> i == 1L))).thenReturn(digitalSessions);
        DigitalSessionService service = new DigitalSessionServiceImpl(digitalSessionRepository, userRepository);
        var sessions = service.findDigitalSessionByUser(1L);
        assertEquals(3, sessions.size());
    }

    @Test
    void findDigitalSessionByUserIdReturnsExceptionIfUserNotFound() {
        when(userRepository.findUserById(longThat(i -> i == 1L))).thenReturn(Optional.empty());
        DigitalSessionService service = new DigitalSessionServiceImpl(digitalSessionRepository, userRepository);
        var exception = assertThrows(UserNotFoundException.class, () -> service.findDigitalSessionByUser(1L));
        assertEquals("User with id '1' not found", exception.getMessage());
    }
}
