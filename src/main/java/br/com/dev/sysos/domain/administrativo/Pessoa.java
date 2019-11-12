package br.com.dev.sysos.domain.administrativo;

import javax.persistence.*;
import java.util.Date;

/**
 * Entidade que representa as pessoas no contexto comum de todo o sistema.
 *
 * @author Alan Jhone
 *
 */
@Entity
@Table(name="pessoa", schema = "public")
public class Pessoa extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * ID da entidade pessoa
     */
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_PESSOA")
    @SequenceGenerator(name="SEQ_PESSOA", sequenceName="public.id_seq_pessoa", allocationSize=1)
    @Column(name="id_pessoa")
    private int id;

    /**
     * Nome da pessoa
     */
    @Column(nullable=false, columnDefinition="varchar(180)")
    private String nome;

    /**
     * CPF da pessoa
     */
    @Column
    private String cpf;

    /**
     * Data de nascimento da pessoa
     */
    @Column(name = "data_nascimento", columnDefinition="DATE", nullable=true)
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    /**
     * Sexo da pessoa
     * I = Indefinido
     * M = Masculino
     * F = Feminino
     *
     */
    @Column(columnDefinition="char(1) default 'I'")
    private char sexo;

    /**
     * E-mail da pessoa
     */
    private String email;

    /**
     * Celular da pessoa
     */
    @Column(columnDefinition="varchar(25)")
    private String celular;

    /**
     * Atributo de controle para indicar se a pessoa está ativa ou não
     * TRUE  = ativo
     * FALSE = inativo
     */
    @Column(columnDefinition="boolean default true", insertable=false, updatable=true)
    private boolean ativo;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
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

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}