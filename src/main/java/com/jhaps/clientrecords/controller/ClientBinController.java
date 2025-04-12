package com.jhaps.clientrecords.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.response.ClientBinResponse;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.client.ClientBinService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client-bins")
@AllArgsConstructor
@Tag(name = "Client Bin API's", description = "Manage Deleted Clients(Restore, Get, Delete)")
public class ClientBinController {

	private ClientBinService clientBinService;
	private ApiResponseBuilder apiResponseBuilder;
	private PagedResourceAssemblerService<ClientBinResponse> pagedResourceAssembler;
	
	
	@GetMapping
	@Operation(summary = "Get all the Clients from ClientBin.")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientBinResponse>>>> getAllClientBin(
			@RequestParam(defaultValue="0") int pageNumber,
			@RequestParam(defaultValue="10") int pageSize,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String direction){
		Pageable pageable = PageableUtils.createPageable(pageNumber, pageSize, sortBy, direction);
		Page<ClientBinResponse> paginatedClient = clientBinService.getAllFromClientBin(pageable);
		PagedModel<EntityModel<ClientBinResponse>> paged = pagedResourceAssembler.toPagedModel(paginatedClient);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, paged);
	}
	
	
	@DeleteMapping("/{id}/delete")
	@PreAuthorize("hasAuthority('admin')") // only admin can delete the client from the bin
	@Operation(summary = "Delete the Selected client From ClientBin.")
	public ResponseEntity<ApiResponseModel<String>> deleteClientFromClientBin(@PathVariable int id){
		clientBinService.deleteFromClientBin(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.CLIENT_BIN_DELETED, HttpStatus.NO_CONTENT);
	}
	
	
	@PostMapping("/{id}/restore")
	@Operation(summary = "Restore the Selected client From ClientBin to Client")
	@PreAuthorize("hasAuthority('admin') or hasAuthority('user')")
	public ResponseEntity<ApiResponseModel<String>> restoreClientFromBin(@PathVariable int id){
		clientBinService.restoreFromClientBin(id);
		return apiResponseBuilder
					.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.ACCEPTED, "Client from ClientBin with id:"+ id + " restored.");
	}
	
	
	
}
