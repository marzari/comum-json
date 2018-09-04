package comum.json.builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import comum.json.JSONNull;
import comum.json.JSONValue;

public interface JSONBuilder {

	JSONObjectBuilder object();

	JSONArrayBuilder array();

	JSONValue value(String value);

	JSONValue value(Boolean value);

	JSONValue value(Number value);

	JSONValue value(LocalDate value);

	JSONValue value(LocalTime value);

	JSONValue value(LocalDateTime value);

	JSONNull nothing();
}
