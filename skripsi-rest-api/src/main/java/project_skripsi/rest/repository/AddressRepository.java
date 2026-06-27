package project_skripsi.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project_skripsi.rest.entity.Address;
import project_skripsi.rest.entity.Contact;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, String> {
    Optional<Address> findFirstByContactAndId(Contact contact, String id);

    List<Address> findAllByContact(Contact contact);
}
