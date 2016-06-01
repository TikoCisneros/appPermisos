package permisos.model.manager;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import permisos.model.dao.entities.AppModulos;
import permisos.model.dao.entities.AppVistas;
import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Funciones;

@Stateless
public class ManagerWSLogin {
	@EJB
    private ManagerDAO mngDAO;
		
	/**
	 * Busca los módulos de una aplicación
	 * @param aplId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AppModulos> findModuloByApp(String aplId){
		return mngDAO.findWhere(AppModulos.class, "o.appAplicacione.aplId='"+aplId+"'", "o.modOrden");
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
	 * Valida si existe un usuario mediante alias y contraseña
	 * @param usuario
	 * @param password
	 * @return true o false
	 */
	@SuppressWarnings("unchecked")
	public boolean existeUsuPass(String usuario, String password){
		List<AppUsuario> lista = mngDAO.findWhere(AppUsuario.class, "o.usuLogin='"+usuario
				+"' AND o.usuPassword='"+Funciones.getMD5(password)+"'", null);
		if(lista.isEmpty() || lista.size()>1)
			return false;
		else
			return true;
	}
	
	/**
	 * Devuelve un JSONObject de una clase AppVista
	 * @param vista
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject objVista(AppVistas vista){
		JSONObject obj = new JSONObject();
		obj.put("nombre", vista.getVisNombre());
		obj.put("link", vista.getVisLink());
		return obj;
	}
	
	/**
	 * Devuelve un JSONObject de módulo partiendo de una clase AppModulo y un JSONArray de vistas
	 * @param modulo
	 * @param vistas
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject objModulo(AppModulos modulo, JSONArray vistas){
		JSONObject obj = new JSONObject();
		obj.put("nombre", modulo.getModNombre());
		obj.put("vistas", vistas);
		return obj;
	}
	
	/**
	 * Devuelve un JSONArray de permisos de usuario a una aplicación
	 * @param aplId
	 * @param usuario
	 * @return JSONArray
	 */
	@SuppressWarnings("unchecked")
	public JSONArray arrayPermisos(String aplId, String usuario){
		JSONArray permisos = new JSONArray();
		List<AppModulos> modulos = findModuloByApp(aplId);
		List<AppVistas> vistas = findAllVistasAppAsign(aplId, usuario);
		for (AppModulos mod : modulos) {
			JSONArray arrVis = new JSONArray();
			for (AppVistas v : vistas) {
				if(v.getAppModulo().getModNombre().equals(mod.getModNombre()))
					arrVis.add(objVista(v));
			}
			if(!arrVis.isEmpty())
				permisos.add(objModulo(mod, arrVis));
		}
		return permisos;
	}
}
