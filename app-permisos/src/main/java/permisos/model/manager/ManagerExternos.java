package permisos.model.manager;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import permisos.model.dao.entities.AppUsuario;
import permisos.model.external.Funciones;

/**
 * Clase que manejará el ingreso de personas y usuarios pertenecientes y no 
 * pertenecientes a Yachay
 * @author lcisneros
 *
 */
@Stateless
public class ManagerExternos {
	
    @EJB
    private ManagerDAO mngDAO;
	
	/******************************************************USUARIOS******************************************************/
	/**
	 * Retorna los usuarios
	 * @return List<AppUsuario>
	 */
	@SuppressWarnings("unchecked")
	public List<AppUsuario> findUsuariosExternos(){
		return mngDAO.findAll(AppUsuario.class);
	}
	
	/**
	 * Busca un usuario por ID
	 * @param id
	 * @return AppUsuario
	 * @throws Exception
	 */
	public AppUsuario findUsuarioByID(String id) throws Exception{
		return (AppUsuario) mngDAO.findById(AppUsuario.class, id);
	}
	
	/**
	 * Valida si existe un usuario con ese alias
	 * @param usuLogin
	 * @return true o false
	 * @throws Exception
	 */
	public boolean existeUsuLogin(String usuLogin) throws Exception{
		AppUsuario usr = findUsuarioByID(usuLogin);
		if(usr!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * Permite ingresar un nuevo usuario
	 * @param usuLogin
	 * @param pass
	 * @param cpass
	 * @param persona
	 * @throws Exception
	 */
	public void ingresarUsuario(String usuLogin, String pass, String cpass, String persona) throws Exception{
		//Validar USULOGIN
		if(this.existeUsuLogin(usuLogin))
			throw new Exception("Ingrese un alias distinto.");
		//Validar PASS
		if(pass.compareTo(cpass)!=0)
			throw new Exception("La contraseña y su confirmación son distintas.");
		AppUsuario usr = new AppUsuario();
		usr.setUsuLogin(usuLogin);
		usr.setPerId(persona);
		usr.setUsuPasswordText(pass);
		usr.setUsuPassword(Funciones.getMD5(pass));
		usr.setUsuEstado(Funciones.estadoActivo);
		mngDAO.insertar(usr);
	}
	
	/**
	 * Permite modificar los datos de un usuario
	 * @param usuLogin
	 * @param pass
	 * @param cpass
	 * @param estado
	 * @throws Exception
	 */
	public void modificarUsuario(String usuLogin, String pass, String cpass, String estado) throws Exception{
		AppUsuario usr = findUsuarioByID(usuLogin);
		//Validar PASS
		if(pass != null && cpass != null){
			if(!pass.equals(cpass))
				throw new Exception("La contraseña y su confirmación son distintas.");
			usr.setUsuPassword(Funciones.getMD5(pass));
			usr.setUsuPasswordText(pass);
		}
		usr.setUsuEstado(estado);
		mngDAO.actualizar(usr);
	}
}
