package permisos.model.manager;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import permisos.model.dao.entities.AppAplicaciones;
import permisos.model.dao.entities.AppAutorizadoraplicaciones;
import permisos.model.dao.entities.AppAutorizadoraplicacionesPK;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Funciones;

/**
 * Maneja los métodos para la asignación de aplicaciones a los usuarios
 * @author lcisneros
 *
 */
@Stateless
public class ManagerAutorizacion {
	
    @EJB
    private ManagerDAO mngDAO;
		
	/**
	 * Busca un usuario por Alias
	 * @param alias
	 * @return AppUsuario
	 * @throws Exception 
	 */
	public AppUsuario findUserByAlias(String alias) throws Exception{
		return (AppUsuario) mngDAO.findById(AppUsuario.class, alias);
	}
	
	/**
	 * Devuelve una lista de todos los Usuarios activos
	 * @return List<AppUsuario>
	 */
	@SuppressWarnings("unchecked")
	public List<AppUsuario> findAllUsers(){
		return mngDAO.findWhere(AppUsuario.class, "o.usuEstado='A'", null);
	}
	
	/**
	 * Devuelve una lista de todas las aplicaciones activas no asignadas
	 * @param usuLogin alias de usuario
	 * @return List<AppAplicaciones>
	 */
	@SuppressWarnings("unchecked")
	public List<AppAplicaciones> findAllAppsLibres(String usuLogin){
		return mngDAO.findJPQL("SELECT o FROM AppAplicaciones o WHERE o.aplEstado='A' AND o.aplId NOT IN "
				+ "(SELECT a.appAplicacione.aplId FROM AppAutorizadoraplicaciones a WHERE a.appUsuario.usuLogin='"+usuLogin+"')");
	}
	
	/**
	 * Devuelve una lista de todas las aplicaciones asignadas
	 * @param usuLogin alias de usuario
	 * @return List<AppAplicaciones>
	 */
	@SuppressWarnings("unchecked")
	public List<AppAplicaciones> findAllAppsUsr(String usuLogin){
		return mngDAO.findJPQL("SELECT o FROM AppAplicaciones o WHERE o.aplId IN "
				+ "(SELECT a.appAplicacione.aplId FROM AppAutorizadoraplicaciones a WHERE a.appUsuario.usuLogin='"+usuLogin+"')");
	}
	
	/**
	 * Guarda una asignación
	 * @param appId
	 * @param usuLogin
	 * @throws Exception
	 */
	public void insertarAutorizacion(String appId, String usuLogin) throws Exception{
		AppAutorizadoraplicacionesPK pk = new AppAutorizadoraplicacionesPK();
		AppAutorizadoraplicaciones aut = new AppAutorizadoraplicaciones();
		pk.setAplId(appId);pk.setUsuLogin(usuLogin);
		aut.setId(pk);aut.setAppEstado(Funciones.estadoActivo);
		mngDAO.insertar(aut);
	}
	
	/**
	 * Elimina un asignación
	 * @param appId
	 * @param usuLogin
	 * @throws Exception
	 */
	public void quitarAutorizacion(String appId, String usuLogin) throws Exception{
		AppAutorizadoraplicacionesPK pk = new AppAutorizadoraplicacionesPK();
		pk.setAplId(appId);pk.setUsuLogin(usuLogin);
		mngDAO.eliminar(AppAutorizadoraplicaciones.class, pk);
	}
	
}
