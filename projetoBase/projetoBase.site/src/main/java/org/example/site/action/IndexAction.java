package org.example.site.action;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.example.anotacao.Acao;
import org.example.dao.PessoaDAO;
import org.example.hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Acao(nome="Index", link="",
	descricao="Página inicial.")
public class IndexAction {
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	@Action(value="",
		results={
			@Result(name="ok", location="paginaInicial.jsp"),
			@Result(name="erro", type="httpheader", params={"status", "409"})}
	)
	public String execute() {
		try {
			Session session = HibernateUtil.getSession();
			PessoaDAO pessoaDAO = new PessoaDAO(session);

			List<Map<String, Object>> pessoas = pessoaDAO.liste();

			for (Map<String, Object> p : pessoas) {
				System.out.println(p);
			}

			return "ok";
			
		} catch (Exception e) {
			e.printStackTrace();
			response.addHeader("erro", e.getMessage());
			return "erro";
		}
	}
	
	// ****************************** GETs e SETs ******************************
}
