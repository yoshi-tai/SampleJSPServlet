import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import bean.EmployeeBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Update extends HttpServlet {

    /** デフォルト・シリアル・バージョンID */
    //private static final long serialVersionUID = 1L;
    
    /**
     * doPostメソッドについてはテキスト74P,doGetメソッドのオーバーライドについてはテキスト51Pを参照
     */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,IOException{
        // 文字コードを設定(UTF-8)
        response.setContentType("text/plain; Charset=UTF-8");
        
        // 文字列出力のためにPrintWriterクラスを使用する
        PrintWriter out = response.getWriter();
        
        // printWriterクラスのprintlnクラスを使用し、html上で文字列を出力する
        out.println("文字出力確認");
    }
    /**
     * Postメソッドでフォーム送信されたときに呼び出される.
     */
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,IOException{
        // 文字コードを設定(UTF-8)
        response.setContentType("text/plain; Charset=UTF-8");
        
        // JSP上にhiddenで持っているmodeの値を取得
        String mode = request.getParameter("mode");
        
        // doupdateのとき
        if("doupdate".equals(mode)) {
            update(request, response);
        } else {
        // リクエストデータからemployeeIDを取得
        int employeeId = Integer.parseInt(request.getParameter(
                "employeeID"));
        
        // セッションデータからreadページで詰めた社員リストを取得
        HttpSession session = request.getSession();
        List<EmployeeBean> readList = (List<EmployeeBean>) session
                .getAttribute("readList");
        
        // 更新画面で表示する社員情報クラスを作成
        EmployeeBean employeeBean = new EmployeeBean();
        
        // セッションデータの社員リストから、更新画面でボタンが押下された社員データを取得
        for(EmployeeBean items : readList) {
            if (items.getEmployeeId() == employeeId) {
                employeeBean.setEmployeeId(items.getEmployeeId());
                employeeBean.setEmployeeName(items.getEmployeeName());
                employeeBean.setHireFiscalYear(items.getHireFiscalYear());
                employeeBean.setEmail(items.getEmail());
            }
        }
        
        // リクエストスコープに、employeeBeanを設定、呼び出しキーは"updateKey"
        request.setAttribute("updateKey", employeeBean);
        
        // 画面遷移処理
        RequestDispatcher rd = request.getRequestDispatcher("update.jsp");
        rd.forward(request, response);
        }
    }
    void update(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
            // コネクションの取得
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");
            Connection con = ds.getConnection();
            
            // フォームの値(画面の値)を取得する
            int employeeId =
                    Integer.parseInt(request.getParameter("employeeID"));
            String email = request.getParameter("email");
            
            // SQL発行
            String sql = "UPDATE Employees "
                    + "SET email ='" + email + "' "
                    + "WHERE employeeID = " + employeeId;
            PreparedStatement st = con.prepareStatement(sql);
            
            // SQLの実行
            st.executeQuery();
            
            // DBの切断
            st.close();
            con.close();
            
        } catch (Exception e) {
            //エラー処理
            e.printStackTrace();
        }
        
        // 画面転移処理(readページに遷移)
        response.sendRedirect("read");
    }
}