package com.wipro.bank.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BranchResponseDTO {
	
	private Long branchId;
	
	private String branchName;
	
	private String branchCode;
	
	private String ifscCode;
	
	private String address;
	

}