package lorablockchain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class Metadata {
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private String time;

}
