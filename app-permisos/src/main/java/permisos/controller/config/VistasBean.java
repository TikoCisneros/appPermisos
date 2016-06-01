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
import permisos.model.dao.entities.AppVistas;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Funciones;
import permisos.model.external.Mensaje;
import permisos.model.manager.ManagerEstructura;

@ManagedBean
@ViewScoped
public class VistasBean implements Serializable{

	/**
	 * SERIAL ID
	 */
	private static final long serialVersionUID = 5853541435618398666L;
	
    @EJB
	private ManagerEstructura mngEs;
	
    private String prefijo;
	private String nombre;
	private String link;
	private Integer orden;
	private String descripcion;
	private String estado;
	private Integer modulo;
	private String appId;
	private List<AppVistas> listVistas;
	private boolean edicion;
	private AppUsuario sesion;
	
    @Inject
    SesionBean sesionBean;
        
	public VistasBean() {
            
	}
        
    @PostConstruct
    public void init(){
    	sesion = sesionBean.userSession("vistas.xhtml");
        edicion = false;
        appId="";
        listVistas = mngEs.findAllVistas();
    }
	
	public AppUsuario getSesion() {
		return sesion;
	}
	
	/**
	 * @return the prefijo
	 */
	public String getPrefijo() {
		return prefijo;
	}

	/**
	 * @param prefijo the prefijo to set
	 */
	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
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
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
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
	 * @return the modulo
	 */
	public Integer getModulo() {
		return modulo;
	}

	/**
	 * @param modulo the modulo to set
	 */
	public void setModulo(Integer modulo) {
		this.modulo = modulo;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the listVistas
	 */
	public List<AppVistas> getListVistas() {
		return listVistas;
	}

	/**
	 * @param listVistas the listVistas to set
	 */
	public void setListVistas(List<AppVistas> listVistas) {
		this.listVistas = listVistas;
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
	 * Lista de estados de vistas
	 * @return List<SelectItem>
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
	 * Lista de módulos activos por aplicación
	 * @return List<SelectItem>
	 */
	public List<SelectItem> listModsApp(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		List<AppModulos> mods = new ArrayList<AppModulos>();
		lista.add(new SelectItem(null, "Seleccionar.."));
		if(!getAppId().isEmpty())
			mods = mngEs.findModulosByApp(getAppId());
		for (AppModulos m : mods) {
			lista.add(new SelectItem(m.getModId(), m.getModNombre()));
		}
		return lista;
	}
	
	/**
	 * Setea los datos del BEAN
	 */
	public void liberarDatos(){
		setPrefijo("");setNombre("");setLink("");setDescripcion("");setOrden(null);
		setModulo(null);setAppId("");
	}
	
	/**
	 * Cancela una acción
	 */
	public void cancelar(){
		liberarDatos();
		RequestContext.getCurrentInstance().execute("PF('dlgvis').hide()");
	}
	
	/**
	 * Abre el formulario para crear una vista
	 */
	public void nuevo(){
		setEdicion(false);
		RequestContext.getCurrentInstance().execute("PF('dlgvis').show()");
	}
	
	/**
	 * Permite cargar los datos de una vista
	 * @param vista
	 */
	public void cargarDatos(AppVistas vista){
		setEdicion(true);
		setAppId(vista.getAppModulo().getAppAplicacione().getAplId());
		setModulo(vista.getAppModulo().getModId());setPrefijo(vista.getVisId());
		setNombre(vista.getVisNombre());setOrden(vista.getVisOrden());
		setDescripcion(vista.getVisDescripcion());setLink(vista.getVisLink());
		setEstado(vista.getVisEstado());
		RequestContext.getCurrentInstance().execute("PF('dlgvis').show()");
	}
	
	/**
	 * Ingresa o modifica datos de una vista
	 */
	public void addModVista(){
		try {
			if(getModulo()==null)
				throw new Exception("Debe seleccionar un módulo.");
			if(isEdicion())
				mngEs.modificarVista(getModulo(),getPrefijo(), getNombre(), getLink(), getDescripcion(), getOrden(), getEstado());
			else
				mngEs.ingresarVista(getModulo(), getPrefijo(), getNombre(), getLink(), getDescripcion(), getOrden());
			liberarDatos();
			setListVistas(mngEs.findAllVistas());
			RequestContext.getCurrentInstance().execute("PF('dlgvis').hide()");
			Mensaje.crearMensajeINFO("Datos de vista ingresados correctamente.");
		} catch (Exception e) {
			Mensaje.crearMensajeERROR(e.getMessage());
		}
	}
	
	/**
	 * Sugiere el orden de la vista a seguir
	 */
	public void sugerirOrden(){
		if(!isEdicion())
			setOrden(mngEs.valorVista(getModulo()));
	}
}
