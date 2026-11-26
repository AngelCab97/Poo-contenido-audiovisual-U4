package modelo;

public abstract class ContenidoAudiovisual {
    private static int contar = 0;
    private String titulo;
    private int duracionEnMinutos;
    private String genero;
    private final int id;

    public ContenidoAudiovisual(String titulo, int duracionEnMinutos, String genero) {
        this.id = contar++;
        this.titulo = titulo;
        this.duracionEnMinutos = duracionEnMinutos;
        this.genero = genero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracionEnMinutos() {
        return duracionEnMinutos;
    }

    public void setDuracionEnMinutos(int duracionEnMinutos) {
        this.duracionEnMinutos = duracionEnMinutos;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    // El id se asigna automáticamente al crear el objeto y no puede cambiar
    public int getId() {
        return id;
    }

    protected String detallesBase(String encabezado) {
        return encabezado + "\n"
                + "ID: " + id + "\n"
                + "Título: " + titulo + "\n"
                + "Duración en minutos: " + duracionEnMinutos + "\n"
                + "Género: " + genero;
    }

    public abstract String obtenerDetalles();
}