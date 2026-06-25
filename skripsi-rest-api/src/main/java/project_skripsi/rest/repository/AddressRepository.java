package project_skripsi.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project_skripsi.rest.entity.Address;

public interface AddressRepository extends JpaRepository<Address, String> {
}
