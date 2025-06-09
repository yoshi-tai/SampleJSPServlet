package bean;
/**
 * 商品情報bean.
 * 
 */
public class ProductBean {
    // 商品ID
    private int productId;
    // 商品コード
    private int productCode;
    // 商品名
    private String productName;
    // 価格(単価)
    private int price;
    // カテゴリID
    private int categoryId;
    // カテゴリーネーム
    private String categoryName;
    
    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public int getProductCode() {
        return productCode;
    }
    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
