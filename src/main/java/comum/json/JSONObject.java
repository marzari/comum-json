package comum.json;

import static comum.json.JSONType.ARRAY;
import static comum.json.JSONType.BOOLEAN;
import static comum.json.JSONType.NUMBER;
import static comum.json.JSONType.OBJECT;
import static comum.json.JSONType.STRING;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map.Entry;

import comum.json.builder.JSONArrayBuilder;
import comum.json.walker.JSONWalker;

public interface JSONObject extends JSONElement, Iterable<Entry<String, JSONElement>> {

	JSONElement property(String name);
	
	Integer propertyCount();

	String string(String name);
	
	Boolean bool(String name);
	
	Number number(String name);
	
	BigDecimal decimal(String name);
	
	Integer integer(String name);
	
	default LocalDate date(String name) {
		JSONValue value = value(name);
		if(value != null) {
			return value.date();
		}
		return null;
	}
	
	default LocalDateTime dateTime(String name) {
		JSONValue value = value(name);
		if(value != null) {
			return value.dateTime();
		}
		return null;
	}
	
	default JSONValue value(String name) {
		JSONElement e = property(name);
		if(e != null) {
			JSONType t = e.type();
			if(t == STRING || t == NUMBER || t == BOOLEAN) {
				return (JSONValue) e;	
			}
		}
		return null;
	}
	
	default JSONArray array(String name) {
		JSONElement e = property(name);
		if(e != null && e.type() == ARRAY) {
			return (JSONArray) e;
		}
		return null;
	}
	
	default JSONObject object(String name) {
		JSONElement e = property(name);
		if(e != null && e.type() == OBJECT) {
			return (JSONObject) e;
		}
		return null;
	}
	
	@Override
	default JSONArray asArray() {
		JSONArrayBuilder array = JSON.array();
		for(Entry<String, JSONElement> p : this) {
			array.add(p.getValue());
		}
		return array;
	}
	
	JSONWalker walk();
	
	@Override
	default JSONType type() {
		return OBJECT;
	}
	
}