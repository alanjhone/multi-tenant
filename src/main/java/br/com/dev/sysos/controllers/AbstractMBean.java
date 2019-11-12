package br.com.dev.sysos.controllers;

import static br.com.dev.sysos.utils.FacesUtils.addMensagemSucesso;


import javax.activation.MimetypesFileTypeMap;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import br.com.dev.sysos.services.CrudService;
import br.com.dev.sysos.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dev.sysos.domain.administrativo.Papel;
import br.com.dev.sysos.domain.administrativo.Usuario;
import br.com.dev.sysos.exceptions.NegocioException;
import br.com.dev.sysos.exceptions.SegurancaException;


/**
 * Classe abstrata para CRUDs.
 *
 * @author Alan Jhone
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractMBean<T> {

    /**
     * Objeto a ser manipulado para cadastro.
     */
    protected T obj;

    /**
     * DataModel para construção das listagens.
     */
    protected DataModel<T> listagem;

    /**
     * Service transacional para persistância.
     */
    protected CrudService<T> service;

    /**
     * Objeto com o usuário logado no sistema.
     */
    protected Usuario usuarioLogado;

    /**
     * Service de usuario
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Método que retorna o diretório padrão a ser manipulado.
     *
     * @return
     */
    protected String getDir() {
        return "";
    }

    /**
     * @return the obj
     */
    public T getObj() {
        return obj;
    }

    /**
     * @param obj
     *            the obj to set
     */
    public void setObj(T obj) {
        this.obj = obj;
    }

    /**
     * @return the usuarioLogado
     */
    public Usuario getUsuarioLogado() {
        if(usuarioLogado == null){
    		usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        
        return usuarioLogado;
    }
    
    /**
     * @param usuarioLogado
     *
     */
    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    /**
     * Método que representa a ação realizada pelo botão de salvar.
     * @return
     */
    public String salvar() throws NegocioException {
        service.salvarOuAtualizar(obj);
        addMensagemSucesso("Dados salvos com sucesso.");
        return getDir() + "/list.jsf";
    }

    /**
     * Método que representa a ação realizada pelo botão de atualizar.
     * @return
     */
    public String atualizar() throws NegocioException{
        service.salvarOuAtualizar(obj);
        addMensagemSucesso("Dados atualizados com sucesso.");
        return getDir() + "/list.jsf";
    }

    /**
     * Método que representa a ação de levar o objeto ao form de alteração.
     * @return
     */
    public String alterar() throws NegocioException{
        obj = (T) listagem.getRowData();
        return getDir() + "/form.jsf";
    }

    /**
     * Método que representa a ação de remoção de um objeto.
     */
    public void remover() throws NegocioException{
        obj = (T) listagem.getRowData();
        service.remover(obj);
        addMensagemSucesso("Registro removido com sucesso.");
        listagem = null;
    }

    /**
     * Método que direciona o usuário para um novo cadastro.
     * @return
     */
    public String novo() {
        return getDir() + "/form.jsf";
    }

    /**
     * Método que direciona o usuário para a listagem.
     * @return
     */
    public String listar() {
        return getDir() + "/list.jsf";
    }

    /**
     * Método que direciona o usuário para o dashboard.
     * @return
     */
    public String menu() {
        return "/pages/dashboard.jsf";
    }

    /**
     * Método que realiza a busca para preenchimento do datamodel.
     *
     * @return
     */
    public DataModel<T> getListagem() {
        if (listagem == null) {
            listagem = new ListDataModel<T>(service.findAll((Class<T>) obj.getClass()));
        }
        return listagem;
    }

    /**
     * Método que retorna um objeto com detalhes do usuário logado.
     *
     * @return
     */
    protected UserDetails getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = null;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        }
        return userDetails;
    }

    /**
     * Método que retorna o login do usuário.
     * @return
     */
    public String getLoginUsuario() {
        return getUserDetails().getUsername();
    }

    /**
     * Método que retorna o login do usuário.
     * @return
     */
    public String getEmpresaAcesso() {
        return getUsuarioLogado().getSchemaAcesso();
    }
    
    /**
     * Método responsável por injetar o service responsável pelas operações com um objeto de domínio.
     */
    protected void setService(CrudService<T> service) {
        this.service = service;
    }


    /**
     * Método para verificação de papéis do usuário.
     * @param papeis
     * @throws SegurancaException
     */
    public void verificarPapeis(int... papeis) throws SegurancaException {
        boolean contemPapel = false;
        
        for (int i = 0; i < papeis.length; i++) {
            if(getUsuarioLogado().getPermissoes().contains(new Papel(papeis[i]))){
                contemPapel = true;
                break;
            }
        }

        if(!contemPapel)
            throw new SegurancaException("Usuário não possui permissão para acessar essa funcionalidade.");
    }

}
