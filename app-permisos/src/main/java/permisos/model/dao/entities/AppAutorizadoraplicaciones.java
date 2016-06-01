package permisos.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the app_autorizadoraplicaciones database table.
 * 
 */
@Entity
@Table(name="app_autorizadoraplicaciones")
@NamedQuery(name="AppAutorizadoraplicaciones.findAll", query="SELECT a FROM AppAutorizadoraplicaciones a")
public class AppAutorizadoraplicaciones implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AppAutorizadoraplicacionesPK id;

	@Column(name="app_estado", columnDefinition="bpchar" ,length=1)
	private String appEstado;

	//bi-directional many-to-one association to AppAplicaciones
	@ManyToOne
	@JoinColumn(name="apl_id", referencedColumnName="apl_id", insertable=false, updatable=false)
	private AppAplicaciones appAplicacione;

	//bi-directional many-to-one association to GenUsuario
	@ManyToOne
	@JoinColumn(name="usu_login", referencedColumnName="usu_login", insertable=false, updatable=false)
	private AppUsuario appUsuario;

	public AppAutorizadoraplicaciones() {
	}

	public AppAutorizadoraplicacionesPK getId() {
		return this.id;
	}

	public void setId(AppAutorizadoraplicacionesPK id) {
		this.id = id;
	}

	public String getAppEstado() {
		return this.appEstado;
	}

	public void setAppEstado(String appEstado) {
		this.appEstado = appEstado;
	}

	public AppAplicaciones getAppAplicacione() {
		return this.appAplicacione;
	}

	public void setAppAplicacione(AppAplicaciones appAplicacione) {
		this.appAplicacione = appAplicacione;
	}

	public AppUsuario getAppUsuario() {
		return this.appUsuario;
	}

	public void setAppUsuario(AppUsuario appUsuario) {
		this.appUsuario = appUsuario;
	}

}