package org.example.entidade;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Pessoa {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nm_nome", length = 150, nullable = false)
    private String nome;

    @Column(name = "nm_cpf", length = 11, nullable = false)
    private String cpf;

    // ************* CONSTRUTORES ****************************************************************************

    public Pessoa(Integer id, String nome, String cpf){
        this.setId(id);
        this.setNome(nome);
        this.setCpf(cpf);
    }

    // ************* GETTERs e SETTERs ************************************************************************

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
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
