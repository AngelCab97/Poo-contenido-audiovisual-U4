package controlador;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.CatalogoContenidos;
import modelo.ContenidoAudiovisual;
import modelo.Pelicula;
import modelo.Podcast;
import persistencia.ErrorPersistenciaException;
import persistencia.RepositorioContenidos;
import vista.Vista;

class ControladorContenidosTest {

    private VistaFalsa vista;
    private CatalogoContenidos catalogo;
    private RepositorioFalso repositorio;
    private ControladorContenidos controlador;

    @BeforeEach
    void preparar() {
        vista = new VistaFalsa();
        catalogo = new CatalogoContenidos();
        repositorio = new RepositorioFalso();
        controlador = new ControladorContenidos(vista, catalogo, repositorio);
    }

    @Test
    void registraPeliculaConActorYLaGuardaEnCatalogo() {
        vista.encolarEnteros(1, 136, 1, 60, 0);
        vista.encolarTextos("Matrix", "Ciencia ficción", "Warner Bros", "Keanu Reeves", "Canadiense");

        controlador.iniciar();

        assertEquals(1, catalogo.obtenerTodos().size());
        Pelicula guardada = assertInstanceOf(Pelicula.class, catalogo.obtenerTodos().get(0));
        assertEquals("Matrix", guardada.getTitulo());
        assertEquals(1, guardada.getActores().size());
        assertTrue(vista.mensajes.stream().anyMatch(m -> m.startsWith("Película registrada con ID ")));
    }

    @Test
    void cargaDatosDeEjemploYEjecutaAccionEspecialEnPodcast() {
        vista.encolarEnteros(11, 0);
        controlador.iniciar();

        assertEquals(5, catalogo.obtenerTodos().size());
        Podcast podcast = assertInstanceOf(Podcast.class,
                catalogo.buscarPorTitulo("Entre Códigos").get(0));
        int episodiosIniciales = podcast.getCantidadEpisodios();

        vista.encolarEnteros(9, podcast.getId(), 0);
        controlador.iniciar();

        assertEquals(episodiosIniciales + 1, podcast.getCantidadEpisodios());
        assertTrue(vista.mensajes.contains("Episodio publicado. Total: " + (episodiosIniciales + 1) + "."));
    }

    @Test
    void eliminaContenidoCuandoUsuarioConfirma() {
        Pelicula pelicula = new Pelicula("Temporal", 90, "Drama", "Estudio");
        catalogo.agregar(pelicula);

        vista.encolarEnteros(10, pelicula.getId(), 0);
        vista.encolarConfirmaciones(true);

        controlador.iniciar();

        assertTrue(catalogo.estaVacio());
        assertTrue(vista.mensajes.contains("Contenido eliminado."));
    }

    @Test
    void muestraMensajeDeErrorCuandoFallaElRepositorioAlCargar() {
        repositorio.fallarAlCargar = true;
        vista.encolarEnteros(12, 0);

        controlador.iniciar();

        assertTrue(vista.mensajes.contains("Error: fallo simulado de lectura"));
    }

    @Test
    void opcionInvalidaMuestraAvisoYContinuaHastaSalir() {
        vista.encolarEnteros(99, 0);

        controlador.iniciar();

        assertTrue(vista.mensajes.contains("Opción no válida."));
        assertTrue(vista.mensajes.contains("Hasta luego."));
    }

    private static class VistaFalsa implements Vista {
        private final Queue<Integer> enteros = new ArrayDeque<>();
        private final Queue<String> textos = new ArrayDeque<>();
        private final Queue<Boolean> confirmaciones = new ArrayDeque<>();
        private final List<String> mensajes = new ArrayList<>();

        void encolarEnteros(Integer... valores) {
            enteros.addAll(List.of(valores));
        }

        void encolarTextos(String... valores) {
            textos.addAll(List.of(valores));
        }

        void encolarConfirmaciones(Boolean... valores) {
            confirmaciones.addAll(List.of(valores));
        }

        @Override
        public void mostrarMenu() { }

        @Override
        public void mostrarMensaje(String mensaje) {
            mensajes.add(mensaje);
        }

        @Override
        public void mostrarDetalles(ContenidoAudiovisual contenido) { }

        @Override
        public void mostrarResumen(List<ContenidoAudiovisual> contenidos) { }

        @Override
        public String leerTexto(String mensaje) {
            return textos.remove();
        }

        @Override
        public int leerEntero(String mensaje, int minimo) {
            return enteros.remove();
        }

        @Override
        public boolean leerSiNo(String mensaje) {
            return confirmaciones.remove();
        }
    }

    private static class RepositorioFalso implements RepositorioContenidos {
        private boolean fallarAlCargar = false;
        private List<ContenidoAudiovisual> datos = new ArrayList<>();

        @Override
        public List<ContenidoAudiovisual> cargar() throws ErrorPersistenciaException {
            if (fallarAlCargar) {
                throw new ErrorPersistenciaException("fallo simulado de lectura");
            }
            return new ArrayList<>(datos);
        }

        @Override
        public void guardar(List<ContenidoAudiovisual> contenidos) throws ErrorPersistenciaException {
            datos = new ArrayList<>(contenidos);
        }
    }
}