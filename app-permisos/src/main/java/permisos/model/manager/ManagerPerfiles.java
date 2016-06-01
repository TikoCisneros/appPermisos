package permisos.model.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import permisos.model.dao.entities.AppAplicaciones;
import permisos.model.dao.entities.AppPerfiles;
import permisos.model.dao.entities.AppPerfilpermisos;
import permisos.model.dao.entities.AppPermisos;
import permisos.model.dao.entities.AppVistas;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Arbolapp;
import permisos.model.external.Conexion;
import permisos.model.external.Funciones;

/**
 * Contiene los método para la gestión de perfiles, asignación de permisos a perfiles y asignación de perfiles a usuarios
 * @author lcisneros
 *
 */
@Stateless
public class ManagerPerfiles {
	@EJB
    private ManagerDAO mngDAO;
    
    @EJB
    private Conexion conexion;

	/************************************************PERFILES************************************************/
	/**
	 * Busca todos los perfiles existentes
	 * @return List<AppPerfiles>
	 */
	@SuppressWarnings("unchecked")
	public List<AppPerfiles> findAllPerfiles(){
		return mngDAO.findAll(AppPerfiles.class);
	}
	
	/**
	 * Busca un perfil por ID
	 * @param perId
	 * @return AppPerfiles
	 * @throws Exception
	 */
	public AppPerfiles findPerfilByID(Integer perId) throws Exception{
		return (AppPerfiles) mngDAO.findById(AppPerfiles.class, perId);
	}
	
	/**
	 * Valida si existe un perfil con ese nombre
	 * @param nombre
	 * @return true o false
	 */
	@SuppressWarnings("unchecked")
	public boolean existePerfil(String nombre){
		List<AppPerfiles> lista = mngDAO.findWhere(AppPerfiles.class, "o.perNombre='"+nombre+"'", null);
		if(lista.isEmpty())
			return false;
		else
			return true;
	}
	
	/**
	 * Permite crear un nuevo perfil
	 * @param nombre
	 * @param descripcion
	 * @throws Exception
	 */
	public void ingresarPerfil(String nombre, String descripcion) throws Exception{
		if(this.existePerfil(nombre))
			throw new Exception("Ya existe un perfil con el mismo nombre.");
		AppPerfiles p = new AppPerfiles();
		p.setPerNombre(nombre);p.setPerDescripcion(descripcion);p.setPerEstado(Funciones.estadoActivo);
		mngDAO.insertar(p);
	}
	
	/**
	 * Permite modificar los datos de un perfil
	 * @param perId
	 * @param nombre
	 * @param descripcion
	 * @param estado
	 * @throws Exception
	 */
	public void modificarPerfil(Integer perId, String nombre, String descripcion, String estado) throws Exception{
		AppPerfiles p = this.findPerfilByID(perId);
		p.setPerNombre(nombre);p.setPerDescripcion(descripcion);p.setPerEstado(estado);
		mngDAO.actualizar(p);
	}
	/*******************************************PERFILES - PERMISOS******************************************/
	/**
	 * Toma un perfil existente por nombre
	 * @param nombre
	 * @return AppPerfiles
	 */
	@SuppressWarnings("unchecked")
	public AppPerfiles findPerfilByNombre(String nombre){
		List<AppPerfiles> lista = mngDAO.findWhere(AppPerfiles.class, "o.perNombre='"+nombre+"'", null);
		return lista.get(0);
	}
	
	/**
	 * Busca todas las aplicaciones activas ordenadas por su id
	 * @return List<AppAplicaciones>
	 */
	@SuppressWarnings("unchecked")
	public List<AppAplicaciones> findAllApps(){
		return mngDAO.findWhere(AppAplicaciones.class, "o.aplEstado='A'", "o.aplId");
	}
	
	/**
	 * Devuelve las vistas activas de una aplicación que no sean asignadas a un perfil
	 * @param aplId
	 * @param perfil
	 * @return List<AppVistas>
	 */
	@SuppressWarnings("unchecked")
	public List<AppVistas> findAllVistasAppPerfLibres(String aplId, AppPerfiles perfil){
		return mngDAO.findWhere(AppVistas.class, "o.appModulo.appAplicacione.aplId='"+aplId+"' AND o.visEstado='A'"
				+ " AND o.visId NOT IN (SELECT p.appVista.visId FROM AppPerfilpermisos p WHERE p.appPerfile.perId="+perfil.getPerId()+")"
				, "o.appModulo.modOrden , o.visOrden");
	}
	
