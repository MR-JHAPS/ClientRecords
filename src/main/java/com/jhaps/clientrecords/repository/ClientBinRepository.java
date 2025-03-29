package com.jhaps.clientrecords.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.ClientBin;

@Repository
public interface ClientBinRepository extends JpaRepository<ClientBin, Integer>{

	Page<ClientBin> findAll(Pageable pageable);
	
	
}
