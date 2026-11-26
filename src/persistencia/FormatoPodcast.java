package persistencia;

import static persistencia.TextoCsv.SEPARADOR_CAMPOS;
import static persistencia.TextoCsv.SEPARADOR_DATOS;
import static persistencia.TextoCsv.convertirAEntero;
import static persistencia.TextoCsv.dividirCampos;
import static persistencia.TextoCsv.dividirDatos;

import modelo.ContenidoAudiovisual;
import modelo.Invitado;
import modelo.Podcast;

public class FormatoPodcast implements FormatoContenido {

    @Override
    public String getTipo() {
        return "PODCAST";
    }

    @Override
    public boolean admite(ContenidoAudiovisual contenido) {
        return contenido instanceof Podcast;
    }

    @Override
    public ContenidoAudiovisual leer(String titulo, int duracion, String genero, String detalle) {
        String[] campos = dividirCampos(detalle, 3);
        Podcast podcast = new Podcast(titulo, duracion, genero,
                campos[0], campos[1], convertirAEntero(campos[2]));
        if (campos.length > 3) {
            String[] datos = dividirDatos(campos[3], 2);
            podcast.setInvitado(new Invitado(datos[0], datos[1]));
        }
        return podcast;
    }

    @Override
    public String escribirDetalle(ContenidoAudiovisual contenido) {
        Podcast podcast = (Podcast) contenido;
        String detalle = String.join(SEPARADOR_CAMPOS, podcast.getAnfitrion(),
                podcast.getPlataforma(), String.valueOf(podcast.getCantidadEpisodios()));
        if (podcast.getInvitado() != null) {
            detalle += SEPARADOR_CAMPOS + invitadoATexto(podcast.getInvitado());
        }
        return detalle;
    }

    private String invitadoATexto(Invitado invitado) {
        return String.join(SEPARADOR_DATOS, invitado.getNombre(), invitado.getProfesion());
    }
}