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
import permisos.model.dao.entities.AppPerfiles;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Mensaje;
import permisos.model.manager.ManagerPerfiles;

/**
 * Controlador para la asignación de permisos a perfiles
 * @author lcisneros
 *
 */
@ManagedBean
@ViewScoped
public class PerfpermBean implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -5493885917727993319L;
	
    @EJB
	private ManagerPerfiles mngPerf;
	
    private AppPerfiles perfil;
	private String aplId;
	private TreeNode[] selectLibres;
	private TreeNode[] selectAsignadas;
	private TreeNode nLibre;
	private TreeNode nAsignada;
	private TreeNode arbolPermisos;
	private AppUsuario sesion;
	
    @Inject
    SesionBean sesionBean;
        
	public PerfpermBean() {
            
	}
        
    @PostConstruct
    public void init(){
        //sesion = sesionBean.userSession("perfperm.xhtml");
        aplId = "";
        perfil = null;
    }
	
	public AppUsuario getSesion() {
		return sesion;
	}
	
	/**
	 * @return the perfil
	 */
	public AppPerfiles getPerfil() {
		return perfil;
	}

	/**
	 * @param perfil the perfil to set
	 */
	public void setPerfil(AppPerfiles perfil) {
		this.perfil = perfil;
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
	 * Permite buscar perfiles por nombre
	 * @param query
	 * @return List<AppPerfiles
	 */
	public List<AppPerfiles> completePerfil(String query){
		List<AppPerfiles> todos = mngPerf.findAllPerfiles();
		List<AppPerfiles> filterPerf = new ArrayList<AppPerfiles>();
		for (AppPerfiles p : todos) {
			if(p.getPerNombre().toLowerCase().contains(query.toLowerCase()))
				filterPerf.add(p);
		}
		return filterPerf;
	}
	
	/**
	 * Setea los datos del BEAN al cambiar de perfil
	 */
	public void liberarDatos(){
		setAplId("");setSelectAsignadas(null);setSelectLibres(null);setnLibre(null);setnAsignada(null);setArbolPermisos(null);
	}
	
	/**
	 * Lista de aplicaciones 
	 * @return
	 */
	public List<SelectItem> listaApp(){
		List<SelectItem> listado = new ArrayList<SelectItem>();
		List<AppAplicaciones> apps = mngPerf.findAllApps();
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
		if(getPerfil()==null)
			Mensaje.crearMensajeWARN("Debe seleccionar un perfil.");
		else{
			setnLibre(mngPerf.arbolPermisos("lib", getAplId(),getPerfil()));
			setnAsignada(mngPerf.arbolPermisos("asg", getAplId(),getPerfil())); 
		}
	}
	
	/**
	 * Asigna permisosa perfil
	 */
	public void addPermisos(){
		if(getPerfil()==null || getAplId().isEmpty())
			Mensaje.crearMensajeWARN("Debe seleccionar un perfil y una aplicación.");
		else{
			try {
				mngPerf.ingresarPermisosPerfil(treeNodeToList(getSelectLibres()), getPerfil(), getAplId());
				setSelectLibres(null);
				cargarDatos();
			} catch (Exception e) {
				Mensaje.crearMensajeERROR(e.getMessage());
			}
		}
	}
	
	/**
	 * Quita permisos a perfil
	 */
	public void removePermisos(){
		if(getPerfil()==null || getAplId().isEmpty())
			Mensaje.crearMensajeWARN("Debe seleccionar un perfil y una aplicación.");
		else{
			try {
				mngPerf.quitarPermisoPerf(treeNodeToList(getSelectAsignadas()), getAplId(), getPerfil());;
				setSelectAsignadas(null);
				cargarDatos();
			} catch (Exception e) {
				Mensaje.crearMensajeERROR(e.getMessage());
			}
		}
	}
	
	/**
	 * Carga el árbol de permisos del perfil seleccionado
	 */
	public void cargarArbolPermisos(){
		if(getPerfil()==null)
			Mensaje.crearMensajeWARN("Debe seleccionar un perfil.");
		else{
			setArbolPermisos(mngPerf.arbolPermisosPerfiles(getPerfil()));
			RequestContext.getCurrentInstance().execute("PF('dlgarbol').show()");
		}
	}
}
