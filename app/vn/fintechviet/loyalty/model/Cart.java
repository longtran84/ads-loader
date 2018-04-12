package vn.fintechviet.loyalty.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import vn.fintechviet.user.model.User;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by tungn on 11/9/2017.
 */
@Entity
@Table(name = "cart")
public class Cart {
    private long id;
    private User user;
    private Phonecard phoneCard;
    private Giftcode giftCode;
    private Gamecard gameCard;
    private Voucher voucher;
    private Integer quantity;
    private Double price;
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

        Cart cart = (Cart) o;

        if (id != cart.id) return false;
        if (quantity != null ? !quantity.equals(cart.quantity) : cart.quantity != null) return false;
        if (price != null ? !price.equals(cart.price) : cart.price != null) return false;
        if (status != null ? !status.equals(cart.status) : cart.status != null) return false;
        if (createdDate != null ? !createdDate.equals(cart.createdDate) : cart.createdDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }
}
