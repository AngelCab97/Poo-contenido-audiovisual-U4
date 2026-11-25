package poo;

import java.nio.file.Path;
import java.util.List;

import persistencia.ErrorPersistenciaException;
import persistencia.RepositorioContenidosCsv;
import uni1a.ContenidoAudiovisual;

public class PruebaCsv {
    public static void main(String[] args) {
        RepositorioContenidosCsv original =
                new RepositorioContenidosCsv(Path.of("datos", "contenidos.csv"));
        RepositorioContenidosCsv copia =
                new RepositorioContenidosCsv(Path.of("datos", "copia.csv"));
        try {
            List<ContenidoAudiovisual> contenidos = original.cargar();
            copia.guardar(contenidos);
            System.out.println("Se guardaron " + contenidos.size() + " contenidos en datos/copia.csv");
        } catch (ErrorPersistenciaException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Causa: " + e.getCause());
        }
    }
}