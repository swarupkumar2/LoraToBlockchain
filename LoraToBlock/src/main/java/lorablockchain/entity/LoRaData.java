package lorablockchain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class LoRaData {
	
	private String app_id;
	private String dev_id;
	private String hardware_serial;
	private String port;
	private String counter;
	private String payload_raw;	
	private String downlink_url;	
	private Payload payload_fields;
	private Metadata metadata;
	
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getDev_id() {
		return dev_id;
	}
	public void setDev_id(String dev_id) {
		this.dev_id = dev_id;
	}
	public String getHardware_serial() {
		return hardware_serial;
	}
	public void setHardware_serial(String hardware_serial) {
		this.hardware_serial = hardware_serial;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getCounter() {
		return counter;
	}
	public void setCounter(String counter) {
		this.counter = counter;
	}
	public String getPayload_raw() {
		return payload_raw;
	}
	public void setPayload_raw(String payloadraw) {
		this.payload_raw = payloadraw;
	}
	public String getDownlink_url() {
		return downlink_url;
	}
	public void setDownlink_url(String downlink_url) {
		this.downlink_url = downlink_url;
	}
	public Payload getPayload_fields() {
		return payload_fields;
	}
	public void setPayload_fields(Payload payload_fields) {
		this.payload_fields = payload_fields;
	}
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}	
	
}
