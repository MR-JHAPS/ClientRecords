package com.jhaps.clientrecords.service.system;


import com.jhaps.clientrecords.dto.request.PdfUploadRequest;

public interface PdfFileService {

	void uploadPdfFile(int userId, PdfUploadRequest uploadRequest );
	
	void deletePdfFile(int userId, int pdfId);
	
	
	
}
