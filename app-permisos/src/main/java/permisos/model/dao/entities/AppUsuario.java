package permisos.model.dao.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the gen_usuario database table.
 * 
 */
@Entity
@Table(name="app_usuario")
@NamedQuery(name="AppUsuario.findAll", query="SELECT g FROM AppUsuario g")
public class AppUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="usu_login", length=20)
	private String usuLogin;

	@Column(name="usu_estado", columnDefinition="bpchar", length=1)
	private String usuEstado;

	@Column(name="usu_password", length=200)
	private String usuPassword;
	
	@Column(name="usu_password_text", length=200)
	private String usuPasswordText;
	
	@Column(name="usu_origen", length=10)
	private String usuOrigen;

	//bi-directional many-to-one association to AppAutorizadoraplicaciones
	@OneToMany(mappedBy="appUsuario", fetch=FetchType.EAGER)
	private List<AppAutorizadoraplicaciones> appAutorizadoraplicaciones;

	//bi-directional many-to-one association to AppPermisos
	@OneToMany(mappedBy="appUsuario", fetch=FetchType.EAGER)
	private List<AppPermisos> appPermisos;

	@Column(name="per_id", length=20)
	private String perId;

	public AppUsuario() {
	}

	public String getUsuLogin() {
		return this.usuLogin;
	}

	public void setUsuLogin(String usuLogin) {
		this.usuLogin = usuLogin;
	}

	public String getUsuEstado() {
		return this.usuEstado;
	}

	public void setUsuEstado(String usuEstado) {
		this.usuEstado = usuEstado;
	}

	public String getUsuPassword() {
		return this.usuPassword;
	}

	public void setUsuPassword(String usuPassword) {
		this.usuPassword = usuPassword;
	}
	
	public String getUsuPasswordText() {
		return usuPasswordText;
	}
	
	public void setUsuPasswordText(String usuPasswordText) {
		this.usuPasswordText = usuPasswordText;
	}
	
	public String getUsuOrigen() {
		return usuOrigen;
	}

	public void setUsuOrigen(String usuOrigen) {
		this.usuOrigen = usuOrigen;
	}

	public List<AppAutorizadoraplicaciones> getAppAutorizadoraplicaciones() {
		return this.appAutorizadoraplicaciones;
	}

	public void setAppAutorizadoraplicaciones(List<AppAutorizadoraplicaciones> appAutorizadoraplicaciones) {
		this.appAutorizadoraplicaciones = appAutorizadoraplicaciones;
	}

	public AppAutorizadoraplicaciones addAppAutorizadoraplicacione(AppAutorizadoraplicaciones appAutorizadoraplicacione) {
		getAppAutorizadoraplicaciones().add(appAutorizadoraplicacione);
		appAutorizadoraplicacione.setAppUsuario(this);

		return appAutorizadoraplicacione;
	}

	public AppAutorizadoraplicaciones removeAppAutorizadoraplicacione(AppAutorizadoraplicaciones appAutorizadoraplicacione) {
		getAppAutorizadoraplicaciones().remove(appAutorizadoraplicacione);
		appAutorizadoraplicacione.setAppUsuario(null);

		return appAutorizadoraplicacione;
	}

	public List<AppPermisos> getAppPermisos() {
		return this.appPermisos;
	}

	public void setAppPermisos(List<AppPermisos> appPermisos) {
		this.appPermisos = appPermisos;
	}

	public AppPermisos addAppPermiso(AppPermisos appPermiso) {
		getAppPermisos().add(appPermiso);
		appPermiso.setAppUsuario(this);

		return appPermiso;
	}

	public AppPermisos removeAppPermiso(AppPermisos appPermiso) {
		getAppPermisos().remove(appPermiso);
		appPermiso.setAppUsuario(null);

		return appPermiso;
	}

	public String getPerId() {
		return perId;
	}
	
	public void setPerId(String perId) {
		this.perId = perId;
	}

}