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
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Funciones;
import permisos.model.external.Mensaje;
import permisos.model.manager.ManagerExternos;

@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable{

	/**
	 * SERIAL ID
	 */
	private static final long serialVersionUID = 6468880553811583989L;
	
    @EJB
	private ManagerExternos mngEx;
	
    private String alias;
	private String password;
	private String cpassword;
	private String estado;
	private String persona;
	private boolean edicion;
	private List<AppUsuario> listaUsrs;
	private AppUsuario sesion;
	
    @Inject
    SesionBean sesionBean;
        
	public UsuarioBean() {
            
	}
        
	@PostConstruct
	public void init(){
	    sesion = sesionBean.userSession("usuarios.xhtml");
	    edicion = false;
	    listaUsrs = mngEx.findUsuariosExternos();
	}
	
	public AppUsuario getSesion() {
		return sesion;
	}
	
	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the cpassword
	 */
	public String getCpassword() {
		return cpassword;
	}

	/**
	 * @param cpassword the cpassword to set
	 */
	public void setCpassword(String cpassword) {
		this.cpassword = cpassword;
	}

	/**
	 * @return the persona
	 */
	public String getPersona() {
		return persona;
	}

	/**
	 * @param persona the persona to set
	 */
	public void setPersona(String persona) {
		this.persona = persona;
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
	 * @return the listaUsrs
	 */
	public List<AppUsuario> getListaUsrs() {
		return listaUsrs;
	}

	/**
	 * @param listaUsrs the listaUsrs to set
	 */
	public void setListaUsrs(List<AppUsuario> listaUsrs) {
		this.listaUsrs = listaUsrs;
	}
		
	/**
	 * Setea los datos del BEAN
	 */
	public void liberarDatos(){
		setAlias("");setPassword(null);setCpassword(null);setPersona(null);
	}
	
	/**
	 * Cancela un acción
	 */
	public void cancelar(){
		liberarDatos();
		if(isEdicion())
			RequestContext.getCurrentInstance().execute("PF('dlgeu').hide()");
		else
			RequestContext.getCurrentInstance().execute("PF('dlgnu').hide()");
	}
	
	/**
	 * Permite abrir la ventana para ingresar un nuevo usuario
	 */
	public void nuevo(){
		setEdicion(false);
		RequestContext.getCurrentInstance().execute("PF('dlgnu').show()");
	}
	
	/**
	 * Permite cargar datos de un usuario
	 * @param u AppUsuario
	 */
	public void cargarDatos(AppUsuario u){
		setEdicion(true);
		setAlias(u.getUsuLogin());setPersona(u.getPerId());
		setPassword(null);setCpassword(null);
		RequestContext.getCurrentInstance().execute("PF('dlgeu').show()");
	}
	
	/**
	 * Permite ingresar un usuario
	 */
	public void ingresarUsuario(){
		try {
			mngEx.ingresarUsuario(getAlias(), getPassword(), getCpassword(), getPersona());
			liberarDatos();
			setListaUsrs(mngEx.findUsuariosExternos());
			RequestContext.getCurrentInstance().execute("PF('dlgnu').hide()");
			Mensaje.crearMensajeINFO("Datos de Usuario ingresados correctamente.");
		} catch (Exception e) {
			Mensaje.crearMensajeWARN(e.getMessage());
		}
	}
	
	/**
	 * Permite editar un usuario
	 */
	public void editarUsuario(){
		try {
			mngEx.modificarUsuario(getAlias(), getPassword(), getCpassword(), getEstado());
			liberarDatos();
			setListaUsrs(mngEx.findUsuariosExternos());
			RequestContext.getCurrentInstance().execute("PF('dlgeu').hide()");
			Mensaje.crearMensajeINFO("Datos de Usuario modificados correctamente.");
		} catch (Exception e) {
			Mensaje.crearMensajeWARN(e.getMessage());
		}
	}
	
	/**
	 * Lista de estados de usuario
	 */
	public List<SelectItem> listEstados(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(Funciones.estadoActivo, Funciones.estadoActivo+" : "+Funciones.valorEstadoActivo));
		lista.add(new SelectItem(Funciones.estadoInactivo,Funciones.estadoInactivo+" : "+Funciones.valorEstadoInactivo));
		return lista;
	}

}
