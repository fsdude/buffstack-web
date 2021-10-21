package org.example.entidade;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Pessoa_Endereco implements Serializable, Comparable<Pessoa_Endereco> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="idFK")
	@GenericGenerator(name="idFK", strategy="foreign", parameters=@Parameter(name="property", value="pessoa"))
	@Column(name="fk_pessoa")
	private Integer id;
	
	@Index(name="nm_numero")
	@Column(name="nm_numero", length=6, nullable=false)
	private String numero;
	
	@Column(name="nm_complemento", length=100)
	private String complemento;
	
	@Index(name="nm_pontoReferencia")
	@Column(name="nm_pontoReferencia", length=100)
	private String pontoReferencia;
	
	// **************************** RELACIONAMENTOS *************************
	@OneToOne
	@ForeignKey(name="FK_Pessoa_Pessoa_Endereco")
	@PrimaryKeyJoinColumn
	private Pessoa pessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@ForeignKey(name="FK_Rua_Pessoa_Endereco")
	@JoinColumn(name="fk_rua", nullable=false)
	private Rua rua;
	
	// **************************** CONTRUTORES *****************************
	public Pessoa_Endereco() {}
	
	public Pessoa_Endereco(Pessoa pessoa, Rua rua, String numero, String complemento, String pontoReferencia) {
		this.setPessoa(pessoa);
		this.setRua(rua);
		this.setNumero(numero);
		this.setComplemento(complemento);
		this.setPontoReferencia(pontoReferencia);
	}
	
	// ****************** HASH, EQUALS, COMPARETO, TOSTRING *****************
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((complemento == null) ? 0 : complemento.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
		result = prime * result + ((pontoReferencia == null) ? 0 : pontoReferencia.hashCode());
		result = prime * result + ((rua == null) ? 0 : rua.hashCode());
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
		Pessoa_Endereco other = (Pessoa_Endereco) obj;
		if (complemento == null) {
			if (other.complemento != null)
				return false;
		} else if (!complemento.equals(other.complemento))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (pessoa == null) {
			if (other.pessoa != null)
				return false;
		} else if (!pessoa.equals(other.pessoa))
			return false;
		if (pontoReferencia == null) {
			if (other.pontoReferencia != null)
				return false;
		} else if (!pontoReferencia.equals(other.pontoReferencia))
			return false;
		if (rua == null) {
            return other.rua == null;
		} else return rua.equals(other.rua);
    }
	
	public int compareTo(Pessoa_Endereco o) {
		return pessoa.compareTo(o.getPessoa());
	}
	
	@Override
	public String toString() {
		return "Logradouro: " + rua.getNome() + ", " + numero +
				"\nComplemento: " + complemento +
				"\nPonto de referência: " + getPontoReferencia() +
				"\nBairro: " + rua.getBairro() +
				"\nCEP: " + rua.getCepFormatado() +
				"\nCidade: " + rua.getBairro().getCidade();
	}
	
	public String toStringCompleto() {
		return pessoa.getNome() + "\n" + toString();
	}

	// **************************** GETs e SETs *****************************
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero == null || numero.isEmpty() ? null : numero.trim();
	}
	
	public String getComplemento() {
		return complemento;
	}
	
	public void setComplemento(String complemento) {
		this.complemento = complemento == null || complemento.isEmpty() ? null : complemento.trim();
	}
	
	public String getPontoReferencia() {
		return pontoReferencia;
	}

	public void setPontoReferencia(String pontoReferencia) {
		this.pontoReferencia = pontoReferencia == null || pontoReferencia.isEmpty() ? null : pontoReferencia.trim();
	}

	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public Rua getRua() {
		return rua;
	}
	
	public void setRua(Rua rua) {
		this.rua = rua;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
