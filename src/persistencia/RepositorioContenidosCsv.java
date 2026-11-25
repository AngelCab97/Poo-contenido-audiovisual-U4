package persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uni1a.Actor;
import uni1a.Cancion;
import uni1a.Concierto;
import uni1a.ContenidoAudiovisual;
import uni1a.Documental;
import uni1a.Investigador;
import uni1a.Invitado;
import uni1a.Pelicula;
import uni1a.Podcast;
import uni1a.SerieDeTV;
import uni1a.Temporada;

public class RepositorioContenidosCsv implements RepositorioContenidos {

    private static final String ENCABEZADO = "tipo,titulo,duracion,genero,detalle";
    private static final String SEPARADOR_COLUMNAS = ",";
    private static final String SEPARADOR_CAMPOS = "#";
    private static final String SEPARADOR_ELEMENTOS = ";";
    private static final String SEPARADOR_DATOS = ":";
    private static final int COLUMNAS_ESPERADAS = 5;

    private final Path rutaArchivo;

    public RepositorioContenidosCsv(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
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
        int duracion = Integer.parseInt(columnas[2].trim());
        String genero = columnas[3].trim();
        String detalle = columnas[4];

        switch (tipo) {
            case "PELICULA":
                return crearPelicula(titulo, duracion, genero, detalle);
            case "SERIE":
                return crearSerie(titulo, duracion, genero, detalle);
            case "DOCUMENTAL":
                return crearDocumental(titulo, duracion, genero, detalle);
            case "PODCAST":
                return crearPodcast(titulo, duracion, genero, detalle);
            case "CONCIERTO":
                return crearConcierto(titulo, duracion, genero, detalle);
            default:
                throw new IllegalArgumentException("tipo desconocido: " + tipo);
        }
    }

    private Pelicula crearPelicula(String titulo, int duracion, String genero, String detalle) {
        String[] campos = dividirCampos(detalle, 1);
        Pelicula pelicula = new Pelicula(titulo, duracion, genero, campos[0]);
        if (campos.length > 1) {
            for (String elemento : dividirElementos(campos[1])) {
                String[] datos = dividirDatos(elemento, 3);
                pelicula.agregarActor(new Actor(datos[0], Integer.parseInt(datos[1].trim()), datos[2]));
            }
        }
        return pelicula;
    }

    private SerieDeTV crearSerie(String titulo, int duracion, String genero, String detalle) {
        SerieDeTV serie = new SerieDeTV(titulo, duracion, genero);
        for (String elemento : dividirElementos(detalle)) {
            String[] datos = dividirDatos(elemento, 3);
            serie.agregarTemporada(Integer.parseInt(datos[0].trim()),
                                   Integer.parseInt(datos[1].trim()),
                                   Integer.parseInt(datos[2].trim()));
        }
        return serie;
    }

    private Documental crearDocumental(String titulo, int duracion, String genero, String detalle) {
        String[] campos = dividirCampos(detalle, 1);
        Documental documental = new Documental(titulo, duracion, genero, campos[0]);
        if (campos.length > 1) {
            String[] datos = dividirDatos(campos[1], 3);
            documental.setInvestigador(
                    new Investigador(datos[0], datos[1], Integer.parseInt(datos[2].trim())));
        }
        return documental;
    }

    private Podcast crearPodcast(String titulo, int duracion, String genero, String detalle) {
        String[] campos = dividirCampos(detalle, 3);
        Podcast podcast = new Podcast(titulo, duracion, genero,
                campos[0], campos[1], Integer.parseInt(campos[2].trim()));
        if (campos.length > 3) {
            String[] datos = dividirDatos(campos[3], 2);
            podcast.setInvitado(new Invitado(datos[0], datos[1]));
        }
        return podcast;
    }

    private Concierto crearConcierto(String titulo, int duracion, String genero, String detalle) {
        String[] campos = dividirCampos(detalle, 3);
        Concierto concierto = new Concierto(titulo, duracion, genero, campos[0], campos[1], campos[2]);
        if (campos.length > 3) {
            for (String elemento : dividirElementos(campos[3])) {
                String[] datos = dividirDatos(elemento, 2);
                concierto.agregarCancion(datos[0], Integer.parseInt(datos[1].trim()));
            }
        }
        return concierto;
    }

    private String[] dividirCampos(String detalle, int minimoEsperado) {
        String[] campos = detalle.split(SEPARADOR_CAMPOS, -1);
        if (campos.length < minimoEsperado) {
            throw new IllegalArgumentException(
                    "el detalle debe tener al menos " + minimoEsperado + " campos");
        }
        return campos;
    }

