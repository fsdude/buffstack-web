package org.example.dao;

import org.example.entidade.Pessoa;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class PessoaDAO extends DAO<Pessoa> {

	public PessoaDAO() {}

	public PessoaDAO(Session session) {
		super(session, Pessoa.class);
	}

	public List<Pessoa> listar(Integer quantidade) {
		return getSession().createCriteria(Pessoa.class)
				.setMaxResults(quantidade)
				.list();
	}

	public List<Pessoa> listar() {
		return getSession().createCriteria(Pessoa.class)
				.list();
	}

	public List<Pessoa> listarPorPeriodo(Calendar inicio, Calendar termino) {
		return getSession().createCriteria(Pessoa.class)
				.add(Restrictions.between("dataNascimento", inicio, termino))
				.list();
	}


	public List<Map<String, Object>> listePessoaPorPeriodo(Calendar inicio, Calendar termino) {
		return getSession().createCriteria(Pessoa.class)
				.add(Restrictions.between("dataNascimento", inicio, termino))
				.setProjection(Projections.projectionList()
				.add(Projections.id(), "id")
				.add(Projections.property("nome"), "nome"))
				.addOrder(Order.asc("nome"))
				.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
				.list();
	}

	public List<Map<String, Object>> liste() {
		return getSession().createCriteria(Pessoa.class)
				.setProjection(Projections.projectionList()
						.add(Projections.id(), "id")
						.add(Projections.property("cpf"), "cpf")
						.add(Projections.property("nome"), "nome")
						.add(Projections.property("nomeSocial"), "nomeSocial")
						.add(Projections.property("dataNascimento"), "dataNascimento"))
				.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
				.list();
	}
}
