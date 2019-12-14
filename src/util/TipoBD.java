package util;

public class TipoBD {

    public static final int INDEFINIDO = -1;

    public static final int INTERBASE = 0;

    public static final int ORACLE = 1;

    public static final int SQLSERVER = 2;

    public static final int ORACLE_7 = 3;

    public static final int ORACLE_ODA = 4;

    public static final int POSTGRE = 5;
    
    public static final int ORACLE_WEB = 6;

    public static int validacao(int codigo) {

        switch (codigo) {
            case ORACLE_ODA:
                return ORACLE_ODA;
            case SQLSERVER:
                return SQLSERVER;
            case ORACLE:
                return ORACLE;
            case ORACLE_WEB:
                return ORACLE_WEB;
            case ORACLE_7:
                return ORACLE_7;
            case POSTGRE:
                return POSTGRE;
            case INTERBASE:
                return INTERBASE;
            default:
                return INDEFINIDO;
        }
    }

    public static int validacao(String nome) {

        if (nome.equalsIgnoreCase("ORACLEODA")) {
            return ORACLE_ODA;
        } else if (nome.equalsIgnoreCase("MSSQL")) {
            return SQLSERVER;
        }
        if (nome.equalsIgnoreCase("ORACLE")) {
            return ORACLE;
        } else if (nome.equalsIgnoreCase("ORACLE7")) {
            return ORACLE_7;
        } else if (nome.equalsIgnoreCase("POSTGRESQL")) {
            return POSTGRE;
        } else if (nome.equalsIgnoreCase("INTRBASE")) {
            return INTERBASE;
        } else if (nome.equalsIgnoreCase("ORACLEWEB")) {
            return ORACLE_WEB;
        } else {
            return INDEFINIDO;
        }
    }

    public static boolean isOracle(int codigo) {

        return (codigo == ORACLE) || (codigo == ORACLE_ODA) || (codigo == ORACLE_7) || (codigo == ORACLE_WEB);
    }

    public static boolean isSQLServer(int codigo) {

        return (codigo == SQLSERVER);
    }

    public static boolean isPostgreSQL(int codigo) {

        return (codigo == POSTGRE);
    }

}