package permisos.model.manager;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import permisos.model.dao.entities.AppAplicaciones;
import permisos.model.dao.entities.AppModulos;
import permisos.model.dao.entities.AppVistas;
import permisos.model.external.Funciones;

/**
 * Contiene los métodos para la gestión de aplicaciones, módulos y vistas
 * @author lcisneros
 *
 */
@Stateless
public class ManagerEstructura {
	
    @EJB
    private ManagerDAO mngDAO;
	
	/******************************************************APLICACIONES******************************************************/
	/**
	 * Devuelve una aplicación por ID
	 * @param aplId
	 * @return AppAplicaciones
	 * @throws Exception
	 */
	public AppAplicaciones findAppByID(String aplId) throws Exception{
		return (AppAplicaciones) mngDAO.findById(AppAplicaciones.class, aplId);
	}
	
	/**
	 * Valida si existe una App con un prefijo determinado
	 * @param prefijo
	 * @return true o false
	 * @throws Exception
	 */
	public boolean existeAppPrefijo(String prefijo) throws Exception{
		AppAplicaciones app = this.findAppByID(prefijo);
		if(app==null)
			return false;
		else
			return true;
	}
	
	/**
	 * Valida si existe una App con el mismo nombre
	 * @param nombre
	 * @return true o false
	 */
	@SuppressWarnings("unchecked")
	public boolean existeAppNombre(String nombre){
		List<AppAplicaciones> lista = mngDAO.findWhere(AppAplicaciones.class, "o.aplNombre='"+nombre+"'", null);
		if(!lista.isEmpty())
			return true;
		else
			return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean existeAppNombre(String nombre, String aplId){
		List<AppAplicaciones> lista = mngDAO.findWhere(AppAplicaciones.class, "o.aplNombre='"+nombre+"'", null);
		if(!lista.isEmpty() && !lista.get(0).getAplId().equals(aplId))
			return true;
		else
			return false;
	}	
	/**
	 * Devuelve la lista de aplicaciones existentes
	 * @return List<AppAplicaciones>
	 */
	@SuppressWarnings("unchecked")
	public List<AppAplicaciones> findAllApps(){
		return mngDAO.findAll(AppAplicaciones.class, "o.aplId");
	}
	
	/**
	 * Permite ingresar una nueva aplicación
	 * @param prefijo
	 * @param nombre
	 * @param descripcion
	 * @throws Exception
	 */
	public void ingresarApp(String prefijo, String nombre, String descripcion) throws Exception{
		if(this.existeAppPrefijo(prefijo))
			throw new Exception("Ya existe una aplicación con ese prefijo.");
		if(this.existeAppNombre(nombre))
			throw new Exception("Ya existe una aplicación con ese nombre.");
		AppAplicaciones app = new AppAplicaciones();
		app.setAplId(prefijo);app.setAplPrefijo(prefijo);app.setAplEstado(Funciones.estadoActivo);
		app.setAplNombre(nombre);app.setAplDescripcion(descripcion);
		mngDAO.insertar(app);
	}
	
	/**
	 * Permite modificar una aplicación existente
	 * @param prefijo
	 * @param nombre
	 * @param descripcion
	 * @param estado
	 * @throws Exception
	 */
	public void modificarApp(String prefijo, String nombre, String descripcion, String estado) throws Exception{
		AppAplicaciones app = this.findAppByID(prefijo);
		app.setAplEstado(estado);
		app.setAplNombre(nombre);
		app.setAplDescripcion(descripcion);
		mngDAO.actualizar(app);
	}
	
	/********************************************************MODULOS*********************************************************/
	/**
	 * Devuelve el ultimo valor de un módulo dentro de una aplicación
	 * @param appId
	 * @return Integer
	 */
	public Integer valorModulo(String appId){
		Integer orden = mngDAO.tomarValorIntJPQL("select max(o.modOrden) from AppModulos o where o.appAplicacione='"+appId+"'");
		if (orden==null)
			orden=1;
		else
			orden+=1;
		return orden;
	}
	
	/**
	 * Lista todos los módulos existentes
	 * @return List<AppModulos>
	 */
	@SuppressWarnings("unchecked")
	public List<AppModulos> findAllModulos(){
		return mngDAO.findAll(AppModulos.class);
	}
	
	/**
	 * Retorna un módulo por ID
	 * @param modId
	 * @return AppModulos
	 * @throws Exception
	 */
	public AppModulos findModuloByID(Integer modId) throws Exception{
		return (AppModulos) mngDAO.findById(AppModulos.class, modId);
	}
	
	/**
	 * Verifica si existe un módulo con ese nombre dentro de una app
	 * @param nombre
	 * @param prefijoApp
	 * @return true o false
	 */
	@SuppressWarnings("unchecked")
	public boolean existeModulo(String nombre, String prefijoApp){
		List<AppModulos> lista = mngDAO.findWhere(AppModulos.class, "o.modNombre='"+nombre
				+"' AND o.appAplicacione.aplId='"+prefijoApp+"'", null);
		if(!lista.isEmpty())
			return true;
		else
			return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean existeModulo(String nombre, String prefijoApp, Integer modId){
		List<AppModulos> lista = mngDAO.findWhere(AppModulos.class, "o.modNombre='"+nombre
				+"' AND o.appAplicacione.aplId='"+prefijoApp+"'", null);
		if(!lista.isEmpty() && lista.get(0).getModId()!=modId)
			return true;
		else
			return false;
	}
	
	/**
	 * Permite ingresar un nuevo módulo dentro de una aplicación
	 * @param appId
	 * @param nombre
	 * @param descripcion
	 * @param orden
	 * @throws Exception
	 */
	public void ingresarModulo(String appId, String nombre, String descripcion, Integer orden) throws Exception{
		if(this.existeModulo(nombre, appId))
			throw new Exception("Ya existe un módulo con ese nombre dentro de la aplicación.");
		AppModulos mod = new AppModulos();
		mod.setAppAplicacione(this.findAppByID(appId));mod.setModNombre(nombre);mod.setModDescripcion(descripcion);
		mod.setModOrden(orden);mod.setModEstado(Funciones.estadoActivo);
		mngDAO.insertar(mod);
	}
	
	/**
	 * Permite modificar los datos de un módulo por su ID
	 * @param modId
	 * @param appId
	 * @param nombre
	 * @param descripcion
	 * @param orden
	 * @param estado
	 * @throws Exception
	 */
	public void modificarModulo(Integer modId, String appId, String nombre, String descripcion, Integer orden, String estado) throws Exception{
		AppModulos mod = this.findModuloByID(modId);
		mod.setModNombre(nombre);mod.setModDescripcion(descripcion);
		mod.setModOrden(orden);mod.setModEstado(estado);
		mngDAO.actualizar(mod);
	}
	
	/*********************************************************VISTAS*********************************************************/
	/**
	 * Devuelve el ultimo valor de una vista dentro de un módulo
	 * @param modId
	 * @return Integer
	 */
	public Integer valorVista(Integer modId){
		Integer orden = mngDAO.tomarValorIntJPQL("select max(o.visOrden) from AppVistas o where o.appModulo="+modId+"");
		if (orden==null)
			orden=1;
		else
			orden+=1;
		return orden;
	}
	
	/**
	 * Devuelve la lista de aplicaciones activas existentes
	 * @return List<AppAplicaciones>
	 */
	@SuppressWarnings("unchecked")
	public List<AppAplicaciones> findAllAppsAct(){
		return mngDAO.findWhere(AppAplicaciones.class, "o.aplEstado='A'", "o.aplId");
	}
	
	/**
	 * Devuelve los módulos activos existentes para una App
	 * @param aplId
	 * @return List<AppModulos>
	 */
	@SuppressWarnings("unchecked")
	public List<AppModulos> findModulosByApp(String aplId){
		return mngDAO.findWhere(AppModulos.class, "o.appAplicacione.aplId='"+aplId+"' AND o.modEstado='A'", "o.modOrden");
	}
	
	/**
	 * Devuelve todas las vistas existentes
	 * @return List<AppVistas>
	 */
	@SuppressWarnings("unchecked")
	public List<AppVistas> findAllVistas(){
		return mngDAO.findAll(AppVistas.class);
	}
	
	/**
	 * Devuelve una vista por id
	 * @param visId
	 * @return AppVistas
	 * @throws Exception
	 */
	public AppVistas findVistaByID(String visId) throws Exception{
		return (AppVistas) mngDAO.findById(AppVistas.class, visId);
	}
	
	/**
	 * Valida si existe una vista por ID
	 * @param visId
	 * @return true o false
	 * @throws Exception
	 */
	public boolean existeVista(String visId) throws Exception{
		AppVistas v = this.findVistaByID(visId);
		if(v==null)
			return false;
		else
			return true;
	}
	
	/**
	 * Ingresa una Vista
	 * @param modulo
	 * @param visId
	 * @param nombre
	 * @param descripcion
	 * @param orden
	 * @throws Exception
	 */
	public void ingresarVista(Integer modulo, String visId, String nombre, String link, String descripcion, Integer orden) throws Exception{
		if(this.existeVista(visId))
			throw new Exception("Ya existe una vista rejistrada con ese prefijo");
		AppVistas v = new AppVistas();
		v.setVisId(visId);v.setAppModulo(this.findModuloByID(modulo));v.setVisNombre(nombre);v.setVisLink(link);
		v.setVisDescripcion(descripcion);v.setVisOrden(orden);v.setVisEstado(Funciones.estadoActivo);
		mngDAO.insertar(v);
	}
	
	/**
	 * Permite modificar los datos de una vista
	 * @param modulo
	 * @param visId
	 * @param nombre
	 * @param descripcion
	 * @param orden
	 * @param estado
	 * @throws Exception
	 */
	public void modificarVista(Integer modulo, String visId, String nombre, String link, String descripcion, Integer orden, String estado) throws Exception{
		AppVistas v = this.findVistaByID(visId);
		v.setAppModulo(this.findModuloByID(modulo));v.setVisNombre(nombre);v.setVisLink(link);
		v.setVisDescripcion(descripcion);v.setVisOrden(orden);v.setVisEstado(estado);
		mngDAO.actualizar(v);
	}
}
