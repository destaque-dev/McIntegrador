package util;

public class Log {

    public static final String HORARIO = "HH:mm:ss";

    public static final String DATA = "dd/MM/yyyy";

    public static void info(String string) {

        String horario = Funcoes.easyDateFormat(DATA + " " + HORARIO);

        System.out.println(horario + " INFO : " + string);

    }

    public static void error(String string, Throwable e) {

        String horario = Funcoes.easyDateFormat(DATA + " " + HORARIO);

        System.err.println(horario + " ERRO : " + string);
        e.printStackTrace();

    }

}
