package modelo;

import java.util.ArrayList;

public class Pelicula extends ContenidoAudiovisual {
    private String estudio;
    private ArrayList<Actor> actores = new ArrayList<>(); // Agregación con Actor

    public Pelicula(String titulo, int duracionEnMinutos, String genero, String estudio) {
        super(titulo, duracionEnMinutos, genero);
        this.estudio = estudio;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }

    public void agregarActor(Actor actor) {
        actores.add(actor);
    }

    public ArrayList<Actor> getActores() {
        return actores;
    }

    @Override
    public String obtenerDetalles() {
        StringBuilder detalles = new StringBuilder(detallesBase("Detalles de la película:"));
        detalles.append("\nEstudio: ").append(estudio);
        if (!actores.isEmpty()) {
            detalles.append("\nReparto:");
            for (Actor actor : actores) {
                detalles.append("\n - ").append(actor);
            }
        }
        return detalles.toString();
    }
}