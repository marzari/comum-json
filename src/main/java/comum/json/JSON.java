package comum.json;

import static comum.json.JSONType.BOOLEAN;
import static comum.json.JSONType.NUMBER;
import static comum.json.JSONType.STRING;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import comum.json.builder.JSONArrayBuilder;
import comum.json.builder.JSONBuilder;
import comum.json.builder.JSONObjectBuilder;
import comum.json.provider.gson.builder.GsonBuilder;
import comum.json.walker.AbstractJSONWalker;

public class JSON {

	private static JSONBuilder activeBuilder = new GsonBuilder();
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static void setActiveBuilder(JSONBuilder builder) {
		activeBuilder = builder;
	}

	public class DefaultJSONObjectBuilder implements JSONObject {

		private Map<String, JSONElement> properties;

		private DefaultJSONObjectBuilder() {
			this.properties = new HashMap<>();
		}

		@Override
		public Iterator<Entry<String, JSONElement>> iterator() {
			return properties.entrySet().iterator();
		}

		@Override
		public JSONElement property(String name) {
			return properties.get(name);
		}

		public DefaultJSONObjectBuilder property(String name, JSONElement element) {
			properties.put(name, element);
			return this;
		}

		public DefaultJSONObjectBuilder property(String name, String value) {
			return property(name, JSON.value(value));
		}

		public DefaultJSONObjectBuilder property(String name, Boolean value) {
			return property(name, JSON.value(value));
		}

		public DefaultJSONObjectBuilder property(String name, Number value) {
			return property(name, JSON.value(value));
		}

		@Override
		public Integer propertyCount() {
			return properties.size();
		}

		@Override
		public DefaultJSONWalkerBuilder walk() {
			return new DefaultJSONWalkerBuilder(this);
		}

		@Override
		public String string(String name) {
			JSONElement e = property(name);
			return e != null && e.type() == STRING ? ((JSONValue) e).string() : null;
		}

		@Override
		public Boolean bool(String name) {
			JSONElement e = property(name);
			return e != null && e.type() == BOOLEAN ? ((JSONValue) e).bool() : null;
		}

		@Override
		public Number number(String name) {
			JSONElement e = property(name);
			return e != null && e.type() == NUMBER ? ((JSONValue) e).number() : null;
		}

		@Override
		public BigDecimal decimal(String name) {
			JSONElement e = property(name);
			return e != null && e.type() == NUMBER ? ((JSONValue) e).decimal() : null;
		}

		@Override
		public Integer integer(String name) {
			JSONElement e = property(name);
			return e != null && e.type() == NUMBER ? ((JSONValue) e).integer() : null;
		}

	}

	public class DefaultJSONArrayBuilder implements JSONArray {

		List<JSONElement> items;

		private DefaultJSONArrayBuilder() {
			this.items = new LinkedList<>();
		}

		@Override
		public Iterator<JSONElement> iterator() {
			return items.iterator();
		}

		@Override
		public Integer size() {
			return items.size();
		}

		public DefaultJSONArrayBuilder add(JSONElement... elements) {
			for (JSONElement e : elements) {
				items.add(e);
			}
			return this;
		}
	}

	public class DefaultJSONValue implements JSONValue {

		private JSONType type;
		private Boolean boolValue;
		private String stringValue;
		private Number numberValue;

		private DefaultJSONValue(Boolean value) {
			this.type = BOOLEAN;
			this.boolValue = value;
		}

		private DefaultJSONValue(String value) {
			this.type = STRING;
			this.stringValue = value;
		}

		private DefaultJSONValue(Number value) {
			this.type = NUMBER;
			this.numberValue = value;
		}

		@Override
		public JSONType type() {
			return type;
		}

		@Override
		public String string() {
			return type() == STRING ? stringValue :
					type() == BOOLEAN ? boolValue.toString() :
							numberValue.toString();
		}

		@Override
		public Boolean bool() {
			return type() == BOOLEAN ? boolValue : null;
		}

		@Override
		public Number number() {
			return type() == NUMBER ? numberValue : null;
		}

		@Override
		public BigDecimal decimal() {
			return type() == NUMBER ?
					numberValue instanceof BigDecimal ? (BigDecimal) numberValue :
							new BigDecimal(numberValue.doubleValue())
					: null;
		}

		@Override
		public Integer integer() {
			return type() == NUMBER ? numberValue.intValue() : null;
		}

	}

	public class DefaultJSONWalkerBuilder extends AbstractJSONWalker<DefaultJSONObjectBuilder> {

		private DefaultJSONWalkerBuilder(DefaultJSONObjectBuilder object) {
			super(object);
		}

		@Override
		protected DefaultJSONObjectBuilder createObject(DefaultJSONObjectBuilder parent, String property) {
			DefaultJSONObjectBuilder object = new DefaultJSONObjectBuilder();
			DefaultJSONObjectBuilder parentBuilder = parent;
			parentBuilder.property(property, object);
			return object;
		}

		public DefaultJSONWalkerBuilder set(String path, JSONElement element) {
			Entry<String, DefaultJSONObjectBuilder> pathInfo = getParent(path, true);
			DefaultJSONObjectBuilder parent = pathInfo.getValue();
			parent.property(pathInfo.getKey(), element);
			return this;
		}

		public DefaultJSONWalkerBuilder set(String path, Boolean value) {
			return set(path, new DefaultJSONValue(value));
		}

		public DefaultJSONWalkerBuilder set(String path, String value) {
			return set(path, new DefaultJSONValue(value));
		}

		public DefaultJSONWalkerBuilder set(String path, Number value) {
			return set(path, new DefaultJSONValue(value));
		}

	}

	public static JSONObjectBuilder object() {
		return activeBuilder.object();
	}

	public static JSONArrayBuilder array() {
		return activeBuilder.array();
	}

	public static JSONValue value(String value) {
		return activeBuilder.value(value);
	}

	public static JSONValue value(Boolean value) {
		return activeBuilder.value(value);
	}

	public static JSONValue value(Number value) {
		return activeBuilder.value(value);
	}

	public static JSONNull nothing() {
		return activeBuilder.nothing();
	}

	public static DateTimeFormatter dateFormatter() {
		return dateFormatter;
	}

	public static DateTimeFormatter timeFormatter() {
		return timeFormatter;
	}

	public static DateTimeFormatter dateTimeFormatter() {
		return dateTimeFormatter;
	}

	public static void setDateFormatter(DateTimeFormatter dateFormatter) {
		JSON.dateFormatter = dateFormatter;
	}

	public static void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
		JSON.dateTimeFormatter = dateTimeFormatter;
	}
}
