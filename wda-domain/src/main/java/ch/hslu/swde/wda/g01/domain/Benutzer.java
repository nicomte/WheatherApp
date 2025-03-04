package ch.hslu.swde.wda.g01.domain;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Benutzer {

	@Id
	@GeneratedValue
	private int id;
	private String benutzerName;
	private String passwort;
	private String vorName;
	private String nachName;
	private String rolle;

	public Benutzer() {

	}

	public Benutzer(String benutzerName, String vorName, String nachName, String rolle) {
		this.benutzerName = benutzerName;
		this.vorName = vorName;
		this.nachName = nachName;
		this.rolle = rolle;
	}

	public int getId() {
		return id;
	}

	public String getBenutzerName() {
		return benutzerName;
	}

	public void setBenutzerName(String benutzerName) {
		this.benutzerName = benutzerName;
	}

	public String getPasswort() {
		return passwort;
	}

	public String getRolle() {
		return rolle;
	}

	public void setRolle(String rolle) {
		this.rolle = rolle;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public String getVorName() {
		return vorName;
	}

	public void setVorName(String vorName) {
		this.vorName = vorName;
	}

	public String getNachName() {
		return nachName;
	}

	public void setNachName(String nachName) {
		this.nachName = nachName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(benutzerName, id, nachName, passwort, rolle, vorName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Benutzer other = (Benutzer) obj;
		return Objects.equals(benutzerName, other.benutzerName) && id == other.id
				&& Objects.equals(nachName, other.nachName) && Objects.equals(passwort, other.passwort)
				&& Objects.equals(rolle, other.rolle) && Objects.equals(vorName, other.vorName);
	}

	@Override
	public String toString() {
		return "Benutzer [id=" + id + ", benutzerName=" + benutzerName + ", passwort=" + passwort + ", vorName="
				+ vorName + ", nachName=" + nachName + ", rolle=" + rolle + "]";
	}

}
