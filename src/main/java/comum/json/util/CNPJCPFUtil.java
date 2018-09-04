package comum.json.util;

public class CNPJCPFUtil {

	//Método extraido do Netfacor
	public boolean validaCNPJCPF(String nro) {
		nro = limpaCNPJCPF(nro);

		String cpft = nro;

		while (cpft.length() < 11)
			cpft = "0" + cpft;

		if (validaCPF(cpft)) return true;

		String cgct = nro;
		while (cgct.length() < 14)
			cgct = "0" + cgct;

		if (validaCNPJ(cgct)) return true;

		return false;

	}

	//Método extraido do Netfacor
	public String limpaCNPJCPF(String cnpjCpf) {
		if (cnpjCpf == null) {
			return null;
		}
		if (cnpjCpf.length() < 11) {
			return cnpjCpf;
		}
		StringBuffer sb = new StringBuffer();
		int length = cnpjCpf.length();
		for (int i = 0; i < length; i++) {
			char chr = cnpjCpf.charAt(i);
			switch (chr) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				sb.append(chr);
				break;
			}
		}
		return sb.toString();
	}

	//Método extraido do Netfacor
	public boolean validaCPF(String pcpf) {
		boolean sim = false;
		if (pcpf.length() != 11) {
			sim = false;
		} else {
			sim = true;
		}

		String iVl = "";

		if (sim) {
			for (int i = 0; ((i <= (pcpf.length() - 1)) && sim); i++) {
				iVl = String.valueOf(pcpf.charAt(i));
				if ((!iVl.equals("9")) && (!iVl.equals("0")) && (!iVl.equals("1")) && (!iVl.equals("2")) && (!iVl.equals("3")) && (!iVl.equals("4")) && (!iVl.equals("5")) && (!iVl.equals("6")) && (!iVl.equals("7")) && (!iVl.equals("8"))) {
					sim = false;
				}
			}

			if (sim) {
				int soma = 0;
				for (int i = 0; i <= 8; i++) {
					iVl = String.valueOf(pcpf.charAt(i));
					soma = soma + (Integer.parseInt(iVl) * (i + 1));
				}

				int dig = 0;
				int resto = soma % 11;
				if (resto > 9) dig = resto - 10;
				else
					dig = resto;
				if (dig != Integer.parseInt(String.valueOf(pcpf.charAt(9)))) {
					sim = false;
				} else {

					soma = 0;
					for (int i = 0; i <= 7; i++) {
						iVl = String.valueOf(pcpf.charAt(i + 1));
						soma = soma + (Integer.parseInt(iVl) * (i + 1));
					}

					soma = soma + (dig * 9);
					resto = soma % 11;
					if (resto > 9) dig = resto - 10;
					else
						dig = resto;
					if (dig != Integer.parseInt(String.valueOf(pcpf.charAt(10)))) {
						sim = false;
					} else
						sim = true;
				}
			}
		}
		return sim;

	}

	//Método extraido do Netfacor
	public boolean validaCNPJ(String pcgc) {
		// verifica o tamanho
		boolean sim = false;
		if (pcgc.length() != 14) sim = false;
		else
			sim = true;

		String iVl = "";
		if (sim) { // verfica se e numero
			for (int i = 0; ((i <= (pcgc.length() - 1)) && sim); i++) {
				iVl = String.valueOf(pcgc.charAt(i));
				if ((!iVl.equals("9")) && (!iVl.equals("0")) && (!iVl.equals("1")) && (!iVl.equals("2")) && (!iVl.equals("3")) && (!iVl.equals("4")) && (!iVl.equals("5")) && (!iVl.equals("6")) && (!iVl.equals("7")) && (!iVl.equals("8"))) {
					sim = false;
				}
			}
			if (sim) {
				int m1 = 0;
				int m2 = 2;
				int soma1 = 0;
				int soma2 = 0;
				for (int i = 11; i >= 0; i--) {
					iVl = String.valueOf(pcgc.charAt(i));
					m1 = m2;
					if (m2 < 9) {
						m2 = m2 + 1;
					} else {
						m2 = 2;
					}
					soma1 = soma1 + (Integer.parseInt(iVl) * m1);
					soma2 = soma2 + (Integer.parseInt(iVl) * m2);
				} // fim do for de soma

				int d1 = 0;
				int d2 = 0;
				soma1 = soma1 % 11;
				if (soma1 < 2) {
					d1 = 0;
				} else {
					d1 = 11 - soma1;
				}

				soma2 = (soma2 + (2 * d1)) % 11;
				if (soma2 < 2) {
					d2 = 0;
				} else {
					d2 = 11 - soma2;
				}

				if ((d1 == Integer.parseInt(String.valueOf(pcgc.charAt(12)))) && (d2 == Integer.parseInt(String.valueOf(pcgc.charAt(13))))) sim = true;
				else
					sim = false;
			}
		} else {
			sim = false;
		}

		return sim;
	}

}
