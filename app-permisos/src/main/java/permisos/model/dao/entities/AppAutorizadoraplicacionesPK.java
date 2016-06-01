package permisos.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the app_autorizadoraplicaciones database table.
 * 
 */
@Embeddable
public class AppAutorizadoraplicacionesPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="apl_id", insertable=false, updatable=false)
	private String aplId;

	@Column(name="usu_login", insertable=false, updatable=false)
	private String usuLogin;

	public AppAutorizadoraplicacionesPK() {
	}
	public String getAplId() {
		return this.aplId;
	}
	public void setAplId(String aplId) {
		this.aplId = aplId;
	}
	public String getUsuLogin() {
		return this.usuLogin;
	}
	public void setUsuLogin(String usuLogin) {
		this.usuLogin = usuLogin;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AppAutorizadoraplicacionesPK)) {
			return false;
		}
		AppAutorizadoraplicacionesPK castOther = (AppAutorizadoraplicacionesPK)other;
		return 
			this.aplId.equals(castOther.aplId)
			&& this.usuLogin.equals(castOther.usuLogin);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.aplId.hashCode();
		hash = hash * prime + this.usuLogin.hashCode();
		
		return hash;
	}
}