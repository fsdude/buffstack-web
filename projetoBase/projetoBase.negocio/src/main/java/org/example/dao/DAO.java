package org.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

public class DAO<T> {
	
	private Class<T> classe;
	private Session session;
	
	protected Session getSession() {
		return session;
	}
	
	public DAO() {}
	
	public DAO(Session session, Class classe) {
		this.session = session;
		this.classe = classe;
	}
	
	public void salvar(T objeto) {
		Transaction ts = session.beginTransaction();
		this.session.save(objeto);
		ts.commit();
	}
	
	public void excluir(T objeto) {
		Transaction ts = session.beginTransaction();
		this.session.delete(objeto);
		ts.commit();
	}
	
	public void atualizar(T objeto) {
		Transaction ts = session.beginTransaction();
		this.session.update(objeto);
		ts.commit();
	}
	
	public void mescle(T objeto) {
		Transaction ts = session.beginTransaction();
		this.session.merge(objeto);
		ts.commit();
	}
	
	public T localizar(Serializable obj) {
		T o = (T) this.session.get(classe, obj);
		return o; 
	}

	public String getQuery(String chave) {
		try {
			InputStream resourceAsStream = DAO.class.getClassLoader().getResourceAsStream("DAO/" + this.classe.getSimpleName().replace("DAO", "") + ".xml");
			Properties properties = new Properties();
			if (resourceAsStream == null) {
				return "XML para a classe " + this.classe.getSimpleName() + " ainda não foi criado";
			}
			properties.loadFromXML(resourceAsStream);
			return (String) properties.get(chave);

		} catch (IOException e) {
			e.printStackTrace();
			return "asdasd";
		}
	}
}