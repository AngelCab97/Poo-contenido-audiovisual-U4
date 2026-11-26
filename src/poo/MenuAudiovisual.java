package poo;

import java.util.ArrayList;
import java.util.Scanner;
import uni1a.*;

public class MenuAudiovisual {
    private static final int ANIO_MINIMO_ESTRENO = 1900;
    private static final Scanner teclado = new Scanner(System.in);
    private static final ArrayList<ContenidoAudiovisual> contenidos = new ArrayList<>();

    private record DatosBasicos(String titulo, int duracion, String genero) { }

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ", 0);
            switch (opcion) {
                case 1 -> registrarPelicula();
                case 2 -> registrarSerie();
                case 3 -> registrarDocumental();
                case 4 -> registrarPodcast();
                case 5 -> registrarConcierto();
                case 6 -> listarTodo();
                case 7 -> buscarPorTitulo();
                case 8 -> agregarElemento();
                case 9 -> accionesEspeciales();
                case 10 -> eliminarContenido();
                case 11 -> cargarDatosDeEjemplo();
                case 0 -> System.out.println("Hasta luego.");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== GESTOR DE CONTENIDO AUDIOVISUAL =====");
        System.out.println("1. Registrar película");
        System.out.println("2. Registrar serie de TV");
        System.out.println("3. Registrar documental");
        System.out.println("4. Registrar podcast");
        System.out.println("5. Registrar concierto");
        System.out.println("6. Listar todo el contenido");
        System.out.println("7. Buscar por título");
        System.out.println("8. Agregar actor/temporada/investigador/invitado/canción");
        System.out.println("9. Acciones especiales (publicar episodio, duración del setlist)");
        System.out.println("10. Eliminar contenido");
        System.out.println("11. Cargar datos de ejemplo");
        System.out.println("0. Salir");
    }

    // ---------- Registro de contenido ----------

    private static DatosBasicos leerDatosBasicos(String mensajeDuracion) {
        String titulo = leerTexto("Título: ");
        int duracion = leerEntero(mensajeDuracion, 1);
        String genero = leerTexto("Género: ");
        return new DatosBasicos(titulo, duracion, genero);
    }

    private static void registrarContenido(ContenidoAudiovisual contenido, String mensajeRegistro) {
        contenidos.add(contenido);
        System.out.println(mensajeRegistro + " con ID " + contenido.getId() + ".");
    }

    private static void repetir(int veces, Runnable accion) {
        for (int i = 0; i < veces; i++) {
            accion.run();
        }
    }

    private static void registrarPelicula() {
        System.out.println("--- Nueva película ---");
        DatosBasicos datos = leerDatosBasicos("Duración en minutos: ");
        String estudio = leerTexto("Estudio: ");
        Pelicula pelicula = new Pelicula(datos.titulo(), datos.duracion(), datos.genero(), estudio);

        int cantidad = leerEntero("¿Cuántos actores quieres agregar? (0 = ninguno): ", 0);
        repetir(cantidad, () -> agregarActorA(pelicula));
        registrarContenido(pelicula, "Película registrada");
    }

    private static void registrarSerie() {
        System.out.println("--- Nueva serie de TV ---");
        DatosBasicos datos = leerDatosBasicos("Duración por episodio en minutos: ");
        SerieDeTV serie = new SerieDeTV(datos.titulo(), datos.duracion(), datos.genero());

        int cantidad = leerEntero("¿Cuántas temporadas quieres agregar? (0 = ninguna): ", 0);
        repetir(cantidad, () -> agregarTemporadaA(serie));
        registrarContenido(serie, "Serie registrada");
    }

    private static void registrarDocumental() {
        System.out.println("--- Nuevo documental ---");
        DatosBasicos datos = leerDatosBasicos("Duración en minutos: ");
        String tema = leerTexto("Tema: ");
        Documental documental = new Documental(datos.titulo(), datos.duracion(), datos.genero(), tema);

        if (leerSiNo("¿Asignar un investigador?")) {
            documental.setInvestigador(crearInvestigador());
        }
        registrarContenido(documental, "Documental registrado");
    }

