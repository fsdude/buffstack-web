package org.example.entidade;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;
import org.example.util.CassUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

@Data
@NoArgsConstructor
@Entity
public class Pessoa implements Serializable, Comparable<Pessoa> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nm_cpf", length = 11, nullable = false)
    private String cpf;

    @Column(name = "nm_nome", length = 150, nullable = false)
    private String nome;

    @Column(name = "nm_nomeSocial", length = 150)
    private String nomeSocial;

    @Column(name = "dt_nascimento", nullable = false, columnDefinition = "date")
    private Calendar dataNascimento;

    // ************* RELACIONAMENTOS *********************************************************************************
    @OneToOne(mappedBy = "pessoa", cascade=CascadeType.ALL)
    private Pessoa_Endereco endereco;

    // ************* CONSTRUTOR **************************************************************************************

    /**
     * Constutor que necessita do nome e data de nascimento.
     * @param nome é uma String com o nome da pessoa
     * @param dataNascimento pode ser uma String ou um Calendar com a data de nascimento da pessoa.
     */
    public Pessoa(String nome, Calendar dataNascimento, String cpf) {
        this.setNome(nome);
        this.setDataNascimento(dataNascimento);
        this.setCpf(cpf);
    }

    public Pessoa(String nome, String dataNascimento, String cpf) {
        this.setNome(nome);
        this.setDataNascimento(dataNascimento);
        this.setCpf(cpf);
    }

    // ************* METODOS *****************************************************************************************
    @Override
    public int compareTo(Pessoa o) {
        int compare = nome.compareTo(o.getNome());
        return compare != 0 ? compare : dataNascimento.compareTo(o.getDataNascimento());
    }

    // ************* GETs e SETs *************************************************************************************
    @Tolerate
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = CassUtil.converterDataStringParaCalendar(dataNascimento);
    }

    public Integer getId() {
        return id;
    }

    public String getCpfFormatado() {
        return CassUtil.mascarar(cpf, "###.###.###-##");
    }

    public void setCpf(String cpf) {
        this.cpf = CassUtil.removerMascara(cpf);
    }
}
