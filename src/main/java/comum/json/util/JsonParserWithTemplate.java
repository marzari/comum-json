package comum.json.util;

import java.util.Map.Entry;

import comum.json.artefatos.JArray;
import comum.json.artefatos.JElement;
import comum.json.artefatos.JObject;
import comum.json.artefatos.JStr;

public class JsonParserWithTemplate {

	private static final String ARRAY = "array";
	private static final String CAMINHO_ARRAY = "caminho_array";

	/**
	 * Dado um conjunto de dados passado por parametro em formato Json e um template de como converter este, o projeto converterá 
	 * o seu conjunto de dados para o template informado.
	 * O objeto a ser mapeado deve ter o caminho completo mapeado no template para poder ser alcançado e transferido para o template(verifique o exemplo abaixo)
	 * @param dados
	 * Ex.: Qualquer tipo de arquivo que tenha um formato Jso
	 * @param template
	 * 	ex.:  {<b>
	  "cedente": {<b>
	    "nome": "nfeProc.NFe.infNFe.emit.xNome",<b>
	    "endereco": {<b>
	      "uf": "nfeProc.NFe.infNFe.emit.enderEmit.UF"<b>
	    }<b>
	  },<b>
	  "sacado": {<b>
	    "nome": "nfeProc.NFe.infNFe.dest.xNome",<b>
	    "endereco": {<b>
	      "uf":"nfeProc.NFe.infNFe.dest.enderDest.UF",<b>
	    }<b>
	  },<b>
	  "duplicatas":{<b> 
			"tipo":"array",<b>
			"caminho_array":"nfeProc.NFe.infNFe.cobr.dup",<b>
			"template":{<b>
				"documento":"nDup",<b>
				"vencimento":"dVenc",<b>
					}<b>
			}<b>
	}<b>
	
	<p>Para iterar um array é necessário criar um atributo com um nome "tipo" e este deve ter o valor array</p>
	<p>O atributo "caminho_array" deve conter a localição do array que deverá ser iterado dentro do arquivo</p>
	<p>O "template" é o template do array que deve ser iterado </p>
	 */
	public JObject converteJsonPorTemplate(JObject dados, JObject template) {
		mergeJObjectWithTemplateXML(dados, template);
		return template;
	}

	private void mergeJObjectWithTemplateXML(JObject dados, JObject templateIterado) {
		for (Entry<String, JElement> elemento : templateIterado) {
			JElement retorno = templateIterado.get(elemento.getKey());
			if (verificaArray(retorno)) {
				JArray array = new JArray();
				JObject templateArray = carregaTemplateArray(retorno.asObject());
				array = mergeJArrayWithTemplateXML(dados.array(retorno.asObject().get(CAMINHO_ARRAY).asVal().string()), templateArray);
				templateIterado.set(elemento.getKey(), array);
			} else if (verificaUltimoNivel(retorno)) {
				templateIterado.set(elemento.getKey(), carregaInformacaoDeJson(dados, ((JStr) retorno).string()));
			} else {
				mergeJObjectWithTemplateXML(dados, retorno.asObject());
			}
		}
	}

	private JArray mergeJArrayWithTemplateXML(JArray arrayIterado, JObject templateArayIterado) {
		JArray retorno = new JArray();
		for (JElement e : arrayIterado) {
			JObject objetoAux = new JObject();
			for (Entry<String, JElement> elemento : templateArayIterado) {
				objetoAux.set(elemento.getKey(), carregaInformacaoDeJson(e.asObject(), templateArayIterado.get(elemento.getKey()).asVal().string()));
			}
			retorno.add(objetoAux);
		}

		return retorno;
	}

	private JObject carregaTemplateArray(JObject template) {
		return template.get("template").asObject();
	}

	private boolean verificaUltimoNivel(JElement elemento) {
		return elemento instanceof JStr;
	}

	private boolean verificaArray(JElement objetoValidacao) {
		if (objetoValidacao instanceof JObject) {
			if (objetoValidacao.asObject().get("tipo") != null) {
				return objetoValidacao.asObject().get("tipo").asVal().string().equals(ARRAY);
			}
		}
		return Boolean.FALSE;
	}

	private String carregaInformacaoDeJson(JObject dados, String caminho) {
		return dados.str(caminho);
	}

}
