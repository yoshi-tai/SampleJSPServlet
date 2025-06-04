package bean;
/**
 * 商品情報bean.
 * 
 */
public class ProductBean {
    // 商品ID
    private int PRODUCTID;
    // 商品コード
    private int PRODUCTCODE;
    // 商品名
    private String PRODUCTNAME;
    // 価格(単価)
    private int PRICE;
 // カテゴリID
    private int CATEGORYID;
    // カテゴリーネーム
    private String CATEGORYNAME;
    
    
    // getterとsetter
    public int getPRODUCTID() {
        return PRODUCTID;
    }
    public void setPRODUCTID(int pRODUCTID) {
        PRODUCTID = pRODUCTID;
    }
    public int getPRODUCTCODE() {
        return PRODUCTCODE;
    }
    public void setPRODUCTCODE(int pRODUCTCODE) {
        PRODUCTCODE = pRODUCTCODE;
    }
    public String getPRODUCTNAME() {
        return PRODUCTNAME;
    }
    public void setPRODUCTNAME(String pRODUCTNAME) {
        PRODUCTNAME = pRODUCTNAME;
    }
    public int getPRICE() {
        return PRICE;
    }
    public void setPRICE(int pRICE) {
        PRICE = pRICE;
    }
    public int getCATEGORYID() {
        return CATEGORYID;
    }
    public void setCATEGORYID(int cATEGORYID) {
        CATEGORYID = cATEGORYID;
    }
    public String getCATEGORYNAME() {
        return CATEGORYNAME;
    }
    public void setCATEGORYNAME(String cATEGORYNAME) {
        CATEGORYNAME = cATEGORYNAME;
    }
    
}
