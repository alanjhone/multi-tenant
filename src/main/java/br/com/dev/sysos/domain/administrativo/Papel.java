package br.com.dev.sysos.domain.administrativo;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Entidade que representa os papeis no contexto comum de todo o sistema.
 *
 * @author Alan Jhone
 *
 */
@Entity
@Table(name="papel", schema = "public")
public class Papel extends AbstractEntity implements GrantedAuthority {

    private static final long serialVersionUID = 1982119540052947955L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAPEL")
    @SequenceGenerator(name = "SEQ_PAPEL", sequenceName = "public.id_seq_papel", allocationSize = 1)
    @Column(name = "id_papel")
    private int id;

    @Column(name = "descricao", columnDefinition = "varchar(500)", nullable = true)
    private String descricao;

    @Column(name = "codigo", columnDefinition = "varchar(500)", nullable = true)
    private String codigo;

    public static final int ROLE_ADMIN = 1;
    
    public static final int ROLE_USER = 2;
    
    public static final int ROLE_ADMIN_GERAL = 3;

    public Papel() {

    }

    public Papel(int id) {
        this.id = id;
    }

    public Papel(int id, String descricao) {
        super();
        this.id = id;
        this.descricao = descricao;
    }

    @Override
    public String getAuthority() {
        return codigo;
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}