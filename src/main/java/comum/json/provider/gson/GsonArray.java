package comum.json.provider.gson;

import static comum.json.provider.gson.GsonJSONFactory.toJSONElement;

import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import comum.json.JSONArray;
import comum.json.JSONElement;

public class GsonArray extends GsonElement implements JSONArray {

	protected JsonArray jsonArray;
	
	public GsonArray(JsonArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	@Override
	public Iterator<JSONElement> iterator() {
		final Iterator<JsonElement> it = jsonArray.iterator();
		return new Iterator<JSONElement>() {
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public JSONElement next() {
				return toJSONElement(it.next());
			}
		};
	}

	@Override
	public Integer size() {
		return jsonArray.size();
	}

	@Override
	public JsonElement getWrappedElement() {
		return jsonArray;
	}

}
