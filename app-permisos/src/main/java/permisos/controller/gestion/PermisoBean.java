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
import permisos.model.dao.entities.AppAplicaciones;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Mensaje;
import permisos.model.manager.ManagerPermisos;

@ManagedBean
@ViewScoped
public class PermisoBean implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1728049035492341449L;
	
    @EJB
	private ManagerPermisos mngPer;
	
    private AppUsuario usuario;
	private String aplId;
	private TreeNode[] selectLibres;
	private TreeNode[] selectAsignadas;
	private TreeNode nLibre;
	private TreeNode nAsignada;
	private TreeNode arbolPermisos;
	private AppUsuario sesion;
	
	@Inject
	SesionBean sesionBean;
        
	public PermisoBean() {
	}
        
    @PostConstruct
    public void init(){
        sesion = sesionBean.userSession("permisos.xhtml");
        aplId = "";
        usuario = null;
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
	 * @return the aplId
	 */
	public String getAplId() {
		return aplId;
	}

	/**
	 * @param aplId the aplId to set
	 */
	public void setAplId(String aplId) {
		this.aplId = aplId;
	}

	/**
	 * @return the selectLibres
	 */
	public TreeNode[] getSelectLibres() {
		return selectLibres;
	}

	/**
	 * @param selectLibres the selectLibres to set
	 */
	public void setSelectLibres(TreeNode[] selectLibres) {
		this.selectLibres = selectLibres;
	}

	/**
	 * @return the selectAsignadas
	 */
	public TreeNode[] getSelectAsignadas() {
		return selectAsignadas;
	}

	/**
	 * @param selectAsignadas the selectAsignadas to set
	 */
	public void setSelectAsignadas(TreeNode[] selectAsignadas) {
		this.selectAsignadas = selectAsignadas;
	}

	/**
	 * @return the nLibre
	 */
	public TreeNode getnLibre() {
		return nLibre;
	}

	/**
	 * @param nLibre the nLibre to set
	 */
	public void setnLibre(TreeNode nLibre) {
		this.nLibre = nLibre;
	}

	/**
	 * @return the nAsignada
	 */
	public TreeNode getnAsignada() {
		return nAsignada;
	}

	/**
	 * @param nAsignada the nAsignada to set
	 */
	public void setnAsignada(TreeNode nAsignada) {
		this.nAsignada = nAsignada;
	}
	
	/**
	 * @return the arbolPermisos
	 */
	public TreeNode getArbolPermisos() {
		return arbolPermisos;
	}

	/**
	 * @param arbolPermisos the arbolPermisos to set
	 */
	public void setArbolPermisos(TreeNode arbolPermisos) {
		this.arbolPermisos = arbolPermisos;
	}

	/**
	 * Permite buscar un usuario por dni de persona
	 * @param query alias
	 * @return List<AppUsuario>
	 */
	public List<AppUsuario> completeUsuario(String query){
		List<AppUsuario> allUsers = mngPer.findAllUsers();
		List<AppUsuario> filterUsers = new ArrayList<AppUsuario>();
		for (AppUsuario u : allUsers) {
			if(u.getUsuLogin().toLowerCase().contains(query.toLowerCase()))
				filterUsers.add(u);
		}
		return filterUsers;
	}
	
	/**
	 * Setea los datos del BEAN al cambiar de usuario
	 */
	public void liberarDatos(){
		setAplId("");setSelectAsignadas(null);setSelectLibres(null);setnLibre(null);setnAsignada(null);setArbolPermisos(null);
	}
	
	/**
	 * Lista de aplicaciones asignadas a usuario
	 * @return
	 */
	public List<SelectItem> listaAppAsig(){
		List<SelectItem> listado = new ArrayList<SelectItem>();
		List<AppAplicaciones> apps = new ArrayList<AppAplicaciones>();
		if(getUsuario()!=null)
			apps = mngPer.findAllAppsAutorizadas(getSesion().getUsuLogin());
		listado.add(new SelectItem("", "Seleccionar App"));
		for (AppAplicaciones a : apps) {
			listado.add(new SelectItem(a.getAplId(), a.getAplNombre()));
		}
		return listado;
	}
	
	/**
	 * Devuelve una Lista de String previo el ingreso de una array de TreeNode
	 * @param nodos array de TreeNode
	 * @return List<String>
	 */
	public List<String> treeNodeToList(TreeNode[] nodos){
		List<String> lista = new ArrayList<String>();
		for (int i = 0; i < nodos.length; i++) {
			lista.add(nodos[i].getData().toString());
		}
		return lista;
	}
	
	/**
	 * Carga los datos de los árboles de nodos
	 */
	public void cargarDatos(){
		if(getUsuario()==null)
			Mensaje.crearMensajeWARN("Debe seleccionar un usuario");
		else{
			setnLibre(mngPer.arbolPermisos("lib", getAplId(), getUsuario().getUsuLogin()));
			setnAsignada(mngPer.arbolPermisos("asg", getAplId(), getUsuario().getUsuLogin()));
		}
	}
	
	/**
	 * Asigna permisos
	 */
	public void addPermisos(){
		if(getUsuario()==null || getAplId().isEmpty())
			Mensaje.crearMensajeWARN("Debe seleccionar un usuario y una aplicación.");
		else{
			try {
				mngPer.ingresarPermisos(treeNodeToList(getSelectLibres()), getSesion().getUsuLogin(), getUsuario(), getAplId());
				setSelectLibres(null);
				cargarDatos();
			} catch (Exception e) {
				Mensaje.crearMensajeERROR(e.getMessage());
			}
		}
	}
	
	/**
	 * Quita permisos
	 */
	public void removePermisos(){
		if(getUsuario()==null || getAplId().isEmpty())
			Mensaje.crearMensajeWARN("Debe seleccionar un usuario y una aplicación.");
		else{
			try {
				mngPer.quitarPermiso(treeNodeToList(getSelectAsignadas()), getUsuario(), getAplId());
				setSelectAsignadas(null);
				cargarDatos();
			} catch (Exception e) {
				Mensaje.crearMensajeERROR(e.getMessage());
			}
		}
	}
	
	/**
	 * Carga el árbol de permisos del usuario seleccionado
	 */
	public void cargarArbolPermisos(){
		if(getUsuario()==null)
			Mensaje.crearMensajeWARN("Debe seleccionar un usuario.");
		else{
			setArbolPermisos(mngPer.arbolPermisosUsuario(getUsuario()));
			RequestContext.getCurrentInstance().execute("PF('dlgarbol').show()");
		}
	}
}
