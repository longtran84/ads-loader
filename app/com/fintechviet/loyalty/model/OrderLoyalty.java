package com.fintechviet.loyalty.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fintechviet.user.model.User;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by tungn on 11/9/2017.
 */
@Entity
@Table(name = "order_loyalty")
public class OrderLoyalty {
    private long id;
    private User user;
    private Phonecard phoneCard;
    private Giftcode giftCode;
    private Gamecard gameCard;
    private Voucher voucher;
    private Integer quantity;
    private Double price;
    private Double total;
    private int totalPoint;
    private String customerName;
    private String address;
    private String phone;
    private String email;
    private String status = "NEW";
    private Timestamp createdDate;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "uid")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "phoneCardId")
    public Phonecard getPhoneCard() {
        return phoneCard;
    }

    public void setPhoneCard(Phonecard phoneCard) {
        this.phoneCard = phoneCard;
    }

    @ManyToOne
    @JoinColumn(name = "giftCodeId")
    public Giftcode getGiftCode() {
        return giftCode;
    }

    public void setGiftCode(Giftcode giftCode) {
        this.giftCode = giftCode;
    }

    @ManyToOne
    @JoinColumn(name = "gameCardId")
    public Gamecard getGameCard() {
        return gameCard;
    }

    public void setGameCard(Gamecard gameCard) {
        this.gameCard = gameCard;
    }

    @ManyToOne
    @JoinColumn(name = "voucherId")
    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    @Basic
    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "total")
    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Basic
    @Column(name = "totalPoint")
    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    @Basic
    @Column(name = "customerName")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "createdDate", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderLoyalty order = (OrderLoyalty) o;

        if (id != order.id) return false;
        if (quantity != null ? !quantity.equals(order.quantity) : order.quantity != null) return false;
        if (price != null ? !price.equals(order.price) : order.price != null) return false;
        if (total != null ? !total.equals(order.total) : order.total != null) return false;
        if (status != null ? !status.equals(order.status) : order.status != null) return false;
        if (createdDate != null ? !createdDate.equals(order.createdDate) : order.createdDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }
}
