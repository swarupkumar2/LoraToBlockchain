package lorablockchain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class Payload {
	
	private String payload;

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
