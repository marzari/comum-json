package comum.json.util;

import java.io.InputStream;
import java.util.Scanner;

import comum.json.artefatos.JObject;
import comum.json.stream.JElementParser;

public class LoadJsonBySchema {

	/**
	 * Dado um schema carregado em seu projeto, basta passar o nome para este utilitário que recebera o Json extraido
	 * @param nomeSchema O nome do schema em seu projeto
	 * @return um objeto json com as informações contidas dentro de seu schema
	 */
	public JObject carregarSchema(String nomeSchema) {
		JObject retorno = new JObject();

		StringBuilder result = new StringBuilder();
		InputStream is = LoadJsonBySchema.class.getClassLoader().getResourceAsStream(nomeSchema);
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
}
