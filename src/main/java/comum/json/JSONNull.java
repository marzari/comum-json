package comum.json;

public interface JSONNull extends JSONElement {

	@Override
	default JSONType type() {
		return JSONType.NULL;
	}

}
