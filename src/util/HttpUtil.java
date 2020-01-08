package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import exception.McConnectionException;

/**
 * Classe utilitária para enviar requisições GET/POST
 *
 * @author michael
 */
public class HttpUtil {

	private static final int TIMEOUT_VALUE = 30000; // 30 segundos

	private static final String PADRAO_ENCODE = StandardCharsets.ISO_8859_1.name();

	private HttpUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Invoca uma URL via <b>GET</b> para executar alguma atividade que retorna <b>texto</b>
	 *
	 * @param url
	 *            - URL contendo os parâmetros
	 * @return - Conteúdo da resposta em uma lista de String
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 */
	public static List<String> doGet(String url) throws McConnectionException {

		return doGet(url, null);
	}

	/**
	 * Invoca uma URL via <b>GET</b> para executar alguma atividade que retorna <b>texto</b>
	 *
	 * @param url
	 *            - URL contendo os parâmetros
	 * @param parametros
	 *            - Parametros da url que serão passados via GET (query param)
	 * @return - Conteúdo da resposta em uma lista de String
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 */
	public static List<String> doGet(String url, Map<String, String> parametros) throws McConnectionException {

		List<String> retorno = new ArrayList<>();
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		try {
			conn = prepararConexaoGet(url, parametros);
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String linha = null;
			while ((linha = reader.readLine()) != null) {
				retorno.add(linha);
			}
		} catch (Exception e) {
			throwMcConnectionException(conn, e);
		} finally {
			clearHttpConn(conn);
			Funcoes.closeQuietly(reader);
		}
		return retorno;
	}

	/**
	 * Invoca uma url via <b>GET</b> para executar alguma atividade que retorna <b>JSON</b>
	 *
	 * @param url
	 *            - URL contendo os parâmetros
	 * @param tipoRetorno
	 *            - Tipo do objeto que será criado a partir do json resposta
	 * @return - Objeto do tipo retorno que corresponde a resposta em json dada pela atividade
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 * @author michael
	 */
	public static <T> T doGetJson(String url, Class<T> tipoRetorno) throws McConnectionException {

		return doGetJson(url, null, null, tipoRetorno);
	}

	/**
	 * Invoca uma url via <b>GET</b> para executar alguma atividade que retorna <b>JSON</b>
	 *
	 * @param url
	 *            - URL contendo os parâmetros
	 * @param parametros
	 *            - Parametros da url que serão passados via GET (query param)
	 * @param tipoRetorno
	 *            - Tipo do objeto que será criado a partir do json resposta
	 * @return - Objeto do tipo retorno que corresponde a resposta em json dada pela atividade
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 * @author michael
	 */
	public static <T> T doGetJson(String url, Map<String, String> parametros, Class<T> tipoRetorno) throws McConnectionException {

		return doGetJson(url, parametros, null, tipoRetorno);
	}

	/**
	 * Invoca uma url via <b>GET</b> para executar alguma atividade que retorna <b>JSON</b>
	 *
	 * @param url
	 *            - URL contendo os parâmetros
	 * @param chaveRetorno
	 *            - Propriedade cujo o value será parseado para criação do objeto resposta ou
	 *            <b>null</b> caso todo o json seja a representação do objeto resposta
	 * @param tipoRetorno
	 *            - Tipo do objeto que será criado a partir do json resposta
	 * @return - Objeto do tipo retorno que corresponde a resposta em json dada pela atividade
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 * @author michael
	 */
	public static <T> T doGetJson(String url, Class<T> tipoRetorno, String chaveRetorno)
			throws McConnectionException {

		return doGetJson(url, null, chaveRetorno, tipoRetorno);
	}

	/**
	 * Invoca uma url via <b>GET</b> para executar alguma atividade que retorna <b>JSON</b>
	 *
	 * @param url
	 *            - URL contendo os parâmetros
	 * @param parametros
	 *            - Parametros da url que serão passados via GET (query param)
	 * @param chaveRetorno
	 *            - Propriedade cujo o value será parseado para criação do objeto resposta ou
	 *            <b>null</b> caso todo o json seja a representação do objeto resposta
	 * @param tipoRetorno
	 *            - Tipo do objeto que será criado a partir do json resposta
	 * @return - Objeto do tipo retorno que corresponde a resposta em json dada pela atividade
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 * @author michael
	 */
	public static <T> T doGetJson(String url, Map<String, String> parametros, String chaveRetorno, Class<T> tipoRetorno)
			throws McConnectionException {

		T retorno = null;
		HttpURLConnection conn = null;
		JsonElement jelResult = null;
		try {
			conn = prepararConexaoGet(url, parametros);
			jelResult = findJsonElement(chaveRetorno, conn);
			if (jelResult != null) {
				Gson gson = new Gson();
				retorno = gson.fromJson(jelResult, tipoRetorno);
			}
		} catch (Exception e) {
			throwMcConnectionException(conn, e);
		} finally {
			HttpUtil.clearHttpConn(conn);
		}
		return retorno;
	}

