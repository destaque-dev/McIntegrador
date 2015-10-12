package util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log {

	 private final static Logger geraLog = Logger.getLogger(Log.class);

	    protected static String dataArquivoLog;

	    protected static String dataArquivoLogUsr;

	    private static String getDataHoje() {

	        Date data = new Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	        String dateNewFormat = formatter.format(data);

	        // Se tiver hora igual a 24:00:00, troca por 00:00:00
	        dateNewFormat = dateNewFormat.replaceAll("24:00:00", "");
	        dateNewFormat = dateNewFormat.replaceAll("00:00:00", "");

	        return dateNewFormat;
	    }

	    private static void configuraLogger(String dataArquivo) {

	        dataArquivoLog = dataArquivo;
	        /*
	         * String nome = getnomeArquivo(dataArquivo);
	         * ((FileAppender) geraLog.getAppender(MC_FILE_LOG)).setFile(nome);
	         * ((FileAppender) geraLog.getAppender(MC_FILE_LOG)).activateOptions();
	         */
	    }

	    public static void debug(Object mensagem) {

	        String data = getDataHoje();
	        if ((geraLog.isDebugEnabled()) && (!data.equals(dataArquivoLog))) {
	            configuraLogger(data);
	        }
	        geraLog.debug(mensagem);
	    }

	    public static void debug(Object mensagem, Throwable throwable) {

	        String data = getDataHoje();
	        if ((geraLog.isDebugEnabled()) && (!data.equals(dataArquivoLog))) {
	            configuraLogger(data);
	        }
	        geraLog.debug(mensagem, throwable);
	    }

	    public static void info(Object mensagem) {

	        String data = getDataHoje();
	        if ((geraLog.isInfoEnabled()) && (!data.equals(dataArquivoLog))) {
	            configuraLogger(data);
	        }
	        geraLog.info(mensagem);
	    }

	    public static void info(Object mensagem, Throwable throwable) {

	        String data = getDataHoje();
	        if ((geraLog.isInfoEnabled()) && (!data.equals(dataArquivoLog))) {
	            configuraLogger(data);
	        }
	        geraLog.info(mensagem, throwable);
	    }

	    public static void warn(Object mensagem) {

	        String data = getDataHoje();
	        if ((geraLog.isEnabledFor(Priority.WARN)) && (!data.equals(dataArquivoLog))) {
	            configuraLogger(data);
	        }
	        geraLog.warn(mensagem);
	    }

	    public static void warn(Object mensagem, Throwable throwable) {

	        String data = getDataHoje();
	        if ((geraLog.isEnabledFor(Priority.WARN)) && (!data.equals(dataArquivoLog))) {
	            configuraLogger(data);
	        }
	        geraLog.warn(mensagem, throwable);
	    }

	    public static void error(Object mensagem) {

	        String data = getDataHoje();
	        if (!data.equals(dataArquivoLog)) {
	            configuraLogger(data);
	        }
	        geraLog.error(mensagem);
	    }

	    public static void error(Object mensagem, Throwable throwable) {

	        String data = getDataHoje();
	        if (!data.equals(dataArquivoLog)) {
	            configuraLogger(data);
	        }
	        geraLog.error(mensagem, throwable);
	    }

	    public static void fatal(Object mensagem) {

	        String data = getDataHoje();
	        if (!data.equals(dataArquivoLog)) {
	            configuraLogger(data);
	        }
	        geraLog.fatal(mensagem);
	    }

	    public static void fatal(Object mensagem, Throwable throwable) {

	        String data = getDataHoje();
	        if (!data.equals(dataArquivoLog)) {
	            configuraLogger(data);
	        }
	        geraLog.fatal(mensagem, throwable);
	    }

}
