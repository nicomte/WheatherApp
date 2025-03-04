package ch.hslu.swde.wda.g01.domain;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Ort {
	// plz auf zip geaendert, da API-Export zip verwendet

	@Id
	@GeneratedValue
	private int id;
	private int zip;
	private String name;

	public Ort() {

	}

	public Ort(int zip, String name) {
		this.zip = zip;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, zip);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ort other = (Ort) obj;
		return Objects.equals(name, other.name) && zip == other.zip;
	}

	@Override
	public String toString() {
		return "Ort [zip=" + zip + ", name=" + name + "]";

	}

}
