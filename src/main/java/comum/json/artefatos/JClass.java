package comum.json.artefatos;

import static comum.json.artefatos.JType.CLASS;

public class JClass extends AbstractJVal<Class> {

	public JClass(Class c) {
		super(c);
	}

	@Override
	public JType type() {
		return CLASS;
	}

	public Class classType() {
		return value;
	}
}
