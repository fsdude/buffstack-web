package org.example.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AutorizadorInterceptador implements Interceptor {

	private static final long serialVersionUID = 1L;

	public void destroy() {}

	public void init() {}

	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();

//		Pessoa pessoaLogada = (Pessoa) invocation.getInvocationContext().getSession().get("pessoaLogada");
//		if (pessoaLogada == null) {
//			response.setHeader("interceptador", "erro");
//			response.setHeader("erro", "Não logado.");
//			String uri = ServletActionContext.getRequest().getRequestURI();
//
//			if (uri.contains("administrativo"))
//				return "admNaoLogado";
//
//			if (uri.contains("corregedoria"))
//				return "corNaoLogado";
//
//			return "naoLogado";
//		}

		List<String> acoes = (List<String>) invocation.getInvocationContext().getSession().get("acoes");
		String acaoSolicitada = ActionContext.getContext().getName();

		if (acoes == null) {
			response.setHeader("interceptador", "erro");
			String uri = ServletActionContext.getRequest().getRequestURI();

			if (uri.contains("administrativo"))
				return "admNaoLogado";

			if (uri.contains("corregedoria"))
				return "corNaoLogado";

			return "naoLogado";

		} else if (!acoes.contains(acaoSolicitada)) {
			response.setHeader("interceptador", "erro");
			response.setHeader("erro", "Você não tem acesso a esse recurso.");
			response.setStatus(409);
			if (acaoSolicitada.contains("Modal"))
				return "desautorizadoModal";

			return "desautorizado";
		}

		response.setHeader("interceptador", "ok");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		return invocation.invoke();
	}
}
