package persistencia;

public final class TextoCsv {

    public static final String SEPARADOR_COLUMNAS = ",";
    public static final String SEPARADOR_CAMPOS = "#";
    public static final String SEPARADOR_ELEMENTOS = ";";
    public static final String SEPARADOR_DATOS = ":";

    private TextoCsv() {
    }

    public static String[] dividirCampos(String detalle, int minimoEsperado) {
        String[] campos = detalle.split(SEPARADOR_CAMPOS, -1);
        if (campos.length < minimoEsperado) {
            throw new IllegalArgumentException(
                    "el detalle debe tener al menos " + minimoEsperado + " campos");
        }
        return campos;
    }

    public static String[] dividirElementos(String texto) {
        if (texto.isBlank()) {
            return new String[0];
        }
        return texto.split(SEPARADOR_ELEMENTOS, -1);
    }

    public static String[] dividirDatos(String elemento, int cantidadEsperada) {
        String[] datos = elemento.split(SEPARADOR_DATOS, -1);
        if (datos.length != cantidadEsperada) {
            throw new IllegalArgumentException(
                    "se esperaban " + cantidadEsperada + " datos en '" + elemento + "'");
        }
        return datos;
    }

    public static int convertirAEntero(String texto) {
        return Integer.parseInt(texto.trim());
    }
}