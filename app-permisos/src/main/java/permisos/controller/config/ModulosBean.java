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
import permisos.model.dao.entities.AppAplicaciones;
import permisos.model.dao.entities.AppModulos;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Funciones;
import permisos.model.external.Mensaje;
import permisos.model.manager.ManagerEstructura;

@ManagedBean
@ViewScoped
public class ModulosBean implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 2941770430260342521L;
	
        @EJB
	private ManagerEstructura mngEs;
        
	private Integer modId;
	private String aplicacion;
	private String nombre;
	private Integer orden;
	private String descripcion;
	private String estado;
	private boolean edicion;
	private List<AppModulos> listModulos;
	private AppUsuario sesion;
	
    @Inject
    SesionBean sesionBean;
        
	public ModulosBean() {
            
	}
        
    @PostConstruct
    public void init(){
        sesion = sesionBean.userSession("modulos.xhtml");
        edicion = false;
        aplicacion = "";
        listModulos = mngEs.findAllModulos();
    }
	
	public AppUsuario getSesion() {
		return sesion;
	}
	
	/**
	 * @return the modId
	 */
	public Integer getModId() {
		return modId;
	}

	/**
	 * @param modId the modId to set
	 */
	public void setModId(Integer modId) {
		this.modId = modId;
	}

	/**
	 * @return the aplicacion
	 */
	public String getAplicacion() {
		return aplicacion;
	}

	/**
	 * @param aplicacion the aplicacion to set
	 */
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
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
	 * @return the orden
	 */
	public Integer getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(Integer orden) {
		this.orden = orden;
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
	 * @return the listModulos
	 */
	public List<AppModulos> getListModulos() {
		return listModulos;
	}

	/**
	 * @param listModulos the listModulos to set
	 */
	public void setListModulos(List<AppModulos> listModulos) {
		this.listModulos = listModulos;
	}
	
	/**
	 * Lista de estados de módulos
	 */
	public List<SelectItem> listEstados(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(Funciones.estadoActivo, Funciones.estadoActivo+" : "+Funciones.valorEstadoActivo));
		lista.add(new SelectItem(Funciones.estadoInactivo,Funciones.estadoInactivo+" : "+Funciones.valorEstadoInactivo));
		return lista;
	}
	
	/**
	 * Lista de aplicaciones activas
	 * @return List<SelectItem>
	 */
	public List<SelectItem> listApps(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		List<AppAplicaciones> apps = mngEs.findAllAppsAct();
		lista.add(new SelectItem("", "Seleccionar.."));
		for (AppAplicaciones a : apps) {
			lista.add(new SelectItem(a.getAplId(), a.getAplNombre()));
		}
		return lista;
	}
	
	/**
	 * Setea los datos del BEAN
	 */
	public void liberarDatos(){
		setModId(null);setAplicacion("");setNombre("");
		setEstado("");setOrden(null);setDescripcion("");
	}
	
	/**
	 * Cancela una acción
	 */
	public void cancelar(){
		liberarDatos();
		RequestContext.getCurrentInstance().execute("PF('dlgmod').hide()");
	}
	
	/**
	 * Permite abrir el formulario de ingreso de módulos
	 */
	public void nuevo(){
		setEdicion(false);
		RequestContext.getCurrentInstance().execute("PF('dlgmod').show()");
	}
	
	/**
	 * Carga los datos de un módulo seleccionado
	 * @param mod
	 */
	public void cargarDatos(AppModulos mod){
		setEdicion(true);
		setModId(mod.getModId());setAplicacion(mod.getAppAplicacione().getAplId());
		setNombre(mod.getModNombre());setOrden(mod.getModOrden());
		setDescripcion(mod.getModDescripcion());setEstado(mod.getModEstado());
		RequestContext.getCurrentInstance().execute("PF('dlgmod').show()");
	}
	
	/**
	 * Permite agregar o modificar un módulo
	 */
	public void addModModulos(){
		try {
			if(isEdicion())
				mngEs.modificarModulo(getModId(), getAplicacion(), getNombre(), 
						getDescripcion(), getOrden(), getEstado());
			else
				mngEs.ingresarModulo(getAplicacion(), getNombre(), getDescripcion(), getOrden());
			liberarDatos();
			setListModulos(mngEs.findAllModulos());
			RequestContext.getCurrentInstance().execute("PF('dlgmod').hide()");
			Mensaje.crearMensajeINFO("Datos de módulo ingresados correctamente.");
		} catch (Exception e) {
			Mensaje.crearMensajeERROR(e.getMessage());
		}
	}
	
	/**
	 * Sugiere el orden del módulo a seguir
	 */
	public void sugerirOrden(){
		if(!isEdicion())
			setOrden(mngEs.valorModulo(getAplicacion()));
	}
}
