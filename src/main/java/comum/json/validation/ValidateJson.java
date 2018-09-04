package comum.json.validation;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map.Entry;
import java.util.Scanner;

import comum.json.artefatos.JArray;
import comum.json.artefatos.JBool;
import comum.json.artefatos.JByte;
import comum.json.artefatos.JElement;
import comum.json.artefatos.JNull;
import comum.json.artefatos.JNumber;
import comum.json.artefatos.JObject;
import comum.json.artefatos.JStr;
import comum.json.stream.JElementParser;
import comum.json.util.CEPUtil;
import comum.json.util.CNPJCPFUtil;
import comum.json.util.EmailUtil;

public class ValidateJson {
	public static final String OBRIGATORIO = "REQUIRED";
	public static final String TIPO = "TYPE_MISSMATCH";
	public static final String TAMANHO = "LENGTH_MISSMATCH";
	public static final String NULLO = "NULL_MISSMATCH";
	public static final String DADOS = "DADOS_MISSMATCH";
	public static final String CARACTERES = "INVALID_CHARACTERS";

	private JArray errors = new JArray();

	/**
	 * Recebe os dados a serem validados e um Json com o template do que validar
	 * chaves :{ "required":"", "nullable":"", "type":"", "maximum":"", "minimum":"",
	 * "cpf":"","cnpj":"", "cpf_cnpj":""
	 * }
	 * Chaves obrigatórias:
	 * 	required 
	 * Valor chaves:
	 * 	required : possiveis valores {true, false}
	 * 	nullable : possiveis valores {true, false}
	 * 	type : possiveis valores {string,date,decimal,byteArray,boolean,number,array }
	 * 	maximum : possiveis valores [0-9]
	 * 	minimum : possiveis valores [0-9]
	 * @param dados
	 * @param arquivo_template.json
	 * @return
	 */
	public boolean validate(JObject dados, JObject schema) {
		JObject chavesJSON = carregarSchema(schema.str("nomeSchema"));

		boolean validoObrigatorio = true;
		boolean validoTipo = true;
		boolean validoTamanho = true;
		boolean validoDado = true;
		boolean validoNullo = true;
		boolean validoCaracteres = true;

		for (Entry<String, JElement> entry : chavesJSON) {
			String chave = entry.getKey();
			JObject metadados = entry.getValue().asObject();

			validoObrigatorio = verificaObrigatorio(dados, chave, metadados);
			if (!validoObrigatorio) {
				System.out.println("problema obrigatorio: chave " + chave);
				errors.add(new JObject().set(chave, OBRIGATORIO));
				break;
			}
			validoNullo = verificaNullo(dados, chave, metadados);
			if (!validoNullo) {
				System.out.println("problema nullabe: chave " + chave);
				errors.add(new JObject().set(chave, NULLO));
				break;
			}

			if (dados.has(chave)) {
				validoTipo = verificaTipo(dados, chave, metadados);
				if (!validoTipo) {
					System.out.println("problema tipo: chave " + chave);
					errors.add(new JObject().set(chave, TIPO));
					break;
				}

				validoTamanho = verificaTamanhos(dados, chave, metadados);
				if (!validoTamanho) {
					System.out.println("problema tamanho: chave " + chave);
					errors.add(new JObject().set(chave, TAMANHO));
					break;
				}

				validoDado = verificaDado(dados, chave, metadados);
				if (!validoDado) {
					System.out.println("problema dado: chave " + chave);
					errors.add(new JObject().set(chave, DADOS));
					break;
				}

				validoCaracteres = verificaCaracteres(dados.str(chave), metadados);
				if (!validoCaracteres) {
					System.out.println("problema caracteres: chave " + chave);
					errors.add(new JObject().set(chave, CARACTERES));
					break;
				}
			}
		}

		return validoObrigatorio && validoTipo && validoTamanho & validoDado && validoNullo && validoCaracteres;
	}

	public JArray getErrors() {
		return errors;
	}

	private boolean verificaCaracteres(String value, JObject metadados) {
		boolean valido = true;
		if (metadados.has("caracteresInvalidos")) {

			for (JElement caractere : metadados.array("caracteresInvalidos")) {
				if (value.contains(caractere.asVal().string())) {
					return false;
				}
			}
		}
		return valido;
	}

