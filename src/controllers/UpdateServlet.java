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
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //_tokenのパラメータを取得
        String _token = request.getParameter("_token");

        //_tokenに値がセットされなかったり、セッションIDと値が異なる場合はデータの登録ができない
        if(_token != null && _token.equals(request.getSession().getId())) {
            //EntityManagerのインスタンスを生成
            EntityManager em = DBUtil.createEntityManager();

            //セッションスコープからメッセージのIDを取得して
            //該当のIDのメッセージ一件のみをデータベースから取得
            Message m = em.find(Message.class, (Integer)(request.getSession().getAttribute("message_id")));

            //フォームの内容を各フィールドに上書き
            String title = request.getParameter("title");
            m.setTitle(title);

            String content = request.getParameter("content");
            m.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            m.setUpdated_at(currentTime);

            //データベースに登録を始める
            em.getTransaction().begin();
            //データベースの変更を確定する
            em.getTransaction().commit();
            //フラッシュメッセージを送信
            request.getSession().setAttribute("flush", "更新が完了しました。");
            //DBの接続切断
            em.close();

            //セッションスコープ上の不要になったデータの削除
            request.getSession().removeAttribute("message_id");

            //indexページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");
        }



    }

}
