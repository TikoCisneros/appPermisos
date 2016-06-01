package permisos.model.external;

public class Arbolapp {
	private String aplicacion,modulo,vista,visId;

	public Arbolapp() {
	}
	
	public Arbolapp(String aplicacion, String modulo, String vista, String visId) {
		this.aplicacion = aplicacion;
		this.modulo = modulo;
		this.vista = vista;
		this.visId = visId;
	}

	/**
	 * @return the aplicacion
	 */
	public String getAplicacion() {
		return aplicacion;
	}

	/**
	 * @param aplicacion the aplicacion to set
	 */
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	/**
	 * @return the modulo
	 */
	public String getModulo() {
		return modulo;
	}

	/**
	 * @param modulo the modulo to set
	 */
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	/**
	 * @return the vista
	 */
	public String getVista() {
		return vista;
	}

	/**
	 * @param vista the vista to set
	 */
	public void setVista(String vista) {
		this.vista = vista;
	}
		
	/**
	 * @return the visId
	 */
	public String getVisId() {
		return visId;
	}

	/**
	 * @param visId the visId to set
	 */
	public void setVisId(String visId) {
		this.visId = visId;
	}

	@Override
	public String toString() {
		return getAplicacion()+" "+getModulo()+" "+getVista()+" ("+getVisId()+")";
	}
	
}
