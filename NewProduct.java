import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import bean.EmployeeBean;
import bean.ProductBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class NewProduct extends HttpServlet {

    /* デフォルト・シリアル・バージョンID. */
    private static final long serialVersionUID = 1L;

    /**
     * dogetメソッド
     */
    
public void doGet(HttpServletRequest request,
       HttpServletResponse response) throws ServletException, IOException {
        // 文字コードを設定(UTF-8)
        response.setContentType("text/plain; charset=UTF-8");

        // 文字出力の為にPrintWriterクラスをしようする
        PrintWriter out = response.getWriter();

        // PrintWriterクラスのprintlnクラスを使用しHTML上で文字列を出力
        out.println("ああああああ");

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

            // SQL発行
            String sql = "SELECT * " + "FROM CATEGORIES ";

            // SQLの発行。作ったSQL文を、コンピュータに使う準備してねってお願いしてる文。
            // PreparedStatementのstにデバックのブレイクポイントを置くことによって、SQL文が正しく動いているかが分かる。
            PreparedStatement st = con.prepareStatement(sql);

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
        request.setAttribute("productList", productList);
    
        // 画面遷移処理
        // ここまでに作ったデータをもって、sales.jspページに行ってね！ってお願いしている。
        // どの画面(ページ)を見せるか決めている文。getRequestDispatcherでこの画面に行くよって誘ってる。
        RequestDispatcher rd = request.getRequestDispatcher("newProduct.jsp");
        // forward()は、リクエスト（お願いされたこと）とレスポンス(返す内容)をそのまま次の画面に表示すること。
        // このリクエストとデータをもって、sales.jspに言って表示して見せてあげてねと言ってる文。
        rd.forward(request, response);
    
    }// requestとresponseの1回目の定義の場所の括弧閉じ
    
/* ここからfetchCategories()メソッドでデータベースから商品カテゴリー一覧を取得して、
 * productBeanのリストとして返す処理を行う。
*/
    // 商品カテゴリのデータをまとめて取ってくるお仕事を始めるよ。(メソッドの始まり)
private List<ProductBean> fetchCategories(){ // fetchCategoriesというメソッドを定義。
    // 後でカテゴリを入れるための、空っぽの入れ物を用意するよ。listはたくさんのデータをしまえる箱みたいなもの。
    // ProductBeanを入れるためのからのリストを作る
List<ProductBean> list = new ArrayList<>(); //データから読み込んだカテゴリ情報を順番に追加する。
    try { 
     // データベース(大きなデータの倉庫)に行く道を探すよ
        InitialContext ic = new InitialContext();
        // この道を使って、ちゃんとデータベースに入れるよ
        DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");// dsはデータソースオブジェクト
        // データベースに繋がっただから、、、
        try (Connection con = ds.getConnection();
             // CATEGORIESテーブルを見せて！ってお願いをする。
             PreparedStatement st = con.prepareStatement("SELECT * FROM CATEGORIES");
             
             ResultSet rs = st.executeQuery()) { // SQL文の入っているstを実行する。
            // データの数文まだある？って繰り返し聞いてある間はずっと繰り返す。
            while (rs.next()) {
                // カテゴリの情報を入れるための小さい箱(bean)を作る。
                ProductBean bean = new ProductBean();
                
                // beanという箱にカテゴリIDとNAMEを入れていく。
                bean.setCATEGORYID(rs.getInt("CATEGORYID"));
                bean.setCATEGORYNAME(rs.getString("CATEGORYNAME"));
                
                list.add(bean); // beanに入れた物をまとめてlistに収納していく。
            }
        }// エラーが起きた時の処理
    } catch (Exception e) {
        // 何かしっぱおしたたきはエラーが出たよってコンソールで見せてもらう。
        e.printStackTrace();
    }// たくさん集めた箱を、まとめて外に渡す。
    return list; // (ここでいう外は、collectData()メソッドを使った人に渡すイメージ。)
    
}// fetchCategories()メソッドのかっことじ

    /**
     *  フォームデータの取得
     */
// privateは同じクラスの中からしかこのメソッドは使えない。
// Map<String(たいが), String(りんご)> たいがとりんごの情報をセットにしてしまえる箱のこと。
// getFormDataという名前のメソッドを定義。フォームのデータを取ってくるやつ
// (HttpServletRequest request)はフォームから来たリクエスト(データ)を受け取る部分。
// requestという袋を使えるようにしている。
private Map<String, String> getFormData(HttpServletRequest request) {
    // Mapという二つの物をしまっておける箱(data)を作っている。
    Map<String, String> data = new HashMap<>();
    
    // フォームの中からそれぞれのデータを取り出して、dataの箱にしまっている。
    data.put("CODE", request.getParameter("CODE"));
    data.put("NAME", request.getParameter("NAME"));
    data.put("PRICE", request.getParameter("PRICE"));
    data.put("CATEGORYID", request.getParameter("CATEGORYID"));
    return data; //　dataをgetFormData()メソッドを呼び出しているところに値をreturnしている。
}

    /**
     * 入力チェック
     */
/* dataにはフォームデータを取得したものが入っている。
 * データの中身が正しいかどうかをチェックする
 *dataでもらった物を見て、間違ってたらエラーメッセージを返す。
 *Mapの形ごと返す*/
private Map<String, String> validateInput(Map<String, String> data) {
    // エラーメッセージを入れておく箱を作ってる。
    Map<String, String> errors = new HashMap<>();
    
    // detaの(CODEとPRICE)が数字じゃなかったら
    if (!num(data.get("CODE"))) {
        errors.put("errorMessage1", "コードは数字で入力してください（書き直してください）");
    }
    
    if (!num(data.get("PRICE"))) {
        errors.put("errorMessage2", "価格は数字で入力してください（書き直してください）");
    }
    
    // dataをvalidateInput()メソッドを呼び出しているところに値をreturnしている。
    return errors; 
}

    /**
     * データ登録処理
     */

/* INSERTデータっていう名前のメソッドを使う。
* insertDataメソッドは、requestとresponseを受け取っている。
throwsは、何かエラーが起きたらそのまま外に投げて教える。*/
private void insertData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 実装済みのInsertメソッドをここに呼び出す
    Insert(request, response); //フォームからのrequestとresponseを渡している。
}

    /**
     * jspへフォワード
     */
