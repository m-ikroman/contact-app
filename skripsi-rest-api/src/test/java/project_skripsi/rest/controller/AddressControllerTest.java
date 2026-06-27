package project_skripsi.rest.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project_skripsi.rest.entity.Address;
import project_skripsi.rest.entity.Contact;
import project_skripsi.rest.entity.User;
import project_skripsi.rest.model.AddressResponse;
import project_skripsi.rest.model.CreateAddressRequest;
import project_skripsi.rest.model.UpdateAddressRequest;
import project_skripsi.rest.model.WebResponse;
import project_skripsi.rest.repository.AddressRepository;
import project_skripsi.rest.repository.ContactRepository;
import project_skripsi.rest.repository.UserRepository;
import project_skripsi.rest.security.BCrypt;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;
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

    @Autowired
    private AddressRepository addressRepository;


    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("ikroman");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("Ikroman");
        user.setToken("ini-token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setId("test");
        contact.setFirstName("Muhamad");
        contact.setLastName("Ikroman");
        contact.setEmail("ikroman@gmail.com");
        contact.setPhone("081818188181");
        contact.setUser(user);
        contactRepository.save(contact);

    }

    @Test
    void testCreateAddressBadReq()throws Exception{
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                post("/api/contacts/test/addresses")
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
    void testCreateAddressSuccess() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Siliwangi");
        request.setCity("Tasikmalaya");
        request.setProvince("West Java");
        request.setCountry("Indonesia");
        request.setPostalCode("45467");

        mockMvc.perform(
                post("/api/contacts/test/addresses")
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

    @Test
    void testGetAddressNotFound()throws Exception{

        mockMvc.perform(
                get("/api/contacts/test/addresses/test")
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
    void testGetAddressSuccess()throws Exception{
        Contact contact = contactRepository.findById("test").orElseThrow();

        Address address = new Address();
        address.setId("test");
        address.setStreet("Siliwangi");
        address.setCity("Tasikmalaya");
        address.setProvince("West Java");
        address.setCountry("Indonesia");
        address.setPostalCode("45467");
        address.setContact(contact);
        addressRepository.save(address);

        mockMvc.perform(
                get("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AddressResponse>>() {
            });

            Assertions.assertNull(response.getErrors());
            Assertions.assertEquals("Siliwangi", response.getData().getStreet());
            Assertions.assertEquals("Tasikmalaya", response.getData().getCity());
            Assertions.assertEquals("West Java", response.getData().getProvince());
            Assertions.assertEquals("Indonesia", response.getData().getCountry());


        });
    }

    @Test
    void testUpdateAddressBadReq()throws Exception{
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                put("/api/contacts/test/addresses/test")
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
    void testUpdateAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();

        Address address = new Address();
        address.setId("test");
        address.setStreet("Siliwangi Lama");
        address.setCity("Tasikmalaya Lama");
        address.setProvince("West Java Lama");
        address.setCountry("Indonesia Lama");
        address.setPostalCode("45467 Lama");
        address.setContact(contact);
        addressRepository.save(address);

        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Siliwangi");
        request.setCity("Tasikmalaya");
        request.setProvince("West Java");
        request.setCountry("Indonesia");
        request.setPostalCode("45467");

        mockMvc.perform(
                put("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {

            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Address addressDB = addressRepository.findById("test").orElseThrow();

            Assertions.assertEquals(addressDB.getStreet(), response.getData().getStreet());
            Assertions.assertEquals(addressDB.getCity(), response.getData().getCity());
            Assertions.assertEquals(addressDB.getProvince(), response.getData().getProvince());
            Assertions.assertEquals(addressDB.getCountry(), response.getData().getCountry());
            Assertions.assertEquals(addressDB.getPostalCode(), response.getData().getPostalCode());

        });
    }

    @Test
    void testDeleteAddressNotFound()throws Exception{

        mockMvc.perform(
                delete("/api/contacts/test/addresses/test")
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
    void testDeleteAddressSuccess()throws Exception{
        Contact contact = contactRepository.findById("test").orElseThrow();

        Address address = new Address();
        address.setId("test");
        address.setStreet("Siliwangi");
        address.setCity("Tasikmalaya");
        address.setProvince("West Java");
        address.setCountry("Indonesia");
        address.setPostalCode("45467");
        address.setContact(contact);
        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            Assertions.assertNull(response.getErrors());
            Assertions.assertEquals("OK", response.getData());
            Assertions.assertFalse(addressRepository.existsById("test"));


        });
    }

    @Test
    void testGetListAddressNotFound()throws Exception{

        mockMvc.perform(
                get("/api/contacts/salah/addresses")
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
    void testGetListAddressSuccess()throws Exception{
        Contact contact = contactRepository.findById("test").orElseThrow();

        Address address = new Address();
        address.setId("test");
        address.setStreet("Siliwangi");
        address.setCity("Tasikmalaya");
        address.setProvince("West Java");
        address.setCountry("Indonesia");
        address.setPostalCode("45467");
        address.setContact(contact);
        addressRepository.save(address);

        mockMvc.perform(
                get("/api/contacts/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "ini-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertNull(response.getErrors());
            Assertions.assertEquals(1, response.getData().size());


        });
    }
}
