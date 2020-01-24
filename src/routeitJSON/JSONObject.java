package routeitJSON;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONObject {

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("start")
	@Expose
	private String start;
	@SerializedName("end")
	@Expose
	private String end;
	@SerializedName("waypoint")
	@Expose
	private List<String> waypoint = null;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStart() {
		return start;
	}
	
	public void setStart(String start) {
		this.start = start;
	}
	
	public String getEnd() {
		return end;
	}
	
	public void setEnd(String end) {
		this.end = end;
	}
	
	public List<String> getWaypoint() {
		return waypoint;
	}
	
	public void setWaypoint(List<String> waypoint) {
		this.waypoint = waypoint;
	}

}