package project_skripsi.rest.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import project_skripsi.rest.entity.Contact;
import project_skripsi.rest.entity.User;
import project_skripsi.rest.model.ContactResponse;
import project_skripsi.rest.model.CreateContactRequest;
import project_skripsi.rest.model.SearchContactRequest;
import project_skripsi.rest.model.UpdateContactRequest;
import project_skripsi.rest.repository.ContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ContactRepository contactRepository;

    @Transactional
    public ContactResponse create(User user, CreateContactRequest request){

        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstname());
        contact.setLastName(request.getLastname());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepository.save(contact);

        return contactResponse(contact);

    }

    @Transactional(readOnly = true)
    public ContactResponse get(User user, String id){

        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found");
                });

        return contactResponse(contact);

    }

    @Transactional
    public ContactResponse update(User user, UpdateContactRequest request){

        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found");
                });

        contact.setFirstName(request.getFirstname());
        contact.setLastName(request.getLastname());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());

        contactRepository.save(contact);

        return contactResponse(contact);

    }

    @Transactional
    public void delete(User user, String id){

        Contact contact = contactRepository.findFirstByUserAndId(user, id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));

        contactRepository.delete(contact);

    }

    private ContactResponse contactResponse(Contact contact){
        return ContactResponse.builder()
                .id(contact.getId())
                .firstname(contact.getFirstName())
                .lastname(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<ContactResponse> search(User user, SearchContactRequest request){

        Specification<Contact> specification = (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(builder.equal(root.get("user"), user));

            if (Objects.nonNull(request.getName())){
                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%"+request.getName()+"%"),
                        builder.like(root.get("lastName"), "%"+request.getName()+"%")
                ));
            }

            if (Objects.nonNull(request.getEmail())){
                predicates.add(builder.or(
                        builder.like(root.get("email"), "%"+request.getEmail()+"%")
                ));
            }

            if (Objects.nonNull(request.getPhone())){
                predicates.add(builder.or(
                        builder.like(root.get("phone"), "%"+request.getPhone()+"%")
                ));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);

        List<ContactResponse> contactResponses = contacts.getContent()
                .stream()
                .map(contact -> contactResponse(contact))
                .collect(Collectors.toList());

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }

}
