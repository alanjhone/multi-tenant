package br.com.dev.sysos.domain.administrativo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entidade que representa os usuários
 *
 * @author Alan Jhone
 *
 */

@Entity
@Table(name="usuario", schema = "public")
public class Usuario extends AbstractEntity implements UserDetails {

    private static final long serialVersionUID = -1370492530379526895L;

    /**
     * Construtor da classe
     */
    public Usuario() {
        pessoa = new Pessoa();
        usuarioEmpresas = new ArrayList<UsuarioEmpresa>();
    }
    
    /**
     * ID da entidade
     */
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_USUARIO")
    @SequenceGenerator(name="SEQ_USUARIO", sequenceName="public.id_seq_usuario", allocationSize=1)
    @Column(name="id_usuario")
    private int id;

    /**
     * Schema de Acesso
     */
    @Transient
    private String schemaAcesso;
    
    /**
     * Login do usuário
     */
    @Column(name="login", unique=true, nullable=false) 
    private String login;
    
    /**
     * Senha de acesso
     */
    private String senha;

    /**
     * Pessoa
     */
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "id_pessoa")
    private Pessoa pessoa;

    /**
     * Campo para confirmação da senha do usuário.
     */
    @Transient
    private String confirmacaoSenha;

    /**
     * Campo para nova da senha do usuário.
     */
    @Transient
    private String novaSenha;

    @Transient
    private UsuarioEmpresa usuarioEmpresa;
    
    @Column(columnDefinition = "boolean default true", insertable = false, updatable = true)
    private boolean ativo;

    @Column(name="solicitacao_acesso", columnDefinition = "boolean default false", insertable = true, updatable = true)
    private boolean solicitacaoAcesso;
    
    @OneToMany(mappedBy = "usuario", orphanRemoval = true)
    private List<UsuarioEmpresa> usuarioEmpresas;
 
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
	/**
	 * @return the solicitacaoAcesso
	 */
	public boolean isSolicitacaoAcesso() {
		return solicitacaoAcesso;
	}

	/**
	 * @param solicitacaoAcesso the solicitacaoAcesso to set
	 */
	public void setSolicitacaoAcesso(boolean solicitacaoAcesso) {
		this.solicitacaoAcesso = solicitacaoAcesso;
	}

	/**
	 * @return the schema_acesso
	 */
	public String getSchemaAcesso() {
		return schemaAcesso;
	}
	
	/**
	 * @param schema_acesso the schema_acesso to set
	 */
	public void setSchemaAcesso(String schemaAcesso) {
		this.schemaAcesso = schemaAcesso;
	}
	
	/**
	 * @return the usuarioEmpresas
	 */
	public List<UsuarioEmpresa> getUsuarioEmpresas() {
		return usuarioEmpresas;
	}

	/**
	 * @param usuarioEmpresas the usuarioEmpresas to set
	 */
	public void setUsuarioEmpresas(List<UsuarioEmpresa> usuarioEmpresas) {
		this.usuarioEmpresas = usuarioEmpresas;
	}

	@SuppressWarnings("unchecked")
	public List<Papel> getPermissoes(){
		List<Papel> permissoes = usuarioEmpresas.stream().flatMap(usuarioEmpresa -> usuarioEmpresa.getPermissoes().stream()).collect(Collectors.toList());
		if(permissoes != null && !permissoes.isEmpty())
			return permissoes;
					
		return Collections.EMPTY_LIST;
	}
	
	/**
	 * 
	 * Permissões do usuário de acordo com o tenant atual
	 * 
	 * @param 
	 */
	@SuppressWarnings("unchecked")
	public List<Papel> getPermissoesCurrentEmpresa(String schema){
		usuarioEmpresa = usuarioEmpresas.stream().filter(usuarioEmpresa -> usuarioEmpresa.getEmpresa().getNome().equals(schema)).findFirst().orElse(null);
		
		if(usuarioEmpresa.getPermissoes() != null && !usuarioEmpresa.getPermissoes().isEmpty())
			return usuarioEmpresa.getPermissoes();
					
		return Collections.EMPTY_LIST;
	}
	
	public List<Empresa> getEmpresas() {
		List<Empresa> empresas = usuarioEmpresas.stream().map(usuarioEmpresa -> usuarioEmpresa.getEmpresa()).collect(Collectors.toList());
		if(empresas != null && !empresas.isEmpty())
			return empresas;
					
		return Collections.EMPTY_LIST;
	}
	
	public void addUsuaEmpresa() {
		usuarioEmpresas.add(usuarioEmpresa);
	}

	
	
	/**
	 * @return the usuarioEmpresa
	 */
	public UsuarioEmpresa getUsuarioEmpresa() {
		return usuarioEmpresa;
	}

	/**
	 * @param usuarioEmpresa the usuarioEmpresa to set
	 */
	public void setUsuarioEmpresa(UsuarioEmpresa usuarioEmpresa) {
		this.usuarioEmpresa = usuarioEmpresa;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getPermissoes();
	}

	@Override
	public String getPassword() {
		return null;
	}


	@Override
	public String getUsername() {
		return login;
	}


	@Override
	public boolean isAccountNonExpired() {
		return false;
	}


	@Override
	public boolean isAccountNonLocked() {
		return false;
	}


	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}


	@Override
	public boolean isEnabled() {
		return false;
	}

}