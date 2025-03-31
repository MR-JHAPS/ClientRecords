package com.jhaps.clientrecords.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PageableUtils {

	private final Sort.Direction DEFAULT_DIRECTION = Direction.ASC;
	public static Pageable createPageable( int pageNumber, int size,
						String sortBy, String direction) {
		
		if(sortBy!=null && direction!=null) {
			return PageRequest.of(pageNumber, size,
					Sort.Direction.fromString(direction),
					sortBy);
		}
		return PageRequest.of(pageNumber, size);
		
		
	}
	
	
	
	
	
	
	
}//ends class