	/**
	 * Invoca uma url via <b>POST</b> para executar alguma atividade que retorna <b>texto</b>
	 *
	 * @param url
	 *            - URL de invocação
	 * @param parametros
	 *            - Parametros da url que serão passados via POST
	 * @return - Conteúdo da resposta em uma lista de String
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 * @author michael
	 */
	public static List<String> doPost(String url, Map<String, String> parametros) throws McConnectionException {

		return doPost(url, parametros, null);
	}

	/**
	 * Invoca uma url via <b>POST</b> para executar alguma atividade que retorna <b>texto</b><br>
	 * A configuração usada é a do setup
	 *
	 * @param url
	 *            - URL de invocação
	 * @param parametros
	 *            - Parametros da url que serão passados via POST
	 * @param setup
	 * 			  - Configuração
	 * @return - Conteúdo da resposta em uma lista de String
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 * @author michael
	 */
	public static List<String> doPost(String url, Map<String, String> parametros, HttpSetup setup) throws McConnectionException {
		List<String> retorno = new ArrayList<>();
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		try {
			conn = prepararConexaoPost(url, parametros, setup);
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String linha = null;
			while ((linha = reader.readLine()) != null) {
				retorno.add(linha);
			}
		} catch (Exception e) {
			throwMcConnectionException(conn, e);
		} finally {
			HttpUtil.clearHttpConn(conn);
		}
		return retorno;
	}

	/**
	 * Invoca uma url via <b>POST</b> para executar alguma atividade que retorna <b>JSON</b>
	 *
	 * @param url
	 *            - URL contendo os parâmetros
	 * @param parametros
	 *            - Parametros da url que serão passados via POST
	 * @param tipoRetorno
	 *            - Tipo do objeto que será criado a partir do json resposta
	 * @return - Objeto do tipo retorno que corresponde a resposta em json dada pela atividade
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 * @author michael
	 */
	public static <T> T doPostJson(String url, Map<String, String> parametros, Class<T> tipoRetorno) throws McConnectionException {

		return doPostJson(url, parametros, null, tipoRetorno);
	}

	/**
	 * Invoca uma url via <b>POST</b> para executar alguma atividade que retorna <b>JSON</b>
	 *
	 * @param url
	 *            - URL contendo os parâmetros
	 * @param parametros
	 *            - Parametros da url que serão passados via POST (query param)
	 * @param chaveRetorno
	 *            - Propriedade cujo o value será parseado para criação do objeto resposta ou
	 *            <b>null</b> caso todo o json seja a representação do objeto resposta
	 * @param tipoRetorno
	 *            - Tipo do objeto que será criado a partir do json resposta
	 * @return - Objeto do tipo retorno que corresponde a resposta em json dada pela atividade
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 * @author michael
	 */
	public static <T> T doPostJson(String url, Map<String, String> parametros, String chaveRetorno, Class<T> tipoRetorno)
			throws McConnectionException {

		T retorno = null;
		HttpURLConnection conn = null;
		JsonElement jelResult = null;
		try {
			conn = prepararConexaoPost(url, parametros, null);
			jelResult = findJsonElement(chaveRetorno, conn);
			if (jelResult != null) {
				Gson gson = new Gson();
				retorno = gson.fromJson(jelResult, tipoRetorno);
			}
		} catch (Exception e) {
			throwMcConnectionException(conn, e);
		} finally {
			HttpUtil.clearHttpConn(conn);
		}
		return retorno;

	}

	/**
	 * Invoca uma url via <b>POST</b> para executar alguma atividade que retorna <b>JSON</b>
	 *
	 * @param url
	 *            - URL de invocação
	 * @param parametros
	 *            - Parametros da url que serão passados via POST
	 * @param tipoRetorno
	 *            - Tipo do objeto que será criado a partir do json resposta
	 * @return - Lista de objeto do tipo retorno que corresponde a resposta em json dada pela
	 *         atividade
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 * @author michael
	 */
	public static <T> List<T> doPostListJson(String url, Map<String, String> parametros, Class<T> tipoRetorno, Type tipoList) throws McConnectionException {

		return doPostListJson(url, parametros, null, tipoRetorno, tipoList);
	}

