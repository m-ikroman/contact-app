package project_skripsi.rest.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project_skripsi.rest.entity.Address;
import project_skripsi.rest.entity.Contact;
import project_skripsi.rest.entity.User;
import project_skripsi.rest.model.AddressResponse;
import project_skripsi.rest.model.CreateAddressRequest;
import project_skripsi.rest.repository.AddressRepository;
import project_skripsi.rest.repository.ContactRepository;
import project_skripsi.rest.repository.UserRepository;

import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    public AddressResponse create(User user, CreateAddressRequest request){

        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId()).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Address Not Found");
        });

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setContact(contact);

        addressRepository.save(address);

        return addressResponse(address);

    }

    private AddressResponse addressResponse(Address address){
        return AddressResponse
                .builder()
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }

}
