package com.jhaps.clientrecords.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

	/*
	 * we are using "_" because we are accessing the field of User.
	 * Example: Image has field User and of that User we are accessing it's field so we need "_".
	 * Image -> User -> id.
	 * Image : [ "id" , "imageName" , "User: [id, email]" ];
	 *  for id and imageName we don't need "_". 
	 *  for User Properties we need "_" because it is nested property.
	 * */
	
	Page<Image> findByUser_Id(int id, Pageable pageable);
	
	Page<Image> findByUser_Email(String email, Pageable pageable);
	
	
	
}
