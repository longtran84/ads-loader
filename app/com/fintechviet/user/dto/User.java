package com.fintechviet.user.dto;

/**
 * Created by tungn on 9/17/2017.
 */
public class User {
    private String email;
    private String gender;
    private int dob;
    private String location;
    private String earning;
    private String inviteCode;
    private String inviteCodeUsed;
    
    public User(){
    	
    }

    public User(String email, String gender, int dob, String location, String earning, String inviteCode, String inviteCodeUsed) {
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.location = location;
        this.earning = earning;
        this.inviteCode = inviteCode;
        this.inviteCodeUsed = inviteCodeUsed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getDob() {
        return dob;
    }

    public void setDob(int dob) {
        this.dob = dob;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEarning() {
        return earning;
    }

    public void setEarning(String earning) {
        this.earning = earning;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInviteCodeUsed() {
        return inviteCodeUsed;
    }

    public void setInviteCodeUsed(String inviteCodeUsed) {
        this.inviteCodeUsed = inviteCodeUsed;
    }
}
