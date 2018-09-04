package comum.json.walker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import comum.json.JSONArray;
import comum.json.JSONElement;
import comum.json.JSONObject;
import comum.json.JSONValue;

public interface JSONWalker {

	JSONElement get(String path);

	JSONValue value(String path);

	JSONObject object(String path);

	JSONArray array(String path);

	Boolean bool(String path);

	Number number(String path);

	BigDecimal decimal(String path);

	String string(String path);

	Integer integer(String path);

	LocalDate date(String path);

	LocalTime time(String path);

	LocalDateTime dateTime(String path);

}
