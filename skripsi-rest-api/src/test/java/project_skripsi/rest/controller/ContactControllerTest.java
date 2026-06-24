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
import project_skripsi.rest.model.ContactResponse;
import project_skripsi.rest.model.CreateContactRequest;
import project_skripsi.rest.model.UpdateContactRequest;
import project_skripsi.rest.model.WebResponse;
import project_skripsi.rest.repository.ContactRepository;
import project_skripsi.rest.repository.UserRepository;
import project_skripsi.rest.security.BCrypt;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    }

    @Test
    void testCreateBadRequest() throws Exception {

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstname("");
        request.setEmail("");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {

            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            Assertions.assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateSuccess() throws Exception {

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstname("Muhamad");
        request.setLastname("Ikroman");
        request.setEmail("ikroman9a@gmail.com");
        request.setPhone("08181818188");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {

            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
            });

            assertNull(response.getErrors());
            assertEquals("Muhamad", response.getData().getFirstname());
            assertEquals("Ikroman", response.getData().getLastname());
        });
    }

    @Test
    void testGetNotFound() throws Exception {

        mockMvc.perform(
                get("/api/contacts/1232323")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {

            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            Assertions.assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetSuccess() throws Exception {
        User user = userRepository.findById("ikroman").orElse(null);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Muhamad");
        contact.setLastName("Ikroman");
        contact.setEmail("ikroman@gmail.com");
        contact.setPhone("02020202020");
        contact.setUser(user);

        contactRepository.save(contact);

        mockMvc.perform(
                get("/api/contacts/"+contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {

            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
            });

            assertNull(response.getErrors());
            assertEquals("Muhamad", response.getData().getFirstname());
        });
    }

    @Test
    void testUpdateBadRequest() throws Exception {

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstname("");
        request.setEmail("");

        mockMvc.perform(
                put("/api/contacts/12212121")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {

            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            Assertions.assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateSuccess() throws Exception {
        User user = userRepository.findById("ikroman").orElse(null);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Muhamad");
        contact.setLastName("Ikroman");
        contact.setEmail("ikroman@gmail.com");
        contact.setPhone("02020202020");
        contact.setUser(user);

        contactRepository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstname("Software");
        request.setLastname("Engineer");
        request.setEmail("ikroman9a@gmail.com");
        request.setPhone("08181818188");

        mockMvc.perform(
                put("/api/contacts/"+ contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {

            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
            });

            assertNull(response.getErrors());
            assertEquals("Software", response.getData().getFirstname());
            assertEquals("Engineer", response.getData().getLastname());
        });
    }

    @Test
    void testDeleteNotFound() throws Exception {

        mockMvc.perform(
                delete("/api/contacts/1232323")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {

            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            Assertions.assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteSuccess() throws Exception {
        User user = userRepository.findById("ikroman").orElse(null);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Muhamad");
        contact.setLastName("Ikroman");
        contact.setEmail("ikroman@gmail.com");
        contact.setPhone("02020202020");
        contact.setUser(user);

        contactRepository.save(contact);

        mockMvc.perform(
                delete("/api/contacts/"+ contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {

            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNull(response.getErrors());
            assertEquals("OK", response.getData());
        });
    }

    @Test
    void searchNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(0, response.getData().size());
            assertEquals(0, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getSize());
        });
    }

    @Test
    void searchSuccess() throws Exception {
        User user = userRepository.findById("ikroman").orElseThrow();

        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setId(UUID.randomUUID().toString());
            contact.setUser(user);
            contact.setFirstName("Eko " + i);
            contact.setLastName("Khanedy");
            contact.setEmail("eko@example.com");
            contact.setPhone("9238423432");
            contactRepository.save(contact);
        }

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Eko")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(10, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Khanedy")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(10, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("email", "example.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(10, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "38423")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(10, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getSize());
        });

    }
}