    private String[] dividirElementos(String texto) {
        if (texto.isBlank()) {
            return new String[0];
        }
        return texto.split(SEPARADOR_ELEMENTOS, -1);
    }

    private String[] dividirDatos(String elemento, int cantidadEsperada) {
        String[] datos = elemento.split(SEPARADOR_DATOS, -1);
        if (datos.length != cantidadEsperada) {
            throw new IllegalArgumentException(
                    "se esperaban " + cantidadEsperada + " datos en '" + elemento + "'");
        }
        return datos;
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
        String tipo;
        String detalle;
        if (contenido instanceof Pelicula) {
            tipo = "PELICULA";
            detalle = detallePelicula((Pelicula) contenido);
        } else if (contenido instanceof SerieDeTV) {
            tipo = "SERIE";
            detalle = detalleSerie((SerieDeTV) contenido);
        } else if (contenido instanceof Documental) {
            tipo = "DOCUMENTAL";
            detalle = detalleDocumental((Documental) contenido);
        } else if (contenido instanceof Podcast) {
            tipo = "PODCAST";
            detalle = detallePodcast((Podcast) contenido);
        } else if (contenido instanceof Concierto) {
            tipo = "CONCIERTO";
            detalle = detalleConcierto((Concierto) contenido);
        } else {
            throw new ErrorPersistenciaException(
                    "Tipo de contenido no soportado: " + contenido.getClass().getSimpleName());
        }
        return String.join(SEPARADOR_COLUMNAS, tipo, contenido.getTitulo(),
                String.valueOf(contenido.getDuracionEnMinutos()), contenido.getGenero(), detalle);
    }

    private String detallePelicula(Pelicula pelicula) {
        String detalle = pelicula.getEstudio();
        if (!pelicula.getActores().isEmpty()) {
            String reparto = pelicula.getActores().stream()
                    .map(this::actorATexto)
                    .collect(Collectors.joining(SEPARADOR_ELEMENTOS));
            detalle += SEPARADOR_CAMPOS + reparto;
        }
        return detalle;
    }

    private String detalleSerie(SerieDeTV serie) {
        return serie.getTemporadas().stream()
                .map(this::temporadaATexto)
                .collect(Collectors.joining(SEPARADOR_ELEMENTOS));
    }

    private String detalleDocumental(Documental documental) {
        String detalle = documental.getTema();
        if (documental.getInvestigador() != null) {
            detalle += SEPARADOR_CAMPOS + investigadorATexto(documental.getInvestigador());
        }
        return detalle;
    }

    private String detallePodcast(Podcast podcast) {
        String detalle = String.join(SEPARADOR_CAMPOS, podcast.getAnfitrion(),
                podcast.getPlataforma(), String.valueOf(podcast.getCantidadEpisodios()));
        if (podcast.getInvitado() != null) {
            detalle += SEPARADOR_CAMPOS + invitadoATexto(podcast.getInvitado());
        }
        return detalle;
    }

    private String detalleConcierto(Concierto concierto) {
        String detalle = String.join(SEPARADOR_CAMPOS,
                concierto.getArtista(), concierto.getLugar(), concierto.getFecha());
        if (!concierto.getSetlist().isEmpty()) {
            String setlist = concierto.getSetlist().stream()
                    .map(this::cancionATexto)
                    .collect(Collectors.joining(SEPARADOR_ELEMENTOS));
            detalle += SEPARADOR_CAMPOS + setlist;
        }
        return detalle;
    }

    private String actorATexto(Actor actor) {
        return String.join(SEPARADOR_DATOS, actor.getNombre(),
                String.valueOf(actor.getEdad()), actor.getNacionalidad());
    }

    private String temporadaATexto(Temporada temporada) {
        return String.join(SEPARADOR_DATOS, String.valueOf(temporada.getNumero()),
                String.valueOf(temporada.getCantidadEpisodios()),
                String.valueOf(temporada.getAnioEstreno()));
    }

    private String investigadorATexto(Investigador investigador) {
        return String.join(SEPARADOR_DATOS, investigador.getNombre(),
                investigador.getEspecialidad(), String.valueOf(investigador.getAniosExperiencia()));
    }

    private String invitadoATexto(Invitado invitado) {
        return String.join(SEPARADOR_DATOS, invitado.getNombre(), invitado.getProfesion());
    }

    private String cancionATexto(Cancion cancion) {
        return String.join(SEPARADOR_DATOS, cancion.getTitulo(),
                String.valueOf(cancion.getDuracionSegundos()));
    }
}