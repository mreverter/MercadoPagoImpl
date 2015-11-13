package model;

import java.math.BigDecimal;

/**
 * Created by mreverter on 10/11/15.
 */
public class ItemView {

    private String description;
    private BigDecimal price;
    private String imgUrl;

    public ItemView(String description, BigDecimal price){
        this.setDescription(description);
        this.setPrice(price);
    }
    public ItemView(String description, BigDecimal price, String imgUrl){
        this.setDescription(description);
        this.setPrice(price);
        this.imgUrl = imgUrl;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
