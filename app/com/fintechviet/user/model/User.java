package com.fintechviet.user.model;

import com.fintechviet.content.model.NewsCategory;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by tungn on 9/12/2017.
 */
@Entity
@Table(name = "user_mobile")
public class User {
	private long id;
	private String deviceToken;
	private String email;
	private String gender;
	private Integer dob;
	private String location;
	private long earning;
	private String status = "ACTIVE";
	private Date createdDate;
	private Set<NewsCategory> newsCategories;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "deviceToken")
	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}


	@Basic
	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Basic
	@Column(name = "gender")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Basic
	@Column(name = "dob")
	public Integer getDob() {
		return dob;
	}

	public void setDob(Integer dob) {
		this.dob = dob;
	}

	@Basic
	@Column(name = "location")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Basic
	@Column(name = "earning")
	public long getEarning() {
		return earning;
	}

	public void setEarning(long earning) {
		this.earning = earning;
	}

	@Basic
	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Basic
	@Column(name = "createdDate", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@ManyToMany
	@JoinTable(name = "user_mobile_interest", joinColumns = @JoinColumn(name = "uid"), inverseJoinColumns = @JoinColumn(name = "newsCategoryId"))
	public Set<NewsCategory> getNewsCategories() {
		return newsCategories;
	}

	public void setNewsCategories(Set<NewsCategory> newsCategories) {
		this.newsCategories = newsCategories;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User that = (User) o;

		if (id != that.id) return false;
		if (earning != that.earning) return false;
		if (email != null ? !email.equals(that.email) : that.email != null) return false;
		if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
		if (dob != null ? !dob.equals(that.dob) : that.dob != null) return false;
		if (location != null ? !location.equals(that.location) : that.location != null) return false;
		if (status != null ? !status.equals(that.status) : that.status != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (gender != null ? gender.hashCode() : 0);
		result = 31 * result + (dob != null ? dob.hashCode() : 0);
		result = 31 * result + (location != null ? location.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		return result;
	}
}
