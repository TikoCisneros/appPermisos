package permisos.model.external;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Permite obtener los permisos de aplicaciones 
 * para usuarios y perfiles transformados en nodos 
 * @author lcisneros
 *
 */
@Stateless
public class Conexion {

    public Conexion() {}
    
	/**
	 * 
	 * @param usuLogin
	 * @return List<Arbolapp>
	 * @throws Exception
	 */
	public List<Arbolapp> appsPermisosByUSR(String usuLogin) throws Exception{
		String cc = "jdbc:postgresql://localhost:5432/app_permisos?user=postgres&password=root";
		Connection conexion = null;
		Statement comando = null;
		ResultSet consulta = null;
		List<Arbolapp> lstResp = new ArrayList<Arbolapp>();
		try {
			Class.forName("org.postgresql.Driver");
		    conexion = DriverManager.getConnection(cc);
		    comando = conexion.createStatement();
		    String query = "select a.apl_nombre as app , m.mod_nombre as mod, v.vis_nombre as vis , v.vis_id vid "
					+ "from app_aplicaciones a, app_modulos m, app_vistas v, app_permisos p "
					+ "where a.apl_id = m.apl_id and m.mod_id = v.mod_id "
					+ "and p.vis_id=v.vis_id and apl_estado='A' "
					+ "and mod_estado='A' and vis_estado='A' "
					+ "and p.per_estado='A' and p.usu_login='"+usuLogin+"'"
					+ " order by a.apl_id, m.mod_orden, v.vis_orden";
		    consulta = comando.executeQuery(query);
		    while(consulta.next()){
				lstResp.add(new Arbolapp(consulta.getString("app"), consulta.getString("mod"), consulta.getString("vis"), consulta.getString("vid")));
			}
		    return lstResp;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			if (consulta != null)
				consulta.close();
			if (comando != null)
				comando.close();
			if (conexion!=null)
				conexion.close();
		}
	}
	
	/**
	 * 
	 * @param perfilId
	 * @return List<Arbolapp>
	 * @throws Exception
	 */
	public List<Arbolapp> appsPermisosByPerfil(Integer perfilId) throws Exception{
		String cc = "jdbc:postgresql://localhost:5432/app_permisos?user=postgres&password=root";
		Connection conexion = null;
		Statement comando = null;
		ResultSet consulta = null;
		List<Arbolapp> lstResp = new ArrayList<Arbolapp>();
		try {
			Class.forName("org.postgresql.Driver");
		    conexion = DriverManager.getConnection(cc);
		    comando = conexion.createStatement();
		    String query = "select a.apl_nombre as app , m.mod_nombre as mod, v.vis_nombre as vis , v.vis_id vid "
		    		+ "from app_aplicaciones a, app_modulos m, app_vistas v, app_perfilpermisos p "
		    		+ "where a.apl_id = m.apl_id and m.mod_id = v.mod_id "
		    		+ "and p.vis_id=v.vis_id and apl_estado='A' "
		    		+ "and mod_estado='A' and vis_estado='A' "
		    		+ "and p.ppe_estado='A' and p.per_id="+perfilId
		    		+ " order by a.apl_id, m.mod_orden, v.vis_orden";
		    consulta = comando.executeQuery(query);
		    while(consulta.next()){
				lstResp.add(new Arbolapp(consulta.getString("app"), consulta.getString("mod"), consulta.getString("vis"), consulta.getString("vid")));
			}
		    return lstResp;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			if (consulta != null)
				consulta.close();
			if (comando != null)
				comando.close();
			if (conexion!=null)
				conexion.close();
		}
	}
}
