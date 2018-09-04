package comum.json.artefatos;

import static comum.json.artefatos.JDateType.LOCALDATE;
import static comum.json.artefatos.JDateType.LOCALDATETIME;
import static comum.json.artefatos.JType.DATE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

public class JDate extends AbstractJVal<Temporal> {

	private final JDateType dateType;

	public JDate(LocalDate date) {
		super(date);
		dateType = LOCALDATE;
	}

	public JDate(LocalDateTime dateTime) {
		super(dateTime);
		dateType = LOCALDATETIME;
	}

	@Override
	public JType type() {
		return DATE;
	}

	@Override
	public LocalDate date() {
		return (LocalDate) value;
	}

	@Override
	public LocalDateTime dateTime() {
		return (LocalDateTime) value;
	}
}
