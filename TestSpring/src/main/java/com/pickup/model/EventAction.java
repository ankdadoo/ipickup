package com.pickup.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "EventActions")
public class EventAction {

	
	@Id
	@Column(name = "id")
	private String id ;
	
	@Column(name="description")
	private String description ;
	
	@Column(name="odrNbr")
	private Integer odrNbr;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getOdrNbr() {
		return odrNbr;
	}
	public void setOdrNbr(Integer odrNbr) {
		this.odrNbr = odrNbr;
	}
	
	
}
