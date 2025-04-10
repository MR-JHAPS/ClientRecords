package com.jhaps.clientrecords.repository.system;



import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.system.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
	
	
	/* Checking if the ImageName for the given user already Exists. */
	@Query("SELECT i From Image i WHERE i.imageName=:imageName and i.user.email=:userEmail")
	Optional<Image> findByImageNameAndUserEmail(@Param("imageName") String imageName, @Param("userEmail") String email);
	
//	@Query("SELECT COUNT(i)>1 From Image i WHERE i.imageName=:imageName and i.user.email=:userEmail")
//	boolean existsByImageNameAndUserEmail(@Param("imageName") String imageName, @Param("userEmail") String email);
	
	Page<Image> findByUser_Email(String email, Pageable pageable); /* This is the main*/
	
	
	//UPDATED.
	/* Get list of images of Authenticated User. */
	Page<Image> findByUser_Id(int userId, Pageable pageable); 
	
	
	
	
	
	List<Image> findAllByUser_Email(String email); //This is used only for the delete List<images> of selected userEmail. 
	
	Optional<Image> findByUser_Id(int userId);
	
	Optional<Image> findByUser_Email(String userEmail);
	
//	@Query("SELECT i FROM Image i WHERE i.imageName='defaultImage.png'")
//	Optional<Image> findDefaultImage();
	
	Optional<Image> findByImageName(String imageName);
	
	boolean existsByImageName(String imageName);
	

	// Check if image exists and belongs to user (for @PreAuthorize)
    @Query("SELECT COUNT(i) > 0 FROM Image i WHERE i.id = :imageId AND i.user.id = :userId")
    boolean existsByIdAndUserId(@Param("imageId") int imageId, 
                              @Param("userId") int userId);
    
    @Modifying
    @Query("DELETE FROM Image i WHERE i.id = :imageId AND i.user.id = :userId")
    int deleteByIdAndUserId(@Param("imageId") int imageId, 
                          @Param("userId") int userId);

	
	
	@Modifying
	@Query("DELETE FROM Image i WHERE i.user.id=:userId")
	void deleteAllImagesByUserId(@Param("userId") int userId);
	
	@Query("SELECT i From Image i WHERE i.imageName=:imageName and i.user.id=:userId")
	Optional<Image> findByImageNameAndUserId(@Param("imageName") String imageName, @Param("userId") int userId);
	
	
	
	
}
