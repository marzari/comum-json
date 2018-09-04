package comum.json.artefatos;

import static comum.json.artefatos.JType.ENUM;

public class JEnum extends AbstractJVal<Enum> {

	public JEnum(Enum e) {
		super(e);
	}

	@Override
	public JType type() {
		return ENUM;
	}

	public Enum enumType() {
		return value;
	}
}
