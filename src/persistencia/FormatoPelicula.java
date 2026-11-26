package persistencia;

import static persistencia.TextoCsv.SEPARADOR_CAMPOS;
import static persistencia.TextoCsv.SEPARADOR_DATOS;
import static persistencia.TextoCsv.SEPARADOR_ELEMENTOS;
import static persistencia.TextoCsv.convertirAEntero;
import static persistencia.TextoCsv.dividirCampos;
import static persistencia.TextoCsv.dividirDatos;
import static persistencia.TextoCsv.dividirElementos;

import java.util.stream.Collectors;

import uni1a.Actor;
import uni1a.ContenidoAudiovisual;
import uni1a.Pelicula;

public class FormatoPelicula implements FormatoContenido {

    @Override
    public String getTipo() {
        return "PELICULA";
    }

    @Override
    public boolean admite(ContenidoAudiovisual contenido) {
        return contenido instanceof Pelicula;
    }

    @Override
    public ContenidoAudiovisual leer(String titulo, int duracion, String genero, String detalle) {
        String[] campos = dividirCampos(detalle, 1);
        Pelicula pelicula = new Pelicula(titulo, duracion, genero, campos[0]);
        if (campos.length > 1) {
            for (String elemento : dividirElementos(campos[1])) {
                String[] datos = dividirDatos(elemento, 3);
                pelicula.agregarActor(new Actor(datos[0], convertirAEntero(datos[1]), datos[2]));
            }
        }
        return pelicula;
    }

    @Override
    public String escribirDetalle(ContenidoAudiovisual contenido) {
        Pelicula pelicula = (Pelicula) contenido;
        String detalle = pelicula.getEstudio();
        if (!pelicula.getActores().isEmpty()) {
            String reparto = pelicula.getActores().stream()
                    .map(this::actorATexto)
                    .collect(Collectors.joining(SEPARADOR_ELEMENTOS));
            detalle += SEPARADOR_CAMPOS + reparto;
        }
        return detalle;
    }

    private String actorATexto(Actor actor) {
        return String.join(SEPARADOR_DATOS, actor.getNombre(),
                String.valueOf(actor.getEdad()), actor.getNacionalidad());
    }
}