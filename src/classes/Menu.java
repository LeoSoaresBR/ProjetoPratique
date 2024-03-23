package classes;

import java.util.Scanner;

public class Menu {

    public static class MenuPrincipal {

        private SistemaEventos sistema;
        private Scanner scanner;

        public MenuPrincipal(SistemaEventos sistema, Scanner scanner) {
            this.sistema = sistema;
            this.scanner = scanner;
        }

        public void exibirMenu() {
            while (true) {
                System.out.println("\n===== Menu Principal =====");
                System.out.println("1. Cadastrar Evento");
                System.out.println("2. Exibir Eventos Futuros");
                System.out.println("3. Exibir Eventos Passados");
                System.out.println("4. Verificar Eventos Ocorrendo Agora");
                System.out.println("5. Cadastrar Usuário");
                System.out.println("6. Consultar Usuário");
                System.out.println("7. Marcar Presença em Evento");
                System.out.println("8. Cancelar Presença em Evento");
                System.out.println("0. Sair");
                System.out.print("\nEscolha uma opção: ");
                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        sistema.cadastrarEvento();
                        break;
                    case 2:
                        sistema.exibirEventos();
                        break;
                    case 3:
                        sistema.exibirEventosPassados();
                        break;
                    case 4:
                        sistema.verificarEventosOcorrendoAgora();
                        break;
                    case 5:
                        sistema.cadastrarUsuario();
                        break;
                    case 6:
                        sistema.consultarUsuarios();
                        break;
                    case 7:
                        sistema.marcarPresenca();
                        break;
                    case 8:
                        sistema.cancelarPresenca();
                        break;
                    case 0:
                        System.out.println("Encerrando o programa...");
                        System.exit(0);
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            }
        }
    }

}