	/**
	 * Recebe os dados a serem validados e um Json com o template do que validar
	 * chaves :{ "required":"", "nullable":"", "type":"", "maximum":"", "minimum":"",
	 * "cpf":"","cnpj":"", "cpf_cnpj":""
	 * }
	 * Chaves obrigatórias:
	 * 	required 
	 * Valor chaves:
	 * 	required : possiveis valores {true, false}
	 * 	nullable : possiveis valores {true, false}
	 * 	type : possiveis valores {string,date,decimal,byteArray,boolean,number,array }
	 * 	maximum : possiveis valores [0-9]
	 * 	minimum : possiveis valores [0-9]
	 * @param dados
	 * @param arquivo_template.json
	 * @return JObject com todas chaves que deram errado
	 * 	{"chave":["obrigatorio","nullo"],"chave2":["obrigatorio","nullo"]}
	 */
	public JObject validateWithReturn(JObject dados, JObject schema, Boolean validateAllKeys) {
		JObject chavesJSON = carregarSchema(schema.str("nomeSchema"));
		JObject retorno = new JObject();

		boolean validoObrigatorio = true;
		boolean validoTipo = true;
		boolean validoTamanho = true;
		boolean validoDado = true;
		boolean validoNullo = true;

		for (Entry<String, JElement> entry : chavesJSON) {
			String chave = entry.getKey();
			JObject metadados = entry.getValue().asObject();

			validoObrigatorio = verificaObrigatorio(dados, chave, metadados);
			if (!validoObrigatorio) {
				setInfoFailKey(retorno, chave, new JObject().set(OBRIGATORIO, true));
				if (!validateAllKeys) {
					break;
				}
			}
			validoNullo = verificaNullo(dados, chave, metadados);
			if (!validoNullo) {
				setInfoFailKey(retorno, chave, new JObject().set(NULLO, true));
				if (!validateAllKeys) {
					break;
				}
			}
			validoTipo = verificaTipo(dados, chave, metadados);
			if (!validoTipo) {
				setInfoFailKey(retorno, chave, new JObject().set(TIPO, true));
				if (!validateAllKeys) {
					break;
				}
			}
			validoTamanho = verificaTamanhos(dados, chave, metadados);
			if (!validoTamanho) {
				setInfoFailKey(retorno, chave, new JObject().set(TAMANHO, true));
				if (!validateAllKeys) {
					break;
				}
			}
			validoDado = verificaDado(dados, chave, metadados);
			if (!validoDado) {
				setInfoFailKey(retorno, chave, new JObject().set(DADOS, true));
				if (!validateAllKeys) {
					break;
				}
			}
		}

		return retorno;
	}

	private void setInfoFailKey(JObject retorno, String chaveFail, JObject pendency) {

		if (!retorno.has(chaveFail)) {
			JArray pendencies = new JArray();
			pendencies.add(pendency);
			retorno.set(chaveFail, pendencies);
		} else {
			retorno.array(chaveFail).add(pendency);
		}

	}

	public JObject carregarSchema(String nomeSchema) {
		JObject retorno = new JObject();

		StringBuilder result = new StringBuilder();
		InputStream is = ValidateJson.class.getClassLoader().getResourceAsStream(nomeSchema);
		//ClassLoader classLoader = BrokerService.class.getClassLoader();
		//File file = new File(classLoader.getResource(nomeFiltro).getFile());
		try (Scanner scanner = new Scanner(is)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line);
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String json = result.toString();

		JElementParser parser = new JElementParser();
		retorno = parser.parse(json).asObject();

		return retorno;
	}

	private boolean verificaTamanhos(JObject dados, String chave, JObject metadados) {
		boolean validoMinimum = true;
		boolean validoMaximum = true;
		boolean validoMinimumItens = true;

		if (metadados.has("type") && dados.has(chave)) {

			//VERIFICA TAMANHO STRING
			if (metadados.str("type").equals("string") && dados.get(chave) instanceof JStr) {
				if (metadados.has("minimum")) {
					if (!(dados.str(chave).length() >= metadados.integer("minimum"))) {
						validoMinimum = false;
					}
				}

				if (metadados.has("maximum")) {
					if (!(dados.str(chave).length() <= metadados.integer("maximum"))) {
						validoMaximum = false;
					}
				}
				return validoMinimum && validoMaximum;
			}

			//VERIFICA TAMANHO INTEIRO
			if (metadados.str("type").equals("number") && dados.get(chave) instanceof JNumber) {
				if (metadados.has("minimum")) {
					if (!(dados.integer(chave) >= metadados.integer("minimum"))) {
						validoMinimum = false;
					}
				}

				if (metadados.has("maximum")) {
					if (!(dados.integer(chave) <= metadados.integer("maximum"))) {
						validoMaximum = false;
					}
				}

				return validoMinimum && validoMaximum;
			}

			if (metadados.str("type").equals("decimal") && dados.get(chave) instanceof JNumber) {
				if (metadados.has("minimum")) {
					if (!(dados.decimal(chave).compareTo(metadados.decimal("minimum")) > -1)) {
						validoMinimum = false;
					}
				}

				if (metadados.has("maximum")) {
					if (!(dados.decimal(chave).compareTo(metadados.decimal("maximum")) < 1)) {
						validoMaximum = false;
					}
				}

				return validoMinimum && validoMaximum;
			}

			//VERIFICA TAMANHO JARRAY
			if (metadados.str("type").equals("array") && dados.get(chave) instanceof JArray) {
				if (metadados.has("minimumItens")) {
					if (!(dados.array(chave).size() >= metadados.integer("minimumItens"))) {
						validoMinimumItens = false;
					}
				}
				return validoMinimumItens;
			}
		}

		return true;
	}

