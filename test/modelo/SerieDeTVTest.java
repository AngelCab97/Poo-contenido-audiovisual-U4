package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SerieDeTVTest {

    @Test
    void iniciaSinTemporadas() {
        SerieDeTV serie = new SerieDeTV("Dark", 55, "Ciencia ficción");

        assertEquals("Dark", serie.getTitulo());
        assertEquals(55, serie.getDuracionEnMinutos());
        assertEquals("Ciencia ficción", serie.getGenero());
        assertEquals(0, serie.getCantidadTemporadas());
        assertTrue(serie.getTemporadas().isEmpty());
    }

    @Test
    void agregaTemporadasYActualizaConteo() {
        SerieDeTV serie = new SerieDeTV("Game of Thrones", 60, "Fantasía");

        serie.agregarTemporada(1, 10, 2011);
        serie.agregarTemporada(2, 10, 2012);

        assertEquals(2, serie.getCantidadTemporadas());
        Temporada primera = serie.getTemporadas().get(0);
        assertEquals(1, primera.getNumero());
        assertEquals(10, primera.getCantidadEpisodios());
        assertEquals(2011, primera.getAnioEstreno());
    }

    @Test
    void obtenerDetallesIncluyeTemporadas() {
        SerieDeTV serie = new SerieDeTV("Game of Thrones", 60, "Fantasía");
        serie.agregarTemporada(1, 10, 2011);

        String detalles = serie.obtenerDetalles();

        assertTrue(detalles.contains("Detalles de la serie de TV:"));
        assertTrue(detalles.contains("Temporadas: 1"));
        assertTrue(detalles.contains("Temporada 1 - 10 episodios (2011)"));
    }

    @Test
    void obtenerDetallesSinTemporadasMuestraCero() {
        SerieDeTV serie = new SerieDeTV("Miniserie", 45, "Drama");

        String detalles = serie.obtenerDetalles();

        assertTrue(detalles.contains("Temporadas: 0"));
    }
}