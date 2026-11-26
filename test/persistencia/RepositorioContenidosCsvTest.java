package persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import modelo.Actor;
import modelo.Concierto;
import modelo.ContenidoAudiovisual;
import modelo.Documental;
import modelo.Investigador;
import modelo.Invitado;
import modelo.Pelicula;
import modelo.Podcast;
import modelo.SerieDeTV;

class RepositorioContenidosCsvTest {

    @TempDir
    Path carpetaTemporal;

    private List<FormatoContenido> todosLosFormatos() {
        return List.of(
                new FormatoPelicula(),
                new FormatoSerie(),
                new FormatoDocumental(),
                new FormatoPodcast(),
                new FormatoConcierto());
    }

    @Test
    void guardaYCargaLosCincoTiposDeContenido() throws ErrorPersistenciaException {
        Path archivo = carpetaTemporal.resolve("subcarpeta").resolve("contenidos.csv");
        RepositorioContenidosCsv repositorio = new RepositorioContenidosCsv(archivo, todosLosFormatos());

        Pelicula pelicula = new Pelicula("Avatar", 125, "Acción", "20th Century Studios");
        pelicula.agregarActor(new Actor("Sam Worthington", 49, "Australiana"));

        SerieDeTV serie = new SerieDeTV("Game of Thrones", 60, "Fantasía");
        serie.agregarTemporada(1, 10, 2011);

        Documental documental = new Documental("Cosmos", 45, "Ciencia", "Astronomía");
        documental.setInvestigador(new Investigador("Carl Sagan", "Astronomía", 40));

        Podcast podcast = new Podcast("Entre Códigos", 60, "Tecnología",
                "Andrés Mora", "Spotify", 25);
        podcast.setInvitado(new Invitado("Laura Vega", "Ingeniera de software"));

        Concierto concierto = new Concierto("Noche de Rock", 120, "Rock",
                "Los Andinos", "Estadio", "15/11/2026");
        concierto.agregarCancion("Camino al Sur", 245);

        repositorio.guardar(List.of(pelicula, serie, documental, podcast, concierto));
        List<ContenidoAudiovisual> cargados = repositorio.cargar();

        assertEquals(5, cargados.size());

        Pelicula pCargada = assertInstanceOf(Pelicula.class, cargados.get(0));
        assertEquals("20th Century Studios", pCargada.getEstudio());
        assertEquals(1, pCargada.getActores().size());

        SerieDeTV sCargada = assertInstanceOf(SerieDeTV.class, cargados.get(1));
        assertEquals(1, sCargada.getCantidadTemporadas());

        Documental dCargado = assertInstanceOf(Documental.class, cargados.get(2));
        assertEquals("Carl Sagan", dCargado.getInvestigador().getNombre());

        Podcast podCargado = assertInstanceOf(Podcast.class, cargados.get(3));
        assertEquals("Laura Vega", podCargado.getInvitado().getNombre());

        Concierto cCargado = assertInstanceOf(Concierto.class, cargados.get(4));
        assertEquals(1, cCargado.getSetlist().size());
    }

    @Test
    void cargarArchivoSoloConEncabezadoYLineasEnBlancoDevuelveListaVacia()
            throws IOException, ErrorPersistenciaException {
        Path archivo = carpetaTemporal.resolve("vacio.csv");
        Files.writeString(archivo, "tipo,titulo,duracion,genero,detalle\n\n   \n", StandardCharsets.UTF_8);
        RepositorioContenidosCsv repositorio = new RepositorioContenidosCsv(archivo, todosLosFormatos());

        List<ContenidoAudiovisual> cargados = repositorio.cargar();

        assertTrue(cargados.isEmpty());
    }

    @Test
    void cargarArchivoInexistenteLanzaErrorPersistencia() {
        Path inexistente = carpetaTemporal.resolve("no_existe.csv");
        RepositorioContenidosCsv repositorio = new RepositorioContenidosCsv(inexistente, todosLosFormatos());

        assertThrows(ErrorPersistenciaException.class, repositorio::cargar);
    }

    @Test
    void cargarLineaConColumnasIncompletasLanzaErrorPersistencia() throws IOException {
        Path archivo = carpetaTemporal.resolve("invalido.csv");
        Files.writeString(archivo,
                "tipo,titulo,duracion,genero,detalle\nPELICULA,Avatar,125\n",
                StandardCharsets.UTF_8);
        RepositorioContenidosCsv repositorio = new RepositorioContenidosCsv(archivo, todosLosFormatos());

        assertThrows(ErrorPersistenciaException.class, repositorio::cargar);
    }

    @Test
    void cargarTipoDesconocidoLanzaErrorPersistencia() throws IOException {
        Path archivo = carpetaTemporal.resolve("tipo_desconocido.csv");
        Files.writeString(archivo,
                "tipo,titulo,duracion,genero,detalle\nVIDEO,Prueba,10,Varios,detalle\n",
                StandardCharsets.UTF_8);
        RepositorioContenidosCsv repositorio = new RepositorioContenidosCsv(archivo, todosLosFormatos());

        assertThrows(ErrorPersistenciaException.class, repositorio::cargar);
    }

    @Test
    void guardarTipoNoSoportadoLanzaErrorPersistencia() {
        Path archivo = carpetaTemporal.resolve("sin_formato.csv");
        RepositorioContenidosCsv repositorio = new RepositorioContenidosCsv(archivo, List.of());
        Pelicula pelicula = new Pelicula("Avatar", 125, "Acción", "Estudio");

        assertThrows(ErrorPersistenciaException.class, () -> repositorio.guardar(List.of(pelicula)));
    }
}