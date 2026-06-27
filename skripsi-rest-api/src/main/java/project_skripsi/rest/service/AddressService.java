package project_skripsi.rest.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import project_skripsi.rest.entity.Address;
import project_skripsi.rest.entity.Contact;
import project_skripsi.rest.entity.User;
import project_skripsi.rest.model.AddressResponse;
import project_skripsi.rest.model.CreateAddressRequest;
import project_skripsi.rest.model.UpdateAddressRequest;
import project_skripsi.rest.repository.AddressRepository;
import project_skripsi.rest.repository.ContactRepository;
import project_skripsi.rest.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Transactional
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

    @Transactional(readOnly = true)
    public AddressResponse get(User user,String contactId, String addressId){

        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found");
                });

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Address Not Found");
                });

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

    @Transactional
    public AddressResponse update(User user, UpdateAddressRequest request){
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId())
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found");
                });

        Address address = addressRepository.findFirstByContactAndId(contact, request.getAddressId())
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Address Not Found");
                });

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        addressRepository.save(address);

        return addressResponse(address);

    }

    @Transactional
    public void remove(User user, String contactId, String addressId){

        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found");
                });

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Address Not Found");
                });

        addressRepository.delete(address);

    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(User user, String contactId){
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found");
                });

        List<Address> addresses = addressRepository.findAllByContact(contact);

        return addresses.stream().map(this::addressResponse).toList();
    }

}
