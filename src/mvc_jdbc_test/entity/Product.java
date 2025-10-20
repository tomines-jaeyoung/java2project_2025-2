package mvc_jdbc_test.entity;

public class Product {
    private String product_id;
    private String product_name;
    private String product_stock;
    private String product_price;
    private String product_manufacturer;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_stock() {
        return product_stock;
    }

    public void setProduct_stock(String product_stock) {
        this.product_stock = product_stock;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_manufacturer() {
        return product_manufacturer;
    }

    public void setProduct_manufacturer(String product_manufacturer) {
        this.product_manufacturer = product_manufacturer;
    }
}
