package permisos.model.external;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import permisos.model.dao.entities.AppUsuario;
import permisos.model.manager.ManagerAutorizacion;

@ManagedBean
@RequestScoped
@FacesConverter("usuarioConverter")
public class UsuarioConverter implements Converter {
        
    @EJB
	ManagerAutorizacion m;
    
    @Override
	public Object getAsObject(FacesContext fc, UIComponent ui, String valor) {
		if (valor != null && valor.trim().length() > 0) {
			try {
				return m.findUserByAlias(valor);
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
			return String.valueOf(((AppUsuario) object).getUsuLogin());
		else
			return null;
	}

}
