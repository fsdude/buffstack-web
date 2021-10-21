package org.example.entidade;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Cidade implements Serializable, Comparable<Cidade> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="nm_nome", length=150, nullable=false)
	private String nome;
	
	@Column(name="nm_codigoIBGE", length=10, nullable=false)
	private String codigoIBGE;
	
	@Column(name="bl_capital", nullable=false, columnDefinition="tinyint")
	private boolean capital;
	
	// **************************** RELACIONAMENTOS *************************
	@ManyToOne(fetch=FetchType.LAZY)
	@ForeignKey(name="FK_UF_Cidade")
	@JoinColumn(name="fk_uf", nullable=false)
	private UF uf;
	
	@OneToMany(mappedBy="cidade")
	private Set<Bairro> bairros;
	
	// **************************** CONTRUTORES *****************************
	public Cidade() {}

	public Cidade(UF uf, String nome, String codigoIBGE, boolean capital) {
		this.setUf(uf);
		this.setNome(nome);
		this.setCodigoIBGE(codigoIBGE);
		this.setCapital(capital);
	}

	// ****************** HASH, EQUALS, COMPARETO, TOSTRING *****************
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoIBGE == null) ? 0 : codigoIBGE.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((uf == null) ? 0 : uf.hashCode());
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
		Cidade other = (Cidade) obj;
		if (codigoIBGE == null) {
			if (other.codigoIBGE != null)
				return false;
		} else if (!codigoIBGE.equals(other.codigoIBGE))
			return false;
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
		if (uf == null) {
			if (other.uf != null)
				return false;
		} else if (!uf.equals(other.uf))
			return false;
		
		return true;
	}

	public int compareTo(Cidade o) {
		int compare = uf.compareTo(o.getUf());
		return compare != 0 ? compare : nome.compareTo(o.getNome());
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
	public String toStringCompleto() {
		return uf.getNome() + "|" + nome;
	}
	
	// **************************** GETS e SETs *****************************
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome == null ? null : nome.trim();
	}

	public String getCodigoIBGE() {
		return codigoIBGE;
	}

	public void setCodigoIBGE(String codigoIBGE) {
		this.codigoIBGE = codigoIBGE == null ? null : codigoIBGE;
	}

	public boolean isCapital() {
		return capital;
	}

	public String getCapitalFormartada() {
		return capital ? "SIM" : "NÃO";
	}

	public void setCapital(boolean capital) {
		this.capital = capital;
	}

	public UF getUf() {
		return uf;
	}

	public void setUf(UF uf) {
		this.uf = uf;
	}
	
	public Set<Bairro> getBairros() {
		return bairros;
	}

	public void setBairros(Set<Bairro> bairros) {
		this.bairros = bairros;
	}
}
