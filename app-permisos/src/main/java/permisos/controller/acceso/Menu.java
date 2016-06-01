package permisos.controller.acceso;

import java.util.List;

public class Menu {
	private String nombre;
	private List<Submenu> lstLinks;
	
	public Menu() {
	}

	public Menu(String nombre, List<Submenu> lstLinks) {
		this.nombre = nombre;
		this.lstLinks = lstLinks;
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
	 * @return the lstLinks
	 */
	public List<Submenu> getLstLinks() {
		return lstLinks;
	}

	/**
	 * @param lstLinks the lstLinks to set
	 */
	public void setLstLinks(List<Submenu> lstLinks) {
		this.lstLinks = lstLinks;
	}
	
	
}
