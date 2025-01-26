package com.jhaps.clientrecords.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>{

	List<Client> findByFirstName(String firstName);
	List<Client> findByLastName(String lastName);
	List<Client> findByDateOfBirth(LocalDate dateOfBirth);
	List<Client> findByPostalCode(String postalCode);
	
}
