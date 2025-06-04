package edu.uoc.epcsd.user.application.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.repository.DigitalItemRepository;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class DigitalItemRESTControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DigitalItemRepository digitalItemRepository;

    @Autowired
    private DigitalSessionRepository digitalSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByDigitalItemsBySessionIdFromControllerReturnsItems() throws Exception {
        var user = User.builder()
                .email("test@test.com")
                .fullName("Test User")
                .password("password")
                .phoneNumber("+34123456789")
                .build();
        var userId = userRepository.createUser(user);
        Assertions.assertNotNull(userId, "Error during test setup (not part of test), could not create user");
        var digitalSession = DigitalSession.builder()
                .description("description")
                .link("link")
                .location("location")
                .userId(userId)
                .build();
        var digitalSessionId = digitalSessionRepository.createDigitalSession(digitalSession);
        Assertions.assertNotNull(digitalSessionId, "Error during test setup (not part of test) could not create digital session");

        DigitalItem digitalItem1 = DigitalItem.builder()
                .digitalSessionId(digitalSessionId)
                .lat(1L)
                .lon(1L)
                .description("description1")
                .link("link")
                .build();
        DigitalItem digitalItem2 = DigitalItem.builder()
                .digitalSessionId(digitalSessionId)
                .lat(1L)
                .lon(1L)
                .description("description2")
                .link("link")
                .build();
        DigitalItem digitalItem3 = DigitalItem.builder()
                .digitalSessionId(digitalSessionId)
                .lat(1L)
                .lon(1L)
                .description("description3")
                .link("link")
                .build();

        digitalItemRepository.createDigitalItem(digitalItem1);
        digitalItemRepository.createDigitalItem(digitalItem2);
        digitalItemRepository.createDigitalItem(digitalItem3);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/digitalItem/digitalItemBySession")
                .queryParam("digitalSessionId", digitalSessionId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        List<DigitalItem> digitalItems = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        Assertions.assertNotNull(digitalItems);
        Assertions.assertEquals(3, digitalItems.size());
        Assertions.assertTrue(digitalItems.stream().anyMatch(digitalItem -> digitalItem.getDescription().equals(digitalItem1.getDescription())));
        Assertions.assertTrue(digitalItems.stream().anyMatch(digitalItem -> digitalItem.getDescription().equals(digitalItem2.getDescription())));
        Assertions.assertTrue(digitalItems.stream().anyMatch(digitalItem -> digitalItem.getDescription().equals(digitalItem3.getDescription())));

    }

}
