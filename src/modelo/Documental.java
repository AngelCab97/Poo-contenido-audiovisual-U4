package modelo;

public class Documental extends ContenidoAudiovisual {
    private String tema;
    private Investigador investigador; // Asociación con Investigador

    public Documental(String titulo, int duracionEnMinutos, String genero, String tema) {
        super(titulo, duracionEnMinutos, genero);
        this.tema = tema;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public Investigador getInvestigador() {
        return investigador;
    }

    public void setInvestigador(Investigador investigador) {
        this.investigador = investigador;
    }

    @Override
    public String obtenerDetalles() {
        StringBuilder detalles = new StringBuilder(detallesBase("Detalles del documental:"));
        detalles.append("\nTema: ").append(tema);
        if (investigador != null) {
            detalles.append("\nInvestigador: ").append(investigador);
        }
        return detalles.toString();
    }
}