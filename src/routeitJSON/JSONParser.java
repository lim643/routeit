package routeitJSON;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONParser {
	
	private ArrayList<JSONObject> jo;

	public JSONParser(String JSON) {
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<JSONObject>>() {}.getType();
		jo = gson.fromJson(JSON, type);
	}

	public ArrayList<JSONObject> getJSONObjects() { return jo; }
}