	private boolean verificaTipo(JObject dados, String chave, JObject metadados) {
		boolean valido = true;

		if (metadados.has("type") && dados.has(chave)) {
			String type = metadados.str("type");
			JElement value = dados.get(chave);
			if (type.equals("string") && value instanceof JStr) {
				valido = true;
			} else if (type.equals("boolean") && value instanceof JBool) {
				valido = true;
			} else if (type.equals("number") && value instanceof JNumber) {
				valido = true;
			} else if (type.equals("array") && value instanceof JArray) {
				valido = true;
			} else if (type.equals("byteArray") && value instanceof JByte) {
				valido = true;
			} else if (type.equals("decimal") && value instanceof JNumber) {
				valido = true;
			} else if (type.equals("date")) {
				LocalDate date = parseLocalDate(dados.str(chave), metadados.str("format"));
				if (date == null) {
					valido = false;
				}
			} else if (metadados.has("nullable") && metadados.bool("nullable").equals(Boolean.TRUE) && value instanceof JNull) {
				valido = true;
			} else {
				valido = false;
			}
		}
		return valido;
	}

	private boolean verificaDado(JObject dados, String chave, JObject metadados) {
		boolean valido = true;
		if (metadados.has("type") && dados.has(chave)) {
			if (metadados.str("type").equals("string")) {
				String value = dados.str(chave);
				if (metadados.has("cpf_cnpj")) {
					CNPJCPFUtil util = new CNPJCPFUtil();
					valido = util.validaCNPJCPF(value);
				}

				if (metadados.has("cnpj")) {
					CNPJCPFUtil util = new CNPJCPFUtil();
					valido = util.validaCNPJ(value);
				}

				if (metadados.has("cpf")) {
					CNPJCPFUtil util = new CNPJCPFUtil();
					valido = util.validaCPF(value);
				}

				if (metadados.has("cep")) {
					CEPUtil util = new CEPUtil();
					valido = util.validarCEP(value);
				}

				if (metadados.has("email")) {
					EmailUtil util = new EmailUtil();
					valido = util.isEmailValid(value);
				}

				if (metadados.has("only_number") && metadados.bool("only_number")) {
					valido = value != null ? value.matches("[0-9]+") : Boolean.TRUE;
				}
			}
		}

		return valido;
	}

	private boolean verificaObrigatorio(JObject dados, String chave, JObject metadados) {
		boolean valido = true;
		if (metadados.hasNotNull("required") && metadados.bool("required") && dados.has(chave)) {
			valido = true;
		} else if (metadados.hasNotNull("required") && !metadados.bool("required")) {
			valido = true;
		} else {
			valido = false;
		}
		return valido;
	}

	private boolean verificaNullo(JObject dados, String chave, JObject metadados) {
		boolean valido = true;
		if (metadados.hasNotNull("nullable") && !metadados.bool("nullable")) {
			if (dados.hasNull(chave)) {
				valido = false;
			}
		}
		return valido;
	}

	public LocalDate parseLocalDate(String dateValue, String format) {
		format = verificaFormato(format);
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			return LocalDate.parse(dateValue, formatter);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Para o caso do campo format for nullo é retornado o padrão experado do localDate
	 * @param format
	 * @return format or formatoDataPadraoLocalDate
	 */
	public String verificaFormato(String format) {
		String formatoDataPadraoLocalDate = "yyyyMMdd";
		if (format == null) {
			return formatoDataPadraoLocalDate;
		}
		return format;
	}

	/**
	 * @param dados
	 * @param schema
	 * @return resultadoValidacoes
	 *		<ul>
	 *			<li>key</li>
	 			<li>invalid</li>
	 *		</ul>
	 * 	
	 */
	public JObject verify(JObject dados, JObject schema) {
		JObject chavesJSON = carregarSchema(schema.str("nomeSchema"));
		JObject resultadoValidacoes = new JObject();

		for (Entry<String, JElement> entry : chavesJSON) {
			String chave = entry.getKey();
			JObject metadados = entry.getValue().asObject();
			if (!verificaObrigatorio(dados, chave, metadados)) {
				resultadoValidacoes.set(chave, OBRIGATORIO);
				continue;
			}

			if (!verificaTipo(dados, chave, metadados)) {
				resultadoValidacoes.set(chave, TIPO);
				continue;
			}

			if (!verificaTamanhos(dados, chave, metadados)) {
				resultadoValidacoes.set(chave, TAMANHO);
				continue;
			}
		}

		return resultadoValidacoes;
	}
}
