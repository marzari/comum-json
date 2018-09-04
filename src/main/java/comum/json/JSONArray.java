package comum.json;

import static comum.json.JSON.array;

import java.util.Iterator;
import java.util.function.Predicate;

import comum.json.builder.JSONArrayBuilder;

public interface JSONArray extends JSONElement, Iterable<JSONElement> {

	Integer size();

	@Override
	default JSONType type() {
		return JSONType.ARRAY;
	}

	default Iterable<JSONObject> objects() {
		final Iterator<JSONElement> elementsIt = iterator();
		return () -> new Iterator<JSONObject>() {
			@Override
			public boolean hasNext() {
				return elementsIt.hasNext();
			}

			@Override
			public JSONObject next() {
				return elementsIt.next().asObject();
			}
		};
	}

	@SuppressWarnings("unchecked")
	default <T extends JSONElement> JSONArray filter(Predicate<T> predicate) {
		JSONArrayBuilder array = array();
		for (JSONElement e : this) {
			if (predicate.test((T) e)) {
				array.add(e);
			}
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	default <T extends JSONElement> JSONArray sort(Predicate<T> predicate) {
		JSONArrayBuilder array = array();
		for (JSONElement e : this) {
			if (predicate.test((T) e)) {
				array.add(e);
			}
		}
		return array;
	}
}
