package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.repository.DigitalItemRepository;
import edu.uoc.epcsd.user.domain.service.DigitalItemServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DigitalItemRESTControllerUnitTest {

    @Mock
    private DigitalItemRepository digitalItemRepository;

    @Test
    void findDigitalItemBySessionReturnsDigitalItems() {
        var mockedDigitalItems = List.of(
                DigitalItem.builder()
                        .id(1L)
                        .build(),
                DigitalItem.builder()
                        .id(2L)
                        .build(),
                DigitalItem.builder()
                        .id(3L)
                        .build()
        );
        Mockito.when(digitalItemRepository.findDigitalItemBySession(Mockito.longThat(i -> i == 1L))).thenReturn(mockedDigitalItems);
        var digitalItemService = new DigitalItemServiceImpl(digitalItemRepository);
        var controller = new DigitalItemRESTController(digitalItemService);
        var digitalItems = controller.findDigitalItemBySession(1L);
        Assertions.assertNotNull(digitalItems);
        Assertions.assertEquals(mockedDigitalItems.size(), digitalItems.size());
        Assertions.assertEquals(mockedDigitalItems, digitalItems);
    }
}
