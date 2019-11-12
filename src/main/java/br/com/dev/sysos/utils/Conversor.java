/**
 * 
 */
package br.com.dev.sysos.utils;

/**
 * @author Alan Jhone
 *
 */

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.dev.sysos.domain.administrativo.AbstractEntity;

@FacesConverter("conversor")
public class Conversor implements Converter {

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null) {
			return this.getAttributesFrom(uic).get(value);
		}
		return null;
	}

	public String getAsString(FacesContext fc, UIComponent uic, Object object) {

		if (object != null && !"".equals(object)) {
			AbstractEntity entity = (AbstractEntity) object;
			if (entity.getId() > 0) {
				this.addAttribute(uic, entity);
				return String.valueOf(entity.getId());
			}
		}
		return "";
	}

	private void addAttribute(UIComponent component, AbstractEntity o) {
		this.getAttributesFrom(component).put(String.valueOf(o.getId()), o);
	}

	private Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}

}