	/**
	 * Invoca uma url via <b>POST</b> para executar alguma atividade que retorna <b>JSON</b>
	 *
	 * @param url
	 *            - URL de invocação
	 * @param parametros
	 *            - Parametros da url que serão passados via POST
	 * @param chaveRetorno
	 *            - Propriedade cujo o value será parseado para criação do objeto resposta ou
	 *            <b>null</b> caso todo o json seja a representação do objeto resposta
	 * @param tipoRetorno
	 *            - Tipo do objeto que será criado a partir do json resposta
	 * @return - Lista de objeto do tipo retorno que corresponde a resposta em json dada pela
	 *         atividade
	 * @throws McConnectionException
	 *             - Em caso de resposta com status de erro (4XX, 5XX, ...) ou qualquer outro erro
	 *             de conexão
	 * @author michael
	 */
	public static <T> List<T> doPostListJson(String url, Map<String, String> parametros, String chaveRetorno,
			Class<T> tipoRetorno, Type tipoList) throws McConnectionException {

		List<T> retorno = null;
		HttpURLConnection conn = null;
		JsonElement jelResult = null;
		try {

			conn = prepararConexaoPost(url, parametros, null);

			jelResult = findJsonElement(chaveRetorno, conn);
			if (jelResult != null) {
				Gson gson = new Gson();
				retorno = gson.fromJson(jelResult, tipoList);
			}
		} catch (Exception e) {
			throwMcConnectionException(conn, e);
		} finally {
			HttpUtil.clearHttpConn(conn);
		}
		return retorno;
	}

