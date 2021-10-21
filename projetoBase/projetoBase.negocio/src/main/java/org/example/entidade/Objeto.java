package org.example.entidade;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Objeto implements Serializable, Comparable<Objeto> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="nm_nome", length=50, nullable=false, unique=true)
	private String nome;
	
	// **************************** RELACIONAMENTOS *************************

	// **************************** CONTRUTORES *****************************
	public Objeto() {}

	public Objeto(String nome) {
		this.setNome(nome);
	}

	// ****************** HASH, EQUALS, COMPARETO, TOSTRING *****************
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Objeto other = (Objeto) obj;
		if (id == null) {
			if (other.id!= null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	public int compareTo(Objeto o) {
		return nome.compareTo(o.getNome());
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
	// **************************** GETS e SETs *****************************
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome == null ? null : nome.trim();
	}
	
}
