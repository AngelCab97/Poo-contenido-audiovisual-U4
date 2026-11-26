package persistencia;

import static persistencia.TextoCsv.SEPARADOR_COLUMNAS;
import static persistencia.TextoCsv.convertirAEntero;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uni1a.ContenidoAudiovisual;

public class RepositorioContenidosCsv implements RepositorioContenidos {

    private static final String ENCABEZADO = "tipo,titulo,duracion,genero,detalle";
    private static final int COLUMNAS_ESPERADAS = 5;

    private final Path rutaArchivo;
    private final List<FormatoContenido> formatos;
    private final Map<String, FormatoContenido> formatosPorTipo = new HashMap<>();

    public RepositorioContenidosCsv(Path rutaArchivo, List<FormatoContenido> formatos) {
        this.rutaArchivo = rutaArchivo;
        this.formatos = formatos;
        for (FormatoContenido formato : formatos) {
            formatosPorTipo.put(formato.getTipo(), formato);
        }
    }

    // ===================== LECTURA =====================

    @Override
    public List<ContenidoAudiovisual> cargar() throws ErrorPersistenciaException {
        List<String> lineas = leerLineas();
        List<ContenidoAudiovisual> contenidos = new ArrayList<>();
        // La posición 0 es el encabezado
        for (int i = 1; i < lineas.size(); i++) {
            if (!lineas.get(i).isBlank()) {
                contenidos.add(convertirLinea(lineas.get(i), i + 1));
            }
        }
        return contenidos;
    }

    private List<String> leerLineas() throws ErrorPersistenciaException {
        try {
            return Files.readAllLines(rutaArchivo, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ErrorPersistenciaException("No se pudo leer el archivo: " + rutaArchivo, e);
        }
    }

    private ContenidoAudiovisual convertirLinea(String linea, int numeroLinea)
            throws ErrorPersistenciaException {
        try {
            return crearContenido(linea);
        } catch (IllegalArgumentException e) {
            throw new ErrorPersistenciaException(
                    "Línea " + numeroLinea + " inválida: " + e.getMessage(), e);
        }
    }

    private ContenidoAudiovisual crearContenido(String linea) {
        String[] columnas = linea.split(SEPARADOR_COLUMNAS, COLUMNAS_ESPERADAS);
        if (columnas.length != COLUMNAS_ESPERADAS) {
            throw new IllegalArgumentException(
                    "se esperaban " + COLUMNAS_ESPERADAS + " columnas y llegaron " + columnas.length);
        }
        String tipo = columnas[0].trim();
        String titulo = columnas[1].trim();
        int duracion = convertirAEntero(columnas[2]);
        String genero = columnas[3].trim();
        String detalle = columnas[4];

        return buscarFormatoPorTipo(tipo).leer(titulo, duracion, genero, detalle);
    }

    private FormatoContenido buscarFormatoPorTipo(String tipo) {
        FormatoContenido formato = formatosPorTipo.get(tipo);
        if (formato == null) {
            throw new IllegalArgumentException("tipo desconocido: " + tipo);
        }
        return formato;
    }

    // ===================== ESCRITURA =====================

    @Override
    public void guardar(List<ContenidoAudiovisual> contenidos) throws ErrorPersistenciaException {
        List<String> lineas = new ArrayList<>();
        lineas.add(ENCABEZADO);
        for (ContenidoAudiovisual contenido : contenidos) {
            lineas.add(convertirAFila(contenido));
        }
        escribirLineas(lineas);
    }

    private void escribirLineas(List<String> lineas) throws ErrorPersistenciaException {
        try {
            crearCarpetaPadreSiNoExiste();
            Files.write(rutaArchivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ErrorPersistenciaException("No se pudo escribir el archivo: " + rutaArchivo, e);
        }
    }

    private void crearCarpetaPadreSiNoExiste() throws IOException {
        Path carpeta = rutaArchivo.toAbsolutePath().getParent();
        if (carpeta != null) {
            Files.createDirectories(carpeta);
        }
    }

    private String convertirAFila(ContenidoAudiovisual contenido) throws ErrorPersistenciaException {
        FormatoContenido formato = buscarFormatoPara(contenido);
        return String.join(SEPARADOR_COLUMNAS, formato.getTipo(), contenido.getTitulo(),
                String.valueOf(contenido.getDuracionEnMinutos()), contenido.getGenero(),
                formato.escribirDetalle(contenido));
    }

    private FormatoContenido buscarFormatoPara(ContenidoAudiovisual contenido)
            throws ErrorPersistenciaException {
        for (FormatoContenido formato : formatos) {
            if (formato.admite(contenido)) {
                return formato;
            }
        }
        throw new ErrorPersistenciaException(
                "Tipo de contenido no soportado: " + contenido.getClass().getSimpleName());
    }
}