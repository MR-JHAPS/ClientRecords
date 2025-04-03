package com.jhaps.clientrecords.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BulkImageDeleteRequest {

	@NotEmpty(message = "ImagedeleteMultiple: 'idList' cannot be empty.")
	@Size(min=1 , message = "ImageDeleteMultiple: 'idList' must atleast contain 1 id.")
	private List<Integer> idList;
	
	
}
