package br.com.dev.sysos.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.context.RequestContext;


/**
 * Class uyils
 *
 * @author Alan Jhone
 *
 */

public class FacesUtils {

    /**
     * Método para executar algum comando javascript JSF
     * @param toExecute
     */
    public static void execute(String toExecute){
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute(toExecute);
    }

    /**
     * Método para adicionar mensagens de sucesso.
     * @param msg
     */
    public static void addMensagemSucesso(String msg){
        FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, fmsg);
    }

    /**
     * Método para adicionar mensagens de erro.
     * @param msg
     */
    public static void addMensagemErro(String msg){
        FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, fmsg);
    }

    /**
     * Método para adicionar mensagens de erro.
     * @param msg
     */
    public static void addMensagemErro(String clientId,String msg){
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }

    /**
     * Método para transformar uma coleção em lista de select itens.
     * @param col
     * @param idProperty
     * @param labelProperty
     * @return
     */
    public static List<SelectItem> toSelectItens(Collection<?> col, String idProperty, String labelProperty){
        try {
            List<SelectItem> result = new ArrayList<SelectItem>();
            for (Object obj : col) {
                String id = BeanUtils.getProperty(obj, idProperty);
                String label = BeanUtils.getProperty(obj, labelProperty);
                result.add(new SelectItem(id, label));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Limpa os dados dos componentes de edição e de seus filhos.
     *
     * Quando o método, por algum motivo, não funcionar, force a limpeza
     * e limpe o componente assim:
     * <p><blockquote><pre>
     * 	component.getChildren().clear()
     * </pre></blockquote>
     *
     */
    public static void cleanSubmittedValues(UIComponent component) {
        if (component instanceof EditableValueHolder) {
            EditableValueHolder evh = (EditableValueHolder) component;
            evh.setSubmittedValue(null);
            evh.setValue(null);
            evh.setLocalValueSet(false);
            evh.setValid(true);
        }
        if(component.getChildCount()>0){
            for (UIComponent child : component.getChildren()) {
                cleanSubmittedValues(child);
            }
        }
    }
    
    
	/**
	 * 
	 * Acessa o contexto externo do JavaServer Faces
	 * 
	 **/
	public static ExternalContext getExternalContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getExternalContext();
	}
    
	/**
	 * Retorna o request.
	 */
	public static HttpServletRequest getCurrentRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}
    
	/**
	 * Retonar a sessão corrente.
	 */
	public static HttpSession getCurrentSession() {
		return getCurrentRequest().getSession(true);
	}
	
}

