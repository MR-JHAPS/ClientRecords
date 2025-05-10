package com.jhaps.clientrecords.repository.system;



import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.system.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
	
	/* Get list of images of Authenticated User. */
	Page<Image> findByUser_Id(int userId, Pageable pageable); 
	
	List<Image> findAllByUser_Email(String email); //This is used only for the delete List<images> of selected userEmail. 
	
	Optional<Image> findByImageName(String imageName);
	
	
	@Query("SELECT i FROM Image i WHERE i.id=:imageId AND i.user.id=:userId")
	Optional<Image> findByIdAndUserId(@Param("imageId") int imageId, @Param("userId") int userId);
	
	@Query("SELECT i From Image i WHERE i.imageName=:imageName and i.user.id=:userId")
	Optional<Image> findByImageNameAndUserId(@Param("imageName") String imageName, @Param("userId") int userId);
	
	
	@Query("SELECT i From Image i WHERE i.storedFileName=:storedFileName and i.user.id=:userId")
	Optional<Image> findByStoredFileNameAndUserId(@Param("storedFileName") String storedFileName, @Param("userId") int userId);
	
	
	// Check if image exists and belongs to user (for @PreAuthorize in controller method)
    @Query("SELECT COUNT(i) > 0 FROM Image i WHERE i.id = :imageId AND i.user.id = :userId")
    boolean existsByIdAndUserId(@Param("imageId") int imageId, @Param("userId") int userId);
    
    @Modifying
    @Query("DELETE FROM Image i WHERE i.id = :imageId AND i.user.id = :userId")
    int deleteByIdAndUserId(@Param("imageId") int imageId, @Param("userId") int userId);

    
    /*
     * Deletes all the Images of the given user.
     */
	@Modifying
	@Query("DELETE FROM Image i WHERE i.user.id=:userId")
	void deleteAllImagesByUserId(@Param("userId") int userId);
	
	
	/* 
	 * This gets the Image-Url from the given userId and List<Integer> imageId
	 */
	@Query("SELECT i.url from Image i WHERE i.id IN :ids AND i.user.id=:userId")
	List<String> findUrlsByImageIds(@Param("ids") List<Integer> ids, @Param("userId") int userId);
	
	
	/* 
	 * This is to required to delete the image From Cloudinary-Server.
	 * This gets the Image-Pubic_Id from the given userId and List<Integer> imageId
	 */
	@Query("SELECT i.publicId from Image i WHERE i.id IN :ids AND i.user.id=:userId")
	List<String> findPublicIdByImageIds(@Param("ids") List<Integer> ids, @Param("userId") int userId);
	
	
	
	
	/*
	 * Deletes the Images by List<Integer> imageIds and given userId. 
	 */
	@Modifying
    @Query("DELETE FROM Image i WHERE i.id IN :imageIds AND i.user.id = :userId")
    int deleteByIdsAndUserId(@Param("imageIds") List<Integer> imageIds, @Param("userId") int userId);
	
	
	
	/* Finding Image By Name and userId*/
	Image findByImageNameAndUser_Id(String imageName, int userId);
	
	boolean existsByImageNameAndUser_Id(String imageName, int userId);
	
	
	
}
