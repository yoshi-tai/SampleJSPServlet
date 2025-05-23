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
import jakarta.servlet.http.HttpSession;

public class Read extends HttpServlet {
    /** デフォルト・シリアル・バージョンID. */
    private static final long serialVersionUID = 1L;
    
    // doPostメソッドについてはテキスト74P、doGetメソッドのオーバーライドについてはテキスト51Pを参照
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,IOException { //dogetメソッドでrequestとresponceを引数として渡している
                                                                                //throwsで例外クラス1と2を呼び出している。(throws 例外クラス名1,例外クラス名2)
        // 文字コードを設定
        response.setContentType("text/plain; charset=UTF-8");
        
        // jsp表示用のEmployeeBeanオブジェクトのリストを作成
        List<EmployeeBean> employeeList = new ArrayList<EmployeeBean>();
        
        // DBアクセス(テキスト193P～参照)
        // DBの社員情報を取得する
        
        try {
            // コネクションの取得
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");//datasouceはデータベースへの出入口。この出入口を使って
            Connection con = ds.getConnection();//connectionはデータベースとやり取りするための通路。
                                                //conはデータベースとの接続を表す
            
            // SQL発行
            String sql = "SELECT * FROM Employees";
            PreparedStatement st = con.prepareStatement(sql);//prepareStatementはjavaで安全・効率的に実行するためのクラス。
            
            // SQLの取得結果をrsに設定
            ResultSet rs = st.executeQuery();//executeQueryはselect文を実行するためのメソッド。実行してからrsに入れてる
            
            while (rs.next()) {//（）この括弧の中にtrueかfalseが入っている
                                //ｒｓがｔｒｕｅだった場合、、、
                // 参照画面Beanのインスタンス化
                EmployeeBean employeeBean = new EmployeeBean();
                
                // 社員ID、社員名、入社年、eメールを取得してbeanに設定
                employeeBean.setEmployeeId(rs.getInt("employeeId"));//ｇｅｔｔｅｒで値を取り出している
                employeeBean.setEmployeeName(rs.getString("employeeName"));
                employeeBean.setHireFiscalYear(rs.getInt("hireFiscalYear"));
                employeeBean.setEmail(rs.getString("email"));
                
                // リストにemployeeBeanを追加
                employeeList.add(employeeBean);
            }
            
            // DBの切断処理
            st.close();
            con.close();
            
        } catch (Exception e) {
            // エラー処理
            e.printStackTrace();
        }
        
        // リクエストスコープに、社員リスト(aemployeeBeanを詰めたリスト)を設定
        // 呼び出しキーは"empList"
        request.setAttribute("empList", employeeList);
        
        // セッションスコープに検索結果をセット
        HttpSession session = request.getSession();
        session.setAttribute("readList", employeeList);
        
        //画面遷移処理
        RequestDispatcher rd = request.getRequestDispatcher("read.jsp");
        rd.forward(request, response);
        
//        // EmployeeBeanのインスタンス化
//        EmployeeBean employeeBean = new EmployeeBean();
//        
//        // EmployeeBeanのemployeeName(社員名)プロパティに値を設定する
//        employeeBean.setEmployeeName("葛城");
//        
//        request.setAttribute("empInfo",employeeBean);
//        
//        // JSPに画面遷移するための処理(テキストP128～参照)
//        RequestDispatcher rd = request.getRequestDispatcher("read.jsp");
//        rd.forward(request, response);
        
        //サンプル課題1の1シート目
//        // 文字コードを設定(UTF-8)
//        response.setContentType("text/plain; charset-UTF-8");
//        
//        //文字列出力の為にPrintWriterクラスを使用する
//        PrintWriter out  = response.getWriter();
//        
//        // PrintWriterクラスのprintlnメソッドを使用し、html上で文字列を出力する
//        out.println("好きな文字列を入力する");
        
    }
}
