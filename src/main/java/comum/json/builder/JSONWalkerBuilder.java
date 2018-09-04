package comum.json.builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import comum.json.JSONElement;
import comum.json.JSONObject;
import comum.json.walker.JSONWalker;

public interface JSONWalkerBuilder extends JSONWalker {

	JSONWalkerBuilder set(String path, JSONElement element);

	JSONWalkerBuilder set(String path, Boolean value);

	JSONWalkerBuilder set(String path, String value);

	JSONWalkerBuilder set(String path, Number value);

	JSONWalkerBuilder set(String path, LocalDate value);

	JSONWalkerBuilder set(String path, LocalTime value);

	JSONWalkerBuilder set(String path, LocalDateTime value);

	JSONWalkerBuilder merge(String path, JSONObject object);

}