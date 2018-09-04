package comum.json.provider.gson.builder;

import static comum.json.JSON.dateFormatter;
import static comum.json.JSON.dateTimeFormatter;
import static comum.json.JSON.timeFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import comum.json.JSONArray;
import comum.json.JSONElement;
import comum.json.JSONNull;
import comum.json.JSONObject;
import comum.json.JSONType;
import comum.json.JSONValue;
import comum.json.builder.JSONArrayBuilder;
import comum.json.builder.JSONBuilder;
import comum.json.builder.JSONObjectBuilder;
import comum.json.builder.JSONWalkerBuilder;
import comum.json.provider.gson.GsonArray;
import comum.json.provider.gson.GsonElement;
import comum.json.provider.gson.GsonNull;
import comum.json.provider.gson.GsonObject;
import comum.json.provider.gson.GsonValue;
import comum.json.provider.gson.GsonWalker;

public class GsonBuilder implements JSONBuilder {

	private JsonElement extract(JSONElement element) {
		try {
			return ((GsonElement) element).getWrappedElement();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("GsonBuilder only accepts GsonElement instances!", e);
		}
	}

	public class GsonObjectBuilder extends GsonObject implements JSONObjectBuilder {

		private GsonObjectBuilder() {
			super(new JsonObject());
		}

		@Override
		public JSONObjectBuilder property(String name, JSONElement element) {
			jsonObject.add(name, extract(element));
			return this;
		}

		@Override
		public JSONObjectBuilder property(String name, String value) {
			jsonObject.addProperty(name, value);
			return this;
		}

		@Override
		public JSONObjectBuilder property(String name, Boolean value) {
			jsonObject.addProperty(name, value);
			return this;
		}

		@Override
		public JSONObjectBuilder property(String name, Number value) {
			jsonObject.addProperty(name, value);
			return this;
		}

		@Override
		public JSONObjectBuilder property(String name, LocalDate value) {
			jsonObject.addProperty(name, dateFormatter().format(value));
			return this;
		}

		@Override
		public JSONObjectBuilder property(String name, LocalDateTime value) {
			jsonObject.addProperty(name, dateTimeFormatter().format(value));
			return this;
		}

		@Override
		public JSONObjectBuilder delete(String name) {
			jsonObject.remove(name);
			return this;
		}

		@Override
		protected GsonWalker createWalker() {
			return new GsonJsonWalkerBuilder(this);
		}

		@Override
		public JSONWalkerBuilder walk() {
			return (JSONWalkerBuilder) super.walk();
		}

		@Override
		public JSONObjectBuilder merge(JSONObject object) {
			for (Entry<String, JSONElement> property : object) {
				property(property.getKey(), property.getValue());
			}
			return this;
		}
	}

	public class GsonArrayBuilder extends GsonArray implements JSONArrayBuilder {

		private GsonArrayBuilder() {
			super(new JsonArray());
		}

		@Override
		public JSONArrayBuilder addAll(JSONArray array) {
			array.forEach(e -> add(e));
			return this;
		}

		@Override
		public JSONArrayBuilder add(JSONElement... elements) {
			for (JSONElement e : elements) {
				jsonArray.add(extract(e));
			}
			return this;
		}

	}

	private class GsonJsonWalkerBuilder extends GsonWalker implements JSONWalkerBuilder {

		public GsonJsonWalkerBuilder(GsonObjectBuilder root) {
			super(root);
		}

		@Override
		protected GsonObject createObject(GsonObject parent, String property) {
			JsonObject parentObject = parent.getWrappedElement().getAsJsonObject();
			GsonObject child = new GsonObjectBuilder();
			parentObject.add(property, child.getWrappedElement());
			return child;
		}

		@Override
		public JSONWalkerBuilder set(String path, JSONElement element) {
			Entry<String, GsonObject> pathInfo = getParent(path, true);
			GsonObject parent = pathInfo.getValue();
			JsonObject object = parent.getWrappedElement().getAsJsonObject();
			object.add(pathInfo.getKey(), extract(element));
			return this;
		}

		@Override
		public JSONWalkerBuilder set(String path, Boolean value) {
			return set(path, GsonBuilder.this.value(value));
		}

		@Override
		public JSONWalkerBuilder set(String path, String value) {
			return set(path, GsonBuilder.this.value(value));
		}

		@Override
		public JSONWalkerBuilder set(String path, Number value) {
			return set(path, GsonBuilder.this.value(value));
		}

		@Override
		public JSONWalkerBuilder set(String path, LocalDate value) {
			return set(path, GsonBuilder.this.value(value));
		}

		@Override
		public JSONWalkerBuilder set(String path, LocalTime value) {
			return set(path, GsonBuilder.this.value(value));
		}

		@Override
		public JSONWalkerBuilder set(String path, LocalDateTime value) {
			return set(path, GsonBuilder.this.value(value));
		}

		@Override
		public JSONWalkerBuilder merge(String path, JSONObject object) {
			JSONElement property = get(path);
			if (property == null) {
				property = object;
			} else {
				if (property.type() != JSONType.OBJECT)
					throw new IllegalArgumentException("Cannot merge " + property.type() + " property");
				property = new GsonObjectBuilder()
						.merge((JSONObject) property)
						.merge(object);
			}
			set(path, property);
			return this;
		}
	}

	@Override
	public JSONObjectBuilder object() {
		return new GsonObjectBuilder();
	}

	@Override
	public JSONArrayBuilder array() {
		return new GsonArrayBuilder();
	}

	@Override
	public JSONValue value(String value) {
		return new GsonValue(new JsonPrimitive(value));
	}

	@Override
	public JSONValue value(Boolean value) {
		return new GsonValue(new JsonPrimitive(value));
	}

	@Override
	public JSONValue value(Number value) {
		return new GsonValue(new JsonPrimitive(value));
	}

	@Override
	public JSONValue value(LocalDate value) {
		return new GsonValue(new JsonPrimitive(dateFormatter().format(value)));
	}

	@Override
	public JSONValue value(LocalTime value) {
		return new GsonValue(new JsonPrimitive(timeFormatter().format(value)));
	}

	@Override
	public JSONValue value(LocalDateTime value) {
		return new GsonValue(new JsonPrimitive(dateTimeFormatter().format(value)));
	}

	@Override
	public JSONNull nothing() {
		return GsonNull.INSTANCE;
	}

}