/* INSERTデータっていう名前のメソッドを使う。
 * forwardToJSPメソッドは、requestとresponseを受け取っている。
throwsは、何かエラーが起きたらそのまま外に投げて教える。*/
private void forwardToJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // dispatcherに(newProduct.jsp)をもったrequestを送っている。
    RequestDispatcher dispatcher = request.getRequestDispatcher("newProduct.jsp");
    dispatcher.forward(request, response); // 画面遷移
}



    /**
     * dopostメソッド
     */

    // ボタンが押されたとき（POSTされたとき）に動くメソッド（命令のかたまり）を始めるよ！
public void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException,IOException{
        // ① 文字コード設定
        response.setContentType("text/plain; Charset=UTF-8");

        // ② 入力データ取得 (getFormDataを呼び出して、処理後の結果をもらって、formDataに格納している) 
        Map<String, String> formData = getFormData(request);

        // ③ 入力チェック
        Map<String, String> errors = validateInput(formData);

        // ④ エラーメッセージがある場合、リクエストにセット errorsから1つずつエラーを取り出してerrorっていう箱にしまっていく。
        for (Map.Entry<String, String> error : errors.entrySet()) {
            request.setAttribute(error.getKey(), error.getValue()); //　requestに()の値が入ってるかどうかを判定。　ここでは何も入っていない。
        }
        
        // ⑤ フォームの値をリクエストにセット（再表示時にも必要）
        request.setAttribute("CODEKEY", formData.get("CODE"));
        request.setAttribute("NAMEKEY", formData.get("NAME"));
        request.setAttribute("PRICEKEY", formData.get("PRICE"));
        request.setAttribute("CATEGORY", formData.get("CATEGORYID"));

        // ⑥ カテゴリ情報をDBから取得して、リクエストに追加
        List<ProductBean> productList = fetchCategories();
        request.setAttribute("productList", productList);
        
        // errorMessage1かerrorMessage2があるかどうか調べている。
        if(!errors.containsKey("errorMessage1") && !errors.containsKey("errorMessage2")) {
            // なかった場合この処理をやる
            
            // ⑦ データ登録（Insert） 
            insertData(request, response);

            // ⑧ メッセージをセットしてJSPにフォワード
            request.setAttribute("MSGKEY", "登録しました");
          }    
        forwardToJSP(request, response);
    }


    /**postで呼び出している
     * insertメソッド
     */
