package modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogoContenidosTest {

    private CatalogoContenidos catalogo;

    @BeforeEach
    void prepararCatalogo() {
        catalogo = new CatalogoContenidos();
    }

    @Test
    void iniciaVacio() {
        assertTrue(catalogo.estaVacio());
        assertTrue(catalogo.obtenerTodos().isEmpty());
    }

    @Test
    void agregaYBuscaPorIdExistenteEInexistente() {
        Pelicula avatar = new Pelicula("Avatar", 125, "Acción", "20th Century Studios");
        catalogo.agregar(avatar);

        assertFalse(catalogo.estaVacio());
        Optional<ContenidoAudiovisual> encontrado = catalogo.buscarPorId(avatar.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Avatar", encontrado.get().getTitulo());

        assertTrue(catalogo.buscarPorId(-1).isEmpty());
    }

    @Test
    void buscaPorTituloSinDistinguirMayusculas() {
        catalogo.agregar(new Pelicula("El Señor de los Anillos", 178, "Fantasía", "New Line"));
        catalogo.agregar(new Documental("Cosmos", 45, "Ciencia", "Astronomía"));

        List<ContenidoAudiovisual> encontrados = catalogo.buscarPorTitulo("señor");
        assertEquals(1, encontrados.size());
        assertEquals("El Señor de los Anillos", encontrados.get(0).getTitulo());

        assertTrue(catalogo.buscarPorTitulo("Inexistente").isEmpty());
    }

    @Test
    void eliminaExistenteYDevuelveFalseSiNoEsta() {
        Pelicula avatar = new Pelicula("Avatar", 125, "Acción", "20th Century Studios");
        Pelicula otra = new Pelicula("Otra", 90, "Drama", "Estudio");
        catalogo.agregar(avatar);

        assertTrue(catalogo.eliminar(avatar));
        assertFalse(catalogo.eliminar(otra));
        assertTrue(catalogo.estaVacio());
    }

    @Test
    void reemplazarConSustituyeElementosPrevios() {
        catalogo.agregar(new Pelicula("Vieja", 100, "Drama", "Estudio"));
        Documental cosmos = new Documental("Cosmos", 45, "Ciencia", "Astronomía");

        catalogo.reemplazarCon(List.of(cosmos));

        assertEquals(1, catalogo.obtenerTodos().size());
        assertEquals("Cosmos", catalogo.obtenerTodos().get(0).getTitulo());
    }

    @Test
    void obtenerTodosDevuelveListaInmutable() {
        Pelicula avatar = new Pelicula("Avatar", 125, "Acción", "20th Century Studios");
        catalogo.agregar(avatar);

        List<ContenidoAudiovisual> lista = catalogo.obtenerTodos();

        assertThrows(UnsupportedOperationException.class, () -> lista.add(avatar));
    }
}