package com.jhaps.clientrecords.util;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;




public class PageableUtils {

	
	
	
	
	public static Pageable createPageable(int pageNumber, int size, String sortBy, String direction) {
		
		/* These are the only allowed direction values. */
		List<String> validDirectionList = List.of("asc" , "ascending", "a", "desc", "descending", "d");
		
		/*
		 *  @direction : it is the parameter of the method: createPageable(String direction).
		 *	@validDirectionList : it contains a list of valid directions.
		 *  
		 *  @return : if the direction parameter exists inside the validDirectionList then it is valid.
		 */
		boolean isDirectionParameterValid = validDirectionList.stream().anyMatch(d-> d.equalsIgnoreCase(direction));
		
		
		/*if sortBy value is null/empty return pageable without sort. */
		if(sortBy==null || sortBy.isEmpty()) {
		Pageable pageableWithoutSorting = PageRequest.of(pageNumber, size);
		return pageableWithoutSorting;
		}
		
		/* creating default sortDirection. */
		Sort.Direction defaultSortDirection = Direction.ASC;
		Sort.Direction sortDirection = Direction.ASC;		
		/* If direction and sortBy value is present. */
		if(direction!=null && !direction.isEmpty()) {
			if(direction.equalsIgnoreCase("asc") || direction.equalsIgnoreCase("ascending") || direction.equalsIgnoreCase("a")) {
				sortDirection = Direction.ASC;
				return PageRequest.of(pageNumber, size, sortDirection, sortBy);
			}
			else if(direction.equalsIgnoreCase("desc") || direction.equalsIgnoreCase("descending") ||direction.equalsIgnoreCase("d")) {
				sortDirection = Direction.DESC;
				return PageRequest.of(pageNumber, size, sortDirection, sortBy);
			}
			else if(!isDirectionParameterValid) {
				throw new IllegalArgumentException("Invalid sortDirection Argument.");
			}
		}
		
		/* returns with sortBy and defaultSortDirection("Asc"). */
		Pageable pageable =  PageRequest.of(pageNumber, size, defaultSortDirection, sortBy);
		return pageable;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * Conditions are:
	 * 1.	If sortBy is null or Empty:  It will return Pageable without sorting.
	 * 
	 * 2.	If 'sortBy is present'  but 'sort-direction' is missing : Sorting will be done in ASCENDING order.
	 * 
	 * 3,	If 'sortBy' and 'sort-Direction' both are present : It will return the Pageable with  requested sorting.
	 * 			@Important: 
 * 							sort-Direction can be : ['asc','ascending' or 'a'] : this all returns ASCENDING.
	 * 											    ['desc','descending' or 'd'] : this all returns DESCENDING.
	 * */
	
//	public static Pageable createPageable( int pageNumber, int size,
//						String sortBy, String direction) {
//		
//		Sort.Direction sortDirection ;
//		
//		if(sortBy==null || sortBy.isEmpty()) {
//			Pageable pageableWithoutSorting = PageRequest.of(pageNumber, size);
//			return pageableWithoutSorting;
//		}
//		
//		/*	 If sortBy is given but direction is not given: Set Ascending as default. 	*/
//		if(  ( !sortBy.isEmpty() || sortBy!=null )	&&   (direction==null || direction.isEmpty())  ){
//			sortDirection = Direction.ASC; //default sortDirection is Asc.
//			Pageable pageable = PageRequest.of(pageNumber, size , sortDirection ,sortBy );
//			return pageable;
//		}//ends if
//		
//		
//		List<String> validDirectionList = List.of("asc" , "ascending", "a", "desc", "descending", "d");
//		// @direction : it is the parameter of the method: createPageable(String direction).
//		// @validDirectionList : it contains a list of valid directions.
//		boolean isDirectionValid = validDirectionList.stream().anyMatch(d-> d.equalsIgnoreCase(direction));
//		
//		/* If direction and sortBy value is present. */
//		if(sortBy!=null && direction!=null || sortBy==null && direction!=null) {
//			if(direction.equalsIgnoreCase("asc") || direction.equalsIgnoreCase("ascending") || direction.equalsIgnoreCase("a")) {
//				sortDirection = Direction.ASC;
//				return PageRequest.of(pageNumber, size, sortDirection, sortBy);
//			}
//			else if(direction.equalsIgnoreCase("desc") || direction.equalsIgnoreCase("descending") ||direction.equalsIgnoreCase("d")) {
//				sortDirection = Direction.DESC;
//				return PageRequest.of(pageNumber, size, sortDirection, sortBy);
//			}
//			else if(!isDirectionValid) {
//				throw new IllegalArgumentException("Invalid sortDirection Argument.");
//			}
//		}
//		
//		Pageable pageableWithoutSorting = PageRequest.of(pageNumber, size);
//		return pageableWithoutSorting;
//	}
		
		
	
		
	
	
	
	
	
}//ends class
