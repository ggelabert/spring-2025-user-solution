package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DigitalSessionServiceTest {

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
        when(digitalSessionRepository.findDigitalSessionByUser(longThat(i -> i == 1L))).thenReturn(List.of(DigitalSession.builder().id(1L).build()));
        DigitalSessionService service = new DigitalSessionServiceImpl(digitalSessionRepository, userRepository);
        var sessions = service.findDigitalSessionByUser(1L);
    }
}
