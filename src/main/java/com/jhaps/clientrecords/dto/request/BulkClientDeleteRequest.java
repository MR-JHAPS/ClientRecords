package com.jhaps.clientrecords.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkClientDeleteRequest {
	
	@NotEmpty(message = "clientDeleteMultiple: 'idList' cannot be empty.")
	@Size(min=1 , message = "clientDeleteMultiple: 'idList' must atleast contain 1 id.")
	private List<Integer> idList;
}
