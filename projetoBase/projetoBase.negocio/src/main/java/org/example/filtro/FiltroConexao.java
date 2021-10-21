package org.example.filtro;

import org.example.hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.*;
import java.io.IOException;

public class FiltroConexao implements Filter {

    public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // CHEGADA DA SOLICITAÇÃO
        Session session = HibernateUtil.getSession();
        request.setAttribute("sessao", session);

        // SEGUE O FULXO NORMAL
        chain.doFilter(request, response);

        // RESPOSTA DA SOLICITAÇÃO
        session.close();
    }

    public void init(FilterConfig config) throws ServletException {}
}
