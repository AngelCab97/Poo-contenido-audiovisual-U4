package poo;

import java.nio.file.Path;
import java.util.List;

import persistencia.ErrorPersistenciaException;
import persistencia.FormatoConcierto;
import persistencia.FormatoContenido;
import persistencia.FormatoDocumental;
import persistencia.FormatoPelicula;
import persistencia.FormatoPodcast;
import persistencia.FormatoSerie;
import persistencia.RepositorioContenidosCsv;
import uni1a.ContenidoAudiovisual;

public class PruebaCsv {
    public static void main(String[] args) {
        List<FormatoContenido> formatos = List.of(
                new FormatoPelicula(),
                new FormatoSerie(),
                new FormatoDocumental(),
                new FormatoPodcast(),
                new FormatoConcierto());

        RepositorioContenidosCsv original =
                new RepositorioContenidosCsv(Path.of("datos", "contenidos.csv"), formatos);
        RepositorioContenidosCsv copia =
                new RepositorioContenidosCsv(Path.of("datos", "copia.csv"), formatos);
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