// numという名前のメソッドを使う。
// input１つを受け取って、それが数字かどうかを調べる。
// 結果として数字ならtrue、違うならfalseを返す。
public boolean num(String input){
    try {   // どっちかがエラーの場合。両方OKじゃないとエラーになる。
        Integer.parseInt(input); // 数字ならOK。空白じゃなくても。
        return true; // trueを呼び出してるvalidateInput()メソッドの中のif文に返す
        
        } catch (NumberFormatException e) {
        return false; 
    }
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
            
            int CODE = Integer.parseInt(request.getParameter("CODE"));   // JSPで受け取った値を代入
            String NAME = request.getParameter("NAME");                  // JSPで受け取った値を代入
            int PRICE = Integer.parseInt(request.getParameter("PRICE")); // JSPで受け取った値を代入
            int CATEGORY = Integer.parseInt(request.getParameter("CATEGORYID")); // JSPで受け取った値を代入
           

            //　ＳＱＬ発行 IDの自動採番
            // PRODUCTIDの最大値を取得する。一番大きい値を取得してDBに代入している。
            String DB = "SELECT MAX(PRODUCTID) FROM PRODUCTS ";
            // DBをst1に格納
            PreparedStatement st1 = con.prepareStatement(DB);
            // SQLの実行 st1を実行してdbに代入
            ResultSet db =  st1.executeQuery();
           
            // NEXTIDの初期値を1に設定。　変数の初期化。
            int nextId = 1;
            // db.next()でデータベースの答えを読み、結果があるかをチェックしている。
            if(db.next() ) {
            // db.getInt(1)は最大の番号を取り出している。これに+1をしている。それをnextIdに代入している。
                nextId = db.getInt(1) + 1;
            }
            // 次に使う商品IDの番号をJSPに渡して、画面表示するという意味
            // 次の商品番号を"naxtId"という名前で紙にメモをするという意味。
            request.setAttribute("nextId", nextId);
            // 1つ上の処理のメモをもって、商品を入力するページに行ってねという意味。
 //           request.getRequestDispatcher("/newProduct.jsp");
            
            // 求めたnextIdをIDという変数に代入する。そしたらINSERTする際の主キーとすることができるようになる。
            int ID = nextId; // javaで保持したいときのために代入している。

            
            // SQL発行 テーブル登録。新しい商品を、PRODUCTSテーブルに追加するよというSQL文。
            String sql = "Insert into TAIGA.PRODUCTS (PRODUCTID,PRODUCTCODE, "
                    + "PRODUCTNAME,PRICE,CATEGORYID) values (?,?,?,?,?)";
            // 変数sqlを実行する準備をしている。PreparedStatementは安全に、しかも高速にSQLを実行するための特別なクラス。
            PreparedStatement st = con.prepareStatement(sql);
            
            // ５項目をセットする。
            st.setInt(1, ID); // ?の1番目のところに商品のIDをsetしている。
            st.setInt(2, CODE); // ?の2番目のところに商品のCODEをsetしている。
            st.setString(3, NAME); // ?の3番目のところに商品のNAMEをsetしている。
            st.setInt(4, PRICE); // ?の4番目のところに商品のをPRICEをsetしている。
            st.setInt(5, CATEGORY); // ?の5番目のところに商品のCATEGORYをsetしている。

            // SQLの実行をする文章。
            // st.executeQuery();　　　← SQL文を実行するときに使う関数。
            st.executeUpdate();  // ← INSERT, UPDATE, DELETE のときに使う関数。
            
             // DBの切断
            st.close();
            con.close();
            
            } catch (Exception e) {
            // エラー処理
            e.printStackTrace();
            }
       
    }

   
}// 継承の部分の括弧閉じ
