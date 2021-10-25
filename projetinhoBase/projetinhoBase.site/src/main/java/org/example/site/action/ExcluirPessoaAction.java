package org.example.site.action;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.example.dao.PessoaDAO;
import org.example.entidade.Pessoa;
import org.example.hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcluirPessoaAction {

    private HttpServletRequest request = ServletActionContext.getRequest();
    private HttpServletResponse response = ServletActionContext.getResponse();

    @Action(value = "excluirPessoa", results = {
            @Result(name = "ok", location = "index.jsp"),
            @Result(name = "erro", type = "httpheader", params = {"status", "409"})
    })

    public String execute() {
        try {
            Session session = HibernateUtil.getSession();
            PessoaDAO pessoaDAO = new PessoaDAO(session);
            String pessoaBean = request.getParameter("pessoaBean");
            System.out.println(request.getAttribute("pessoaBean"));

            System.out.println("entrei em ExcluirPessoa");
//            pessoaDAO.excluir(pessoaBean());

            return "ok";

        } catch (Exception e) {
            e.printStackTrace();
            response.addHeader("erro", e.getMessage());
            return "erro";
        }
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
