package project_skripsi.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project_skripsi.rest.entity.User;
import project_skripsi.rest.model.AddressResponse;
import project_skripsi.rest.model.CreateAddressRequest;
import project_skripsi.rest.model.WebResponse;
import project_skripsi.rest.service.AddressService;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(
            path = "/api/contacts/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> create(User user,
                                               @PathVariable String contactId,
                                               @RequestBody CreateAddressRequest request){
        request.setContactId(contactId);

        AddressResponse addressResponse = addressService.create(user, request);

        return WebResponse.<AddressResponse>
                builder()
                .data(addressResponse)
                .build();
    }

}
