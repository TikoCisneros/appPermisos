package permisos.controller.gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import permisos.controller.acceso.SesionBean;
import permisos.model.dao.entities.AppAplicaciones;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Mensaje;
import permisos.model.manager.ManagerAutorizacion;

@ManagedBean
@ViewScoped
public class AutorizacionBean implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8875002942668875098L;
	
    @EJB
	private ManagerAutorizacion mngAut;
	
    private AppUsuario usuario;
	private String appIdLibre;
	private String appIdAsig;
	private List<AppAplicaciones> appsLibres;
	private List<AppAplicaciones> appsAsig;
	private AppUsuario sesion;
	
    @Inject
    SesionBean sesionBean;
        
	public AutorizacionBean() {
            
	}
        
    @PostConstruct
    public void init(){
        sesion = sesionBean.userSession("autorizacion.xhtml");
        appsLibres = new ArrayList<AppAplicaciones>();
        appsAsig = new ArrayList<AppAplicaciones>();
        appIdLibre = "";
        appIdAsig = "";
    }
	
	public AppUsuario getSesion() {
		return sesion;
	}
	
	/**
	 * @return the usuario
	 */
	public AppUsuario getUsuario() {
		return usuario;
	}


	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(AppUsuario usuario) {
		this.usuario = usuario;
	}


	/**
	 * @return the appsLibres
	 */
	public List<AppAplicaciones> getAppsLibres() {
		return appsLibres;
	}
	
	/**
	 * @return the appIdLibre
	 */
	public String getAppIdLibre() {
		return appIdLibre;
	}

	/**
	 * @param appIdLibre the appIdLibre to set
	 */
	public void setAppIdLibre(String appIdLibre) {
		this.appIdLibre = appIdLibre;
	}

	/**
	 * @return the appIdAsig
	 */
	public String getAppIdAsig() {
		return appIdAsig;
	}

	/**
	 * @param appIdAsig the appIdAsig to set
	 */
	public void setAppIdAsig(String appIdAsig) {
		this.appIdAsig = appIdAsig;
	}

	/**
	 * @param appsLibres the appsLibres to set
	 */
	public void setAppsLibres(List<AppAplicaciones> appsLibres) {
		this.appsLibres = appsLibres;
	}

	/**
	 * @return the appsAsig
	 */
	public List<AppAplicaciones> getAppsAsig() {
		return appsAsig;
	}

	/**
	 * @param appsAsig the appsAsig to set
	 */
	public void setAppsAsig(List<AppAplicaciones> appsAsig) {
		this.appsAsig = appsAsig;
	}
	
	/**
	 * Carga los datos de las listas al seleccionar un usuario
	 */
	public void cargarDatosUsuario(){
		setAppsLibres(mngAut.findAllAppsLibres(getUsuario().getUsuLogin()));
		setAppsAsig(mngAut.findAllAppsUsr(getUsuario().getUsuLogin()));
	}
	
	/**
	 * Permite asignar una aplicación
	 */
	public void crearAsignacion(){
		try {
			if(getUsuario()==null){
				Mensaje.crearMensajeWARN("No ha seleccionado un usuario.");
			}else if(appIdLibre.isEmpty()){
				Mensaje.crearMensajeWARN("No ha seleccionado una aplicación.");
			}else{
				mngAut.insertarAutorizacion(getAppIdLibre(), getUsuario().getUsuLogin());
				cargarDatosUsuario();
				setAppIdLibre("");
			}
		} catch (Exception e) {
			Mensaje.crearMensajeERROR(e.getMessage());
		}
	}
	
	/**
	 * Permite quitar una asignación existente
	 */
	public void quitarAsignacion(){
		try {
			if(getUsuario()==null){
				Mensaje.crearMensajeWARN("No ha seleccionado un usuario.");
			}else if(appIdAsig.isEmpty()){
				Mensaje.crearMensajeWARN("No ha seleccionado una aplicación.");
			}else{
				mngAut.quitarAutorizacion(getAppIdAsig(), getUsuario().getUsuLogin());
				cargarDatosUsuario();
				setAppIdAsig("");
			}
		} catch (Exception e) {
			Mensaje.crearMensajeERROR(e.getMessage());
		}
	}
	
	/**
	 * Permite buscar un usuario por dni de persona
	 * @param query alias
	 * @return List<AppUsuario>
	 */
	public List<AppUsuario> completeUsuario(String query){
		List<AppUsuario> allUsers = mngAut.findAllUsers();
		List<AppUsuario> filterUsers = new ArrayList<AppUsuario>();
		for (AppUsuario u : allUsers) {
			if(u.getUsuLogin().toLowerCase().contains(query.toLowerCase()))
				filterUsers.add(u);
		}
		return filterUsers;
	}
}
