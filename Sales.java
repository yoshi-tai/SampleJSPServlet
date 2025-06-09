import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import bean.EmployeeBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Sales extends HttpServlet {
    /** デフォルト・シリアル・バージョンID */
    private static final long serialVersionUID = 1L;

    /**
     * doPostメソッドについてはテキスト74P,doGetメソッドのオーバーライドについてはテキスト51Pを参照
     */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // 文字コード
        response.setContentType("text/plain; Charset=UTF-8");
        
        List<EmployeeBean> salesList = new ArrayList<EmployeeBean>();

        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");
            Connection con = ds.getConnection();

            String sql = "SELECT E.EMPLOYEENAME AS 従業員名, " + "E.EMPLOYEEID, "
                    + "COALESCE(SUM(P.PRICE * S.QUANTITY), 0) AS 売上金額 "
                    + "FROM EMPLOYEES E "
                    + "LEFT JOIN SALES S ON E.EMPLOYEEID = S.EMPLOYEEID "
                    + "LEFT JOIN PRODUCTS P ON S.PRODUCTID = P.PRODUCTID "
                    + "GROUP BY E.EMPLOYEEID,E.EMPLOYEENAME "
                    + "ORDER BY E.EMPLOYEEID ASC ";

            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                EmployeeBean employeeBean = new EmployeeBean();
                employeeBean.setEmployeeName(rs.getString("従業員名"));
                employeeBean.setSalesSum(rs.getInt("売上金額"));
                salesList.add(employeeBean);
            }
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("salesList", salesList);

        // 画面遷移処理
        RequestDispatcher rd = request.getRequestDispatcher("sales.jsp");
        rd.forward(request, response);

    }
}
