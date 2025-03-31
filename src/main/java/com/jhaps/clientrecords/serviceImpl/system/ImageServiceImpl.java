package com.jhaps.clientrecords.serviceImpl.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.jhaps.clientrecords.dto.response.ImageDto;
import com.jhaps.clientrecords.entity.system.Image;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.exception.system.ImageDeletionException;
import com.jhaps.clientrecords.exception.system.ImageNotFoundException;
import com.jhaps.clientrecords.exception.system.UserNotFoundException;
import com.jhaps.clientrecords.repository.system.ImageRepository;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.service.system.ImageService;
import com.jhaps.clientrecords.service.system.UserService;
import com.jhaps.clientrecords.util.Mapper;
import com.jhaps.clientrecords.util.SecurityUtils;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ImageServiceImpl implements ImageService{

	private ImageRepository imageRepo;
//	private UserService userService;  // if i use this this will cause circular dependency issues so i am using userRepo.
	private Mapper mapper;
	private UserRepository userRepo;
	
	
	
/*------------------------------------------------- IMAGES BY USER ---------------------------------------------------------------*/	
	
	@Override
	public Page<ImageDto> getImagesByEmail(String activeUserEmail, Pageable pageable) {
		log.info("Getting images for Active_User_Email: {}", activeUserEmail);
		Page<Image> images = imageRepo.findByUser_Email(activeUserEmail , pageable);
		if(images.isEmpty()) {
			log.info("Message: Images for User_Email : {} not found in Database.", activeUserEmail);
		}
		log.info("Message: {} Images for User_Email : {} fetched.", images.getNumberOfElements(), activeUserEmail);
		return images.map(mapper::toImageDto);
	}
	
	
//	@Override
//	public Page<ImageDto> getImagesByUser(int id, Pageable pageable) {
//		log.info("Getting images for User_Id: {}", id);
//		userService.findUserById(id); // if user is not found it will throw UserNotFoundException in userService.
//		Page<Image> images = imageRepo.findByUser_Id(id , pageable);
//		if(images.isEmpty()) {
//			log.info("Message: Images for User_Id : {} not found in Database.", id);
//		}
//		log.info("Message: {} Images for User_id : {} fetched.", images.getNumberOfElements(), id);
//		return images.map(mapper::toImageDto);
//	}	
	
	
	@Override
	public Page<ImageDto> getImagesByUserEmail(String email, Pageable pageable) {
		log.info("Getting images for User_Email: {}", email);
		Page<Image> images = imageRepo.findByUser_Email(email , pageable);
		if(images.isEmpty()) {
			log.info("Message: Images for User_Email : {} not found in Database.", email);
		}
		log.info("Message: {} Images for User_Email : {} fetched.", images.getNumberOfElements(), email);
		return images.map(mapper::toImageDto);
	}

	
	
/*------------------------------------------------- IMAGES CRUD ---------------------------------------------------------------*/

	@Override
	public ImageDto getImageById(int id) {
		log.info("Action: Getting image By Image_Id: {}", id);
		Image image = imageRepo.findById(id)
				.orElseThrow(() -> new ImageNotFoundException("Image with Id: " + id + " not found."));
		return mapper.toImageDto(image);
	}

	
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
	public void saveImage(String userEmail,ImageDto imageDto) {
		log.info("Action: saving Image: {}", imageDto.getImageName());
		User user = userRepo.findByEmail(userEmail)
				.orElseThrow(()-> new UserNotFoundException("Unable to find the User with email: " + userEmail));
			Image image = new Image();
			image.setImageName(imageDto.getImageName());
			image.setUser(user);
		log.info("Action: Image: {} saved successfully by User_Email: {}.", imageDto.getImageName(), userEmail);
		imageRepo.save(image);
	}


	@Override
	@Transactional
	public void deleteImagesByUserEmail(String email) {
		List<Image> images = imageRepo.findByUser_Email(email);
		if(images.isEmpty()) {
			log.info("Message: unable to find the images for user: {} .", email);
			return;
		}
		//safety checking the images contains the email of the user we want to delete.
		images.forEach(img -> {
				if(!img.getUser().getEmail().equals(email)) {
					log.error("Security Violation: Attempted to delete image of id: {} belonging to wrong user, "
							+ "expected User: {}, actual User: {}",img.getId(),email, img.getUser().getEmail());
					throw new SecurityException("Verification failed for Image ownership");
				}//ends if
			}// ends lambda			
		);//ends for each
		try {
			imageRepo.deleteAllInBatch(images);
			log.info("Deleted {} images for user: {}", images.size(), email);
		}catch(Exception e) {
			log.error("Failed to delete images for user: {}", email, e);
			throw new ImageDeletionException("Image Deletion failed for User: " + email );
		}
	}

	
	
	
	
	
}
