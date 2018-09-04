package comum.json.walker;

import static comum.json.JSONType.ARRAY;
import static comum.json.JSONType.OBJECT;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import comum.json.JSONArray;
import comum.json.JSONElement;
import comum.json.JSONObject;
import comum.json.JSONValue;

import java.util.Scanner;

public abstract class AbstractJSONWalker<T extends JSONObject> implements JSONWalker {

	protected T root;

	protected AbstractJSONWalker(T object) {
		this.root = object;
	}

	protected T createObject(T parent, String property) {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Entry<String, T> getParent(String path, boolean create) {
		try (Scanner walk = new Scanner(path)) {
			walk.useDelimiter("\\.");
			T parent = root;
			String property;
			JSONElement e;
			do {
				property = walk.next();
				if (walk.hasNext()) {
					e = parent.property(property);
					if (e == null) {
						if (!create) {
							return null;
						}
						parent = createObject(parent, property);
						if (parent == null) {
							throw new IllegalStateException("Could not create a JSON Object in the path!");
						}
						continue;
					}
					if (e.type() != OBJECT) {
						throw new IllegalArgumentException("Property " + property + " is not a JSON Object!");
					}
					parent = (T) e;
				}
			} while (walk.hasNext());

			return new SimpleEntry<>(property, parent);
		}
	}

	protected JSONElement get(String path, Boolean create) {
		Entry<String, T> pathInfo = getParent(path, create);
		if (pathInfo != null) {
			JSONObject parent = pathInfo.getValue();
			return parent != null ? parent.property(pathInfo.getKey()) : null;
		}
		return null;
	}

	@Override
	public JSONElement get(String path) {
		return get(path, false);
	}

	@Override
	public JSONValue value(String path) {
		JSONElement e = get(path);
		return e != null && e instanceof JSONValue ? (JSONValue) e : null;
	}

	@Override
	public JSONObject object(String path) {
		JSONElement e = get(path);
		return e != null && e.type() == OBJECT ? (JSONObject) e : null;
	}

	@Override
	public JSONArray array(String path) {
		JSONElement e = get(path);
		return e != null && e.type() == ARRAY ? (JSONArray) e : null;
	}

	@Override
	public Boolean bool(String path) {
		JSONValue v = value(path);
		return v != null ? v.bool() : null;
	}

	@Override
	public Number number(String path) {
		JSONValue v = value(path);
		return v != null ? v.number() : null;
	}

	@Override
	public String string(String path) {
		JSONValue v = value(path);
		return v != null ? v.string() : null;
	}

	@Override
	public BigDecimal decimal(String path) {
		JSONValue v = value(path);
		return v != null ? v.decimal() : null;
	}

	@Override
	public Integer integer(String path) {
		JSONValue v = value(path);
		return v != null ? v.integer() : null;
	}

	@Override
	public LocalDate date(String path) {
		JSONValue v = value(path);
		return v != null ? v.date() : null;
	}

	@Override
	public LocalTime time(String path) {
		JSONValue v = value(path);
		return v != null ? v.time() : null;
	}

	@Override
	public LocalDateTime dateTime(String path) {
		JSONValue v = value(path);
		return v != null ? v.dateTime() : null;
	}
}
