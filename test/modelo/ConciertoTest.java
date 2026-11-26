package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConciertoTest {

    @Test
    void iniciaConSetlistVacioYDuracionCero() {
        Concierto concierto = new Concierto("Noche de Rock", 120, "Rock",
                "Los Andinos", "Estadio Central", "15/11/2026");

        assertEquals("Los Andinos", concierto.getArtista());
        assertEquals("Estadio Central", concierto.getLugar());
        assertEquals("15/11/2026", concierto.getFecha());
        assertTrue(concierto.getSetlist().isEmpty());
        assertEquals(0.0, concierto.calcularDuracionSetlist(), 0.001);
    }

    @Test
    void agregaCancionesYCalculaDuracionSetlist() {
        Concierto concierto = new Concierto("Noche de Rock", 120, "Rock",
                "Los Andinos", "Estadio Central", "15/11/2026");

        concierto.agregarCancion("Camino al Sur", 240);
        concierto.agregarCancion("Luces de Cuenca", 180);

        assertEquals(2, concierto.getSetlist().size());
        assertEquals(7.0, concierto.calcularDuracionSetlist(), 0.001);

        Cancion primera = concierto.getSetlist().get(0);
        assertEquals("Camino al Sur", primera.getTitulo());
        assertEquals(240, primera.getDuracionSegundos());
        assertEquals("Camino al Sur (4:00)", primera.toString());
    }

    @Test
    void accionEspecialDevuelveResumenDelSetlist() {
        Concierto concierto = new Concierto("Acústico", 90, "Indie",
                "Sol", "Teatro", "20/11/2026");
        concierto.agregarCancion("Tema 1", 240);
        concierto.agregarCancion("Tema 2", 180);

        String esperado = String.format("Duración del setlist: %.1f min (2 canciones)", 7.0);
        assertEquals(esperado, concierto.ejecutarAccionEspecial());
    }

    @Test
    void obtenerDetallesConYSinSetlist() {
        Concierto sinCanciones = new Concierto("En vivo", 60, "Jazz",
                "Trío", "Club", "01/12/2026");
        assertFalse(sinCanciones.obtenerDetalles().contains("Setlist"));

        Concierto conCanciones = new Concierto("Noche de Rock", 120, "Rock",
                "Los Andinos", "Estadio", "15/11/2026");
        conCanciones.agregarCancion("Camino al Sur", 245);

        String detalles = conCanciones.obtenerDetalles();
        assertTrue(detalles.contains("Detalles del concierto:"));
        assertTrue(detalles.contains("Artista: Los Andinos"));
        assertTrue(detalles.contains("Setlist (1 canciones"));
        assertTrue(detalles.contains("Camino al Sur (4:05)"));
    }
}