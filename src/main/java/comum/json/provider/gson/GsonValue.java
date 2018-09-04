package comum.json.provider.gson;

import static comum.json.JSONType.BOOLEAN;
import static comum.json.JSONType.NUMBER;
import static comum.json.JSONType.STRING;

import java.math.BigDecimal;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import comum.json.JSONType;
import comum.json.JSONValue;

public class GsonValue extends GsonElement implements JSONValue {

	private JsonPrimitive primitive;
	
	public GsonValue(JsonPrimitive primitive) {
		this.primitive = primitive;
	}

	@Override
	public JSONType type() {
		if(primitive.isBoolean()) return BOOLEAN;
		if(primitive.isString()) return STRING;
		if(primitive.isNumber())  return NUMBER;
		throw new IllegalStateException("No suitable type for json element.");
	}

	@Override
	public JsonElement getWrappedElement() {
		return primitive;
	}

	@Override
	public String string() {
		return primitive.getAsString();
	}

	@Override
	public Boolean bool() {
		return primitive.getAsBoolean();
	}

	@Override
	public Number number() {
		return primitive.getAsNumber();
	}

	@Override
	public BigDecimal decimal() {
		return primitive.getAsBigDecimal();
	}

	@Override
	public Integer integer() {
		return primitive.getAsInt();
	}

}
