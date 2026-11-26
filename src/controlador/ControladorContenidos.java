package controlador;

import java.util.List;
import java.util.Optional;

import modelo.AccionEspecial;
import modelo.Actor;
import modelo.CatalogoContenidos;
import modelo.Concierto;
import modelo.ContenidoAudiovisual;
import modelo.Documental;
import modelo.Investigador;
import modelo.Invitado;
import modelo.Pelicula;
import modelo.Podcast;
import modelo.SerieDeTV;
import persistencia.ErrorPersistenciaException;
import persistencia.RepositorioContenidos;
import vista.Vista;

public class ControladorContenidos {

    private static final int ANIO_MINIMO_ESTRENO = 1900;

    private final Vista vista;
    private final CatalogoContenidos catalogo;
    private final RepositorioContenidos repositorio;

    private record DatosBasicos(String titulo, int duracion, String genero) { }

    public ControladorContenidos(Vista vista, CatalogoContenidos catalogo,
                                 RepositorioContenidos repositorio) {
        this.vista = vista;
        this.catalogo = catalogo;
        this.repositorio = repositorio;
    }

    public void iniciar() {
        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.leerEntero("Elige una opción: ", 0);
            ejecutarOpcion(opcion);
        } while (opcion != 0);
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> registrarPelicula();
            case 2 -> registrarSerie();
            case 3 -> registrarDocumental();
            case 4 -> registrarPodcast();
            case 5 -> registrarConcierto();
            case 6 -> listarTodo();
            case 7 -> buscarPorTitulo();
            case 8 -> agregarElemento();
            case 9 -> ejecutarAccionEspecial();
            case 10 -> eliminarContenido();
            case 11 -> cargarDatosDeEjemplo();
            case 12 -> cargarDesdeCsv();
            case 13 -> guardarEnCsv();
            case 0 -> vista.mostrarMensaje("Hasta luego.");
            default -> vista.mostrarMensaje("Opción no válida.");
        }
    }

    // ---------- Registro ----------

    private DatosBasicos leerDatosBasicos(String mensajeDuracion) {
        String titulo = vista.leerTexto("Título: ");
        int duracion = vista.leerEntero(mensajeDuracion, 1);
        String genero = vista.leerTexto("Género: ");
        return new DatosBasicos(titulo, duracion, genero);
    }

    private void registrarContenido(ContenidoAudiovisual contenido, String mensajeRegistro) {
        catalogo.agregar(contenido);
        vista.mostrarMensaje(mensajeRegistro + " con ID " + contenido.getId() + ".");
    }

    private void repetir(int veces, Runnable accion) {
        for (int i = 0; i < veces; i++) {
            accion.run();
        }
    }

    private void registrarPelicula() {
        vista.mostrarMensaje("--- Nueva película ---");
        DatosBasicos datos = leerDatosBasicos("Duración en minutos: ");
        String estudio = vista.leerTexto("Estudio: ");
        Pelicula pelicula = new Pelicula(datos.titulo(), datos.duracion(), datos.genero(), estudio);

        int cantidad = vista.leerEntero("¿Cuántos actores quieres agregar? (0 = ninguno): ", 0);
        repetir(cantidad, () -> agregarActorA(pelicula));
        registrarContenido(pelicula, "Película registrada");
    }

    private void registrarSerie() {
        vista.mostrarMensaje("--- Nueva serie de TV ---");
        DatosBasicos datos = leerDatosBasicos("Duración por episodio en minutos: ");
        SerieDeTV serie = new SerieDeTV(datos.titulo(), datos.duracion(), datos.genero());

        int cantidad = vista.leerEntero("¿Cuántas temporadas quieres agregar? (0 = ninguna): ", 0);
        repetir(cantidad, () -> agregarTemporadaA(serie));
        registrarContenido(serie, "Serie registrada");
    }

    private void registrarDocumental() {
        vista.mostrarMensaje("--- Nuevo documental ---");
        DatosBasicos datos = leerDatosBasicos("Duración en minutos: ");
        String tema = vista.leerTexto("Tema: ");
        Documental documental = new Documental(datos.titulo(), datos.duracion(), datos.genero(), tema);

        if (vista.leerSiNo("¿Asignar un investigador?")) {
            documental.setInvestigador(crearInvestigador());
        }
        registrarContenido(documental, "Documental registrado");
    }

    private void registrarPodcast() {
        vista.mostrarMensaje("--- Nuevo podcast ---");
        DatosBasicos datos = leerDatosBasicos("Duración por episodio en minutos: ");
        String anfitrion = vista.leerTexto("Anfitrión: ");
        String plataforma = vista.leerTexto("Plataforma: ");
        int episodios = vista.leerEntero("Cantidad de episodios actuales: ", 0);
        Podcast podcast = new Podcast(datos.titulo(), datos.duracion(), datos.genero(),
                anfitrion, plataforma, episodios);

        if (vista.leerSiNo("¿Agregar un invitado?")) {
            podcast.setInvitado(crearInvitado());
        }
        registrarContenido(podcast, "Podcast registrado");
    }

    private void registrarConcierto() {
        vista.mostrarMensaje("--- Nuevo concierto ---");
        DatosBasicos datos = leerDatosBasicos("Duración en minutos: ");
        String artista = vista.leerTexto("Artista: ");
        String lugar = vista.leerTexto("Lugar: ");
        String fecha = vista.leerTexto("Fecha (dd/mm/aaaa): ");
        Concierto concierto = new Concierto(datos.titulo(), datos.duracion(), datos.genero(),
                artista, lugar, fecha);

        int cantidad = vista.leerEntero("¿Cuántas canciones tiene el setlist? (0 = ninguna): ", 0);
        repetir(cantidad, () -> agregarCancionA(concierto));
        registrarContenido(concierto, "Concierto registrado");
    }

    private Actor crearActor() {
        String nombre = vista.leerTexto("  Nombre del actor: ");
        int edad = vista.leerEntero("  Edad: ", 1);
        String nacionalidad = vista.leerTexto("  Nacionalidad: ");
        return new Actor(nombre, edad, nacionalidad);
    }

    private Investigador crearInvestigador() {
        String nombre = vista.leerTexto("  Nombre del investigador: ");
        String especialidad = vista.leerTexto("  Especialidad: ");
        int anios = vista.leerEntero("  Años de experiencia: ", 0);
        return new Investigador(nombre, especialidad, anios);
    }

    private Invitado crearInvitado() {
        String nombre = vista.leerTexto("  Nombre del invitado: ");
        String profesion = vista.leerTexto("  Profesión: ");
        return new Invitado(nombre, profesion);
    }

    private void agregarActorA(Pelicula pelicula) {
        vista.mostrarMensaje("Actor " + (pelicula.getActores().size() + 1) + ":");
        pelicula.agregarActor(crearActor());
    }

    private void agregarTemporadaA(SerieDeTV serie) {
        int numero = serie.getCantidadTemporadas() + 1;
        vista.mostrarMensaje("Temporada " + numero + ":");
        int episodios = vista.leerEntero("  Cantidad de episodios: ", 1);
        int anio = vista.leerEntero("  Año de estreno: ", ANIO_MINIMO_ESTRENO);
        serie.agregarTemporada(numero, episodios, anio);
    }

    private void agregarCancionA(Concierto concierto) {
        vista.mostrarMensaje("Canción " + (concierto.getSetlist().size() + 1) + ":");
        String titulo = vista.leerTexto("  Título: ");
        int segundos = vista.leerEntero("  Duración en segundos: ", 1);
        concierto.agregarCancion(titulo, segundos);
    }

    // ---------- Consulta ----------

    private void listarTodo() {
        if (catalogo.estaVacio()) {
            vista.mostrarMensaje("No hay contenido registrado todavía.");
            return;
        }
        mostrarDetallesDe(catalogo.obtenerTodos());
    }

    private void buscarPorTitulo() {
        String texto = vista.leerTexto("Texto a buscar en el título: ");
        List<ContenidoAudiovisual> encontrados = catalogo.buscarPorTitulo(texto);
        if (encontrados.isEmpty()) {
            vista.mostrarMensaje("No se encontró ningún contenido con ese título.");
            return;
        }
        mostrarDetallesDe(encontrados);
    }

    private void mostrarDetallesDe(List<ContenidoAudiovisual> contenidos) {
        for (ContenidoAudiovisual contenido : contenidos) {
            vista.mostrarDetalles(contenido);
        }
    }

    private Optional<ContenidoAudiovisual> elegirContenido() {
        if (catalogo.estaVacio()) {
            vista.mostrarMensaje("No hay contenido registrado todavía.");
            return Optional.empty();
        }
        vista.mostrarResumen(catalogo.obtenerTodos());
        int id = vista.leerEntero("ID del contenido: ", 0);
        Optional<ContenidoAudiovisual> contenido = catalogo.buscarPorId(id);
        if (contenido.isEmpty()) {
            vista.mostrarMensaje("No existe un contenido con ese ID.");
        }
        return contenido;
    }

    // ---------- Manipulación ----------

    private void agregarElemento() {
        Optional<ContenidoAudiovisual> elegido = elegirContenido();
        if (elegido.isEmpty()) {
            return;
        }
        ContenidoAudiovisual contenido = elegido.get();
        if (contenido instanceof Pelicula pelicula) {
            pelicula.agregarActor(crearActor());
            vista.mostrarMensaje("Actor agregado a \"" + pelicula.getTitulo() + "\".");
        } else if (contenido instanceof SerieDeTV serie) {
            agregarTemporadaA(serie);
            vista.mostrarMensaje("Temporada agregada. Total: " + serie.getCantidadTemporadas() + ".");
        } else if (contenido instanceof Documental documental) {
            documental.setInvestigador(crearInvestigador());
            vista.mostrarMensaje("Investigador asignado a \"" + documental.getTitulo() + "\".");
        } else if (contenido instanceof Podcast podcast) {
            podcast.setInvitado(crearInvitado());
            vista.mostrarMensaje("Invitado asignado a \"" + podcast.getTitulo() + "\".");
        } else if (contenido instanceof Concierto concierto) {
            agregarCancionA(concierto);
            vista.mostrarMensaje("Canción agregada al setlist.");
        }
    }

    private void ejecutarAccionEspecial() {
        Optional<ContenidoAudiovisual> elegido = elegirContenido();
        if (elegido.isEmpty()) {
            return;
        }
        if (elegido.get() instanceof AccionEspecial accion) {
            vista.mostrarMensaje(accion.ejecutarAccionEspecial());
        } else {
            vista.mostrarMensaje("Este contenido no tiene acciones especiales.");
        }
    }

    private void eliminarContenido() {
        Optional<ContenidoAudiovisual> elegido = elegirContenido();
        if (elegido.isEmpty()) {
            return;
        }
        ContenidoAudiovisual contenido = elegido.get();
        if (vista.leerSiNo("¿Seguro que quieres eliminar \"" + contenido.getTitulo() + "\"?")) {
            catalogo.eliminar(contenido);
            vista.mostrarMensaje("Contenido eliminado.");
        } else {
            vista.mostrarMensaje("Operación cancelada.");
        }
    }

    // ---------- Archivos y datos de ejemplo ----------

    private void cargarDatosDeEjemplo() {
        Pelicula avatar = new Pelicula("Avatar", 125, "Accion", "20th Century Studios");
        avatar.agregarActor(new Actor("Sam Worthington", 49, "Australiana"));
        avatar.agregarActor(new Actor("Zoe Saldaña", 48, "Estadounidense"));

        SerieDeTV gameOfThrones = new SerieDeTV("Game of Thrones", 60, "Fantasy");
        gameOfThrones.agregarTemporada(1, 10, 2011);
        gameOfThrones.agregarTemporada(2, 10, 2012);

        Documental cosmos = new Documental("Cosmos", 45, "Science", "Astronomy");
        cosmos.setInvestigador(new Investigador("Carl Sagan", "Astronomía", 40));

        Podcast entreCodigos = new Podcast("Entre Códigos", 60, "Tecnología",
                "Andrés Mora", "Spotify", 25);
        entreCodigos.setInvitado(new Invitado("Laura Vega", "Ingeniera de software"));

        Concierto nocheDeRock = new Concierto("Noche de Rock", 120, "Rock",
                "Los Andinos", "Estadio Alejandro Serrano Aguilar", "15/11/2026");
        nocheDeRock.agregarCancion("Camino al Sur", 245);
        nocheDeRock.agregarCancion("Luces de Cuenca", 198);

        List<ContenidoAudiovisual> ejemplos = List.of(
                avatar, gameOfThrones, cosmos, entreCodigos, nocheDeRock);
        ejemplos.forEach(catalogo::agregar);
        vista.mostrarMensaje("Se cargaron " + ejemplos.size() + " contenidos de ejemplo.");
    }

    private void cargarDesdeCsv() {
        if (!catalogo.estaVacio()
                && !vista.leerSiNo("Esto reemplaza el contenido actual. ¿Continuar?")) {
            vista.mostrarMensaje("Operación cancelada.");
            return;
        }
        try {
            List<ContenidoAudiovisual> cargados = repositorio.cargar();
            catalogo.reemplazarCon(cargados);
            vista.mostrarMensaje("Se cargaron " + cargados.size() + " contenidos desde el archivo.");
        } catch (ErrorPersistenciaException e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }

    private void guardarEnCsv() {
        try {
            repositorio.guardar(catalogo.obtenerTodos());
            vista.mostrarMensaje("Se guardaron " + catalogo.obtenerTodos().size()
                    + " contenidos en el archivo.");
        } catch (ErrorPersistenciaException e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }
}