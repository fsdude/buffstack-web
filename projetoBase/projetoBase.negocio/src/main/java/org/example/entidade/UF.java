package org.example.entidade;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class UF implements Serializable, Comparable<UF> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="nm_nome", length=100, nullable=false)
	private String nome;

	@Column(name="nm_sigla", length=2, nullable=false)
	private String sigla;

	// **************************** RELACIONAMENTOS *************************
	@ManyToOne(fetch = FetchType.LAZY)
	@ForeignKey(name="FK_Nacao_UF")
	@JoinColumn(name="fk_nacao", nullable = false)
	private Nacao nacao;
	
	@OneToMany(mappedBy="uf", fetch=FetchType.LAZY)
	private Set<Cidade> cidades;
	
	// **************************** CONTRUTORES *****************************
	public UF() {}

	public UF(Nacao nacao, String nome, String sigla) {
		this.setNacao(nacao);
		this.setNome(nome);
		this.setSigla(sigla);
	}
	
	// ****************** HASH, EQUALS, COMPARETO, TOSTRING *****************
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nacao == null) ? 0 : nacao.hashCode());
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
		UF other = (UF) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nacao == null) {
			if (other.nacao != null)
				return false;
		} else if (!nacao.equals(other.nacao))
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
	
	public int compareTo(UF o) {
		int compare = nacao.compareTo(o.getNacao());
		return compare != 0 ? compare : nome.compareTo(o.getNome());
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
	public String toStringCompleto() {
		return nacao.getNome()+"|"+nome;
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
		this.nome = sigla == null ? null : nome.trim();
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla == null ? null : sigla.trim();
	}

	public Nacao getNacao() {
		return nacao;
	}

	public void setNacao(Nacao nacao) {
		this.nacao = nacao;
	}

	public Set<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(Set<Cidade> cidades) {
		this.cidades = cidades;
	}
	
}
