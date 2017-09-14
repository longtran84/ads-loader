package com.fintech.adl.model;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user_mobile")
public class User {
    private Long id;
    private String deviceToken;
    private String email;
    private String location;
    private Long earning;
    private String status;
    private Date createdDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getEarning() {
		return earning;
	}

	public void setEarning(Long earning) {
		this.earning = earning;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
