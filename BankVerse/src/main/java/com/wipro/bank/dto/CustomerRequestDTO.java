package com.wipro.bank.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CustomerRequestDTO {
	
   private String name;
	
	private String email;
	
	private String phnone;
	
	private String address;
	

}
