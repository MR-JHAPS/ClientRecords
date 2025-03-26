package com.jhaps.clientrecords.service;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

/*
 * 		---> ( pageable ) send as args in repository 
 * 			---> page<Entity> stores entity with pageable info
 * 				---> PagedModel<EntityModel<Entity>> contains "content, links, page"
 * 
 * 
 * This class is responsible to convert a :
 *  			  Page<T>  To  PagedModel<EntityModel<T>>
 * 	Example:
 * 			 Page<ClientDto>  To  PagedModel<EntityModel<ClientDto>>.
 * */
@Service
public class PagedResourceAssemblerService<T> {

	private final PagedResourcesAssembler<T> pagedResourceAssember;
	
	//Constructor
	public PagedResourceAssemblerService(PagedResourcesAssembler<T> pagedResourceAssember) {
		this.pagedResourceAssember = pagedResourceAssember;
	}
	
	
	/*	Method: Converts page<T> to PagedModel<EntityModel<T>>	*/
	public  PagedModel<EntityModel<T>> toPagedModel(Page<T> paginatedEntity) {
        return (PagedModel<EntityModel<T>>) pagedResourceAssember.toModel(paginatedEntity);
    }
	
	
	
}//ends class
