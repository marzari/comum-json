package comum.json.provider.gson;

import static comum.json.provider.gson.GsonJSONFactory.toJSONElement;

import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import comum.json.JSONElement;
import comum.json.JSONObject;
import comum.json.walker.JSONWalker;

public class GsonObject extends GsonElement implements JSONObject {

	protected JsonObject jsonObject;
	protected GsonWalker walker;
	
	public GsonObject(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
		this.walker = createWalker();
	}
	
	protected GsonWalker createWalker() {
		return new GsonWalker(this);
	}
	
	@Override
	public JSONElement property(String name) {
		JsonElement e = jsonObject.get(name);
		return e != null ? toJSONElement(e) : null;
	}

	@Override
	public Integer propertyCount() {
		return jsonObject != null ? jsonObject.entrySet().size() : 0;
	}

	@Override
	public JsonElement getWrappedElement() {
		return jsonObject;
	}

	@Override
	public JSONWalker walk() {
		return this.walker;
	}

	@Override
	public Iterator<Entry<String, JSONElement>> iterator() {
		return jsonObject
				.entrySet()
				.stream()
				.map((e) -> (Entry<String, JSONElement>) 
						new SimpleEntry<String, JSONElement>(e.getKey(), toJSONElement(e.getValue())))
				.collect(Collectors.toList())
				.iterator();
	}

	@Override
	public String string(String name) {
		JsonElement element = getOrNull(name);
		return element != null ? element.getAsString() : null;
	}

	@Override
	public Boolean bool(String name) {
		JsonElement element = getOrNull(name);
		return element != null ? element.getAsBoolean() : null;
	}

	@Override
	public Number number(String name) {
		JsonElement element = getOrNull(name);
		return element != null ? element.getAsNumber() : null;
	}

	@Override
	public BigDecimal decimal(String name) {
		JsonElement element = getOrNull(name);
		return element != null ? element.getAsBigDecimal() : null;
	}

	@Override
	public Integer integer(String name) {
		JsonElement element = getOrNull(name);
		return element != null ? element.getAsInt() : null;
	}
	
	private JsonElement getOrNull(String name) {
		JsonElement element = jsonObject.get(name);
		return element == JsonNull.INSTANCE ? null : element;
	}
	
}
