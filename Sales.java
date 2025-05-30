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

public class Sales extends HttpServlet {
    /** デフォルト・シリアル・バージョンID */
    private static final long serialVersionUID = 1L;
    
    /**
     * doPostメソッドについてはテキスト74P,doGetメソッドのオーバーライドについてはテキスト51Pを参照
     */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,IOException{
            
        // 文字コードを設定(UTF-8)
        response.setContentType("text/plain; Charset=UTF-8");
        
        // DBに接続する(EmployeeBean.javaにアクセスして、必要な情報を取ってくるようにする文)
        List<EmployeeBean> salesList = new ArrayList<EmployeeBean>();
        
        try {
            // コネクションの取得
            InitialContext ic = new InitialContext();//InitialContextは接続設定を参照するためのクラス
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");//dsはデータソースオブジェクト
                                                //datasouceはデータベースへの出入口。この出入口を使って
            Connection con = ds.getConnection();//接続管理するためのコネクションクラス
                                                //connectionはデータベースとやり取りするための通路。
                                                //conはデータベースとの接続を表す
         // SQL発行
            String sql = 
                     "SELECT E.EMPLOYEENAME AS 従業員名, "
                   + "E.EMPLOYEEID, "
                   + "COALESCE(SUM(P.PRICE * S.QUANTITY), 0) AS 売上金額 "
                   + "FROM EMPLOYEES E "+"LEFT JOIN SALES S ON E.EMPLOYEEID = S.EMPLOYEEID "
                   + "LEFT JOIN PRODUCTS P ON S.PRODUCTID = P.PRODUCTID "
                   + "GROUP BY E.EMPLOYEEID,E.EMPLOYEENAME "
                   + "ORDER BY E.EMPLOYEEID ASC " ; 
                  
         // SQLの発行。作ったSQL文を、コンピュータに使う準備してねってお願いしてる文。
         // PreparedStatementのstにデバックのブレイクポイントを置くことによって、SQL文が正しく動いているかが分かる。
         PreparedStatement st = con.prepareStatement(sql);
            
         // SQLの取得結果をrsに設定。この命令を実行して！って伝えて、答えをもらってrsに代入している。
         ResultSet rs = st.executeQuery();//executeQueryはselect文を実行するためのメソッド。実行してからrsに入れてる
         
         // rsの中にある答えを1行ずつ見ていって、次のデータがあるかどうかをチェックして、あれば中の処理をするっていう意味。
         while (rs.next()) {
             // 参照画面Beanのインスタンス化。1人分の情報を入れる箱を作ってる。
             // SQL文で表示されるもの(名前とか売上金額)をここに入れていくための箱を作っている。
             EmployeeBean employeeBean = new EmployeeBean();
         
             // カラム名で取得してbeanに追加。
             // データベースから従業員名を取り出して、1個上の処理の名前をさっき作った箱(employeeBean)に入れている
             employeeBean.setEmployeeName(rs.getString("従業員名"));
             // データベースから売上金額を取り出して、2個上の処理の名前をさっき作った箱(employeeBean)に入れている
             employeeBean.setSalesSum(rs.getInt("売上金額"));
             
             /* 
              *SELECT文の中にある、E.EMPLOYEEIDは、ASCでEMPLOYEEID順にしたかっただけだから、
              *表示しない場合はここで取り出さなけれなければJSPで表示されることはない。
             */
             
             // リストにemployeeBeanを追加
             // 1個上(と2個上)の処理(employeeBean)をsalesListっていう大きい箱の中に集めて、追加していってる
             salesList.add(employeeBean);
         }
         
         // DBの切断処理
         st.close();
         con.close();       
        
         // もし上のほうのコードでエラー(失敗)が起きた時にどうするかを決める為の文。
        }catch(Exception e){
         /* 1個上の文章で止まった場合、どんなエラーが起きたかをコンピュータに表示させる為の文。*/
            e.printStackTrace();
        }
         // リクエストスコープとは、今回1度きりの画面表示のための情報入れ物のこと。
         // リクエストスコープに、社員リスト(aemployeeBeanを詰めたリスト)を設定
         // 呼び出しキーは"empList"
         // requestとは画面に渡す情報のこと。
        /* "salesList"っていう社員の売上データをいっぱい入れたリストをrequestにくっつけている。*/
        request.setAttribute("salesList", salesList);
      
     // セッションスコープに検索結果をセット
     // sessionとは、画面を変えてもデータを覚えておいてくれるもの。
     // sessionが働くように、する文。
        // sessionという特別な入れ物を使うかばん(入れ物)を準備をしている文。
        HttpSession session = request.getSession();
        // 1個上の処理で準備したかばんの中に、"saleList"というラベルをつけて、社員のデータをｓｅｓｓｉｏｎに入れるという文。
        session.setAttribute("saleList", salesList);
        
      //画面遷移処理
      // ここまでに作ったデータをもって、sales.jspページに行ってね！ってお願いしている。
        // どの画面(ページ)を見せるか決めている文。getRequestDispatcherでこの画面に行くよって誘ってる。
        RequestDispatcher rd = request.getRequestDispatcher("sales.jsp");
      // 　forward()は、リクエスト（お願いされたこと）とレスポンス(返す内容)をそのまま次の画面に表示すること。
        // このリクエストとデータをもって、sales.jspに言って表示して見せてあげてねと言ってる文。
        rd.forward(request, response);
      
    }//requestとresponseの2回目の定義の場所の括弧閉じ
    
    }//終わり【】