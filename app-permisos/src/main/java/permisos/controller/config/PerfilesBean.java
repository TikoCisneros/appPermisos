package permisos.controller.config;

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

import permisos.controller.acceso.SesionBean;
import permisos.model.dao.entities.AppPerfiles;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Funciones;
import permisos.model.external.Mensaje;
import permisos.model.manager.ManagerPerfiles;

@ManagedBean
@ViewScoped
public class PerfilesBean implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 6725521956698839856L;
	
    @EJB
	private ManagerPerfiles mngPer;
	
    private Integer perId;
	private String nombre;
	private String descripcion;
	private String estado;
	private boolean edicion;
	private List<AppPerfiles> listPerfiles;
	private AppUsuario sesion;
	
    @Inject
    SesionBean sesionBean;
        
	public PerfilesBean() {
            
	}
        
    @PostConstruct
    public void init(){
        sesion = sesionBean.userSession("perfiles.xhtml");
        edicion = false;
        listPerfiles = mngPer.findAllPerfiles();
    }

	public AppUsuario getSesion() {
		return sesion;
	}
	
	/**
	 * @return the perId
	 */
	public Integer getPerId() {
		return perId;
	}

	/**
	 * @param perId the perId to set
	 */
	public void setPerId(Integer perId) {
		this.perId = perId;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the edicion
	 */
	public boolean isEdicion() {
		return edicion;
	}

	/**
	 * @param edicion the edicion to set
	 */
	public void setEdicion(boolean edicion) {
		this.edicion = edicion;
	}

	/**
	 * @return the listPerfiles
	 */
	public List<AppPerfiles> getListPerfiles() {
		return listPerfiles;
	}

	/**
	 * @param listPerfiles the listPerfiles to set
	 */
	public void setListPerfiles(List<AppPerfiles> listPerfiles) {
		this.listPerfiles = listPerfiles;
	}
	
	/**
	 * Lista de estados de perfiles
	 */
	public List<SelectItem> listEstados(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(Funciones.estadoActivo, Funciones.estadoActivo+" : "+Funciones.valorEstadoActivo));
		lista.add(new SelectItem(Funciones.estadoInactivo,Funciones.estadoInactivo+" : "+Funciones.valorEstadoInactivo));
		return lista;
	}
	
	/**
	 * Setea los atributos del BEAN
	 */
	public void liberarDatos(){
		setPerId(null);setNombre("");setDescripcion("");setEstado("");
	}
	
	/**
	 * Cancela una acción
	 */
	public void cancelar(){
		liberarDatos();
		RequestContext.getCurrentInstance().execute("PF('dlgperf').hide()");
	}
	
	/**
	 * Permite abrir el formulario de perfiles
	 */
	public void nuevo(){
		setEdicion(false);
		RequestContext.getCurrentInstance().execute("PF('dlgperf').show()");
	}
	
	/**
	 * Carga los datos de un perfil
	 * @param perfil
	 */
	public void cargarDatos(AppPerfiles perfil){
		setEdicion(true);
		setPerId(perfil.getPerId());setNombre(perfil.getPerNombre());
		setDescripcion(perfil.getPerDescripcion());setEstado(perfil.getPerEstado());
		RequestContext.getCurrentInstance().execute("PF('dlgperf').show()");
	}
	
	/**
	 * Ingresa o modifica los datos de un perfil
	 */
	public void addModPerfil(){
		try {
			if(isEdicion())
				mngPer.modificarPerfil(getPerId(), getNombre(), getDescripcion(), getEstado());
			else
				mngPer.ingresarPerfil(getNombre(), getDescripcion());
			liberarDatos();
			setListPerfiles(mngPer.findAllPerfiles());
			RequestContext.getCurrentInstance().execute("PF('dlgperf').hide()");
			Mensaje.crearMensajeINFO("Datos de perfil ingresados correctamente.");
		} catch (Exception e) {
			Mensaje.crearMensajeERROR(e.getMessage());
		}
	}
}
