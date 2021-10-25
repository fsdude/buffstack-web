package org.example.site.action;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.example.anotacao.Acao;
import org.example.dao.PessoaDAO;
import org.example.entidade.Pessoa;
import org.example.hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Acao(nome = "Index", link = "",
        descricao = "Página inicial.")
public class IndexAction {

    private HttpServletRequest request = ServletActionContext.getRequest();
    private HttpServletResponse response = ServletActionContext.getResponse();
    List<Pessoa> pessoas;
    int pessoaId;
    String action = "liste";

    @Action(value = "",
            results = {
                    @Result(name = "ok", location = "paginaInicial.jsp"),
                    @Result(name = "erro", type = "httpheader", params = {"status", "409"})}
    )
    public String execute() {
        try {
            Session session = HibernateUtil.getSession();
            PessoaDAO pessoaDAO = new PessoaDAO(session);

            System.out.println(action);

            if (action.equals("liste")) {
                System.out.println("listando");
                this.pessoas = pessoaDAO.listar();
            } else if (action.equals("delete")) {
                System.out.println(pessoaId);
                pessoaDAO.delete(pessoaId);
                this.pessoas = pessoaDAO.listar();
            }


//            switch (action) {
//                case "delete":
//                    System.out.println(pessoa.getId());
//                    pessoaDAO.excluir(pessoa);
//                    break;
//                case "liste":
//
//                    break;
//            }
//			List<Map<String, Object>> pessoas = pessoaDAO.liste();
//
//			for (Map<String, Object> p : pessoas) {
//				System.out.println(p);
//			}
//			for (Pessoa p : pessoas) {
//				System.out.println(p);
//			}
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            response.addHeader("erro", e.getMessage());
            return "erro";
        }
    }

    // ****************************** GETs e SETs ******************************


    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public int getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(int pessoaId) {
        this.pessoaId = pessoaId;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