    private static void registrarPodcast() {
        System.out.println("--- Nuevo podcast ---");
        DatosBasicos datos = leerDatosBasicos("Duración por episodio en minutos: ");
        String anfitrion = leerTexto("Anfitrión: ");
        String plataforma = leerTexto("Plataforma: ");
        int episodios = leerEntero("Cantidad de episodios actuales: ", 0);
        Podcast podcast = new Podcast(datos.titulo(), datos.duracion(), datos.genero(),
                anfitrion, plataforma, episodios);

        if (leerSiNo("¿Agregar un invitado?")) {
            podcast.setInvitado(crearInvitado());
        }
        registrarContenido(podcast, "Podcast registrado");
    }

    private static void registrarConcierto() {
        System.out.println("--- Nuevo concierto ---");
        DatosBasicos datos = leerDatosBasicos("Duración en minutos: ");
        String artista = leerTexto("Artista: ");
        String lugar = leerTexto("Lugar: ");
        String fecha = leerTexto("Fecha (dd/mm/aaaa): ");
        Concierto concierto = new Concierto(datos.titulo(), datos.duracion(), datos.genero(),
                artista, lugar, fecha);

        int cantidad = leerEntero("¿Cuántas canciones tiene el setlist? (0 = ninguna): ", 0);
        repetir(cantidad, () -> agregarCancionA(concierto));
        registrarContenido(concierto, "Concierto registrado");
    }

    // ---------- Creación de clases de apoyo ----------

    private static Actor crearActor() {
        String nombre = leerTexto("  Nombre del actor: ");
        int edad = leerEntero("  Edad: ", 1);
        String nacionalidad = leerTexto("  Nacionalidad: ");
        return new Actor(nombre, edad, nacionalidad);
    }

    private static Investigador crearInvestigador() {
        String nombre = leerTexto("  Nombre del investigador: ");
        String especialidad = leerTexto("  Especialidad: ");
        int anios = leerEntero("  Años de experiencia: ", 0);
        return new Investigador(nombre, especialidad, anios);
    }

    private static Invitado crearInvitado() {
        String nombre = leerTexto("  Nombre del invitado: ");
        String profesion = leerTexto("  Profesión: ");
        return new Invitado(nombre, profesion);
    }

    private static void agregarActorA(Pelicula pelicula) {
        System.out.println("Actor " + (pelicula.getActores().size() + 1) + ":");
        pelicula.agregarActor(crearActor());
    }

    private static void agregarTemporadaA(SerieDeTV serie) {
        int numero = serie.getCantidadTemporadas() + 1;
        System.out.println("Temporada " + numero + ":");
        int episodios = leerEntero("  Cantidad de episodios: ", 1);
        int anio = leerEntero("  Año de estreno: ", ANIO_MINIMO_ESTRENO);
        serie.agregarTemporada(numero, episodios, anio);
    }

    private static void agregarCancionA(Concierto concierto) {
        System.out.println("Canción " + (concierto.getSetlist().size() + 1) + ":");
        String titulo = leerTexto("  Título: ");
        int segundos = leerEntero("  Duración en segundos: ", 1);
        concierto.agregarCancion(titulo, segundos);
    }

    // ---------- Consulta y manipulación ----------

    private static void listarTodo() {
        if (contenidos.isEmpty()) {
            System.out.println("No hay contenido registrado todavía.");
            return;
        }
        for (ContenidoAudiovisual contenido : contenidos) {
            contenido.mostrarDetalles();
        }
    }

    private static void listarResumen() {
        for (ContenidoAudiovisual contenido : contenidos) {
            System.out.println("[" + contenido.getId() + "] " + contenido.getTitulo()
                    + " (" + contenido.getClass().getSimpleName() + ")");
        }
    }

