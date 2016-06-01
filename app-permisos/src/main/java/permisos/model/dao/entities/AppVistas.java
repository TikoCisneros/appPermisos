package permisos.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the app_vistas database table.
 * 
 */
@Entity
@Table(name="app_vistas")
@NamedQuery(name="AppVistas.findAll", query="SELECT a FROM AppVistas a")
public class AppVistas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="vis_id", length=100)
	private String visId;

	@Column(name="vis_descripcion", columnDefinition="text")
	private String visDescripcion;

	@Column(name="vis_estado", columnDefinition="bpchar", length=1)
	private String visEstado;

	@Column(name="vis_link", length=100)
	private String visLink;

	@Column(name="vis_nombre", length=100)
	private String visNombre;

	@Column(name="vis_orden")
	private Integer visOrden;

	//bi-directional many-to-one association to AppPerfilpermisos
	@OneToMany(mappedBy="appVista")
	private List<AppPerfilpermisos> appPerfilpermisos;

	//bi-directional many-to-one association to AppPermisos
	@OneToMany(mappedBy="appVista")
	private List<AppPermisos> appPermisos;

	//bi-directional many-to-one association to AppModulos
	@ManyToOne
	@JoinColumn(name="mod_id")
	private AppModulos appModulo;

	public AppVistas() {
	}

	public String getVisId() {
		return this.visId;
	}

	public void setVisId(String visId) {
		this.visId = visId;
	}

	public String getVisDescripcion() {
		return this.visDescripcion;
	}

	public void setVisDescripcion(String visDescripcion) {
		this.visDescripcion = visDescripcion;
	}

	public String getVisEstado() {
		return this.visEstado;
	}

	public void setVisEstado(String visEstado) {
		this.visEstado = visEstado;
	}

	public String getVisLink() {
		return this.visLink;
	}

	public void setVisLink(String visLink) {
		this.visLink = visLink;
	}

	public String getVisNombre() {
		return this.visNombre;
	}

	public void setVisNombre(String visNombre) {
		this.visNombre = visNombre;
	}

	public Integer getVisOrden() {
		return this.visOrden;
	}

	public void setVisOrden(Integer visOrden) {
		this.visOrden = visOrden;
	}

	public List<AppPerfilpermisos> getAppPerfilpermisos() {
		return this.appPerfilpermisos;
	}

	public void setAppPerfilpermisos(List<AppPerfilpermisos> appPerfilpermisos) {
		this.appPerfilpermisos = appPerfilpermisos;
	}

	public AppPerfilpermisos addAppPerfilpermiso(AppPerfilpermisos appPerfilpermiso) {
		getAppPerfilpermisos().add(appPerfilpermiso);
		appPerfilpermiso.setAppVista(this);

		return appPerfilpermiso;
	}

	public AppPerfilpermisos removeAppPerfilpermiso(AppPerfilpermisos appPerfilpermiso) {
		getAppPerfilpermisos().remove(appPerfilpermiso);
		appPerfilpermiso.setAppVista(null);

		return appPerfilpermiso;
	}

	public List<AppPermisos> getAppPermisos() {
		return this.appPermisos;
	}

	public void setAppPermisos(List<AppPermisos> appPermisos) {
		this.appPermisos = appPermisos;
	}

	public AppPermisos addAppPermiso(AppPermisos appPermiso) {
		getAppPermisos().add(appPermiso);
		appPermiso.setAppVista(this);

		return appPermiso;
	}

	public AppPermisos removeAppPermiso(AppPermisos appPermiso) {
		getAppPermisos().remove(appPermiso);
		appPermiso.setAppVista(null);

		return appPermiso;
	}

	public AppModulos getAppModulo() {
		return this.appModulo;
	}

	public void setAppModulo(AppModulos appModulo) {
		this.appModulo = appModulo;
	}

}