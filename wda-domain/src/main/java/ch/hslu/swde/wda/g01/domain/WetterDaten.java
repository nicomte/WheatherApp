package ch.hslu.swde.wda.g01.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;

@NamedQuery(name = "WetterDaten.findByPlaceDateTime", query = "SELECT e FROM WetterDaten e "
		+ "WHERE e.ort.name = :name " + "AND (" + "  (e.datum = :datumVon AND e.uhrZeit >= :uhrZeitVon) " + // Start
																											// date and
																											// time
																											// condition
		"  OR (e.datum = :datumBis AND e.uhrZeit <= :uhrZeitBis) " + // End date and time condition
		"  OR (e.datum > :datumVon AND e.datum < :datumBis)" + // Full days in between
		")")

//<<<<<<< HEAD Changes MT
@NamedQuery(name = "WetterDaten.findMaxValuesByPlaceDateTime", query = "SELECT MAX(e.luftDruck), MAX(e.luftFeuchtigkeit), MAX(e.temperatur) FROM WetterDaten e WHERE e.ort=:ort AND e.datum=:datum AND e.uhrZeit<=:uhrZeit")
@NamedQuery(name = "WetterDaten.findMaxValuesByDateTime", query = "SELECT MAX(e.luftDruck), MAX(e.luftFeuchtigkeit), MAX(e.temperatur) FROM WetterDaten e WHERE e.datum=:datum AND e.uhrZeit<=:uhrZeitBis AND e.uhrZeit >= :uhrZeitVon")
@NamedQuery(name = "WetterDaten.findMaxDateTime", query = "SELECT MAX(e.datum), MAX(e.uhrZeit) FROM WetterDaten e")
@NamedQuery(name = "WetterDaten.findMaxDate", query = "SELECT MAX(e.datum) FROM WetterDaten e")
@NamedQuery(name = "WetterDaten.findMaxTimeWhereDatum", query = "SELECT MAX(e.uhrZeit) FROM WetterDaten e WHERE e.datum=:datum")
// Changes from NG
/*
 * @NamedQuery(name = "WetterDaten.findMaxValuesByPlaceDateTime", query =
 * "SELECT MAX(e.luftDruck), MAX(e.luftFeuchtigkeit), MAX(e.temperatur) FROM WetterDaten e WHERE e.ort=:ort e.datum<=:datumBis AND e.datum>=:datumVon AND e.uhrZeit>=:uhrZeitVon AND e.uhrZeit<=:uhrZeitBis"
 * )
 * 
 * @NamedQuery(name = "WetterDaten.findMaxValuesByDateTime", query =
 * "SELECT MAX(e.luftDruck), MAX(e.luftFeuchtigkeit), MAX(e.temperatur) FROM WetterDaten e WHERE e.datum<=:datumBis AND e.datum>=:datumVon AND e.uhrZeit>=:uhrZeitVon AND e.uhrZeit<=:uhrZeitBis"
 * )
 * 
 * @NamedQuery(name = "WetterDaten.findMaxDateTime", query =
 * "SELECT MAX(e.uhrZeit) FROM WetterDaten e WHERE e.datum = (SELECT MAX(e.datum) FROM WetterDaten e)"
 * )
 */
// >>>>>>> 3d5e5d0c885fa576de1c28c834415a0ed7c17479

@Entity
public class WetterDaten {

	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	private Ort ort;
	private LocalTime uhrZeit;
	private LocalDate datum;
	private int luftDruck;
	private int luftFeuchtigkeit;
	private double temperatur;

	public WetterDaten() {

	}

	public WetterDaten(Ort ort, LocalTime uhrZeit, LocalDate datum, int luftDruck, int luftFeuchtigkeit,
			double temperatur) {
		this.ort = ort;
		this.uhrZeit = uhrZeit;
		this.datum = datum;
		this.luftDruck = luftDruck;
		this.luftFeuchtigkeit = luftFeuchtigkeit;
		this.temperatur = temperatur;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Ort getOrt() {
		return ort;
	}

	public void setOrt(Ort ort) {
		this.ort = ort;
	}

	public LocalTime getUhrZeit() {
		return uhrZeit;
	}

	public void setUhrZeit(LocalTime uhrZeit) {
		this.uhrZeit = uhrZeit;
	}

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	public int getLuftDruck() {
		return luftDruck;
	}

	public void setLuftDruck(int luftDruck) {
		this.luftDruck = luftDruck;
	}

	public int getLuftFeuchtigkeit() {
		return luftFeuchtigkeit;
	}

	public void setLuftFeuchtigkeit(int luftFeuchtigkeit) {
		this.luftFeuchtigkeit = luftFeuchtigkeit;
	}

	public double getTemperatur() {
		return temperatur;
	}

	public void setTemperatur(double temperatur) {
		this.temperatur = temperatur;
	}

	@Override
	public int hashCode() {
		return Objects.hash(datum, luftDruck, luftFeuchtigkeit, ort, temperatur, uhrZeit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WetterDaten other = (WetterDaten) obj;
		return Objects.equals(datum, other.datum) && luftDruck == other.luftDruck
				&& luftFeuchtigkeit == other.luftFeuchtigkeit && Objects.equals(ort, other.ort)
				&& Double.doubleToLongBits(temperatur) == Double.doubleToLongBits(other.temperatur)
				&& Objects.equals(uhrZeit, other.uhrZeit);
	}

	@Override
	public String toString() {
		return "WetterDaten [ort=" + ort + ", uhrZeit=" + uhrZeit + ", datum=" + datum + ", luftDruck=" + luftDruck
				+ ", luftFeuchtigkeit=" + luftFeuchtigkeit + ", temperatur=" + temperatur + "]";
	}

}
