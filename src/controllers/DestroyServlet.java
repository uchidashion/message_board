package controllers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

/**
 * Servlet implementation class DestroyServlet
 */
@WebServlet("/destroy")
public class DestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //_tokenのパラメータを受け取る
        String _token = request.getParameter("_token");
        //_tokenが空、もしくはセッションIDと値が異なる場合はDBに接続できない
        if(_token != null && _token.equals(request.getSession().getId())) {

            //EntityManagerのインスタンスを生成
            EntityManager em = DBUtil.createEntityManager();

            //セッションスコープからメッセージのIDを取得して
            //該当のIDのメッセージ一件のみをデータベースから取得
            Message m = em.find(Message.class, (Integer)(request.getSession().getAttribute("message_id")));

            //DBの操作を開始
            em.getTransaction().begin();
            //データ削除
            em.remove(m);
            //変更した内容を確定
            em.getTransaction().commit();
            //DBの接続切断
            em.close();

            //セッションスコープ上の不要になったデータを削除
            request.getSession().removeAttribute("message_id");

            //indexページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

}