package com.jhaps.clientrecords.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.system.ImageService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.PageableUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Image API's", description = "Insert, Delete, Get images of the User")
@AllArgsConstructor
public class ImageController {

	private ImageService imageService;
	private ApiResponseBuilder apiResponseBuilder;
	private PagedResourceAssemblerService<ImageResponse> pagedResourceAssemblerService;
	
	
	@GetMapping("/user/current")
	@Operation(summary = "Gets All Images of logged in user.")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ImageResponse>>>> getImagesOfActiveUser(
						@RequestParam(defaultValue="0") int pageNumber,
						@RequestParam(defaultValue="10") int pageSize,
						@RequestParam(required = false) String sortBy,
						@RequestParam(required = false) String direction,
						@AuthenticationPrincipal UserDetails userDetails){
		String userEmail = userDetails.getUsername();
		Pageable pageable = PageableUtils.createPageable(pageNumber, pageSize, sortBy, direction);
		Page<ImageResponse> paginatedImages = imageService.getImagesByUserEmail(userEmail, pageable); 
		PagedModel<EntityModel<ImageResponse>> pagedImageModel = pagedResourceAssemblerService.toPagedModel(paginatedImages);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_OBTAINED, HttpStatus.OK, pagedImageModel);
	}	
	
	
	@GetMapping("/{id}")
	@Operation(summary = "Get Image by Id.",
	description = "This allows the users who have uploaded multiple images to select and view each images individually.")
	public ResponseEntity<ApiResponseModel<ImageResponse>> getImageById(@PathVariable int id){
		ImageResponse imageResponse = imageService.getImageResponseById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_SAVED, HttpStatus.OK, imageResponse);									
	}
	
	
	@PostMapping("/insert")
	@Operation(summary = "Upload image for authenticated user")
	public ResponseEntity<ApiResponseModel<String>> saveImage(@RequestBody ImageRequest request,
			@AuthenticationPrincipal UserDetails userDetails){
		String userEmail = userDetails.getUsername();
		imageService.saveImage(userEmail, request);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_SAVED, HttpStatus.OK,
						"Image: " + request.getImageName() + " saved successfully");
	}

	
	/* Deletes the single-selected Image by id*/
	@DeleteMapping("delete/{id}")
	@Operation(summary = "Delete user's image(single) by ID")
	public ResponseEntity<?> deleteImageById(@PathVariable int id){
		imageService.deleteImageById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	/* Deletes the multiple-selected Images by id */
	@DeleteMapping("delete/multiple")
	@Operation(summary = "Delete user's image(multiple) by ID's")
	public ResponseEntity<?> deleteMultipleImagesById(@RequestBody BulkImageDeleteRequest request){
		List<Integer> idList = request.getIdList();
		imageService.deleteMultipleImagesById(idList);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	
}// ends controller.
