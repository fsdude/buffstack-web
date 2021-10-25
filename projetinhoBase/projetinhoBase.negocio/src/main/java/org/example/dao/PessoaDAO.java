package org.example.dao;

import org.example.entidade.Pessoa;
import org.hibernate.Session;

import java.util.List;

public class PessoaDAO extends DAO {

    public PessoaDAO() {
    }

    public PessoaDAO(Session session) {
        super(session, Pessoa.class);
    }

    public List<Pessoa> listar() {
		return getSession().createCriteria(Pessoa.class)
				.list();
	}

}
