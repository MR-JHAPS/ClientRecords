package com.jhaps.clientrecords.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.ImageDto;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.response.ApiResponseModel;
import com.jhaps.clientrecords.service.ImageService;
import com.jhaps.clientrecords.service.PagedResourceAssemblerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/images")
@Tag(name = "User Image Controller", description = "Insert, Delete, Get images of the User")
@AllArgsConstructor
public class ImageController {

	private ImageService imageService;
	private ApiResponseBuilder apiResponseBuilder;
	private PagedResourceAssemblerService<ImageDto> pagedResourceAssemblerService;
	
	
	@GetMapping("/user/current")
	@Operation(summary = "gets Images of logged in user.")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ImageDto>>>> getImagesOfActiveUser(
				@PageableDefault(page = 0, size = 10) Pageable pageable){
		Page<ImageDto> paginatedImages = imageService.getImagesOfActiveUser(pageable); 
		/* 
		 * Converting to "Hateoas" format Api response 
		 * because it provides Pagination links like: FirstPage, NextPage, LastPage.
		 */ 
		PagedModel<EntityModel<ImageDto>> pagedImageModel = pagedResourceAssemblerService.toPagedModel(paginatedImages);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_OBTAINED, HttpStatus.OK, pagedImageModel);
	}
	
	
	@GetMapping("/user/{id}")
	@Operation(summary = "gets Images By User ID.")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ImageDto>>>> getImagesByUserId(@PathVariable int id,
								@PageableDefault(page = 0, size = 10) Pageable pageable){
		Page<ImageDto> paginatedImages = imageService.getImagesByUser(id, pageable); 
		PagedModel<EntityModel<ImageDto>> pagedImageModel = pagedResourceAssemblerService.toPagedModel(paginatedImages);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_OBTAINED, HttpStatus.OK, pagedImageModel);	
	}
	
	
	@GetMapping("/user/email")
	@Operation(summary = "gets Images By User Email.")
	@PreAuthorize("hasRole('admin')")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ImageDto>>>> getImagesByUserEmail(@RequestParam String userEmail,
												@PageableDefault(page = 0, size = 10) Pageable pageable){
		Page<ImageDto> paginatedImages = imageService.getImagesByUser(userEmail ,pageable); 
		PagedModel<EntityModel<ImageDto>> pagedImageModel = pagedResourceAssemblerService.toPagedModel(paginatedImages);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_OBTAINED, HttpStatus.OK, pagedImageModel);
	}
	
	
	@GetMapping("/{id}")
	@Operation(summary = "gets Images by Image_Id.")
	public ResponseEntity<ApiResponseModel<ImageDto>> getImageById(@PathVariable int id){
		ImageDto imageDto = imageService.getImageById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_SAVED, HttpStatus.OK, imageDto);									
	}
	
	
	@PostMapping("/insert")
	@Operation(summary = "Saves Images")
	public ResponseEntity<ApiResponseModel<String>> saveImage(ImageDto imageDto){
		imageService.saveImage(imageDto);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.IMAGE_SAVED, HttpStatus.OK,
													"Image: " + imageDto.getImageName() + " saved successfully");
	}
	
	@DeleteMapping("delete/{id}")
	@Operation(summary = "Delete image by id")
	public ResponseEntity<?> deleteImageById(ImageDto imageDto){
		imageService.deleteImageById(imageDto.getId());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	
	
	
}// ends controller.
