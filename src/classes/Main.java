package classes;
// Classe principal

import java.util.Scanner;

import classes.Menu.MenuPrincipal;

public class Main {
    public static void main(String[] args) {

        SistemaEventos sistema = new SistemaEventos();
        Scanner scanner = new Scanner(System.in);

        MenuPrincipal menu = new MenuPrincipal(sistema, scanner);
        menu.ExibirMenu();
    }
}