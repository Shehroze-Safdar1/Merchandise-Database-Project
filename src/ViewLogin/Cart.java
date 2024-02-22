package ViewLogin;

public class Cart {
    private String username;
    private int merchandiseId;
    private String productName;
    private String manufacturer;
    private int model;
    private int year;
    private float price;

    public Cart() {
    }

    public Cart(String username, int merchandiseId, String productName, String manufacturer, int model, int year, float price) {
        this.username = username;
        this.merchandiseId = merchandiseId;
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.price = price;
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

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
