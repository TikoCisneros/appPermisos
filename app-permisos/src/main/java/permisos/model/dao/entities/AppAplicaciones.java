package permisos.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the app_aplicaciones database table.
 * 
 */
@Entity
@Table(name="app_aplicaciones")
@NamedQuery(name="AppAplicaciones.findAll", query="SELECT a FROM AppAplicaciones a")
public class AppAplicaciones implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="apl_id", length=10)
	private String aplId;

	@Column(name="apl_descripcion", columnDefinition="text")
	private String aplDescripcion;

	@Column(name="apl_estado", columnDefinition="bpchar" ,length=1)
	private String aplEstado;

	@Column(name="apl_nombre", length=50)
	private String aplNombre;

	@Column(name="apl_prefijo", length=10)
	private String aplPrefijo;

	//bi-directional many-to-one association to AppAutorizadoraplicaciones
	@OneToMany(mappedBy="appAplicacione", fetch= FetchType.EAGER)
	private List<AppAutorizadoraplicaciones> appAutorizadoraplicaciones;

	//bi-directional many-to-one association to AppModulos
	@OneToMany(mappedBy="appAplicacione", fetch= FetchType.EAGER)
	private List<AppModulos> appModulos;

	public AppAplicaciones() {
	}

	public String getAplId() {
		return this.aplId;
	}

	public void setAplId(String aplId) {
		this.aplId = aplId;
	}

	public String getAplDescripcion() {
		return this.aplDescripcion;
	}

	public void setAplDescripcion(String aplDescripcion) {
		this.aplDescripcion = aplDescripcion;
	}

	public String getAplEstado() {
		return this.aplEstado;
	}

	public void setAplEstado(String aplEstado) {
		this.aplEstado = aplEstado;
	}

	public String getAplNombre() {
		return this.aplNombre;
	}

	public void setAplNombre(String aplNombre) {
		this.aplNombre = aplNombre;
	}

	public String getAplPrefijo() {
		return this.aplPrefijo;
	}

	public void setAplPrefijo(String aplPrefijo) {
		this.aplPrefijo = aplPrefijo;
	}

	public List<AppAutorizadoraplicaciones> getAppAutorizadoraplicaciones() {
		return this.appAutorizadoraplicaciones;
	}

	public void setAppAutorizadoraplicaciones(List<AppAutorizadoraplicaciones> appAutorizadoraplicaciones) {
		this.appAutorizadoraplicaciones = appAutorizadoraplicaciones;
	}

	public AppAutorizadoraplicaciones addAppAutorizadoraplicacione(AppAutorizadoraplicaciones appAutorizadoraplicacione) {
		getAppAutorizadoraplicaciones().add(appAutorizadoraplicacione);
		appAutorizadoraplicacione.setAppAplicacione(this);

		return appAutorizadoraplicacione;
	}

	public AppAutorizadoraplicaciones removeAppAutorizadoraplicacione(AppAutorizadoraplicaciones appAutorizadoraplicacione) {
		getAppAutorizadoraplicaciones().remove(appAutorizadoraplicacione);
		appAutorizadoraplicacione.setAppAplicacione(null);

		return appAutorizadoraplicacione;
	}

	public List<AppModulos> getAppModulos() {
		return this.appModulos;
	}

	public void setAppModulos(List<AppModulos> appModulos) {
		this.appModulos = appModulos;
	}

	public AppModulos addAppModulo(AppModulos appModulo) {
		getAppModulos().add(appModulo);
		appModulo.setAppAplicacione(this);

		return appModulo;
	}

	public AppModulos removeAppModulo(AppModulos appModulo) {
		getAppModulos().remove(appModulo);
		appModulo.setAppAplicacione(null);

		return appModulo;
	}

}