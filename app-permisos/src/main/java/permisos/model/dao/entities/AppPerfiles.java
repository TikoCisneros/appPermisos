package permisos.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the app_perfiles database table.
 * 
 */
@Entity
@Table(name="app_perfiles")
@NamedQuery(name="AppPerfiles.findAll", query="SELECT a FROM AppPerfiles a")
public class AppPerfiles implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="APP_PERFILES_PERID_GENERATOR", sequenceName="SEQ_APP_PERFILES", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_PERFILES_PERID_GENERATOR")
	@Column(name="per_id")
	private Integer perId;

	@Column(name="per_descripcion", columnDefinition="text")
	private String perDescripcion;

	@Column(name="per_estado", columnDefinition="bpchar", length=1)
	private String perEstado;

	@Column(name="per_nombre", length=20)
	private String perNombre;

	//bi-directional many-to-one association to AppPerfilpermisos
	@OneToMany(mappedBy="appPerfile")
	private List<AppPerfilpermisos> appPerfilpermisos;

	public AppPerfiles() {
	}

	public Integer getPerId() {
		return this.perId;
	}

	public void setPerId(Integer perId) {
		this.perId = perId;
	}

	public String getPerDescripcion() {
		return this.perDescripcion;
	}

	public void setPerDescripcion(String perDescripcion) {
		this.perDescripcion = perDescripcion;
	}

	public String getPerEstado() {
		return this.perEstado;
	}

	public void setPerEstado(String perEstado) {
		this.perEstado = perEstado;
	}

	public String getPerNombre() {
		return this.perNombre;
	}

	public void setPerNombre(String perNombre) {
		this.perNombre = perNombre;
	}

	public List<AppPerfilpermisos> getAppPerfilpermisos() {
		return this.appPerfilpermisos;
	}

	public void setAppPerfilpermisos(List<AppPerfilpermisos> appPerfilpermisos) {
		this.appPerfilpermisos = appPerfilpermisos;
	}

	public AppPerfilpermisos addAppPerfilpermiso(AppPerfilpermisos appPerfilpermiso) {
		getAppPerfilpermisos().add(appPerfilpermiso);
		appPerfilpermiso.setAppPerfile(this);

		return appPerfilpermiso;
	}

	public AppPerfilpermisos removeAppPerfilpermiso(AppPerfilpermisos appPerfilpermiso) {
		getAppPerfilpermisos().remove(appPerfilpermiso);
		appPerfilpermiso.setAppPerfile(null);

		return appPerfilpermiso;
	}

}