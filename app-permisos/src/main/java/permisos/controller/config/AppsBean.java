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
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Funciones;
import permisos.model.external.Mensaje;
import permisos.model.manager.ManagerEstructura;

@ManagedBean
@ViewScoped
public class AppsBean implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1234578965446656561L;
	
    @EJB
	private ManagerEstructura mngEs;
        	
    private String prefijo; 
	private String nombre;
	private String descripcion;
	private String estado;
	private boolean edicion;
	private List<AppAplicaciones> listApps;
	private AppUsuario sesion;
	
    @Inject
    SesionBean sesionBean;
        
	public AppsBean() {}
        
        @PostConstruct
        public void init(){
            sesion = sesionBean.userSession("aplicaciones.xhtml");
            edicion = false;
            listApps = mngEs.findAllApps();
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
	 * @return the listApps
	 */
	public List<AppAplicaciones> getListApps() {
		return listApps;
	}

	/**
	 * @param listApps the listApps to set
	 */
	public void setListApps(List<AppAplicaciones> listApps) {
		this.listApps = listApps;
	}
	
	/**
	 * Setea los datos del BEAN
	 */
	public void liberarDatos(){
		setPrefijo(null);setNombre("");setDescripcion("");
	}
	
	/**
	 * Cancela cualquier acción
	 */
	public void cancelar(){
		liberarDatos();
		RequestContext.getCurrentInstance().execute("PF('dlgapp').hide()");
	}
	
	/**
	 * Permite abrir el formulario de ingreso de aplicación
	 */
	public void nuevo(){
		setEdicion(false);
		RequestContext.getCurrentInstance().execute("PF('dlgapp').show()");
	}
	
	/**
	 * Carga los datos de una aplicación
	 * @param app aplicación
	 */
	public void cargarDatos(AppAplicaciones app){
		setEdicion(true);
		setPrefijo(app.getAplId());setNombre(app.getAplNombre());
		setDescripcion(app.getAplDescripcion());setEstado(app.getAplEstado());
		RequestContext.getCurrentInstance().execute("PF('dlgapp').show()");
	}
	
	/**
	 * Permite ingresar o modificar un aplicación
	 */
	public void addModApp(){
		try {
			System.out.println(isEdicion());
			if(isEdicion())
				mngEs.modificarApp(getPrefijo(), getNombre(), getDescripcion(), getEstado());
			else
				mngEs.ingresarApp(getPrefijo(), getNombre(), getDescripcion());
			liberarDatos();
			setListApps(mngEs.findAllApps());
			RequestContext.getCurrentInstance().execute("PF('dlgapp').hide()");
			Mensaje.crearMensajeINFO("Datos de aplicación ingresados correctamente.");
		} catch (Exception e) {
			Mensaje.crearMensajeERROR(e.getMessage());
		}
	}
	
	/**
	 * Lista de estados de Aplicaciones
	 */
	public List<SelectItem> listEstados(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(Funciones.estadoActivo, Funciones.estadoActivo+" : "+Funciones.valorEstadoActivo));
		lista.add(new SelectItem(Funciones.estadoInactivo,Funciones.estadoInactivo+" : "+Funciones.valorEstadoInactivo));
		return lista;
	}
}
