package com.jhaps.clientrecords.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.request.ClientDto;
import com.jhaps.clientrecords.dto.response.ClientResponse;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.client.ClientService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.SortBuilder;

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
	*/
	private ApiResponseBuilder apiResponseBuilder;
	
	private ClientService clientService;
	
	private PagedResourceAssemblerService<ClientResponse> pagedResourceAssemblerService;

	
	public ClientSearchController(ClientService clientService, ApiResponseBuilder apiResponseBuilder,
						PagedResourceAssemblerService<ClientResponse> pagedResourceAssemblerService) {
		this.clientService = clientService;
		this.apiResponseBuilder = apiResponseBuilder;
		this.pagedResourceAssemblerService = pagedResourceAssemblerService;
	}
	
	
	
	@Operation(summary = "get clients by searchQuery")
	@GetMapping("/{searchQuery}")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientResponse>>>> getClientsBySearchQuery(
								@PathVariable @NotBlank String searchQuery,
								@RequestParam(defaultValue ="0") int pageNumber,
								@RequestParam(defaultValue ="10") int pageSize,
								@RequestParam(required = false) String sortBy,
								@RequestParam(required = false) String direction){
		//if "sortBy" and "Direction" parameters is null then "Sort sort" will return null.
		Sort sort = SortBuilder.createSorting(direction, sortBy);		
		//if sort returns null, don't apply sorting in PageRequest/pageable.
		Pageable pageable = (sort==null)? PageRequest.of(pageNumber, pageSize) : PageRequest.of(pageNumber, pageSize, sort);
		Page<ClientResponse>  paginatedClients= clientService.findClientBySearchQuery(searchQuery, pageable);
		PagedModel<EntityModel<ClientResponse>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedClients);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
	}
	
	
	@Operation(summary = "get clients by firstName")
	@GetMapping("/firstName/{firstName}")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientResponse>>>> getClientsByFirstName(@PathVariable @NotBlank String firstName,
									@RequestParam(defaultValue = "0") int pageNumber,
									@RequestParam(defaultValue = "10") int pageSize,
									@RequestParam(required = false) String sortBy,
									@RequestParam(required = false) String direction){
		//if "sortBy" and "Direction" parameters is null then "Sort sort" will return null.
		Sort sort = SortBuilder.createSorting(direction, sortBy);		
		//if sort returns null, don't apply sorting in PageRequest/pageable.
		Pageable pageable = (sort==null)? PageRequest.of(pageNumber, pageSize) : PageRequest.of(pageNumber, pageSize, sort);
		Page<ClientResponse> paginatedclients = clientService.findClientsByFirstName(firstName, pageable);
		PagedModel<EntityModel<ClientResponse>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedclients);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
	}
	
	
	
	@Operation(summary = "get clients by lastName")
	@GetMapping("/lastName/{lastName}")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientResponse>>>> getClientsByLastName(@PathVariable @NotBlank String lastName,
									@RequestParam(defaultValue = "0") int pageNumber,
									@RequestParam(defaultValue = "10") int pageSize,
									@RequestParam(required = false) String sortBy,
									@RequestParam(required = false) String direction
									){
		//if "sortBy" and "Direction" parameters is null then "Sort sort" will return null.
		Sort sort = SortBuilder.createSorting(direction, sortBy);		
		//if sort returns null, don't apply sorting in PageRequest/pageable.
		Pageable pageable = (sort==null)? PageRequest.of(pageNumber, pageSize) : PageRequest.of(pageNumber, pageSize, sort);
		Page<ClientResponse> paginatedClients =  clientService.findClientsByLastName(lastName, pageable);
		PagedModel<EntityModel<ClientResponse>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedClients);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
	}
	
	
	
	@Operation(summary = "get clients by postalCode")
	@GetMapping("/postalCode/{postalCode}")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientResponse>>>> getClientsByPostalCode(@PathVariable @NotBlank String postalCode,
									@RequestParam(defaultValue = "0") int pageNumber,
									@RequestParam(defaultValue = "10") int pageSize,
									@RequestParam(required = false) String sortBy,
									@RequestParam(required = false) String direction
									){
		//if "sortBy" and "Direction" parameters is null then "Sort sort" will return null.
		Sort sort = SortBuilder.createSorting(direction, sortBy);		
		//if sort returns null, don't apply sorting in PageRequest/pageable.
		Pageable pageable = (sort==null)? PageRequest.of(pageNumber, pageSize) : PageRequest.of(pageNumber, pageSize, sort);
		Page<ClientResponse> paginatedClients = clientService.findClientsByPostalCode(postalCode, pageable);	
		PagedModel<EntityModel<ClientResponse>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedClients);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
	}
	
	
	
	
	//LASTLY BY SEARCH BY DATE OF BIRTH:
	
	
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
