package permisos.model.dao.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;


/**
 * The persistent class for the app_permisos database table.
 * 
 */
@Entity
@Table(name="app_permisos")
@NamedQuery(name="AppPermisos.findAll", query="SELECT a FROM AppPermisos a")
public class AppPermisos implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="APP_PERMISOS_PERID_GENERATOR", sequenceName="SEQ_APP_PERMISO", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_PERMISOS_PERID_GENERATOR")
	@Column(name="per_id")
	private Integer perId;

	@Column(name="per_estado", columnDefinition="bpchar", length=1)
	private String perEstado;

	@Column(name="per_fecha_registro")
	private Timestamp perFechaRegistro;

	@Column(name="usu_registro", length=20)
	private String usuRegistro;

	//bi-directional many-to-one association to AppVistas
	@ManyToOne
	@JoinColumn(name="vis_id")
	private AppVistas appVista;

	//bi-directional many-to-one association to GenUsuario
	@ManyToOne
	@JoinColumn(name="usu_login")
	private AppUsuario appUsuario;

	public AppPermisos() {
	}

	public Integer getPerId() {
		return this.perId;
	}

	public void setPerId(Integer perId) {
		this.perId = perId;
	}

	public String getPerEstado() {
		return this.perEstado;
	}

	public void setPerEstado(String perEstado) {
		this.perEstado = perEstado;
	}

	public Timestamp getPerFechaRegistro() {
		return this.perFechaRegistro;
	}

	public void setPerFechaRegistro(Timestamp perFechaRegistro) {
		this.perFechaRegistro = perFechaRegistro;
	}

	public String getUsuRegistro() {
		return this.usuRegistro;
	}

	public void setUsuRegistro(String usuRegistro) {
		this.usuRegistro = usuRegistro;
	}

	public AppVistas getAppVista() {
		return this.appVista;
	}

	public void setAppVista(AppVistas appVista) {
		this.appVista = appVista;
	}

	public AppUsuario getAppUsuario() {
		return this.appUsuario;
	}

	public void setAppUsuario(AppUsuario appUsuario) {
		this.appUsuario = appUsuario;
	}

}