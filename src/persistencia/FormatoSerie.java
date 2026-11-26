package persistencia;

import static persistencia.TextoCsv.SEPARADOR_DATOS;
import static persistencia.TextoCsv.SEPARADOR_ELEMENTOS;
import static persistencia.TextoCsv.convertirAEntero;
import static persistencia.TextoCsv.dividirDatos;
import static persistencia.TextoCsv.dividirElementos;

import java.util.stream.Collectors;

import uni1a.ContenidoAudiovisual;
import uni1a.SerieDeTV;
import uni1a.Temporada;

public class FormatoSerie implements FormatoContenido {

    @Override
    public String getTipo() {
        return "SERIE";
    }

    @Override
    public boolean admite(ContenidoAudiovisual contenido) {
        return contenido instanceof SerieDeTV;
    }

    @Override
    public ContenidoAudiovisual leer(String titulo, int duracion, String genero, String detalle) {
        SerieDeTV serie = new SerieDeTV(titulo, duracion, genero);
        for (String elemento : dividirElementos(detalle)) {
            String[] datos = dividirDatos(elemento, 3);
            serie.agregarTemporada(convertirAEntero(datos[0]),
                                   convertirAEntero(datos[1]),
                                   convertirAEntero(datos[2]));
        }
        return serie;
    }

    @Override
    public String escribirDetalle(ContenidoAudiovisual contenido) {
        SerieDeTV serie = (SerieDeTV) contenido;
        return serie.getTemporadas().stream()
                .map(this::temporadaATexto)
                .collect(Collectors.joining(SEPARADOR_ELEMENTOS));
    }

    private String temporadaATexto(Temporada temporada) {
        return String.join(SEPARADOR_DATOS, String.valueOf(temporada.getNumero()),
                String.valueOf(temporada.getCantidadEpisodios()),
                String.valueOf(temporada.getAnioEstreno()));
    }
}