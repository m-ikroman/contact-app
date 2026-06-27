package project_skripsi.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project_skripsi.rest.entity.User;
import project_skripsi.rest.model.AddressResponse;
import project_skripsi.rest.model.CreateAddressRequest;
import project_skripsi.rest.model.UpdateAddressRequest;
import project_skripsi.rest.model.WebResponse;
import project_skripsi.rest.service.AddressService;

import java.util.List;

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

    @GetMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> get(User user, @PathVariable String contactId, @PathVariable String addressId){
        AddressResponse addressResponse = addressService.get(user, contactId, addressId);

        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @PutMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> update(User user,
                                               @PathVariable String contactId,
                                               @PathVariable String addressId,
                                               @RequestBody UpdateAddressRequest request
                                               ){

        request.setAddressId(addressId);
        request.setContactId(contactId);

        AddressResponse addressResponse = addressService.update(user, request);

        return WebResponse.<AddressResponse>builder().data(addressResponse).build();

    }

    @DeleteMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> remove(User user, @PathVariable String contactId, @PathVariable String addressId){

        addressService.remove(user, contactId, addressId);

        return WebResponse.<String>builder().data("OK").build();

    }

    @GetMapping(
            path = "/api/contacts/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddressResponse>> get(User user, @PathVariable String contactId){
        List<AddressResponse> addressResponses = addressService.list(user, contactId);

        return WebResponse.<List<AddressResponse>>builder().data(addressResponses).build();
    }

}
