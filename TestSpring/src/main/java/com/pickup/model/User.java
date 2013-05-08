package com.pickup.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "User")
public class User {

	@Id
	@Column(name = "id")
	private String id ;
	
	@Column( name="login")
	private String username ;
	
	@Column(name="password")
	private String password ;
	
	
	@Column(name="firstName")
	private String firstName;
	
	@Column( name="lastName")
	private String lastName;
	
	@ManyToOne
    @JoinColumn(name = "UserRoleId")
	private UserRole role ;
	
	
	@Column(name = "address1")
	private  String address1 ;
	
	
	@Column(name = "address2")
	private  String address2 ;
	
	
	@Column(name = "city")
	private  String city ;
	
	@Column(name = "state")
	private  String stat ;
	
	
	@Column(name = "zip")
	private  String zip ;
	
	@Column(name = "email")
	private  String email ;
	
	
	@Column(name = "phoneH")
	private  String phoneH ;
	
	@Column(name = "phoneW")
	private  String phoneW ;
	
	
	@Column(name = "phoneM")
	private  String phoneM ;
	
	
	
	@Transient
	private String state;
	
	@Transient
	private String stateDescription;
	
	
	@Transient 
	private String kidEventXrefId;
	
	@Transient 
	private String kidSchoolId;
	
	
	
	public String getKidSchoolId() {
		return kidSchoolId;
	}
	public void setKidSchoolId(String kidSchoolId) {
		this.kidSchoolId = kidSchoolId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneH() {
		return phoneH;
	}
	public void setPhoneH(String phoneH) {
		this.phoneH = phoneH;
	}
	public String getPhoneW() {
		return phoneW;
	}
	public void setPhoneW(String phoneW) {
		this.phoneW = phoneW;
	}
	public String getPhoneM() {
		return phoneM;
	}
	public void setPhoneM(String phoneM) {
		this.phoneM = phoneM;
	}
	public String getStateDescription() {
		return stateDescription;
	}
	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}
	public String getKidEventXrefId() {
		return kidEventXrefId;
	}
	public void setKidEventXrefId(String kidEventXrefId) {
		this.kidEventXrefId = kidEventXrefId;
	}
	
	
	
}