	/**
	 * Downloads a file from a URL
	 * @param fileURL HTTP URL of the file to be downloaded
	 * @param out output to save the file
	 * @throws McConnectionException
	 */
	public static void downloadFile(String fileURL, OutputStream out) throws McConnectionException {

		HttpURLConnection conn = null;

		try {
			conn = getConexao(fileURL);
			conn = tratarRedirecionamento(conn);
			InputStream inputStream = conn.getInputStream();

			int bytesRead = -1;
			byte[] buffer = new byte[4096];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}

		} catch (Exception e) {
			throwMcConnectionException(conn, e);
		} finally {
			HttpUtil.clearHttpConn(conn);
		}

	}

	public static void clearHttpConn(HttpURLConnection conn) {

		if (conn != null) {
			byte[] buffer = new byte[4096];
			try {
				InputStream is = conn.getInputStream();
				while ((is.read(buffer)) > 0) {
				}
				// close the inputstream
				Funcoes.closeQuietly(is);
			} catch (Exception e) {
				try {
					InputStream es = conn.getErrorStream();
					// read the response body
					while ((es.read(buffer)) > 0) {
					}
					// close the errorstream
					Funcoes.closeQuietly(es);
				} catch (Exception ex) {
					// deal with the exception
				}
			}
		}
	}

	private static void throwMcConnectionException(HttpURLConnection conn, Exception e) throws McConnectionException {

		if (conn != null) {

			BufferedReader reader = null;
			StringBuilder sb = new StringBuilder();
			String msg = null;
			try {
				InputStream es = conn.getErrorStream();
				reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				String linha = null;
				while ((linha = reader.readLine()) != null) {
					sb.append(linha);
				}
				// close the errorstream
				Funcoes.closeQuietly(es);
				Document doc = Jsoup.parse(sb.toString());
				msg = doc.text();
			} catch (Exception ex) {
				// deal with the exception
			} finally {
				Funcoes.closeQuietly(reader);
			}

			int responseCode = -1;
			try {
				responseCode = conn.getResponseCode();
			} catch (Exception ex) {
				throw new McConnectionException(msg, e);
			}
			throw new McConnectionException(responseCode, msg, e);

		}
		throw new McConnectionException(e);
	}

	private static HttpURLConnection prepararConexaoGet(String url, Map<String, String> parametros)
			throws IOException {

		if (Funcoes.isNullOrEmpty(url)) {
			throw new IllegalArgumentException("Opss... url vazia");
		}

		if (!Funcoes.isNullOrEmpty(parametros)) {
			StringBuilder sb = montarQueryParam(parametros);
			url = url + '?' + sb.toString();
		}
		return getConexao(url);

	}

	private static HttpURLConnection prepararConexaoPost(String url, Map<String, String> parametros, HttpSetup setup)
			throws UnsupportedEncodingException, IOException, ProtocolException {

		String charset = PADRAO_ENCODE;
		if(setup != null && !Funcoes.isNullOrEmpty(setup.getCharset())) {
			charset = setup.getCharset();
		}
		StringBuilder sb = montarQueryParam(parametros);
		byte[] postData = sb.toString().getBytes(charset);
		int postDataLength = postData.length;

		HttpURLConnection conn = getConexao(url);
		if(setup != null && setup.getTimeout() > 0){
			conn.setConnectTimeout(setup.getTimeout());
			conn.setReadTimeout(setup.getTimeout());
		}
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setInstanceFollowRedirects(false);
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=" + charset);
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		conn.setUseCaches(false);
		try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
			wr.write(postData);
		}
		return conn;
	}

	private static StringBuilder montarQueryParam(Map<String, String> parametros) throws UnsupportedEncodingException {

		StringBuilder sb = new StringBuilder();
		if (!Funcoes.isNullOrEmpty(parametros)) {
			for (Entry<String, String> paramEntry : parametros.entrySet()) {
				sb.append(paramEntry.getKey());
				sb.append("=");
				if(!Funcoes.isNullOrEmpty(paramEntry.getValue())) {
					sb.append(URLEncoder.encode(paramEntry.getValue(), PADRAO_ENCODE));
				}
				sb.append("&");
			}
			Funcoes.deletarUltimoCaractere(sb);
		}
		return sb;
	}

	private static JsonElement findJsonElement(String chaveRetorno, HttpURLConnection conn) throws IOException {

		JsonElement jelResult = null;
		JsonElement jelement = getJsonConexao(conn);

		if (jelement != null) {
			if (jelement.isJsonObject()) {
				JsonObject jobject = jelement.getAsJsonObject();
				if (jobject != null) {
					if (!Funcoes.isNullOrEmpty(chaveRetorno)) {
						Entry<String, JsonElement> entry = null;
						for (Entry<String, JsonElement> e : jobject.entrySet()) {
							if (chaveRetorno.equals(e.getKey())) {
								entry = e;
								break;
							}
						}
						jelResult = entry.getValue();
					} else {
						jelResult = jobject;
					}
				}
			} else {
				jelResult = jelement;
			}
		}
		return jelResult;
	}

	private static JsonElement getJsonConexao(HttpURLConnection conn) throws IOException {

		Reader reader = null;
		try {
			reader = new InputStreamReader(conn.getInputStream());
			JsonElement jelement = new JsonParser().parse(reader);
			return jelement;
		} finally {
			Funcoes.closeQuietly(reader);
		}
	}

	private static HttpURLConnection getConexao(String url) throws IOException {

		if (Funcoes.isNullOrEmpty(url)) {
			throw new IllegalArgumentException("Opss... url vazia");
		}

		URL urlObj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection(Proxy.NO_PROXY);
		conn.setDoInput(true);
		conn.setConnectTimeout(TIMEOUT_VALUE);
		conn.setReadTimeout(TIMEOUT_VALUE);

		return conn;

	}

	private static HttpURLConnection tratarRedirecionamento(HttpURLConnection conn) throws IOException,
	MalformedURLException {

		boolean redirect = isRedirecionamento(conn);
		while (redirect) {

			// get redirect url from "location" header field
			String newUrl = conn.getHeaderField("Location");

			// get the cookie if need, for login
			String cookies = conn.getHeaderField("Set-Cookie");

			// open the new connnection again
			HttpUtil.clearHttpConn(conn);
			conn = (HttpURLConnection) new URL(newUrl).openConnection();

			if(!Funcoes.isNullOrEmpty(cookies)) {
				conn.setRequestProperty("Cookie", cookies);
			}

			redirect = isRedirecionamento(conn);
		}
		return conn;
	}

	private static boolean isRedirecionamento(HttpURLConnection conn) throws IOException {

		boolean redirect = false;

		try {
			// normally, 3xx is redirect
			int status = conn.getResponseCode();

			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP
						|| status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_SEE_OTHER) {
					redirect = true;
				}
			}
		} catch (Exception e) {
			// false
		}
		return redirect;
	}

}
