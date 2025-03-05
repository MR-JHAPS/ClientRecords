package com.jhaps.clientrecords.service;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

/*
 * This class is responsible to convert a    Page<T> ---> Page<ClientDto> 
 * 													TO
 *											 PagedModel<T> --> PagedModel<EntityModel<ClientDto>>.
 * */
@Service
public class PagedResourceAssemblerService<T> {

	private final PagedResourcesAssembler<T> pagedResourceAssember;
	
	public PagedResourceAssemblerService(PagedResourcesAssembler<T> pagedResourceAssember) {
		this.pagedResourceAssember = pagedResourceAssember;
	}
	
	
	//Converts page<T> to PagedModel<EntityModel<T>>.
	public  PagedModel<EntityModel<T>> toPagedModel(Page<T> page) {
        return (PagedModel<EntityModel<T>>) pagedResourceAssember.toModel(page);
    }
	
	
	
}//ends class
