package poo;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import controlador.ControladorContenidos;
import modelo.CatalogoContenidos;
import persistencia.FormatoConcierto;
import persistencia.FormatoContenido;
import persistencia.FormatoDocumental;
import persistencia.FormatoPelicula;
import persistencia.FormatoPodcast;
import persistencia.FormatoSerie;
import persistencia.RepositorioContenidos;
import persistencia.RepositorioContenidosCsv;
import vista.Vista;
import vista.VistaConsola;

public class Aplicacion {

    private static final Path RUTA_CSV = Path.of("datos", "contenidos.csv");

    public static void main(String[] args) {
        List<FormatoContenido> formatos = List.of(
                new FormatoPelicula(),
                new FormatoSerie(),
                new FormatoDocumental(),
                new FormatoPodcast(),
                new FormatoConcierto());

        Vista vista = new VistaConsola(new Scanner(System.in));
        CatalogoContenidos catalogo = new CatalogoContenidos();
        RepositorioContenidos repositorio = new RepositorioContenidosCsv(RUTA_CSV, formatos);

        new ControladorContenidos(vista, catalogo, repositorio).iniciar();
    }
}