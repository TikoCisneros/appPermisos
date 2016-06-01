package permisos.model.external;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import permisos.model.dao.entities.AppPerfiles;
import permisos.model.manager.ManagerPerfiles;

@ManagedBean
@RequestScoped
@FacesConverter("perfilConverter")
public class PerfilConverter implements Converter{
        
    @EJB
    ManagerPerfiles m;
        
	@Override
	public Object getAsObject(FacesContext fc, UIComponent ui, String valor) {
		if (valor != null && valor.trim().length() > 0) {
			try {
				return m.findPerfilByNombre(valor);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if(object != null)
			return String.valueOf(((AppPerfiles) object).getPerNombre());
		else
			return null;
	}

}