	/**
	 * Devuelve las vistas activas de una aplicación que están asignadas a un perfil
	 * @param aplId
	 * @param perfil
	 * @return List<AppVistas>
	 */
	@SuppressWarnings("unchecked")
	public List<AppVistas> findAllVistasAppPerfAsign(String aplId, AppPerfiles perfil){
		return mngDAO.findWhere(AppVistas.class, "o.appModulo.appAplicacione.aplId='"+aplId+"' AND o.visEstado='A'"
				+ " AND o.visId IN (SELECT p.appVista.visId FROM AppPerfilpermisos p WHERE p.appPerfile.perId="+perfil.getPerId()+")"
				, "o.appModulo.modOrden , o.visOrden");
	}
	
	/**
	 * Permite ingresar los permisos respectivos a un perfil
	 * @param vistas
	 * @param perfil
	 * @param aplId
	 * @throws Exception
	 */
	public void ingresarPermisosPerfil(List<String> vistas, AppPerfiles perfil, String aplId) throws Exception{
		List<AppVistas> libres = findAllVistasAppPerfLibres(aplId, perfil);
		for (AppVistas vista : libres) {
			for (String sVist : vistas) {
				if(vista.getVisNombre().equals(sVist)){
					AppPerfilpermisos pp = new AppPerfilpermisos();
					pp.setAppPerfile(perfil);pp.setAppVista(vista);
					pp.setPpeEstado(Funciones.estadoActivo);
					mngDAO.insertar(pp);
				}
			}
		}
	}
	
	/**
	 * Busca un permiso otorgado por vista y perfil
	 * @param vista
	 * @param perfil
	 * @return AppPerfilpermisos
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public AppPerfilpermisos findPermisoByPerfil(String vista, AppPerfiles perfil) throws Exception{
		List<AppPerfilpermisos> lista = mngDAO.findWhere(AppPerfilpermisos.class, 
				"o.appVista.visId='"+vista+"' AND o.appPerfile.perId="+perfil.getPerId(), null);
		if(lista.size() > 1 || lista.isEmpty())
			throw new Exception("La consulta dió un resultado inesperado.");
		return lista.get(0);
	}
	
	/**
	 * Elimina los permisos otorgados a un perfil
	 * @param vistas
	 * @param aplId
	 * @param perfil
	 * @throws Exception
	 */
	public void quitarPermisoPerf(List<String> vistas, String aplId, AppPerfiles perfil) throws Exception{
		List<AppVistas> otorgadas = findAllVistasAppPerfAsign(aplId, perfil);
		for (AppVistas vista : otorgadas) {
			for (String sVist : vistas) {
				if(vista.getVisNombre().equals(sVist)){
					AppPerfilpermisos p = findPermisoByPerfil(vista.getVisId(), perfil);
					mngDAO.eliminar(AppPerfilpermisos.class, p.getPpeId());
				}
			}
		}
	}
	
	/**
	 * Devuelve un arbol de nodos de permisos
	 * @param tipo
	 * @param aplId
	 * @param perfil
	 * @return TreeNode
	 */
	@SuppressWarnings("unused")
	public TreeNode arbolPermisos(String tipo, String aplId, AppPerfiles perfil){
		TreeNode root = new DefaultTreeNode(aplId, null);
		List<AppVistas> vistas = new ArrayList<AppVistas>();
		if(tipo.equals("lib"))
			vistas = findAllVistasAppPerfLibres(aplId, perfil);
		else if(tipo.equals("asg"))
			vistas = findAllVistasAppPerfAsign(aplId, perfil);
		Integer modAnt = 0;
		TreeNode mod = new DefaultTreeNode();
		for (AppVistas v : vistas) {
			if(modAnt != v.getAppModulo().getModId()){
				mod = new DefaultTreeNode(v.getAppModulo().getModNombre(), root);
				TreeNode vis = new DefaultTreeNode(v.getVisNombre(), mod);
			}else{
				TreeNode vis = new DefaultTreeNode(v.getVisNombre(), mod);
			}
			modAnt = v.getAppModulo().getModId();
		}
		return root;
	}
	
