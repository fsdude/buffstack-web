package org.example.entidade;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Sexo implements Serializable, Comparable<Sexo> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;	
	
	@Column(name="nm_nome", length=9, unique=true, nullable=false)
	private String nome;
	
	@Column(name="nm_sigla", length=1, nullable=false, unique=true)
	private String sigla;	
	
	// **************************** RELACIONAMENTOS *************************
	
	// **************************** CONTRUTORES *****************************
	public Sexo() {}
	
	public Sexo(String nome, String sigla) {
		this.setNome(nome);
		this.setSigla(sigla);
	}
	
	// ****************** HASH, EQUALS, COMPARETO, TOSTRING *****************
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((sigla == null) ? 0 : sigla.hashCode());
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
		Sexo other = (Sexo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (sigla == null) {
			if (other.sigla != null)
				return false;
		} else if (!sigla.equals(other.sigla))
			return false;
		return true;
	}
	
	public int compareTo(Sexo o) {
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
	
	public String getSigla() {
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla == null ? null : sigla.trim();
	}
	
}
