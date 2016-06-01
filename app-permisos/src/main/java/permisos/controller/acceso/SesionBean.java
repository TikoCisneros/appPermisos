package permisos.controller.acceso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import permisos.model.external.Mensaje;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.manager.ManagerAcceso;

@ManagedBean
@SessionScoped
public class SesionBean implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	
    @EJB
	private ManagerAcceso mngAcs;
	
    private String usuLogin;
	private String usuPass;
	private AppUsuario sesion;
	private List<Menu> menu;
	
	public SesionBean() {
		menu = new ArrayList<Menu>();
	}

	/**
	 * @return the usuLogin
	 */
	public String getUsuLogin() {
		return usuLogin;
	}

	/**
	 * @param usuLogin the usuLogin to set
	 */
	public void setUsuLogin(String usuLogin) {
		this.usuLogin = usuLogin;
	}

	/**
	 * @return the usuPass
	 */
	public String getUsuPass() {
		return usuPass;
	}

	/**
	 * @param usuPass the usuPass to set
	 */
	public void setUsuPass(String usuPass) {
		this.usuPass = usuPass;
	}

	/**
	 * @return the sesion
	 */
	public AppUsuario getSesion() {
		return sesion;
	}

	/**
	 * @param sesion the sesion to set
	 */
	public void setSesion(AppUsuario sesion) {
		this.sesion = sesion;
	}

	/**
	 * @return the menu
	 */
	public List<Menu> getMenu() {
		return menu;
	}

	/**
	 * @param menu the menu to set
	 */
	public void setMenu(List<Menu> menu) {
		this.menu = menu;
	}
	
	/**
	 * Realiza el logeo en el sistema
	 * @return String
	 */
	public String login(){
            try {
                AppUsuario user = mngAcs.findUserByAliasPass(getUsuLogin(), getUsuPass());
                List<Menu> lstmenu = mngAcs.menuByUsrApp(user, "SGUP");
                if(lstmenu.isEmpty())
                        throw new Exception("Usted no posee acceso al sistema.");
                setMenu(lstmenu);
                setSesion(user);
                setUsuPass("");
                return "/views/index?faces-redirect=true";
            } catch (Exception e) {
                Mensaje.crearMensajeWARN(e.getMessage());
                return "";
            }
	}
	
	/**
	 * Permite deslogearse del sistema
	 * @return
	 */
	 public String logout(){
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.invalidate();
            setMenu(new ArrayList<Menu>());setSesion(null);setUsuLogin("");
            return "/index?faces-redirect=true";
         }
	 
	 /**
	  Verifica y devuelve el usuario en sesión
	  * @param vista página principal de acceso
	  * @return AppUsuario
	  */
	 public AppUsuario userSession(String vista){
		 HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	                .getExternalContext().getSession(false);
	     SesionBean user = (SesionBean) session.getAttribute("sesionBean");
	     if (user==null || user.getSesion() == null) {
	            try {
	                FacesContext.getCurrentInstance().getExternalContext().redirect("/app-permisos/index.xhtml");
	            } catch (IOException ex) {
	            	Mensaje.crearMensajeERROR(ex.getMessage());
	            }
	            return null;
	        } else {
	        	if(mngAcs.poseePermiso(user.getUsuLogin(), vista)){
		        	return user.getSesion();
	        	}else{
	        		try {
		                FacesContext.getCurrentInstance().getExternalContext().redirect("/app-permisos/views/index.xhtml");
		            } catch (IOException ex) {
		            	Mensaje.crearMensajeERROR(ex.getMessage());
		            }
		            return null;
	        	}
	        }
	 }

}
