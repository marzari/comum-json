package comum.json;

import static comum.json.JSON.dateFormatter;
import static comum.json.JSON.dateTimeFormatter;
import static comum.json.JSON.timeFormatter;
import static comum.json.JSONType.NUMBER;
import static comum.json.JSONType.STRING;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface JSONValue extends JSONElement {

	String string();

	Boolean bool();

	Number number();

	BigDecimal decimal();

	Integer integer();

	default LocalDate date() {
		JSONType t = type();
		if (t == STRING) {
			return LocalDate.parse(string(), dateFormatter());
		}
		if (t == NUMBER) {
			Instant instant = Instant.ofEpochMilli(decimal().longValue());
			return LocalDate.from(instant);
		}
		throw new IllegalStateException("JSONValue needs to be either of type STRING or NUMBER to be converted into LocalDate");
	}

	default LocalTime time() {
		JSONType t = type();
		if (t == STRING) {
			return LocalTime.parse(string(), timeFormatter());
		}
		if (t == NUMBER) {
			Instant instant = Instant.ofEpochMilli(decimal().longValue());
			return LocalTime.from(instant);
		}
		throw new IllegalStateException("JSONValue needs to be either of type STRING or NUMBER to be converted into LocalTime");
	}

	default LocalDateTime dateTime() {
		JSONType t = type();
		if (t == STRING) {
			return LocalDateTime.parse(string(), dateTimeFormatter());
		}
		if (t == NUMBER) {
			Instant instant = Instant.ofEpochMilli(decimal().longValue());
			return LocalDateTime.from(instant);
		}
		throw new IllegalStateException("JSONValue needs to be either of type STRING or NUMBER to be converted into LocalDateTime");
	}

}
