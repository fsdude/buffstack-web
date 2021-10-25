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

public class ListarPessoasAction {

    private HttpServletRequest request = ServletActionContext.getRequest();
    private HttpServletResponse response = ServletActionContext.getResponse();

    private List<Pessoa> pessoaList;

    @Action(value = "listarPessoas", results = {
            @Result(name = "ok", location = "index.jsp", type = "redirectAction"),
            @Result(name = "erro", type = "httpheader", params = {"status", "409"})
    })

    public String execute() {
        try {
            Session session = HibernateUtil.getSession();
            PessoaDAO pessoaDAO = new PessoaDAO(session);
//            Pessoa pessoa = new Pessoa(pessoaBean.getId(), pessoaBean.getNome(), pessoaBean.getCpf());

            System.out.println("entrei em listarPessoas");
            System.out.println(pessoaDAO.listar());
            pessoaList = pessoaDAO.listar();

//            pessoaDAO.salvar(pessoa);

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

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public List<Pessoa> getPessoaList() {
        return pessoaList;
    }

    public void setPessoaList(List<Pessoa> pessoaList) {
        this.pessoaList = pessoaList;
    }
}
