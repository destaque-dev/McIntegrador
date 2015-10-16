package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.HttpStatusException;

import exception.BDException;
import integracao.IntegradorBase;

public class Funcoes {

	public static Date addDateTime(Date date, Date time) {

		if (date == null || time == null) {
			return null;
		}
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);
		Calendar calTime = Calendar.getInstance();
		calTime.setTime(time);
		Calendar calDateTime = Calendar.getInstance();
		calDateTime.clear();
		calDateTime.set(calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH), calDate.get(Calendar.DAY_OF_MONTH),
				calTime.get(Calendar.HOUR_OF_DAY), calTime.get(Calendar.MINUTE), calTime.get(Calendar.SECOND));
		return calDateTime.getTime();
	}

	// Retorna data / hora corrente formatada
	public static String easyDateFormat(String pFormat) {

		return easyDateFormat(new Date(), pFormat);
	}

	// Retorna data / hora formatada
	public static String easyDateFormat(Date data, String pFormat) {

		return easyDateFormat(data, pFormat, false);
	}

	// Retorna data / hora formatada
	public static String easyDateFormat(Date data, String pFormat, boolean mostrarHoraZerada) {

		if (data == null) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
		String dateNewFormat = formatter.format(data);
		if (mostrarHoraZerada) {
			if (dateNewFormat.indexOf("24:00:00") >= 0) {
				dateNewFormat = dateNewFormat.substring(0, dateNewFormat.indexOf("24:00:00")) + "00:00:00";
			}
		} else {
			// Se tiver hora igual a 24:00:00, troca por 00:00:00
			dateNewFormat = dateNewFormat.replaceAll("24:00:00", "");
			dateNewFormat = dateNewFormat.replaceAll("00:00:00", "");
		}

		return dateNewFormat;
	}

	public static String arrayToString(String separator, Object... array) {

		if (array == null || separator == null) {
			return null;
		}

		StringBuffer result = new StringBuffer();

		if (array.length > 0) {
			result.append(array[0]);
			for (int i = 1; i < array.length; i++) {
				result.append(separator);
				result.append(array[i]);
			}
		}

		return result.toString();
	}

	public static void trataErro(Throwable e, IntegradorBase integrador) {

		if (e instanceof HttpStatusException) {
			Log.error("Erro em McIntegrador", e);
		} else if (e instanceof BDException && integrador instanceof IntegradorBase) {
			try {
				Log.error("Erro de banco de dados", e);
				integrador.conectaBancoDados();
			} catch (Exception e1) {
				Log.error("Falha ao reconectar ao banco de dados", e);
			}
		} else {
			Log.error("Erro em McIntegrador", e);
		}

	}

}
