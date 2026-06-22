package project_skripsi.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project_skripsi.rest.entity.User;
import project_skripsi.rest.model.ContactResponse;
import project_skripsi.rest.model.CreateContactRequest;
import project_skripsi.rest.model.UpdateContactRequest;
import project_skripsi.rest.model.WebResponse;
import project_skripsi.rest.service.ContactService;

import javax.print.attribute.standard.Media;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request){

        ContactResponse contactResponse = contactService.create(user, request);

        return WebResponse.<ContactResponse>builder().data(contactResponse).build();

    }

    @GetMapping(
            path = "/api/contacts/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> get(User user, @PathVariable String id){

        ContactResponse contactResponse = contactService.get(user, id);

        return WebResponse.<ContactResponse>builder().data(contactResponse).build();

    }

    @PutMapping(
            path = "/api/contacts/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> update(
            User user,
            @PathVariable String id,
            @RequestBody UpdateContactRequest request){

        request.setId(id);

        ContactResponse contactResponse = contactService.update(user, request);

        return WebResponse.<ContactResponse>builder().data(contactResponse).build();

    }

    @DeleteMapping(
            path = "/api/contacts/{id}"
    )
    public WebResponse<String> delete(User user, @PathVariable String id){

        contactService.delete(user, id);

        return WebResponse.<String>builder().data("OK").build();

    }

}
