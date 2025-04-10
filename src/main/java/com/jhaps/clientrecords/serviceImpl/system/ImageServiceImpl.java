package com.jhaps.clientrecords.serviceImpl.system;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.jhaps.clientrecords.dto.request.ImageRequest;
import com.jhaps.clientrecords.dto.response.ImageResponse;
import com.jhaps.clientrecords.entity.system.Image;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.exception.system.ImageDeletionException;
import com.jhaps.clientrecords.exception.system.ImageNotFoundException;
import com.jhaps.clientrecords.exception.system.UserNotFoundException;
import com.jhaps.clientrecords.repository.system.ImageRepository;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.service.system.ImageService;
import com.jhaps.clientrecords.util.mapper.ImageMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ImageServiceImpl implements ImageService{

	private ImageRepository imageRepo;
	private UserRepository userRepo;
	
	
	
/*------------------------------------------------- IMAGES BY USER ---------------------------------------------------------------*/	
	
	
	public Image getProfileImageOfAuthenticatedUser(String email) {
		Image image = imageRepo.findByUser_Email(email)
				.orElseGet(()->{
						log.info("Action: Profile Image for User: {} not found, falling back to defaultProfile picture.", email);
						return getDefaultProfileImage();  //if custom ima
						}
					);
		return image;
	}
	
	
	/*	
	 * While Registering new user a default profile image is set.
	 * If the profile image(default/custom) for user is not found it will throw error.
	 *
	 */
	@Override
	public Image getProfileImageByUserId(int id){
		log.info("Action: Attempting to get the profile image of user with id: {}", id);
		User user = userRepo.findById(id)
					.orElseThrow(()-> new UserNotFoundException("User with id: " + id + " not found "));
		Image userImage = imageRepo.findByUser_Id(id)
						.orElseThrow(()-> {
							log.info("Unable to find an image for user with id: {}, email: {}", id, user.getEmail());
							throw new ImageNotFoundException("Image for user with id "+ id +" not found");
						});
		return userImage;
	}
	
	
	/* Gets all the images of the logged in user using userEmail */
	@Override
	public Page<Image> getImagesByUserEmail(String email, Pageable pageable) {
		log.info("Getting images for User_Email: {}", email);
		Page<Image> images = imageRepo.findByUser_Email(email , pageable);
		if(images.isEmpty()) {
			log.info("Message: Images for User_Email : {} not found in Database.", email);
		}
		log.info("Message: {} Images for User_Email : {} fetched.", images.getNumberOfElements(), email);
		return images;
	}

	
	@Override
	@Transactional
	public Image saveImage(String userEmail,ImageRequest imageRequest) {
		log.info("Action: saving Image: {}", imageRequest.getImageName());
		User user = userRepo.findByEmail(userEmail)
				.orElseThrow(()-> new UserNotFoundException("Unable to find the User with email: " + userEmail));
		Image image = new Image();
		image.setImageName(imageRequest.getImageName());
		image.setUser(user);
		log.info("Action: Image: {} saved successfully by User_Email: {}.", imageRequest.getImageName(), userEmail);
		imageRepo.save(image);
		return image;
	}
	

/*------------------------------------------------- IMAGE CRUD ---------------------------------------------------------------*/

	@Override
	public Image getImageById(int id) {
		log.info("Action: Getting image By Image_Id: {}", id);
		Image image = imageRepo.findById(id)
				.orElseThrow(() -> new ImageNotFoundException("Image with Id: " + id + " not found."));
		return image;
	}
	
	
	/*
	 * In ImageRepository I have used:
	 * 				 @PreAuthorize("#image.user.email == authentication.name").
	 * This allows the deletion only if the image belongs to the authenticated user.
	 * 
	 * */
	@Override
	@Transactional
	public void deleteImageById(int id) {
		log.info("Action: Deleting Image of Id: {}", id);
		Image image = imageRepo.findById(id)
				.orElseThrow(() -> new ImageNotFoundException("Image with Id: " + id + " not found."));
		log.info("Action: Image of Id:{} deleted Successfully.", id);
		imageRepo.delete(image);
	}

	
	@Override
	@Transactional
	public void deleteMultipleImagesById(List<Integer> idList) {
		log.info("Action: Deleting Multiple Images of Id's: {}", idList);		
		for(Integer id : idList) {
			Image image = imageRepo.findById(id)
					.orElseThrow(() -> new ImageNotFoundException("Image with Id: " + id + " not found."));
			log.info("Action: Image of Id:{} deleted Successfully.", id);
			imageRepo.delete(image);
		}
	}

	
	/* To get the default profileImage from the ImageRepository. */
	@Override
	public Image getDefaultProfileImage() {
		final String DEFAULT_IMAGE = "defaultImage.png";
			return imageRepo.findByImageName(DEFAULT_IMAGE)
						.orElseGet(()->{
							//if image doesn't exist create a new one.
							Image defaultImage = new Image();
							defaultImage.setImageName(DEFAULT_IMAGE);
							defaultImage.setUser(null);
							return imageRepo.save(defaultImage);
						});
	}


	@Override
	public Image findByImageNameAndUserEmail(String imageName, String email) {
		Image image = imageRepo.findByImageNameAndUserEmail(imageName, email)
				.orElseThrow(() -> new ImageNotFoundException("Error: Image with name: " + imageName + " and user-email: " + email + "not found"));
		return image;
	}
	
	
	@Override
	public boolean doesImageExistsByImageNameAndUserEmail(String imageName, String email) {
		if(imageRepo.existsByImageNameAndUserEmail(imageName, email)) {
			return true;
		}
		return false;
	}

	
	@Override
	public Image saveDefaultProfileImageForGivenUser(int userId) {
		String defaultProfileImage = "defaultImage.png";
		User user = findUserById(userId);
		try {
			Image defaultImage = new Image();
			defaultImage.setImageName(defaultProfileImage);
			defaultImage.setUser(user);
			return imageRepo.save(defaultImage);
		}catch (Exception e) {
			log.error("Error: unable to save new Default ProfileImage for given user with id : {}", userId, e);
			return null;
		}		
	}
	
	
	
	
	@Override
	public void deleteAllImagesOfGivenUser(int userId) {		
		try{
			imageRepo.deleteAllImagesByUserId(userId);
			log.info("Delete all images with userId successful.");
		}catch (Exception e) {
			log.error("Error deleting images for user {}", userId, e);
			throw new RuntimeException("Delete All images by user id failed.");
		}
	}
	
	
	/*--------------Private method of user for images ----------------------------*/
	private User findUserById(int id) {
		return userRepo.findById(id)
				.orElseThrow(()-> new UserNotFoundException("User with id : " + id + " not found"));
	}
	
	
	
	
}//ends class.
