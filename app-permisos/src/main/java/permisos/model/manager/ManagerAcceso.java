package permisos.model.manager;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import permisos.controller.acceso.Menu;
import permisos.controller.acceso.Submenu;
import permisos.model.dao.entities.AppModulos;
import permisos.model.dao.entities.AppPermisos;
import permisos.model.dao.entities.AppVistas;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Funciones;

@Stateless
public class ManagerAcceso {
	
        @EJB
        private ManagerDAO mngDAO;
	
		
	/**
	 * Busca un usario por alias y password
	 * @param alias
	 * @param pass 
	 * @return AppUsuario
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public AppUsuario findUserByAliasPass(String alias, String pass) throws Exception{
		List<AppUsuario> lista = mngDAO.findByParam(AppUsuario.class, "o.usuLogin", alias, null);
		if(lista == null || lista.isEmpty())
			throw new Exception("No se ha encontrado el usuario.");
		AppUsuario u = lista.get(0);
		if(!u.getUsuEstado().equals("A"))
			throw new Exception("Su usuario no esta activo.");
		if (u.getUsuPassword().equals(Funciones.getMD5(pass))) {
			return u;
		}else{
			throw new Exception("Usuario o contraseña invalidos");
		}
	}
	
	/**
	 * Encuentra la lista de vistas asignadas al usuario
	 * @param usuario
	 * @param aplId
	 * @return List<AppVistas>
	 */
	@SuppressWarnings("unchecked")
	public List<AppVistas> findVistasByUsrApp(AppUsuario usuario, String aplId){
		List<AppVistas> lista = mngDAO.findJPQL("SELECT v FROM AppVistas v  "+
                "WHERE v.visId IN (SELECT p.appVista.visId FROM AppPermisos p WHERE p.appUsuario.usuLogin='"+
                usuario.getUsuLogin()+"' AND p.perEstado='A' AND p.appVista.appModulo.appAplicacione.aplId='"+aplId+"') "+
                "ORDER BY v.appModulo.modOrden, v.appModulo.modId, v.visOrden ");
		return lista;
	}
	
	/**
	 * Busca los módulos de una aplicación
	 * @param aplId
	 * @return List<AppModulos>
	 */
	@SuppressWarnings("unchecked")
	public List<AppModulos> findModuloByApp(String aplId){
		return mngDAO.findWhere(AppModulos.class, "o.appAplicacione.aplId='"+aplId+"'", "o.modOrden");
	}
	
	/**
	 * Valida si un usuario posee permiso a una vista principal
	 * @param alias
	 * @param vista
	 * @return true o false
	 */
	@SuppressWarnings("unchecked")
	public boolean poseePermiso(String alias, String vista){
		List<AppPermisos> permiso = mngDAO.findWhere(AppPermisos.class, "o.appUsuario.usuLogin='"+alias
				+"' AND o.appVista.visLink='"+vista+"'", null);
		if(permiso == null || permiso.isEmpty())
			return false;
		else
			return true;
	}
	
	/**
	 * Crea la barra de menpu de la aplicación según el usuario
	 * @param usuario
	 * @param aplId
	 * @return
	 */
	public List<Menu> menuByUsrApp(AppUsuario usuario, String aplId){
		List<Menu> menus = new ArrayList<Menu>();
		List<AppVistas> vistas = findVistasByUsrApp(usuario, aplId);
		List<AppModulos> modulos = findModuloByApp(aplId);
		for (AppModulos mod : modulos) {
			List<Submenu> submenus = new ArrayList<Submenu>();
			for (AppVistas v : vistas) {
				if(v.getAppModulo().getModNombre().equals(mod.getModNombre()))
					submenus.add(new Submenu(v.getVisNombre(), v.getVisLink()));
			}
			if(!submenus.isEmpty())
				menus.add(new Menu(mod.getModNombre(), submenus));
		}
		return menus;
	}
}
