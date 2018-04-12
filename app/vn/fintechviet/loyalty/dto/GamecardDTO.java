package vn.fintechviet.loyalty.dto;

/**
 * Created by tungn on 4/11/2018.
 */
public class GamecardDTO {
    private int id;
    private String name;
    private String image;
    private Integer price;
    private String status;
    private int pointExchange;
    private String pointExchangeText;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPointExchange() {
        return pointExchange;
    }

    public void setPointExchange(int pointExchange) {
        this.pointExchange = pointExchange;
    }

    public String getPointExchangeText() {
        return pointExchangeText;
    }

    public void setPointExchangeText(String pointExchangeText) {
        this.pointExchangeText = pointExchangeText;
    }
}
