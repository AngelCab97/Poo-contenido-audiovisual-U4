package persistencia;

import static persistencia.TextoCsv.SEPARADOR_CAMPOS;
import static persistencia.TextoCsv.SEPARADOR_DATOS;
import static persistencia.TextoCsv.SEPARADOR_ELEMENTOS;
import static persistencia.TextoCsv.convertirAEntero;
import static persistencia.TextoCsv.dividirCampos;
import static persistencia.TextoCsv.dividirDatos;
import static persistencia.TextoCsv.dividirElementos;

import java.util.stream.Collectors;

import modelo.Cancion;
import modelo.Concierto;
import modelo.ContenidoAudiovisual;

public class FormatoConcierto implements FormatoContenido {

    @Override
    public String getTipo() {
        return "CONCIERTO";
    }

    @Override
    public boolean admite(ContenidoAudiovisual contenido) {
        return contenido instanceof Concierto;
    }

    @Override
    public ContenidoAudiovisual leer(String titulo, int duracion, String genero, String detalle) {
        String[] campos = dividirCampos(detalle, 3);
        Concierto concierto = new Concierto(titulo, duracion, genero, campos[0], campos[1], campos[2]);
        if (campos.length > 3) {
            for (String elemento : dividirElementos(campos[3])) {
                String[] datos = dividirDatos(elemento, 2);
                concierto.agregarCancion(datos[0], convertirAEntero(datos[1]));
            }
        }
        return concierto;
    }

    @Override
    public String escribirDetalle(ContenidoAudiovisual contenido) {
        Concierto concierto = (Concierto) contenido;
        String detalle = String.join(SEPARADOR_CAMPOS,
                concierto.getArtista(), concierto.getLugar(), concierto.getFecha());
        if (!concierto.getSetlist().isEmpty()) {
            String setlist = concierto.getSetlist().stream()
                    .map(this::cancionATexto)
                    .collect(Collectors.joining(SEPARADOR_ELEMENTOS));
            detalle += SEPARADOR_CAMPOS + setlist;
        }
        return detalle;
    }

    private String cancionATexto(Cancion cancion) {
        return String.join(SEPARADOR_DATOS, cancion.getTitulo(),
                String.valueOf(cancion.getDuracionSegundos()));
    }
}