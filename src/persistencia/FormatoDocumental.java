package persistencia;

import static persistencia.TextoCsv.SEPARADOR_CAMPOS;
import static persistencia.TextoCsv.SEPARADOR_DATOS;
import static persistencia.TextoCsv.convertirAEntero;
import static persistencia.TextoCsv.dividirCampos;
import static persistencia.TextoCsv.dividirDatos;

import uni1a.ContenidoAudiovisual;
import uni1a.Documental;
import uni1a.Investigador;

public class FormatoDocumental implements FormatoContenido {

    @Override
    public String getTipo() {
        return "DOCUMENTAL";
    }

    @Override
    public boolean admite(ContenidoAudiovisual contenido) {
        return contenido instanceof Documental;
    }

    @Override
    public ContenidoAudiovisual leer(String titulo, int duracion, String genero, String detalle) {
        String[] campos = dividirCampos(detalle, 1);
        Documental documental = new Documental(titulo, duracion, genero, campos[0]);
        if (campos.length > 1) {
            String[] datos = dividirDatos(campos[1], 3);
            documental.setInvestigador(
                    new Investigador(datos[0], datos[1], convertirAEntero(datos[2])));
        }
        return documental;
    }

    @Override
    public String escribirDetalle(ContenidoAudiovisual contenido) {
        Documental documental = (Documental) contenido;
        String detalle = documental.getTema();
        if (documental.getInvestigador() != null) {
            detalle += SEPARADOR_CAMPOS + investigadorATexto(documental.getInvestigador());
        }
        return detalle;
    }

    private String investigadorATexto(Investigador investigador) {
        return String.join(SEPARADOR_DATOS, investigador.getNombre(),
                investigador.getEspecialidad(), String.valueOf(investigador.getAniosExperiencia()));
    }
}