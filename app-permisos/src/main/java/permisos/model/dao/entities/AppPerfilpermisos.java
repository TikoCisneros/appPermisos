package permisos.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the app_perfilpermisos database table.
 * 
 */
@Entity
@Table(name="app_perfilpermisos")
@NamedQuery(name="AppPerfilpermisos.findAll", query="SELECT a FROM AppPerfilpermisos a")
public class AppPerfilpermisos implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="APP_PERFILPERMISOS_PPEID_GENERATOR", sequenceName="SEQ_APP_PERFILPERMISOS", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_PERFILPERMISOS_PPEID_GENERATOR")
	@Column(name="ppe_id")
	private Integer ppeId;

	@Column(name="ppe_estado", columnDefinition="bpchar", length=1)
	private String ppeEstado;

	//bi-directional many-to-one association to AppPerfiles
	@ManyToOne
	@JoinColumn(name="per_id")
	private AppPerfiles appPerfile;

	//bi-directional many-to-one association to AppVistas
	@ManyToOne
	@JoinColumn(name="vis_id")
	private AppVistas appVista;

	public AppPerfilpermisos() {
	}

	public Integer getPpeId() {
		return this.ppeId;
	}

	public void setPpeId(Integer ppeId) {
		this.ppeId = ppeId;
	}

	public String getPpeEstado() {
		return this.ppeEstado;
	}

	public void setPpeEstado(String ppeEstado) {
		this.ppeEstado = ppeEstado;
	}

	public AppPerfiles getAppPerfile() {
		return this.appPerfile;
	}

	public void setAppPerfile(AppPerfiles appPerfile) {
		this.appPerfile = appPerfile;
	}

	public AppVistas getAppVista() {
		return this.appVista;
	}

	public void setAppVista(AppVistas appVista) {
		this.appVista = appVista;
	}

}