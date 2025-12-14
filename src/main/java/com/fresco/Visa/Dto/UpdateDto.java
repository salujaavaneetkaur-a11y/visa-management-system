package com.fresco.Visa.Dto;

public class UpdateDto {
	private String status;

	public UpdateDto(String status) {
		super();
		this.status = status;
	}
	public UpdateDto(){}

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
}