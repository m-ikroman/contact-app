package project_skripsi.rest.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project_skripsi.rest.entity.Contact;
import project_skripsi.rest.entity.User;
import project_skripsi.rest.model.AddressResponse;
import project_skripsi.rest.model.CreateAddressRequest;
import project_skripsi.rest.model.WebResponse;
import project_skripsi.rest.repository.ContactRepository;
import project_skripsi.rest.repository.UserRepository;
import project_skripsi.rest.security.BCrypt;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    private Contact contact;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("ikroman");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("Ikroman");
        user.setToken("ini-token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
        userRepository.save(user);

        contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Muhamad");
        contact.setLastName("Ikroman");
        contact.setEmail("ikroman@gmail.com");
        contact.setPhone("081818188181");
        contact.setUser(user);
        contactRepository.save(contact);

    }

    @Test
    void testCreateAddressSuccess() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Siliwangi");
        request.setCity("Tasikmalaya");
        request.setProvince("West Java");
        request.setCountry("Indonesia");
        request.setPostalCode("45467");

        mockMvc.perform(
                post("/api/contacts/"+contact.getId()+"/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {

            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertEquals("Siliwangi", response.getData().getStreet());
            Assertions.assertEquals("Tasikmalaya", response.getData().getCity());
            Assertions.assertEquals("West Java", response.getData().getProvince());
            Assertions.assertEquals("Indonesia", response.getData().getCountry());
            Assertions.assertEquals("45467", response.getData().getPostalCode());

        });
    }
}
