package permisos.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the app_modulos database table.
 * 
 */
@Entity
@Table(name="app_modulos")
@NamedQuery(name="AppModulos.findAll", query="SELECT a FROM AppModulos a")
public class AppModulos implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="APP_MODULOS_MODID_GENERATOR", sequenceName="SEQ_APP_MODULOS", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_MODULOS_MODID_GENERATOR")
	@Column(name="mod_id")
	private Integer modId;

	@Column(name="mod_descripcion", columnDefinition="text")
	private String modDescripcion;

	@Column(name="mod_estado", columnDefinition="bpchar", length=1)
	private String modEstado;

	@Column(name="mod_nombre", length=20)
	private String modNombre;

	@Column(name="mod_orden")
	private Integer modOrden;

	//bi-directional many-to-one association to AppAplicaciones
	@ManyToOne
	@JoinColumn(name="apl_id")
	private AppAplicaciones appAplicacione;

	//bi-directional many-to-one association to AppVistas
	@OneToMany(mappedBy="appModulo")
	private List<AppVistas> appVistas;

	public AppModulos() {
	}

	public Integer getModId() {
		return this.modId;
	}

	public void setModId(Integer modId) {
		this.modId = modId;
	}

	public String getModDescripcion() {
		return this.modDescripcion;
	}

	public void setModDescripcion(String modDescripcion) {
		this.modDescripcion = modDescripcion;
	}

	public String getModEstado() {
		return this.modEstado;
	}

	public void setModEstado(String modEstado) {
		this.modEstado = modEstado;
	}

	public String getModNombre() {
		return this.modNombre;
	}

	public void setModNombre(String modNombre) {
		this.modNombre = modNombre;
	}

	public Integer getModOrden() {
		return this.modOrden;
	}

	public void setModOrden(Integer modOrden) {
		this.modOrden = modOrden;
	}

	public AppAplicaciones getAppAplicacione() {
		return this.appAplicacione;
	}

	public void setAppAplicacione(AppAplicaciones appAplicacione) {
		this.appAplicacione = appAplicacione;
	}

	public List<AppVistas> getAppVistas() {
		return this.appVistas;
	}

	public void setAppVistas(List<AppVistas> appVistas) {
		this.appVistas = appVistas;
	}

	public AppVistas addAppVista(AppVistas appVista) {
		getAppVistas().add(appVista);
		appVista.setAppModulo(this);

		return appVista;
	}

	public AppVistas removeAppVista(AppVistas appVista) {
		getAppVistas().remove(appVista);
		appVista.setAppModulo(null);

		return appVista;
	}

}