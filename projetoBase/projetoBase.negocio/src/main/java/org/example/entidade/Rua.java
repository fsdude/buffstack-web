package org.example.entidade;

import org.example.util.CassUtil;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Rua implements Serializable, Comparable<Rua> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="nm_nome", length=150, nullable=false)
	private String nome;
	
	@Column(name="nm_cep", length=8)
	private String cep;
	
	@Type(type="text")
	@Column(name="tx_sinonimos")
	private String sinonimos;
	
	// **************************** RELACIONAMENTOS *************************
	@ManyToOne(fetch = FetchType.LAZY)
	@ForeignKey(name="FK_Bairro_Rua")
	@JoinColumn(name="fk_bairro", nullable=false)
	private Bairro bairro;

	@ManyToOne(fetch = FetchType.LAZY)
	@ForeignKey(name="FK_Tipo_Rua")
	@JoinColumn(name="fk_tipo", nullable=false)
	private Tipo tipo;
	
	// **************************** CONTRUTORES *****************************
	public Rua() {}

	public Rua(Bairro bairro, Tipo tipo, String nome) {
		this.setBairro(bairro);
		this.setTipo(tipo);
		this.setNome(nome);
	}

	public Rua(Bairro bairro, Tipo tipo, String nome, String cep) {
		this.setBairro(bairro);
		this.setTipo(tipo);
		this.setNome(nome);
		this.setCep(cep);
	}

	public Rua(Bairro bairro, Tipo tipo, String nome, String cep, String sinonimos) {
		this.setBairro(bairro);
		this.setTipo(tipo);
		this.setNome(nome);
		this.setCep(cep);
		this.setSinonimos(sinonimos);
	}

	// ****************** HASH, EQUALS, COMPARETO, TOSTRING *****************
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		Rua other = (Rua) obj;
		if (bairro == null) {
			if (other.bairro != null)
				return false;
		} else if (!bairro.equals(other.bairro))
			return false;
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
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}
	
	public int compareTo(Rua o) {
		return nome.compareTo(o.getNome());
	}
	
	@Override
	public String toString() {
		return tipo.getNome() + " " + nome;
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

	public String getCep() {
		return cep;
	}

	public String getCepFormatado() {
		return CassUtil.mascarar(cep, "##.###-###");
	}

	public void setCep(String cep) {
		this.cep = cep == null ? null : CassUtil.removerMascara(cep);
	}

	public String getSinonimos() {
		return sinonimos;
	}

	public void setSinonimos(String sinonimos) {
		this.sinonimos = sinonimos == null ? null : sinonimos.trim();
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
}
