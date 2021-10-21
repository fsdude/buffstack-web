package org.example.dao;

import org.example.entidade.Sequencial;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class SequencialDAO extends DAO<Sequencial> {
	
	public SequencialDAO() {}
	
	public SequencialDAO(Session session) {
		super(session, Sequencial.class);
	}
	
	public Sequencial getSequencial(Integer x) {
		return (Sequencial) localizar(x);
	}
		
	public Sequencial getSequencialPorNome(String nome) {
		return (Sequencial) getSession().createCriteria(Sequencial.class)
				.add(Restrictions.eq("nome", nome))
				.uniqueResult();
	}
		
}