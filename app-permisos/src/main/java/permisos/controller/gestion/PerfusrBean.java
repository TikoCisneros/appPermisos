package permisos.controller.gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

import permisos.controller.acceso.SesionBean;
import permisos.model.dao.entities.AppPerfiles;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Mensaje;
import permisos.model.manager.ManagerPerfiles;

/**
 * Controlador para la asignación de perfiles a usuarios
 * @author lcisneros
 *
 */
@ManagedBean
@ViewScoped
public class PerfusrBean implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -6267747130142248278L;
	
        @EJB
	private ManagerPerfiles mngPerf;
	
    private AppUsuario usuario;
	private Integer perfId;
	private TreeNode pLibres;
	private TreeNode pAsignados;
	private AppUsuario sesion;
        
    @Inject
    SesionBean sesionBean;
	
	public PerfusrBean() {
		
	}
        
    @PostConstruct
    public void init(){
        sesion = sesionBean.userSession("usrperf.xhtml");
        perfId = -1;
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
	 * @return the perfId
	 */
	public Integer getPerfId() {
		return perfId;
	}

	/**
	 * @param perfId the perfId to set
	 */
	public void setPerfId(Integer perfId) {
		this.perfId = perfId;
	}

	/**
	 * @return the pLibres
	 */
	public TreeNode getpLibres() {
		return pLibres;
	}

	/**
	 * @param pLibres the pLibres to set
	 */
	public void setpLibres(TreeNode pLibres) {
		this.pLibres = pLibres;
	}

	/**
	 * @return the pAsignados
	 */
	public TreeNode getpAsignados() {
		return pAsignados;
	}

	/**
	 * @param pAsignados the pAsignados to set
	 */
	public void setpAsignados(TreeNode pAsignados) {
		this.pAsignados = pAsignados;
	}
	
	/**
	 * Permite buscar un usuario por dni de persona
	 * @param query alias
	 * @return List<AppUsuario>
	 */
	public List<AppUsuario> completeUsuario(String query){
		List<AppUsuario> allUsers = mngPerf.findAllUsers();
		List<AppUsuario> filterUsers = new ArrayList<AppUsuario>();
		for (AppUsuario u : allUsers) {
			if(u.getUsuLogin().toLowerCase().contains(query.toLowerCase()))
				filterUsers.add(u);
		}
		return filterUsers;
	}
	
	/**
	 * Listado de selecItem de perfiles
	 * @return List<SelectItem>
	 */
	public List<SelectItem> listaPerfiles(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		List<AppPerfiles> perfiles = mngPerf.findAllPerfiles();
		lista.add(new SelectItem(-1,"Seleccionar.."));
		for (AppPerfiles p : perfiles) {
			lista.add(new SelectItem(p.getPerId(),p.getPerNombre()));
		}
		return lista;
	}
	
	/**
	 * Setea los datos del BEAN al cambiar de usuario
	 */
	public void liberarDatos(){
		setPerfId(-1);setpLibres(null);setpAsignados(null);
	}
	
	/**
	 * Carga los datos de perfil y de usuario
	 */
	public void cargarDatos(){
		if(getUsuario()==null)
			Mensaje.crearMensajeWARN("Debe seleccionar un usuario.");
		else{
			setpAsignados(mngPerf.arbolPermisosUsuario(getUsuario()));
			setpLibres(mngPerf.arbolPermisosPerfiles(getPerfId()));
		}
	}
	
	/**
	 * Abre el diálogo de confirmación al presionar el botón aceptar
	 */
	public void aceptar(){
		RequestContext.getCurrentInstance().execute("PF('cd').show()");
	}
	
	/**
	 * Añade los permisos del perfil al usuario
	 */
	public void ingresarPermisos(){
		if(getUsuario()==null || getPerfId()==-1)
			Mensaje.crearMensajeWARN("Debe seleccionar un usuario y perfil.");
		else{
			try {
				mngPerf.asignarPerfilUsr(getUsuario(), getPerfId(), getSesion().getUsuLogin());
				cargarDatos();
				Mensaje.crearMensajeINFO("Se han asignado todos los permisos correctamente.");
			} catch (Exception e) {
				Mensaje.crearMensajeERROR(e.getMessage());
			}
		}
	}
}
