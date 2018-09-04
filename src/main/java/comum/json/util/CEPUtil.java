package comum.json.util;

import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;

public class CEPUtil {

	public Boolean validarCEP(String cep) {
		String numeroCep = limpaCEP(cep);
		if (numeroCep != null && numeroCep.length() != 8) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public String getCEPFormatado(String CEPString) {
		JFormattedTextField CEP = new javax.swing.JFormattedTextField();

		String CEPStringNormalizado = CEPString.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "").replaceAll(" ", "");
		if (CEPStringNormalizado.length() == 8) {
			try {
				CEP.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
			} catch (ParseException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
		CEP.setText(CEPStringNormalizado);
		return CEP.getText();
	}

	public String limpaCEP(String cep) {
		if (cep == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		int length = cep.length();
		for (int i = 0; i < length; i++) {
			char chr = cep.charAt(i);
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

}
