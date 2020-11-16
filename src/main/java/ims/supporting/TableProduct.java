package ims.supporting;

public class TableProduct {
    private String brand;
    private String invNum;
    private String givenBy;
    private String givenOn;

    public TableProduct(String brand, String invNum, String givenBy, String givenOn) {
        this.brand = brand;
        this.invNum = invNum;
        this.givenBy = givenBy;
        this.givenOn = givenOn;
    }

    public String getBrand() {
        return brand;
    }

    public String getInvNum() {
        return invNum;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public String getGivenOn() {
        return givenOn;
    }
}
