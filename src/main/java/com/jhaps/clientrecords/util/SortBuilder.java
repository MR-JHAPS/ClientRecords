package com.jhaps.clientrecords.util;

import org.springframework.data.domain.Sort;

public class SortBuilder {

	
	
	public static Sort createSorting(String direction, String sortBy) {
		
		//if either direction or sortBy is null then return Sort as null.
		if(sortBy==null || direction == null) {
			return null;
		}
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")? Sort.Direction.ASC : Sort.Direction.DESC;
		return Sort.by(sortDirection, sortBy);
		
		
		
	}
	
	
	
	
	
	
	
}//ends class
