/**
 * 
 */
package br.com.dev.sysos.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.dao.ProdutoDao;
import br.com.dev.sysos.domain.shared.Produto;
import br.com.dev.sysos.exceptions.NegocioException;
import br.com.dev.sysos.utils.FacesUtils;

/**
 * @author Alan Jhone
 *
 */

@Component
@Scope("session")
public class ProdutoMBean extends AbstractMBean<Produto> {
	
	@Autowired
	private ProdutoDao produtoDao;
	
	private List<Produto> produtos;
	
	public ProdutoMBean() {
		zerarObjs();
	}
	
	public void zerarObjs(){
		obj = new Produto();
		produtos = new ArrayList<Produto>();
	}
	
	public String entrarTelaCadastro(){
		zerarObjs();
		
		return getDir() + "form_cadastro_produto.jsf";
	}
	
	public String entrarTelaListagem(){
		zerarObjs();
		produtos = produtoDao.listar();
		return getDir() + "list.jsf";
	}
	
	@Override
	protected String getDir() {
		return "/pages/produto/";
	}
	
	public String voltar() {
		zerarObjs();
		
		return "/pages/home.jsf";
	}
	
	public void validarProduto(Produto produto) throws NegocioException {
		
		if(obj.getNome().trim().equals("")) {
			throw new NegocioException("todos os campos!");
		}
		
	}
	
	public String cadastrar() {
		
		try {
			validarProduto(obj);
			
			produtoDao.salvarOuAtualizar(obj);
			zerarObjs();
			
			FacesUtils.addMensagemSucesso("Operação realizada com sucesso!");
			
			produtos = produtoDao.listar();
			return getDir() + "list.jsf";
		} catch (NegocioException e) {
			FacesUtils.addMensagemErro(e.getMessage());
		} catch (Exception e) {
			FacesUtils.addMensagemErro("Não foi possível realizar cadastro!");
		}
		
		return "";
	}

	/**
	 * @return the produtos
	 */
	public List<Produto> getProdutos() {
		return produtos;
	}

	/**
	 * @param produtos the produtos to set
	 */
	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
	
	

}
