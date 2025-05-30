package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.DigitalItemStatus;
import edu.uoc.epcsd.user.domain.repository.DigitalItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DigitalItemServiceTest {

    @Mock
    private DigitalItemRepository digitalItemRepository;

    @Test
    void createDigitalItemSetsStatusToAvailable() {
        DigitalItemService service = new DigitalItemServiceImpl(digitalItemRepository);
        edu.uoc.epcsd.user.domain.DigitalItem digitalItem = edu.uoc.epcsd.user.domain.DigitalItem.builder()
                .digitalSessionId(1L)
                .lon(10L)
                .lat(20L)
                .description("description")
                .link("link")
                .build();
        service.addDigitalItem(digitalItem);
        edu.uoc.epcsd.user.domain.DigitalItem expectedDigitalItem = edu.uoc.epcsd.user.domain.DigitalItem.builder()
                .digitalSessionId(1L)
                .lon(10L)
                .lat(20L)
                .description("description")
                .link("link")
                .status(DigitalItemStatus.AVAILABLE)
                .build();
        verify(digitalItemRepository, times(1)).createDigitalItem(expectedDigitalItem);
    }

    @Test
    void createDigitalItemStatusIsNotUnavailable() {
        DigitalItemService service = new DigitalItemServiceImpl(digitalItemRepository);
        edu.uoc.epcsd.user.domain.DigitalItem digitalItem = edu.uoc.epcsd.user.domain.DigitalItem.builder()
                .digitalSessionId(1L)
                .lon(10L)
                .lat(20L)
                .description("description")
                .link("link")
                .build();
        service.addDigitalItem(digitalItem);
        edu.uoc.epcsd.user.domain.DigitalItem expectedDigitalItem = edu.uoc.epcsd.user.domain.DigitalItem.builder()
                .digitalSessionId(1L)
                .lon(10L)
                .lat(20L)
                .description("description")
                .link("link")
                .status(DigitalItemStatus.NOT_AVAILABLE)
                .build();
        verify(digitalItemRepository, times(0)).createDigitalItem(expectedDigitalItem);
    }
}