	/**
	 * Retorna el árbol de permisos por perfil
	 * @param perfil
	 * @return TreeNode
	 */
	@SuppressWarnings("unused")
	public TreeNode arbolPermisosPerfiles(AppPerfiles perfil){
		TreeNode root = new DefaultTreeNode("Arbol", null);
		List<Arbolapp> l = new ArrayList<Arbolapp>();
		try {
			l = conexion.appsPermisosByPerfil(perfil.getPerId());
			String appAnt = "";
			String modAnt = "";
			TreeNode app = new DefaultTreeNode();
			TreeNode mod = new DefaultTreeNode();
			for (Arbolapp a : l) {
				if(!appAnt.equals(a.getAplicacion())){
					app = new DefaultTreeNode(a.getAplicacion(),root);
				}
				if((!appAnt.equals(a.getAplicacion()) && modAnt.equals(a.getModulo()))
						|| !modAnt.equals(a.getModulo())){
					mod = new DefaultTreeNode(a.getModulo(),app);
					TreeNode vis = new DefaultTreeNode(a.getVista(),mod);
				}else{
					TreeNode vis = new DefaultTreeNode(a.getVista(),mod);
				}
				appAnt = a.getAplicacion();
				modAnt = a.getModulo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}
	/*************************************PERFILES & PERMISOS - USUARIOS*************************************/
	/**
	 * Devuelve una lista de todos los Usuarios activos
	 * @return List<AppUsuario>
	 */
	@SuppressWarnings("unchecked")
	public List<AppUsuario> findAllUsers(){
		return mngDAO.findWhere(AppUsuario.class, "o.usuEstado='A'", null);
	}
	
	/**
	 * Verifica si ya existe un permiso asignado a un usuario
	 * @param usuLogin
	 * @param visId
	 * @return true o false
	 */
	@SuppressWarnings("unchecked")
	public boolean existePermisoUsrVis(String usuLogin, String visId){
		List<AppPermisos> lista = mngDAO.findWhere(AppPermisos.class, 
				"o.appUsuario.usuLogin='"+usuLogin+"' AND o.appVista.visId='"+visId+"'", null);
		if(!lista.isEmpty())
			return true;
		else
			return false;
	}
	
	/**
	 * Busca una vista por ID
	 * @param visId
	 * @return
	 * @throws Exception
	 */
	public AppVistas findVistaByID(String visId) throws Exception{
		return (AppVistas) mngDAO.findById(AppVistas.class, visId);
	}
	
	/**
	 * Ingresa todos los permisos de perfil a un usuario
	 * @param usuario
	 * @param perId
	 * @param usrRegistro
	 * @throws Exception
	 */
	public void asignarPerfilUsr(AppUsuario usuario, Integer perId, String usrRegistro) throws Exception{
		List<Arbolapp> permisos = conexion.appsPermisosByPerfil(perId);
		for (Arbolapp p : permisos) {
			if(!existePermisoUsrVis(usuario.getUsuLogin(), p.getVisId())){
				AppPermisos per = new AppPermisos();
				per.setAppVista(findVistaByID(p.getVisId()));per.setAppUsuario(usuario);per.setPerEstado(Funciones.estadoActivo);
				per.setPerFechaRegistro(new Timestamp(new Date().getTime()));per.setUsuRegistro(usrRegistro);
				mngDAO.insertar(per);
			}
		}
	}
	
	/**
	 * Retorna el árbol de permisos por perfil
	 * @param perId
	 * @return TreeNode
	 */
	@SuppressWarnings("unused")
	public TreeNode arbolPermisosPerfiles(Integer perId){
		TreeNode root = new DefaultTreeNode("Arbol", null);
		List<Arbolapp> l = new ArrayList<Arbolapp>();
		try {
			l = conexion.appsPermisosByPerfil(perId);
			String appAnt = "";
			String modAnt = "";
			TreeNode app = new DefaultTreeNode();
			TreeNode mod = new DefaultTreeNode();
			for (Arbolapp a : l) {
				if(!appAnt.equals(a.getAplicacion())){
					app = new DefaultTreeNode(a.getAplicacion(),root);
				}
				if((!appAnt.equals(a.getAplicacion()) && modAnt.equals(a.getModulo()))
						|| !modAnt.equals(a.getModulo())){
					mod = new DefaultTreeNode(a.getModulo(),app);
					TreeNode vis = new DefaultTreeNode(a.getVista(),mod);
				}else{
					TreeNode vis = new DefaultTreeNode(a.getVista(),mod);
				}
				appAnt = a.getAplicacion();
				modAnt = a.getModulo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}
	
	/**
	 * Retorna el árbol de permisos por usuario
	 * @param usuario
	 * @return TreeNode
	 */
	@SuppressWarnings("unused")
	public TreeNode arbolPermisosUsuario(AppUsuario usuario){
		TreeNode root = new DefaultTreeNode("Arbol", null);
		List<Arbolapp> l = new ArrayList<Arbolapp>();
		try {
			l = conexion.appsPermisosByUSR(usuario.getUsuLogin());
			String appAnt = "";
			String modAnt = "";
			TreeNode app = new DefaultTreeNode();
			TreeNode mod = new DefaultTreeNode();
			for (Arbolapp a : l) {
				if(!appAnt.equals(a.getAplicacion())){
					app = new DefaultTreeNode(a.getAplicacion(),root);
				}
				if((!appAnt.equals(a.getAplicacion()) && modAnt.equals(a.getModulo()))
						|| !modAnt.equals(a.getModulo())){
					mod = new DefaultTreeNode(a.getModulo(),app);
					TreeNode vis = new DefaultTreeNode(a.getVista(),mod);
				}else{
					TreeNode vis = new DefaultTreeNode(a.getVista(),mod);
				}
				appAnt = a.getAplicacion();
				modAnt = a.getModulo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}
}
