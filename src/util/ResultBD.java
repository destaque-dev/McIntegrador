package util;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import exception.BDException;

/**
 * O OBJETO ResultBD DEVE FICAR TOTALMENTE ENCAPSULADO AQUI PARA TRATAR PROBLEMAS DE ACESSO COM O
 * BANCO DE DADOS
 */
@SuppressWarnings("deprecation")
public class ResultBD {

    private ResultSet result;

    public ResultBD(ResultSet result) throws BDException {

        if (result == null) {
            throw new BDException("Null ResultSet not allowed");
        }
        this.result = result;
    }

    public boolean isAfterLast() throws BDException {

        try {
            return this.result.isAfterLast();
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public boolean isLast() throws BDException {

        try {
            return this.result.isLast();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public boolean next() throws BDException {

        try {
            return this.result.next();
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public int getRow() throws BDException {

        try {
            return this.result.getRow();
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public String getString(String field) throws BDException {

        try {
            return this.result.getString(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    public String getString(int field) throws BDException {

        try {
            return this.result.getString(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public int getInt(String field) throws BDException {

        try {
            return this.result.getInt(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    public Long getLong(String field) throws BDException {

        try {
            return this.result.getLong(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    public boolean isNull(String field) throws BDException {

        if (!field.equals("SINONIMIA")) {
            try {
                return this.result.getObject(field) == null;
            } catch (SQLException e) {
                throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
            }
        } else {
            return false;
        }
    }

    public boolean isNull(int field) throws BDException {

        try {
            return this.result.getObject(field) == null;
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    public int getInt(int field) throws BDException {

        try {
            return this.result.getInt(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public Long getLong(int field) throws BDException {

        try {
            return this.result.getLong(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public float getFloat(String field) throws BDException {

        try {
            return this.result.getFloat(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    public float getFloat(int field) throws BDException {

        try {
            return this.result.getFloat(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public BigDecimal getBigDecimal(String field) throws BDException {

        try {
            return this.result.getBigDecimal(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage(), e);
        }
    }

    public double getDouble(String field) throws BDException {

        try {
            return this.result.getDouble(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    public double getDouble(int field) throws BDException {

        try {
            return this.result.getDouble(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public boolean getBoolean(String field) throws BDException {

        try {
            return this.result.getBoolean(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    public boolean getBoolean(int field) throws BDException {

        try {
            return this.result.getBoolean(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public java.util.Date getDateTime(String field) throws BDException {

        try {
            return Funcoes.addDateTime(result.getDate(field), result.getTime(field));
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    public java.util.Date getDateTime(int field) throws BDException {

        try {
            return Funcoes.addDateTime(result.getDate(field), result.getTime(field));
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public Date getDate(String field) throws BDException {

        try {
            return this.result.getDate(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    public Date getDate(int field) throws BDException {

        try {
            return this.result.getDate(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public Time getTime(String field) throws BDException {

        try {
            return this.result.getTime(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    public Time getTime(int field) throws BDException {

        try {
            return this.result.getTime(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    /**
     * Os campos que precisam ser lidos com esta função devem ser lidos primeiro devido a uma
     * restrição imposta pelo Oracle7 ao tentar ler campos do tipo LONG.
     * 
     * @param field
     *            Nome do campo
     * @param tipoBD
     *            Tipo do banco: ORACLE&, ORACLE, ...
     * @return Retorna um InputStream com o valor do campo
     * @throws BDException
     */
    public InputStream getBinaryStream(String field, int tipoBD) throws BDException {

        try {
            if ((tipoBD == TipoBD.POSTGRE) || (tipoBD == TipoBD.ORACLE_7) || (tipoBD == TipoBD.SQLSERVER)) {
                return new StringBufferInputStream(this.result.getString(field));
            } else {
                return this.result.getBinaryStream(field);
            }
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): ", e);
        }
    }

    public InputStream getBinaryStream(int field, int tipoBD) throws BDException {

        try {
            if ((tipoBD == TipoBD.POSTGRE) || (tipoBD == TipoBD.ORACLE_7)) {
                return new StringBufferInputStream(this.result.getString(field));
            } else {
                return this.result.getBinaryStream(field);
            }
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage(), e);
        }
    }

    public int getColumnCount() throws BDException {

        try {
            return this.result.getMetaData().getColumnCount();
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public String getColumnName(int indiceCampo) throws BDException {

        try {
            return this.result.getMetaData().getColumnName(indiceCampo);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public int getColumnType(String field) throws BDException {

        try {
            return result.getMetaData().getColumnType(result.findColumn(field));
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

    // Comentado pois quando o resultBD é pego pelo garbagecollector, ele fecha um statement que
    // deveria permanecer aberto.
    // @Override
    // public void finalize() {
    //
    // try {
    // this.close();
    // } catch (BDException e) {
    // // Log.error("Erro ao liberar ResultBD.", e);
    // }
    // }

    public boolean wasNull() throws BDException {

        try {
            return this.result.wasNull();
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o BD: " + e.getMessage());
        }
    }

    public void close() {

        try {
            this.result.getStatement().clearWarnings();
            this.result.getStatement().clearBatch();
            this.result.getStatement().close();
        } catch (SQLException e) {
            Log.error("Erro ao fechar Statement: " + e.getMessage(), e);
        }
        try {
            this.result.close();
        } catch (SQLException e) {
            Log.error("Erro ao fechar ResultSet: " + e.getMessage(), e);
        }
    }

    public boolean containsColumnn(String name) {

        boolean contains = false;
        try {
            this.result.findColumn(name);
            contains = true;
        } catch (SQLException e) { /* result does not contain this column */
        }
        return contains;
    }

    /**
     * @author michael
     *         Pega o campo do tipo Clob
     * @param field
     *            nome do campo do tipo Clob
     * @return
     * @throws BDException
     */
    public Clob getClob(String field) throws BDException {

        try {
            return this.result.getClob(field);
        } catch (SQLException e) {
            throw new BDException("Erro ao ler o campo (" + field + "): " + e.getMessage());
        }
    }

}
