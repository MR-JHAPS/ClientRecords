package com.jhaps.clientrecords.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.ClientDto;
import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.response.ApiResponseModel;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.response.ResponseMessage;
import com.jhaps.clientrecords.service.ClientService;
import com.jhaps.clientrecords.service.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.Variables;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;

@Validated // this is so that "@NotBlank" can be used in @pathVariable
@RestController
@RequestMapping("/api/clients/search")
@Tag(name = "Client Search Controller", description = "Search Clients by 'AnyQuery', 'FirstName', 'LastName', 'PostalCode'")
public class ClientSearchController{	
	/*In the ApiResponseBuilder.class, responseEntity building method is created
		to reduce the boilerplate code
		no need to create :
						ApiResponse<List<Client>> response = new ApiResponse<>(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
						return ResponseEntity.ok(response);
						for each (if/Else).
	*/
	private ApiResponseBuilder apiResponseBuilder;
	
	private ClientService clientService;
	
	private PagedResourceAssemblerService<ClientDto> pagedResourceAssemblerService;

	public ClientSearchController(ClientService clientService, ApiResponseBuilder apiResponseBuilder,
						PagedResourceAssemblerService<ClientDto> pagedResourceAssemblerService) {
		this.clientService = clientService;
		this.apiResponseBuilder = apiResponseBuilder;
		this.pagedResourceAssemblerService = pagedResourceAssemblerService;
	}
	
	
	@Operation(summary = "get clients by searchQuery")
	@GetMapping("/{searchQuery}")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientDto>>>> getClientsBySearchQuery( @PathVariable @NotBlank String searchQuery,
																					@RequestParam(defaultValue ="0") int page,
																					@RequestParam(defaultValue ="10") int size){
		Pageable pageable = PageRequest.of(page, size);
		Page<ClientDto>  paginatedClients= clientService.findClientBySearchQuery(searchQuery, pageable);
		if(!paginatedClients.isEmpty()) {
			PagedModel<EntityModel<ClientDto>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedClients);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@Operation(summary = "get clients by firstName")
	@GetMapping("/firstName/{firstName}")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientDto>>>> getClientsByFirstName(@PathVariable @NotBlank String firstName,
																@RequestParam(defaultValue = "0") int pageNumber,
																@RequestParam(defaultValue = "10") int pageSize){
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ClientDto> paginatedclients = clientService.findClientsByFirstName(firstName, pageable);
		if(!paginatedclients.isEmpty()) {
			PagedModel<EntityModel<ClientDto>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedclients);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}	
	}
	
	
	@Operation(summary = "get clients by lastName")
	@GetMapping("/lastName/{lastName}")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientDto>>>> getClientsByLastName(@PathVariable @NotBlank String lastName,
															@RequestParam(defaultValue = "0") int pageNumber,
															@RequestParam(defaultValue = "10") int pageSize){
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ClientDto> paginatedClients =  clientService.findClientsByLastName(lastName, pageable);
		if(!paginatedClients.isEmpty()) {
			PagedModel<EntityModel<ClientDto>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedClients);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@Operation(summary = "get clients by postalCode")
	@GetMapping("/postalCode/{postalCode}")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientDto>>>> getClientsByPostalCode(@PathVariable @NotBlank String postalCode,
															@RequestParam(defaultValue = "0") int pageNumber,
															@RequestParam(defaultValue = "10") int pageSize){
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ClientDto> paginatedClients = clientService.findClientsByPostalCode(postalCode, pageable);	
		if(!paginatedClients.isEmpty()) {
			//converting paginatedClients to PagedModel to benefit from hateos dependency.
			PagedModel<EntityModel<ClientDto>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedClients);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	
	
//	@GetMapping("/dateOfBirth/{dateOfBirth}")
//	public ResponseEntity<List<Client>> getClientsByDateOfBirth(@PathVariable String dateOfBirth){
//		//Converting string date to LocalDate because service Layer parameter is LocalDate.
//		LocalDate parsedDate = LocalDate.parse(dateOfBirth);
//		List<Client> clientList = clientService.findClientsByDateOfBirth(parsedDate);
//		if(clientList.isEmpty()) {
//			return ResponseEntity.notFound().build();
//		}	
//		return ResponseEntity.ok(clientList);
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//ends class.