    private static void buscarPorTitulo() {
        String texto = leerTexto("Texto a buscar en el título: ").toLowerCase();
        int encontrados = 0;
        for (ContenidoAudiovisual contenido : contenidos) {
            if (contenido.getTitulo().toLowerCase().contains(texto)) {
                contenido.mostrarDetalles();
                encontrados++;
            }
        }
        if (encontrados == 0) {
            System.out.println("No se encontró ningún contenido con ese título.");
        }
    }

    private static ContenidoAudiovisual buscarPorId(int id) {
        for (ContenidoAudiovisual contenido : contenidos) {
            if (contenido.getId() == id) {
                return contenido;
            }
        }
        return null;
    }

    private static ContenidoAudiovisual elegirContenido() {
        if (contenidos.isEmpty()) {
            System.out.println("No hay contenido registrado todavía.");
            return null;
        }
        listarResumen();
        int id = leerEntero("ID del contenido: ", 0);
        ContenidoAudiovisual contenido = buscarPorId(id);
        if (contenido == null) {
            System.out.println("No existe un contenido con ese ID.");
        }
        return contenido;
    }

    private static void agregarElemento() {
        ContenidoAudiovisual contenido = elegirContenido();
        if (contenido == null) return;

        if (contenido instanceof Pelicula pelicula) {
            pelicula.agregarActor(crearActor());
            System.out.println("Actor agregado a \"" + pelicula.getTitulo() + "\".");
        } else if (contenido instanceof SerieDeTV serie) {
            agregarTemporadaA(serie);
            System.out.println("Temporada agregada. Total: " + serie.getCantidadTemporadas() + ".");
        } else if (contenido instanceof Documental documental) {
            documental.setInvestigador(crearInvestigador());
            System.out.println("Investigador asignado a \"" + documental.getTitulo() + "\".");
        } else if (contenido instanceof Podcast podcast) {
            podcast.setInvitado(crearInvitado());
            System.out.println("Invitado asignado a \"" + podcast.getTitulo() + "\".");
        } else if (contenido instanceof Concierto concierto) {
            agregarCancionA(concierto);
            System.out.println("Canción agregada al setlist.");
        }
    }

    private static void accionesEspeciales() {
        ContenidoAudiovisual contenido = elegirContenido();
        if (contenido == null) return;

        if (contenido instanceof Podcast podcast) {
            podcast.publicarEpisodio();
            System.out.println("Episodio publicado. Total: " + podcast.getCantidadEpisodios() + ".");
        } else if (contenido instanceof Concierto concierto) {
            System.out.printf("Duración del setlist: %.1f min (%d canciones)%n",
                    concierto.calcularDuracionSetlist(), concierto.getSetlist().size());
        } else {
            System.out.println("Este contenido no tiene acciones especiales.");
        }
    }

    private static void eliminarContenido() {
        ContenidoAudiovisual contenido = elegirContenido();
        if (contenido == null) return;

        if (leerSiNo("¿Seguro que quieres eliminar \"" + contenido.getTitulo() + "\"?")) {
            contenidos.remove(contenido);
            System.out.println("Contenido eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private static void cargarDatosDeEjemplo() {
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

        contenidos.add(avatar);
        contenidos.add(gameOfThrones);
        contenidos.add(cosmos);
        contenidos.add(entreCodigos);
        contenidos.add(nocheDeRock);
        System.out.println("Se cargaron 5 contenidos de ejemplo.");
    }

    // ---------- Lectura segura de datos ----------

    private static String leerTexto(String mensaje) {
        String texto;
        do {
            System.out.print(mensaje);
            texto = teclado.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Este campo no puede estar vacío.");
            }
        } while (texto.isEmpty());
        return texto;
    }

    private static int leerEntero(String mensaje, int minimo) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(teclado.nextLine().trim());
                if (valor >= minimo) {
                    return valor;
                }
                System.out.println("El valor debe ser mayor o igual a " + minimo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número entero válido.");
            }
        }
    }

    private static boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String respuesta = teclado.nextLine().trim().toLowerCase();
            if (respuesta.equals("s")) return true;
            if (respuesta.equals("n")) return false;
            System.out.println("Responde con 's' o 'n'.");
        }
    }
}