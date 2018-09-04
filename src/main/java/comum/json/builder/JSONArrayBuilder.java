package comum.json.builder;

import static comum.json.JSON.value;

import comum.json.JSONArray;
import comum.json.JSONElement;

public interface JSONArrayBuilder extends JSONArray {

	JSONArrayBuilder add(JSONElement... elements);

	JSONArrayBuilder addAll(JSONArray array);

	default JSONArrayBuilder add(String value) {
		return add(value(value));
	}

	default JSONArrayBuilder add(Boolean value) {
		return add(value(value));
	}

	default JSONArrayBuilder add(Number value) {
		return add(value(value));
	}

	default <T extends Enum<T>> JSONArrayBuilder add(T value) {
		return add(value.name());
	}

}