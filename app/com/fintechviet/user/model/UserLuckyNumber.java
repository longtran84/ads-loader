package com.fintechviet.user.model;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "user_mobile_lucky_number", schema = "mobileads", catalog = "")
public class UserLuckyNumber {

    private Long id;
    private long userMobileId;
    private long luckyNumber;
    private Date startDateWeek;
    private Date endDateWeek;
    private Date createDate;
    private String status;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_mobile_id")
    public long getUserMobileId() {
        return userMobileId;
    }

    public void setUserMobileId(long userMobileId) {
        this.userMobileId = userMobileId;
    }

    @Basic
    @Column(name = "lucky_number")
    public long getLuckyNumber() {
        return luckyNumber;
    }

    public void setLuckyNumber(long luckyNumber) {
        this.luckyNumber = luckyNumber;
    }

    @Basic
    @Column(name = "start_date_week")
    @Temporal(TemporalType.DATE)
    public Date getStartDateWeek() {
        return startDateWeek;
    }

    public void setStartDateWeek(Date startDateWeek) {
        this.startDateWeek = startDateWeek;
    }

    @Basic
    @Column(name = "end_date_week")
    @Temporal(TemporalType.DATE)
    public Date getEndDateWeek() {
        return endDateWeek;
    }

    public void setEndDateWeek(Date endDateWeek) {
        this.endDateWeek = endDateWeek;
    }

    @Basic
    @Column(name = "create_date")
    @Temporal(TemporalType.DATE)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
