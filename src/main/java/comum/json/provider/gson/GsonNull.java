package comum.json.provider.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import comum.json.JSONNull;

public class GsonNull extends GsonElement implements JSONNull {
	
	public static final GsonNull INSTANCE = new GsonNull();
	
	private GsonNull() {}
	
	@Override
	public JsonElement getWrappedElement() {
		return JsonNull.INSTANCE;
	}

}
