package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PodcastTest {

    @Test
    void creaPodcastConDatosIniciales() {
        Podcast podcast = new Podcast("Entre Códigos", 60, "Tecnología",
                "Andrés Mora", "Spotify", 25);

        assertEquals("Entre Códigos", podcast.getTitulo());
        assertEquals("Andrés Mora", podcast.getAnfitrion());
        assertEquals("Spotify", podcast.getPlataforma());
        assertEquals(25, podcast.getCantidadEpisodios());
        assertNull(podcast.getInvitado());
    }

    @Test
    void publicarEpisodioYAccionEspecialIncrementanConteo() {
        Podcast podcast = new Podcast("Entre Códigos", 60, "Tecnología",
                "Andrés Mora", "Spotify", 25);

        podcast.publicarEpisodio();
        assertEquals(26, podcast.getCantidadEpisodios());

        String resultado = podcast.ejecutarAccionEspecial();
        assertEquals(27, podcast.getCantidadEpisodios());
        assertEquals("Episodio publicado. Total: 27.", resultado);
    }

    @Test
    void asignaInvitadoYLoMuestraEnDetalles() {
        Podcast podcast = new Podcast("Entre Códigos", 60, "Tecnología",
                "Andrés Mora", "Spotify", 10);
        Invitado invitado = new Invitado("Laura Vega", "Ingeniera de software");
        podcast.setInvitado(invitado);

        String detalles = podcast.obtenerDetalles();

        assertEquals("Laura Vega", podcast.getInvitado().getNombre());
        assertEquals("Ingeniera de software", podcast.getInvitado().getProfesion());
        assertTrue(detalles.contains("Detalles del podcast:"));
        assertTrue(detalles.contains("Anfitrión: Andrés Mora"));
        assertTrue(detalles.contains("Plataforma: Spotify"));
        assertTrue(detalles.contains("Episodios: 10"));
        assertTrue(detalles.contains("Invitado: Laura Vega (Ingeniera de software)"));
    }

    @Test
    void obtenerDetallesSinInvitadoOmiteLineaDeInvitado() {
        Podcast podcast = new Podcast("Solo", 30, "Charla", "Carlos", "YouTube", 0);

        String detalles = podcast.obtenerDetalles();

        assertTrue(detalles.contains("Episodios: 0"));
        assertFalse(detalles.contains("Invitado:"));
    }
}