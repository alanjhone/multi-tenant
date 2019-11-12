/**
 * 
 */
package br.com.dev.sysos.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.dao.EmpresaDao;
import br.com.dev.sysos.domain.administrativo.Empresa;
import br.com.dev.sysos.exceptions.NegocioException;
import br.com.dev.sysos.helpers.DBScriptRunner;
import br.com.dev.sysos.helpers.FlywayMigrate;
import br.com.dev.sysos.utils.FacesUtils;

/**
 * @author Alan Jhone
 *
 */

@Component
@Scope("session")
public class EmpresaMBean extends AbstractMBean<Empresa> {

	@Autowired
	private EmpresaDao empresaDao;
	
	private List<Empresa> empresas;
	
	public EmpresaMBean() {
		obj = new Empresa();
		empresas = new ArrayList<Empresa>();
	}
	
	/* --------------------------------- CONTROLE DE NAVEGAÇÂO DE PÁGINAS --------------------------------- */
	
	@Override
	protected String getDir() {
		return "/pages/empresa/";
	}

	public String entrarCadastro() {
		obj = new Empresa();
		return getDir() + "form_cadastro_empresa.xhtml?faces-redirect=true";
	}
	
	public String entrarListagem() {
		empresas = empresaDao.findAll();
		return getDir() + "list.xhtml?faces-redirect=true";
	}
	
	public String voltar() {
		obj = new Empresa();
		empresas = new ArrayList<Empresa>();
		
		return "/pages/home.jsf";
	}
	
	
	/* --------------------------------- VALIDAÇÃO/VERIFICAÇÃo DE DADOS --------------------------------- */
	
	public void validarEmpresa(Empresa empresa) throws NegocioException {
		if(empresaDao.findByCnpj(empresa.getCnpj()) != null) {
			throw new NegocioException("Já existe uma empresa com o CNPJ informado.");
		}
		
		if(obj.getNome().trim().equalsIgnoreCase("")) {
			throw new NegocioException("Preencha todos os campos!");
		}
		
	}
	
	public String cadastrar() {
		
		try {
			
			validarEmpresa(obj);
			
			//generateSchema(obj.getNome());
			
			generateMigration(obj.getNome());
			
			empresaDao.salvarOuAtualizar(obj);
			
			obj = new Empresa();
			FacesUtils.addMensagemSucesso("Cadastro realizado com sucesso");

			empresas = empresaDao.findAll();
			return getDir() + "list.jsf?faces-redirect=true";
			
		} catch (NegocioException e) {
			FacesUtils.addMensagemErro(e.getMessage());
		} catch (Exception e) {
			FacesUtils.addMensagemErro("Não foi possível realizar cadastro!");
		}
	
		
		return "";
	}
	
	public void generateMigration(String schema) {
		FlywayMigrate flyway = new FlywayMigrate();
		flyway.migrate(schema);

	}

	public void generateSchema(String schema) throws IOException, SQLException, NegocioException, ClassNotFoundException {
		DBScriptRunner db = new DBScriptRunner();
		db.createSchema(schema);
	}

	/**
	 * @return the empresas
	 */
	public List<Empresa> getEmpresas() {
		return empresas;
	}

	/**
	 * @param empresas the empresas to set
	 */
	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}
	
	
	
}
