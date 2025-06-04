import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import bean.ProductBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class NewProduct extends HttpServlet {
    final private String CATEGORIESall ="SELECT * FROM CATEGORIES ";
    final private String insert ="Insert into TAIGA.PRODUCTS (PRODUCTID,PRODUCTCODE,PRODUCTNAME,PRICE,CATEGORYID) values (?,?,?,?,?)";
    final private String MaxId ="SELECT MAX(PRODUCTID) FROM PRODUCTS ";
    final private String select ="SELECT * FROM PRODUCTS WHERE PRODUCTID=? ";
//    final private String  ="";

    /* デフォルト・シリアル・バージョンID. */
    private static final long serialVersionUID = 1L;

    /**
     * doGetメソッド
     */
public void doGet(HttpServletRequest request,
       HttpServletResponse response) throws ServletException, IOException {
        // 文字コードを設定(UTF-8)
        response.setContentType("text/plain; charset=UTF-8");

        // DBに接続する(EmployeeBean.javaにアクセスして、必要な情報を取ってくるようにする文)
        List<ProductBean> productList = new ArrayList<ProductBean>();

        try {
            // コネクションの取得
            InitialContext ic = new InitialContext();// icは接続設定を参照するためのクラス
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");// dsはデータソースオブジェクト
            // datasouceはデータベースへの出入口。この出入口を使って
            Connection con = ds.getConnection();// 接続管理するためのコネクションクラス
                                                // connectionはデータベースとやり取りするための通路。
                                                // conはデータベースとの接続を表す

            // SQLの発行。作ったSQL文を、コンピュータに使う準備してねってお願いしてる文。
            // PreparedStatementのstにデバックのブレイクポイントを置くことによって、SQL文が正しく動いているかが分かる。
            PreparedStatement st = con.prepareStatement(CATEGORIESall);

            // SQLの取得結果をrsに設定。この命令を実行して！って伝えて、答えをもらってrsに代入している。
            ResultSet rs = st.executeQuery();// executeQueryはselect文を実行するためのメソッド。実行してからrsに入れてる

            // rsの中にある答えを1行ずつ見ていって、次のデータがあるかどうかをチェックして、あれば中の処理をするっていう意味。
                while (rs.next()) {
                    // 参照画面Beanのインスタンス化。1人分の情報を入れる箱を作ってる。
                    // SQL文で表示されるもの(名前とか売上金額)をここに入れていくための箱を作っている。
                    ProductBean employeeBean = new ProductBean();

                    // カラム名で取得してbeanに追加。
                    // データベースから従業員名を取り出して、1個上の処理の名前をさっき作った箱(employeeBean)に入れている
                    employeeBean.setCATEGORYNAME(rs.getString("CATEGORYNAME"));
                    // カラム名で取得してbeanに追加。
                    // データベースから従業員名を取り出して、1個上の処理の名前をさっき作った箱(employeeBean)に入れている
                    employeeBean.setCATEGORYID(rs.getInt("CATEGORYID"));

                /*
                 * SELECT文の中にある、E.EMPLOYEEIDは、ASCでEMPLOYEEID順にしたかっただけだから、 表示しない場合はここで取り出さなけれなければJSPで表示されることはない。
                 */

                    // リストにemployeeBeanを追加
                    // 1個上(と2個上)の処理(employeeBean)をsalesListっていう大きい箱の中に集めて、追加していってる
                    productList.add(employeeBean);
            }

            // DBの切断処理
            st.close();
            con.close();

            // もし上のほうのコードでエラー(失敗)が起きた時にどうするかを決める為の文。
        } catch (Exception e) {
            /* 1個上の文章で止まった場合、どんなエラーが起きたかをコンピュータに表示させる為の文。 */
            e.printStackTrace();

        }
    
        // リクエストスコープとは、今回1度きりの画面表示のための情報入れ物のこと。
        // リクエストスコープに、社員リスト(aemployeeBeanを詰めたリスト)を設定
        // 呼び出しキーは"empList"
        // requestとは画面に渡す情報のこと。
        /* "salesList"っていう社員の売上データをいっぱい入れたリストをrequestにくっつけている。 */
        HttpSession session = request.getSession();
        session.setAttribute("productList", productList);
    
        // 画面遷移処理
        // ここまでに作ったデータをもって、sales.jspページに行ってね！ってお願いしている。
        // どの画面(ページ)を見せるか決めている文。getRequestDispatcherでこの画面に行くよって誘ってる。
        RequestDispatcher rd = request.getRequestDispatcher("newProduct.jsp");
        // forward()は、リクエスト（お願いされたこと）とレスポンス(返す内容)をそのまま次の画面に表示すること。
        // このリクエストとデータをもって、sales.jspに言って表示して見せてあげてねと言ってる文。
        rd.forward(request, response);
    
    }// requestとresponseの1回目の定義の場所の括弧閉じ
    

    // ボタンが押されたとき（POSTされたとき）に動くメソッド（命令のかたまり）を始めるよ！
public void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException,IOException{
        // ① 文字コード設定
        response.setContentType("text/plain; Charset=UTF-8");
        
        /* 登録 */
        String botan = request.getParameter("botan");
        if ("登録".equals(botan)) {
            Insert(request,response);
        }
        
        /* 検索 */
        if ("検索".equals(botan)) {
            try { 
                InitialContext ic = new InitialContext();
                DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");// dsはデータソースオブジェクト
                
                    try (Connection con = ds.getConnection()){ // ここで{を始めればdbとconを省略できる。
                    PreparedStatement st = con.prepareStatement(select); // ?  
                    
                    // データベースの値をそれぞれの?にセットする
                    int id = Integer.parseInt(request.getParameter("ID"));
                    st.setInt(1, id);
                    
                    //実行
                     ResultSet rs = st.executeQuery();  
                    if (rs.next()) {
                        
                        request.setAttribute("IDKEY",rs.getInt("PRODUCTID"));
                        request.setAttribute("disableIDKEY","true");
                        
                        request.setAttribute("CODEKEY",rs.getInt("PRODUCTCODE"));
                        request.setAttribute("NAMEKEY",rs.getString("PRODUCTNAME"));
                        request.setAttribute("PRICEKEY",rs.getInt("PRICE"));
                        request.setAttribute("CATEGORYID",rs.getString("CATEGORYID"));
                        
                    }else {
                        request.setAttribute("MSGKEY", "検索結果が見つかりませんでした。");
                    }
            }
                    /* st.close();  con.close(); */
            } catch (Exception e) {
               e.printStackTrace();
               request.setAttribute("MSGKEY", "エラーが発生しました。");
            }
        }        
            RequestDispatcher dispatcher = request.getRequestDispatcher("newProduct.jsp");
            dispatcher.forward(request, response);
          }  

// Insertメソッドの中でやっていること
 public void Insert(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
            // コネクションの取得。データベースとつなげる先を用意して、接続しているよ。
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");
            Connection con = ds.getConnection();

            // フォームの値(画面の値)を取得する // JSPで受け取った値を代入
            String code = request.getParameter("CODE");
            String price = request.getParameter("PRICE");
            
            PreparedStatement st1 = con.prepareStatement(MaxId);
            ResultSet db =  st1.executeQuery();
            
            int nextId = 1;
            if(db.next() ) {
                nextId = db.getInt(1) + 1;
            }
            request.setAttribute("IDKEY", nextId);
            
            PreparedStatement st = con.prepareStatement(insert);
            // 入力チェック用のエラーメッセージ変数
            String errorMessage1 = code;
            String errorMessage2 = price;
            if(! num(code)){ // 文字列
                errorMessage1=  "コードは数字で入力してください（書き直してください）";
                request.setAttribute("errorMessage1", errorMessage1);
                regist(request,response);
                }
            if(! num(price)) { // 
                errorMessage2 =  "コードは数字で入力してください（書き直してください）";
                request.setAttribute("errorMessage2", errorMessage2);
                regist(request,response);
                }
            int productid = Integer.parseInt(request.getParameter("ID"));
            int productcode = Integer.parseInt(request.getParameter("CODE"));
            String productname = request.getParameter("NAME");
            int productprice = Integer.parseInt(request.getParameter("PRICE"));
            int productcategoryid = Integer.parseInt(request.getParameter("CATEGORYID"));
                // ５項目をセットする。
                st.setInt(1, productid); // ?の1番目のところに商品のIDをsetしている。
                st.setInt(2, productcode); // ?の2番目のところに商品のCODEをsetしている。
                st.setString(3, productname); // ?の3番目のところに商品のNAMEをsetしている。
                st.setInt(4, productprice); // ?の4番目のところに商品のをPRICEをsetしている。
                st.setInt(5, productcategoryid); // ?の5番目のところに商品のCATEGORYをsetしている。
            
             // SQLの実行をする文章。
            // st.executeQuery();　　　← SQL文を実行するときに使う関数。
            int rs =st.executeUpdate();  // ← INSERT, UPDATE, DELETE のときに使う関数。
            if(rs>0) {
                request.setAttribute("IDKEY",productid);
                request.setAttribute("CODEKEY",productcode);
                request.setAttribute("NAMEKEY",productname);
                request.setAttribute("PRICEKEY",productprice);
                request.setAttribute("CATEGORYID",productcategoryid);
                request.setAttribute("MSGKEY","登録しました");
                }
             // DBの切断
            st.close();
            con.close();
            
            } catch (Exception e) {
            // エラー処理
            e.printStackTrace();
            }
       
    }
 /*入力チェック*/
 private boolean num(String number) {
     try {
     Integer.parseInt(number); // 数字だったらtrue、文字列ならfalse
     }catch(Exception e) {
         return false;
     }
    return true;
 }  
 /*　登録時、エラーが出た時にフォームに値を残すためのメソッド　*/
 public void regist(HttpServletRequest request,
         HttpServletResponse response) throws ServletException, IOException {
     
     String productid = request.getParameter("ID");
     String productcode = request.getParameter("CODE");
     String productname = request.getParameter("NAME");
     String productprice = request.getParameter("PRICE");
     String productcategoryid = request.getParameter("CATEGORYID");
         
         request.setAttribute("IDKEY",productid);
         request.setAttribute("CODEKEY",productcode);
         request.setAttribute("NAMEKEY",productname);
         request.setAttribute("PRICEKEY",productprice);
         request.setAttribute("CATEGORYID",productcategoryid);
 }
} // 継承の部分の括弧閉じ
