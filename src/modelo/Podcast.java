package modelo;

public class Podcast extends ContenidoAudiovisual implements AccionEspecial {
    private String anfitrion;
    private String plataforma;
    private int cantidadEpisodios;
    private Invitado invitado; // Asociación con Invitado

    public Podcast(String titulo, int duracionEnMinutos, String genero,
                   String anfitrion, String plataforma, int cantidadEpisodios) {
        super(titulo, duracionEnMinutos, genero);
        this.anfitrion = anfitrion;
        this.plataforma = plataforma;
        this.cantidadEpisodios = cantidadEpisodios;
    }

    public String getAnfitrion() { return anfitrion; }
    public void setAnfitrion(String anfitrion) { this.anfitrion = anfitrion; }

    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }

    public int getCantidadEpisodios() { return cantidadEpisodios; }
    public void setCantidadEpisodios(int cantidadEpisodios) { this.cantidadEpisodios = cantidadEpisodios; }

    public Invitado getInvitado() { return invitado; }
    public void setInvitado(Invitado invitado) { this.invitado = invitado; }

    public void publicarEpisodio() {
        cantidadEpisodios++;
    }

    @Override
    public String ejecutarAccionEspecial() {
        publicarEpisodio();
        return "Episodio publicado. Total: " + cantidadEpisodios + ".";
    }

    @Override
    public String obtenerDetalles() {
        StringBuilder detalles = new StringBuilder(detallesBase("Detalles del podcast:"));
        detalles.append("\nAnfitrión: ").append(anfitrion);
        detalles.append("\nPlataforma: ").append(plataforma);
        detalles.append("\nEpisodios: ").append(cantidadEpisodios);
        if (invitado != null) {
            detalles.append("\nInvitado: ").append(invitado);
        }
        return detalles.toString();
    }
}