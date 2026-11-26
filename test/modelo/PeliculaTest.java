package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PeliculaTest {

    @Test
    void creaPeliculaConDatosInicialesYAsignaIdUnico() {
        Pelicula primera = new Pelicula("Avatar", 125, "Acción", "20th Century Studios");
        Pelicula segunda = new Pelicula("Inception", 148, "Ciencia ficción", "Warner Bros");

        assertEquals("Avatar", primera.getTitulo());
        assertEquals(125, primera.getDuracionEnMinutos());
        assertEquals("Acción", primera.getGenero());
        assertEquals("20th Century Studios", primera.getEstudio());
        assertTrue(primera.getActores().isEmpty());
        assertEquals(primera.getId() + 1, segunda.getId());
    }

    @Test
    void actualizaAtributosConSetters() {
        Pelicula pelicula = new Pelicula("Título inicial", 90, "Drama", "Estudio A");

        pelicula.setTitulo("Título nuevo");
        pelicula.setDuracionEnMinutos(110);
        pelicula.setGenero("Suspenso");
        pelicula.setEstudio("Estudio B");

        assertEquals("Título nuevo", pelicula.getTitulo());
        assertEquals(110, pelicula.getDuracionEnMinutos());
        assertEquals("Suspenso", pelicula.getGenero());
        assertEquals("Estudio B", pelicula.getEstudio());
    }

    @Test
    void agregaActoresYLosIncluyeEnDetalles() {
        Pelicula pelicula = new Pelicula("Avatar", 125, "Acción", "20th Century Studios");
        Actor actor = new Actor("Sam Worthington", 49, "Australiana");

        pelicula.agregarActor(actor);

        assertEquals(1, pelicula.getActores().size());
        assertEquals("Sam Worthington", pelicula.getActores().get(0).getNombre());

        String detalles = pelicula.obtenerDetalles();
        assertTrue(detalles.contains("Detalles de la película:"));
        assertTrue(detalles.contains("Estudio: 20th Century Studios"));
        assertTrue(detalles.contains("Reparto:"));
        assertTrue(detalles.contains("Sam Worthington (49 años, Australiana)"));
    }

    @Test
    void obtenerDetallesSinActoresNoMuestraSeccionReparto() {
        Pelicula pelicula = new Pelicula("Sin reparto", 85, "Documental", "Independiente");

        String detalles = pelicula.obtenerDetalles();

        assertTrue(detalles.contains("Sin reparto"));
        assertFalse(detalles.contains("Reparto:"));
    }
}