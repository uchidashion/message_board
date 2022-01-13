package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //_tokenのパラメータを取得
        String _token = request.getParameter("_token");

        //_tokenに値がセットされなかったりセッションIDと値が異なる場合はデータの登録できない
        if(_token != null && _token.equals(request.getSession().getId())) {

            //EntityManagerのインスタンスを生成
            EntityManager em = DBUtil.createEntityManager();
            //データの新規登録を始める
            em.getTransaction().begin();
            //Messageのインスタンスを生成
            Message m = new Message();
            //titleに受け取った値を格納
            String title = request.getParameter("title");
            m.setTitle(title);
            //contentに受け取った値を格納
            String content = request.getParameter("content");
            m.setContent(content);
            //Timestampに現在日時の情報を格納
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            m.setCreated_at(currentTime);
            m.setUpdated_at(currentTime);

            //データベースに保存
            em.persist(m);
            //データの新規登録の確定
            em.getTransaction().commit();
            //フラッシュメッセージを送信
            request.getSession().setAttribute("flush", "登録が完了しました。");
            //DBの接続を切断
            em.close();

            //index.Servletに遷移
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

}
