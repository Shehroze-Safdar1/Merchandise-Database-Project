package ViewLogin;

public class Merchandise {
    private int merchandiseId;
    private String productName;
    private String manufacturer;
    private int model;
    private int year;
    private float price;
    private String sellBy;

    public Merchandise(int merchandiseId, String productName, String manufacturer, int model, int year, float price, String sellBy) {
        this.merchandiseId = merchandiseId;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.model = model;
        this.year = year;
        this.price = price;
        this.sellBy = sellBy;
    }

    public Merchandise() {

    }

    public int getMerchandiseId() {
        return this.merchandiseId;
    }

    public void setMerchandiseId(int merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getModel() {
        return this.model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSellBy() {
        return this.sellBy;
    }

    public void setSellBy(String sellBy) {
        this.sellBy = sellBy;
    }

}
