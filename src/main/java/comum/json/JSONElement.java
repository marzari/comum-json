
package comum.json;

import static comum.json.JSONType.ARRAY;
import static comum.json.JSONType.BOOLEAN;
import static comum.json.JSONType.NULL;
import static comum.json.JSONType.NUMBER;
import static comum.json.JSONType.OBJECT;
import static comum.json.JSONType.STRING;

public interface JSONElement {

	JSONType type();
	
	default JSONArray asArray() {
		if(type() == ARRAY) return (JSONArray) this;
		throw new IllegalStateException("Element is not a JSONArray instance!");
	}
	
	default JSONObject asObject() {
		if(type() == OBJECT) return (JSONObject) this;
		throw new IllegalStateException("Element is not a JSONObject instance!");
	}
	
	default JSONValue asValue() {
		JSONType t = type();
		if(t == STRING || t == NUMBER || t == BOOLEAN) return (JSONValue) this;
		throw new IllegalStateException("Element is not a JSONValue instance!");
	}
	
	default JSONNull asNull() {
		if(type() == NULL) return (JSONNull) this;
		throw new IllegalStateException("Element is not an JSONNull instance!");
	}
	
}
