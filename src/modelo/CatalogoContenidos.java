package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CatalogoContenidos {

    private final List<ContenidoAudiovisual> contenidos = new ArrayList<>();

    public void agregar(ContenidoAudiovisual contenido) {
        contenidos.add(contenido);
    }

    public boolean eliminar(ContenidoAudiovisual contenido) {
        return contenidos.remove(contenido);
    }

    public void reemplazarCon(List<ContenidoAudiovisual> nuevosContenidos) {
        contenidos.clear();
        contenidos.addAll(nuevosContenidos);
    }

    public Optional<ContenidoAudiovisual> buscarPorId(int id) {
        return contenidos.stream()
                .filter(contenido -> contenido.getId() == id)
                .findFirst();
    }

    public List<ContenidoAudiovisual> buscarPorTitulo(String texto) {
        String textoEnMinusculas = texto.toLowerCase();
        return contenidos.stream()
                .filter(contenido -> contenido.getTitulo().toLowerCase().contains(textoEnMinusculas))
                .collect(Collectors.toList());
    }

    public List<ContenidoAudiovisual> obtenerTodos() {
        return Collections.unmodifiableList(contenidos);
    }

    public boolean estaVacio() {
        return contenidos.isEmpty();
    }
}