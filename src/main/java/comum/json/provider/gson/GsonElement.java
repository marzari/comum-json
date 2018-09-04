package comum.json.provider.gson;

import com.google.gson.JsonElement;

import comum.json.JSONElement;

public abstract class GsonElement implements JSONElement {
	
	public abstract JsonElement getWrappedElement();
	
	@Override
	public String toString() {
		return getWrappedElement().toString();
	}

}
