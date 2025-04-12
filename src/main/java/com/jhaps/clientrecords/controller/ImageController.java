package com.jhaps.clientrecords.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.request.BulkImageDeleteRequest;
import com.jhaps.clientrecords.dto.request.ImageRequest;
import com.jhaps.clientrecords.dto.response.ImageResponse;
import com.jhaps.clientrecords.entity.system.Image;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.security.model.CustomUserDetails;
import com.jhaps.clientrecords.service.system.ImageService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.PageableUtils;
import com.jhaps.clientrecords.util.mapper.ImageMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
/*
 * I have not used Method-Level Security-Authorization.
 * I have authenticated all controller apart from " AuthController ---> /api/public/** "
 * 
 * Because both "user" and "admin" should be able to use this controller.
 * 
 *  If I were to create a new Role then I will need to navigate to each 
 *  method to permit new role. So, I chose not to enable method-level-security.
 * 
 * */



@RestController
@RequestMapping("/api/images")
@Tag(name = "Image API's", description = "Insert, Delete, Get images of the User")
@AllArgsConstructor
public class ImageController {

	private ImageService imageService;
	private ApiResponseBuilder apiResponseBuilder;
	private PagedResourceAssemblerService<ImageResponse> pagedResourceAssemblerService;
	private ImageMapper imageMapper;
	
	
	@GetMapping("/me")
	@Operation(summary = "Get All Images(Paginated) of logged in user.")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ImageResponse>>>> getImagesOfActiveUser(
						@RequestParam(defaultValue="0") int pageNumber,
						@RequestParam(defaultValue="10") int pageSize,
						@RequestParam(required = false) String sortBy,
						@RequestParam(required = false) String direction,
						@AuthenticationPrincipal CustomUserDetails userDetails){
		int userId = userDetails.getUserId();
		Pageable pageable = PageableUtils.createPageable(pageNumber, pageSize, sortBy, direction);
		Page<Image> paginatedImages = imageService.getImagesOfCurrentUser(userId, pageable);
		/* Mapping : Page<Image> to Page<ImageResponse> Dto .*/
		Page<ImageResponse> paginatedResponse = paginatedImages.map(imageMapper::toImageResponse);
		PagedModel<EntityModel<ImageResponse>> pagedImageModel = pagedResourceAssemblerService.toPagedModel(paginatedResponse);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_OBTAINED, HttpStatus.OK, pagedImageModel);
	}	

	
	
	
	@GetMapping("/{imageId}")
	@Operation(summary = "Get Image by Id.",
	description = "This allows the users who have uploaded multiple images to select and view each images individually.")
	@PreAuthorize("@imageRepository.existsByIdAndUserId(#imageId, #userDetails.userId) or hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<ImageResponse>> getImageById(@PathVariable int imageId, @AuthenticationPrincipal CustomUserDetails userDetails){
		int userId = userDetails.getUserId();
		Image image = imageService.getImageById(imageId, userId);
		/* Mapping : Image to ImageResponse Dto .*/
		ImageResponse imageResponse = imageMapper.toImageResponse(image);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_SAVED, HttpStatus.OK, imageResponse);									
	}
	
	
	@PostMapping
	@Operation(summary = "Upload new Image")
	public ResponseEntity<ApiResponseModel<String>> saveImage(@RequestBody ImageRequest request,
			@AuthenticationPrincipal CustomUserDetails userDetails){
		int userId = userDetails.getUserId();
		imageService.saveImage(userId, request);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_SAVED, HttpStatus.OK,
						"Image: " + request.getImageName() + " saved successfully");
	}

	
	/* Deletes the single-selected Image by id if it belongs to authenticated user. */
	@PreAuthorize("@imageRepository.existsByIdAndUserId(#imageId, #userDetails.userId)")
	@DeleteMapping("/{imageId}")
	@Operation(summary = "Delete user's image(single)")
	public ResponseEntity<?> deleteImageById(@PathVariable int imageId,
											@AuthenticationPrincipal CustomUserDetails userDetails){
		int userId = userDetails.getUser().getId();
		imageService.deleteImageById(imageId, userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	/* Deletes the multiple-selected Images by id */
	@DeleteMapping
	@Operation(summary = "Delete multiple Images by ID's")
	public ResponseEntity<?> deleteMultipleImagesById(@RequestBody BulkImageDeleteRequest request,
			@AuthenticationPrincipal CustomUserDetails userDetails){
		int userId = userDetails.getUserId();
		List<Integer> imageIdList = request.getIdList();
		imageService.deleteMultipleImagesById(imageIdList, userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	
}// ends controller.
