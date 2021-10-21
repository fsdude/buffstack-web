package org.example.entidade;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Tipo implements Serializable, Comparable<Tipo> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer id;

	@Column(name="nm_nome", length=100, nullable=false)
	@Expose
	private String nome;
	
	// **************************** RELACIONAMENTOS *************************
	@ManyToOne(fetch=FetchType.LAZY)
	@ForeignKey(name="FK_Objeto_Tipo")
	@JoinColumn(name="fk_objeto", nullable = false)
	private Objeto objeto;
	
	// **************************** CONTRUTORES *****************************
	public Tipo() {}

	public Tipo(Objeto objeto, String nome) {
		this.setObjeto(objeto);
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
		result = prime * result + ((objeto == null) ? 0 : objeto.hashCode());
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
		Tipo other = (Tipo) obj;
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
		if (objeto == null) {
            return other.objeto == null;
		} else return objeto.equals(other.objeto);
    }
	
	public int compareTo(Tipo o) {
		int compare = objeto.compareTo(o.getObjeto());
		return compare != 0 ? compare : nome.compareTo(o.getNome());
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
	public String toStringCompleto() {
		return objeto.getNome()+"|"+nome;
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
		this.nome = nome == null ? null :nome.trim();
	}

	public Objeto getObjeto() {
		return objeto;
	}

	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}

}
