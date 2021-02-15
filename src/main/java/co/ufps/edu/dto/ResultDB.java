package co.ufps.edu.dto;

/**
 * Clase DTO con la información del resultado.
 *
 * @author UFPS
 */
public class ResultDB {

    private int result;

    private long key;

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public ResultDB() {

    }

    /**
     * Constructor de la clase en donde se inicializan las variables.
     */
    public ResultDB(int result, long key) {
        super();
        this.result = result;
        this.key = key;
    }

    /**
     * Get de result.
     *
     * @return valor de result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Set de result.
     *
     * @param result nuevo valor de result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Get de key.
     *
     * @return valor de key.
     */
    public long getKey() {
        return key;
    }

    /**
     * Set de key.
     *
     * @param key nuevo valor de key.
     */
    public void setKey(long key) {
        this.key = key;
    }

}
