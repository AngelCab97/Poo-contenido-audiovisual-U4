package modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DocumentalTest {

    @Test
    void creaDocumentalSinInvestigadorInicial() {
        Documental documental = new Documental("Cosmos", 45, "Ciencia", "Astronomía");

        assertEquals("Cosmos", documental.getTitulo());
        assertEquals(45, documental.getDuracionEnMinutos());
        assertEquals("Ciencia", documental.getGenero());
        assertEquals("Astronomía", documental.getTema());
        assertNull(documental.getInvestigador());
    }

    @Test
    void asignaInvestigadorYActualizaTema() {
        Documental documental = new Documental("Cosmos", 45, "Ciencia", "Espacio");
        Investigador investigador = new Investigador("Carl Sagan", "Astronomía", 40);

        documental.setTema("Astronomía");
        documental.setInvestigador(investigador);

        assertEquals("Astronomía", documental.getTema());
        assertNotNull(documental.getInvestigador());
        assertEquals("Carl Sagan", documental.getInvestigador().getNombre());
        assertEquals("Astronomía", documental.getInvestigador().getEspecialidad());
        assertEquals(40, documental.getInvestigador().getAniosExperiencia());
    }

    @Test
    void obtenerDetallesConInvestigadorLoIncluye() {
        Documental documental = new Documental("Cosmos", 45, "Ciencia", "Astronomía");
        documental.setInvestigador(new Investigador("Carl Sagan", "Astronomía", 40));

        String detalles = documental.obtenerDetalles();

        assertTrue(detalles.contains("Detalles del documental:"));
        assertTrue(detalles.contains("Tema: Astronomía"));
        assertTrue(detalles.contains("Investigador: Carl Sagan - Astronomía (40 años de experiencia)"));
    }

    @Test
    void obtenerDetallesSinInvestigadorOmiteLineaDeInvestigador() {
        Documental documental = new Documental("Planeta Azul", 50, "Naturaleza", "Océanos");

        String detalles = documental.obtenerDetalles();

        assertTrue(detalles.contains("Tema: Océanos"));
        assertFalse(detalles.contains("Investigador:"));
    }
}