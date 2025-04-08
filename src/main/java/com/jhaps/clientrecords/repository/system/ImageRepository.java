package com.jhaps.clientrecords.repository.system;



import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.system.Image;

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
	
	
	Page<Image> findByUser_Email(String email, Pageable pageable); /* This is the main*/
	
	List<Image> findAllByUser_Email(String email); //This is used only for the delete List<images> of selected userEmail. 
	
	Optional<Image> findByUser_Id(int userId);
	
	Optional<Image> findByUser_Email(String userEmail);
	
	@Query("SELECT i FROM Image i WHERE i.imageName='defaultImage.png'")
	Optional<Image> findDefaultImage();
	
	Optional<Image> findByImageName(String imageName);
	
	boolean existsByImageName(String imageName);
	
	
	/*Authorized to delete only if the authenticated user is the owner of the image. */
	@PreAuthorize("#image.user.email == authentication.name")
	void delete(@Param("image") Image image);
	
	
	
	
}
