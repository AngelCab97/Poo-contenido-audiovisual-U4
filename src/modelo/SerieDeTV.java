package modelo;

import java.util.ArrayList;

public class SerieDeTV extends ContenidoAudiovisual {
    private ArrayList<Temporada> temporadas = new ArrayList<>(); // Composición con Temporada

    public SerieDeTV(String titulo, int duracionEnMinutos, String genero) {
        super(titulo, duracionEnMinutos, genero);
    }

    // La serie crea la temporada por dentro (composición)
    public void agregarTemporada(int numero, int cantidadEpisodios, int anioEstreno) {
        temporadas.add(new Temporada(numero, cantidadEpisodios, anioEstreno));
    }

    public ArrayList<Temporada> getTemporadas() {
        return temporadas;
    }

    public int getCantidadTemporadas() {
        return temporadas.size();
    }

    @Override
    public String obtenerDetalles() {
        StringBuilder detalles = new StringBuilder(detallesBase("Detalles de la serie de TV:"));
        detalles.append("\nTemporadas: ").append(getCantidadTemporadas());
        for (Temporada temporada : temporadas) {
            detalles.append("\n - ").append(temporada);
        }
        return detalles.toString();
    }
}