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
import permisos.model.dao.entities.AppPermisos;
import permisos.model.dao.entities.AppVistas;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Arbolapp;
import permisos.model.external.Conexion;
import permisos.model.external.Funciones;

/**
 * Contiene los métodos para asignar los permisos de aplicación a los usuarios
 * @author lcisneros
 *
 */
@Stateless
public class ManagerPermisos {
	@EJB
    private ManagerDAO mngDAO;
        
    @EJB
    private Conexion conexion;
	
	/**
	 * Devuelve una lista de todos los Usuarios activos
	 * @return List<AppUsuario>
	 */
	@SuppressWarnings("unchecked")
	public List<AppUsuario> findAllUsers(){
		return mngDAO.findWhere(AppUsuario.class, "o.usuEstado='A'", null);
	}
	
	/**
	 * Busca todas las aplicaciones que han sido asignadas a el usuario
	 * @param usuario
	 * @return List<AppAplicaciones>
	 */
	@SuppressWarnings("unchecked")
	public List<AppAplicaciones> findAllAppsAutorizadas(String usuario){
		return mngDAO.findJPQL("SELECT o FROM AppAplicaciones o WHERE o.aplId IN "
				+ "(SELECT a.appAplicacione.aplId FROM AppAutorizadoraplicaciones a WHERE a.appUsuario.usuLogin='"+usuario+"')");
	}
	
	/**
	 * Devuelve las vistas activas de una aplicación que no sean asignadas a un usuario
	 * @param aplId
	 * @param usuario
	 * @return List<AppVistas>
	 */
	@SuppressWarnings("unchecked")
	public List<AppVistas> findAllVistasAppLibres(String aplId, String usuario){
		return mngDAO.findWhere(AppVistas.class, "o.appModulo.appAplicacione.aplId='"+aplId+"' AND o.visEstado='A'"
				+ " AND o.visId NOT IN (SELECT p.appVista.visId FROM AppPermisos p WHERE p.appUsuario.usuLogin='"+usuario+"')"
				, "o.appModulo.modOrden , o.visOrden");
	}
	
	/**
	 * Devuelve las vistas activas de una aplicación que ya están asignadas a un usuario
	 * @param aplId
	 * @param usuario
	 * @return List<AppVistas>
	 */
	@SuppressWarnings("unchecked")
	public List<AppVistas> findAllVistasAppAsign(String aplId, String usuario){
		return mngDAO.findWhere(AppVistas.class, "o.appModulo.appAplicacione.aplId='"+aplId+"' AND o.visEstado='A'"
				+ " AND o.visId IN (SELECT p.appVista.visId FROM AppPermisos p WHERE p.appUsuario.usuLogin='"+usuario+"')"
				, "o.appModulo.modOrden , o.visOrden");
	}
	
	/**
	 * Permite ingresar los permisos respectivos a un usuario
	 * @param vistas
	 * @param usrRegistro
	 * @param usuario
	 * @param aplId
	 * @throws Exception
	 */
	public void ingresarPermisos(List<String> vistas, String usrRegistro, AppUsuario usuario, String aplId) throws Exception{
		List<AppVistas> libres = this.findAllVistasAppLibres(aplId, usuario.getUsuLogin());
		for (AppVistas vista : libres) {
			for (String sVist : vistas) {
				if(vista.getVisNombre().equals(sVist)){
					AppPermisos p = new AppPermisos();
					p.setAppVista(vista);p.setAppUsuario(usuario);p.setPerEstado(Funciones.estadoActivo);
					p.setPerFechaRegistro(new Timestamp(new Date().getTime()));p.setUsuRegistro(usrRegistro);
					mngDAO.insertar(p);
				}
			}
		}
	}
	
	/**
	 * Busca un permiso otorgado por vista y usuario
	 * @param vista
	 * @param usuario
	 * @return AppPermisos
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public AppPermisos findPermisoByUsr(String vista, AppUsuario usuario) throws Exception{
		List<AppPermisos> lista = mngDAO.findWhere(AppPermisos.class, 
				"o.appVista.visId='"+vista+"' AND o.appUsuario.usuLogin='"+usuario.getUsuLogin()+"'", null);
		if(lista.size() > 1 || lista.isEmpty())
			throw new Exception("La consulta dió un resultado inesperado.");
		return lista.get(0);
	}
	
	/**
	 * Elimina los permisos otorgados a un usuario
	 * @param vistas
	 * @param usuario
	 * @param aplId
	 * @throws Exception
	 */
	public void quitarPermiso(List<String> vistas, AppUsuario usuario, String aplId)  throws Exception{
		List<AppVistas> otorgadas = findAllVistasAppAsign(aplId, usuario.getUsuLogin());
		for (AppVistas vista : otorgadas) {
			for (String sVist : vistas) {
				if(vista.getVisNombre().equals(sVist)){
					AppPermisos p = findPermisoByUsr(vista.getVisId(), usuario);
					mngDAO.eliminar(AppPermisos.class, p.getPerId());
				}
			}
		}
	}
	
	/**
	 * Devuelve un arbol de nodos de permisos
	 * @param tipo
	 * @param aplId
	 * @param usuario
	 * @return TreeNode
	 */
	@SuppressWarnings("unused")
	public TreeNode arbolPermisos(String tipo, String aplId, String usuario){
		TreeNode root = new DefaultTreeNode(aplId, null);
		List<AppVistas> vistas = new ArrayList<AppVistas>();
		if(tipo.equals("lib"))
			vistas = findAllVistasAppLibres(aplId, usuario);
		else if(tipo.equals("asg"))
			vistas = findAllVistasAppAsign(aplId, usuario);
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
