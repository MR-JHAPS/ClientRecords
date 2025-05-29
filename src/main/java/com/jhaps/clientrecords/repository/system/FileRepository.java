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

import com.jhaps.clientrecords.entity.system.UserFile;

@Repository
public interface FileRepository extends JpaRepository<UserFile, Integer> {
	
	/* Get list of files of Authenticated User. */
	Page<UserFile> findByUser_Id(int userId, Pageable pageable); 
	
	List<UserFile> findAllByUser_Email(String email); //This is used only for the delete List<files> of selected userEmail. 
	
	Optional<UserFile> findByFileName(String fileName);
	
	
	@Query("SELECT f FROM UserFile f WHERE f.id=:fileId AND f.user.id=:userId")
	Optional<UserFile> findByIdAndUserId(@Param("fileId") int fileId, @Param("userId") int userId);
	
	@Query("SELECT f From UserFile f WHERE f.fileName=:fileName and f.user.id=:userId")
	Optional<UserFile> findByFileNameAndUserId(@Param("fileName") String fileName, @Param("userId") int userId);
	
	
	@Query("SELECT i From UserFile i WHERE i.storedFileName=:storedFileName and i.user.id=:userId")
	Optional<UserFile> findByStoredFileNameAndUserId(@Param("storedFileName") String storedFileName, @Param("userId") int userId);
	
	
	// Check if file exists and belongs to user (for @PreAuthorize in controller method)
    @Query("SELECT COUNT(f) > 0 FROM UserFile f WHERE f.id = :fileId AND f.user.id = :userId")
    boolean existsByIdAndUserId(@Param("fileId") int fileId, @Param("userId") int userId);
    
    @Modifying
    @Query("DELETE FROM UserFile f WHERE f.id = :fileId AND f.user.id = :userId")
    int deleteByIdAndUserId(@Param("fileId") int fileId, @Param("userId") int userId);

    
    /*
     * Deletes all the UserFiles of the given user.
     */
	@Modifying
	@Query("DELETE FROM UserFile f WHERE f.user.id=:userId")
	void deleteAllFilesByUserId(@Param("userId") int userId);
	
	
	/* 
	 * This gets the file-Url from the given userId and List<Integer> fileId
	 */
//	@Query("SELECT i.url from file i WHERE i.id IN :ids AND i.user.id=:userId")
//	List<String> findUrlsByfileIds(@Param("ids") List<Integer> ids, @Param("userId") int userId);
	
	
	/* 
	 * This is to required to delete the file From Cloudinary-Server.
	 * This gets the file-Pubic_Id from the given userId and List<Integer> fileId
	 */
	@Query("SELECT f.publicId from UserFile f WHERE f.id IN :ids AND f.user.id=:userId")
	List<String> findPublicIdByFileIds(@Param("ids") List<Integer> ids, @Param("userId") int userId);
	
	
	
	
	/*
	 * Deletes the Files by List<Integer> fileIds and given userId. 
	 */
	@Modifying
    @Query("DELETE FROM UserFile f WHERE f.id IN :fileIds AND f.user.id = :userId")
    int deleteByIdsAndUserId(@Param("fileIds") List<Integer> fileIds, @Param("userId") int userId);
	
	
	
	/* Finding file By Name and userId*/
	UserFile findByFileNameAndUser_Id(String fileName, int userId);
	
	boolean existsByFileNameAndUser_Id(String fileName, int userId);
	
	
	
}
