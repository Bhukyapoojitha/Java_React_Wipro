package com.wipro.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CustomerResponseDTO {
	
	private Long CustomerId;
	
   private String name;
	
	private String email;
	
	private String phone;
	
	private String address;
	

}
