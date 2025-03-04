package ch.hslu.swde.wda.g01.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonMappingObject {

	private Ort city;
	private String data;
	@JsonProperty("lastUpdateTime")
	private String lastUpdateTime;

	public Ort getCity() {
		return city;
	}

	public void setCity(Ort city) {
		this.city = city;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return "WeatherData{" + "city=" + city + ", data='" + data + '\'' + ", lastUpdateTime='" + lastUpdateTime + '\''
				+ '}';
	}

}
