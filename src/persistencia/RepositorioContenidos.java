package persistencia;

import java.util.List;

import modelo.ContenidoAudiovisual;

public interface RepositorioContenidos {

    List<ContenidoAudiovisual> cargar() throws ErrorPersistenciaException;

    void guardar(List<ContenidoAudiovisual> contenidos) throws ErrorPersistenciaException;
}