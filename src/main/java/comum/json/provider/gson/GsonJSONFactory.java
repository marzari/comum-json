package comum.json.provider.gson;

import static comum.json.JSONType.ARRAY;
import static comum.json.JSONType.BOOLEAN;
import static comum.json.JSONType.NULL;
import static comum.json.JSONType.NUMBER;
import static comum.json.JSONType.OBJECT;
import static comum.json.JSONType.STRING;

import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import comum.json.JSONArray;
import comum.json.JSONElement;
import comum.json.JSONObject;
import comum.json.JSONValue;

public class GsonJSONFactory {

	public static JSONElement toJSONElement(JsonElement e) {
		if(e.isJsonPrimitive()) return new GsonValue(e.getAsJsonPrimitive());
		if(e.isJsonArray()) return new GsonArray(e.getAsJsonArray());
		if(e.isJsonObject()) return new GsonObject(e.getAsJsonObject());
		if(e.isJsonNull()) return GsonNull.INSTANCE;
		throw new IllegalArgumentException("Connot convert " + e.getClass().getSimpleName() + " into a JSONElement");
	}
	
	public static JsonElement fromJSONElement(JSONElement e) {
		if(e == null || e.type() == NULL) return JsonNull.INSTANCE;
		if(e instanceof GsonElement) {
			GsonElement gsonElement = (GsonElement) e;
			return gsonElement.getWrappedElement();
		}
		if(e.type() == BOOLEAN || e.type() == STRING || e.type() == NUMBER) {
			JSONValue v = (JSONValue) e;
			if(v.type() == BOOLEAN) return new JsonPrimitive(v.bool());
			if(v.type() == STRING) return new JsonPrimitive(v.string());
			if(v.type() == NUMBER) return new JsonPrimitive(v.number());
		}
		if(e.type() == OBJECT) {
			JSONObject object = (JSONObject) e;
			JsonObject gson = new JsonObject();
			for(Entry<String,JSONElement> property : object) {
				gson.add(property.getKey(), fromJSONElement(property.getValue()));
			}
			return gson;
		}
		if(e.type() == ARRAY) {
			JSONArray array = (JSONArray) e;
			JsonArray gson = new JsonArray();
			for(JSONElement item : array) {
				gson.add(fromJSONElement(item));
			}
			return gson;
		}
		throw new IllegalArgumentException("Connot convert " + e.getClass().getSimpleName() + " into a GSON JsonElement");
	}
	
}
