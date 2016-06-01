package permisos.controller.acceso;

public class Submenu {
	private String nombre;
	private String link;
	
	public Submenu() {
	}
	
	public Submenu(String nombre, String link) {
		this.nombre = nombre;
		this.link = link;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	
	
		
	
}
