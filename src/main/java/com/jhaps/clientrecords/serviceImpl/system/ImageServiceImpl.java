package com.jhaps.clientrecords.serviceImpl.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.response.ImageDto;
import com.jhaps.clientrecords.entity.system.Image;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.exception.system.ImageNotFoundException;
import com.jhaps.clientrecords.repository.system.ImageRepository;
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
	private UserService userService; 
	private Mapper mapper;
	
	
	
/*------------------------------------------------- IMAGES BY USER ---------------------------------------------------------------*/	
	
	@Override
	public Page<ImageDto> getImagesOfActiveUser(String activeUserEmail, Pageable pageable) {
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
		User user = userService.findUserByEmail(userEmail); // not found error is handled in userService.	
			Image image = new Image();
			image.setImageName(imageDto.getImageName());
			image.setUser(user);
		log.info("Action: Image: {} saved successfully by User_Email: {}.", imageDto.getImageName(), userEmail);
		imageRepo.save(image);
	}

	
	
	
	
	
}
