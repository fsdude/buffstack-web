package org.example.entidade;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Bairro implements Serializable, Comparable<Bairro> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Index(name="nm_nome")
	@Column(name="nm_nome", length=50, nullable=false)
	private String nome;
	
	// **************************** RELACIONAMENTOS *************************
	@ManyToOne(fetch = FetchType.LAZY)
	@ForeignKey(name="FK_Cidade_Bairro")
	@JoinColumn(name="fk_cidade", nullable=false)
	private Cidade cidade;
	
	// **************************** CONTRUTORES *****************************
	public Bairro() {}

	public Bairro(Cidade cidade, String nome) {
		this.setCidade(cidade);
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
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
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
		Bairro other = (Bairro) obj;
		if (id == null) {
			if (other.id!= null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	public int compareTo(Bairro o) {
		return nome.compareTo(o.getNome());
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
	public String getStringCompleto() {
		return "Cidade: " + this.cidade.getNome() +
				"\nBairro: " + nome;
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

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

}
