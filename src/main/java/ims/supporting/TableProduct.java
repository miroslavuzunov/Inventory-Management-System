package ims.supporting;

public class TableProduct {
    private String brand = "Lenovo";
    private String invNum = "LL-IV-1231231231-1-10";
    private String givenBy = "Miroslav Uzunov";
    private String givenOn = "Today";

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
