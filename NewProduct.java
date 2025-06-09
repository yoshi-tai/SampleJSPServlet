import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import bean.ProductBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class NewProduct extends HttpServlet {
    final private String CATEGORIESALL = "SELECT * FROM CATEGORIES ";

    final private String INSERT = "Insert into TAIGA.PRODUCTS (PRODUCTID,PRODUCTCODE,PRODUCTNAME,PRICE,CATEGORYID) values (?,?,?,?,?)";

    final private String SELECT = "SELECT * FROM PRODUCTS WHERE PRODUCTID=? ";

    final private String UPDATE = "UPDATE PRODUCTS SET PRODUCTCODE= ?, PRODUCTNAME= ?, PRICE= ?, CATEGORYID= ? where PRODUCTID= ? ";

    final private String DELETE = "DELETE FROM PRODUCTS WHERE PRODUCTID= ?　";
    
    final private String MAXID = "SELECT MAX(PRODUCTID)FROM PRODUCTS" ;

    /* デフォルト・シリアル・バージョンID. */
    private static final long serialVersionUID = 1L;

    /**
     * doGetメソッド
     */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain; charset=UTF-8");
        
        List<ProductBean> productList = new ArrayList<ProductBean>();

        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");
            Connection con = ds.getConnection();
            
            //SQL発行と実行
            PreparedStatement st = con.prepareStatement(CATEGORIESALL);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                ProductBean productBean = new ProductBean();
                productBean.setCategoryName(rs.getString("categoryName"));
                productBean.setCategoryId(rs.getInt("categoryId"));
                productList.add(productBean);
            }
            st.close();
            con.close();
            
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        HttpSession session = request.getSession();
        session.setAttribute("productList", productList);
        // 画面遷移
        RequestDispatcher rd = request.getRequestDispatcher("newProduct.jsp");// ("searchProduct.jsp");
        rd.forward(request, response);
    }
    
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain; Charset=UTF-8");
        
        /* 登録 */
        String botan = request.getParameter("botan");
        if ("登録".equals(botan)) {
            Insert(request, response);
        }
        
        /* 検索 */
        if ("検索".equals(botan)) {
            try {
                InitialContext ic = new InitialContext();
                DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");

                try (Connection con = ds.getConnection()) { 
                    PreparedStatement st = con.prepareStatement(SELECT);
                    int id = Integer.parseInt(request.getParameter("ID"));
                    st.setInt(1, id);                    
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        request.setAttribute("IDKEY", rs.getInt("productId"));
                        request.setAttribute("CODEKEY", rs.getInt("productCode"));
                        request.setAttribute("NAMEKEY", rs.getString("productName"));
                        request.setAttribute("PRICEKEY", rs.getInt("price"));
                        request.setAttribute("CATEGORYID", rs.getString("categoryId"));
                        request.setAttribute("MSGKEY", "検索結果を取得しました");
                    } else {
                        request.setAttribute("MSGKEY", "検索結果が見つかりませんでした。");
                    }
                }
            } catch (SQLException | NamingException e) {
                e.printStackTrace();
            }
        }

        /* 更新 */
        if ("更新".equals(botan)) {
            try {
                InitialContext ic = new InitialContext();
                DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");

                try (Connection con = ds.getConnection()) {
                    PreparedStatement st = con.prepareStatement(UPDATE);
                    
                    int code = Integer.parseInt(request.getParameter("CODE"));
                    String name = request.getParameter("NAME");
                    int price = Integer.parseInt(request.getParameter("PRICE"));
                    int categoryId = Integer.parseInt(request.getParameter("CATEGORYID"));
                    int id = Integer.parseInt(request.getParameter("ID"));
                    
                    st.setInt(1, code);
                    st.setString(2, name);
                    st.setInt(3, price);
                    st.setInt(4, categoryId);
                    st.setInt(5, id);
                    
                    st.executeUpdate();
                    
                    request.setAttribute("IDKEY", id);
                    request.setAttribute("CODEKEY", code);
                    request.setAttribute("NAMEKEY", name);
                    request.setAttribute("PRICEKEY", price);
                    request.setAttribute("CATEGORYID", categoryId);
                    
                    request.setAttribute("MSGKEY", "更新しました");
                    request.setAttribute("updateKEY", "true");
                    request.setAttribute("deleteKEY", "true");
                }
            } catch (SQLException | NamingException e) {
                e.printStackTrace();
            }
        }

        if ("削除".equals(botan)) {
            try {
                InitialContext ic = new InitialContext();
                DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");
                try (Connection con = ds.getConnection()) { 
                    PreparedStatement st = con.prepareStatement(DELETE);
                    
                    int id = Integer.parseInt(request.getParameter("ID"));
                    st.setInt(1, id);
                    
                    st.executeUpdate();
                    
                    request.setAttribute("MSGKEY", "削除しました");
                    request.setAttribute("updateKEY", "true");
                    request.setAttribute("deleteKEY", "true");
                }
            } catch (SQLException | NamingException e) {
                e.printStackTrace();
            }
        }
        // 画面遷移
        RequestDispatcher dispatcher = request.getRequestDispatcher("newProduct.jsp"); // ("searchProduct.jsp");
        dispatcher.forward(request, response);
        
        
    }
    // 登録処理。入力チェック処理。
    public void Insert(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");
            Connection con = ds.getConnection();
            
            String code = request.getParameter("CODE");
            String price = request.getParameter("PRICE");
            
            PreparedStatement st1 = con.prepareStatement(MAXID);
            ResultSet db =  st1.executeQuery();
            
            int nextId = 1;
            if(db.next() ) {
                nextId = db.getInt(1) + 1;
            }
            request.setAttribute("IDKEY", nextId);
            
            PreparedStatement st = con.prepareStatement(INSERT);
            
            String errorMessage1 = code;
            String errorMessage2 = price;
            
            if (!num(code)) {
                errorMessage1 = "コードは数字で入力してください（書き直してください）";
                request.setAttribute("errorMessage1", errorMessage1);
            }
            
            if (!num(price)) { 
                errorMessage2 = "コードは数字で入力してください（書き直してください）";
                request.setAttribute("errorMessage2", errorMessage2);
            }
            
            int productId = Integer.parseInt(request.getParameter("ID"));
            int productCode = Integer.parseInt(request.getParameter("CODE"));
            String productName = request.getParameter("NAME");
            int productPrice = Integer.parseInt(request.getParameter("PRICE"));
            int productCategoryId = Integer.parseInt(request.getParameter("CATEGORYID"));
           
            st.setInt(1, productId);
            st.setInt(2, productCode);
            st.setString(3, productName);
            st.setInt(4, productPrice);
            st.setInt(5, productCategoryId);
            
            int rs = st.executeUpdate(); 
            if (rs > 0) {
                request.setAttribute("CODEKEY", productCode);
                request.setAttribute("NAMEKEY", productName);
                request.setAttribute("PRICEKEY", productPrice);
                request.setAttribute("CATEGORYID", productCategoryId);
                request.setAttribute("MSGKEY", "登録しました");
            }
            
            st.close();
            con.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

    }
    /* 入力チェック */
    private boolean num(String check) {
        if(check == null || check.isEmpty()) {   
            return false;
        }
        return check.matches("-?\\d+");
    }
}
