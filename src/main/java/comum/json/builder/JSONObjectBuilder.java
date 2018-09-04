package comum.json.builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import comum.json.JSONElement;
import comum.json.JSONObject;

public interface JSONObjectBuilder extends JSONObject {
	
	JSONObjectBuilder property(String name, JSONElement element);
	
	JSONObjectBuilder property(String name, String value);
	
	default <T extends Enum<T>> JSONObjectBuilder property(String name, T value) {
		return property(name, value.name());
	}
	
	JSONObjectBuilder property(String name, Boolean value);
	
	JSONObjectBuilder property(String name, Number value);
	
	JSONObjectBuilder property(String name, LocalDate value);
	
	JSONObjectBuilder property(String name, LocalDateTime value);
	
	JSONObjectBuilder delete(String name);
	
	JSONObjectBuilder merge(JSONObject object);
	
	@Override
	JSONWalkerBuilder walk();
	
}