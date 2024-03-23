package classes;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// Classe para representar o sistema de cadastro e notificação de eventos
class SistemaEventos {
    private List<Evento> eventos;
    private List<Usuario> usuarios;
    private Scanner scanner;

    // Construtor
    public SistemaEventos() {
        eventos = new ArrayList<>();
        usuarios = new ArrayList<>();
        scanner = new Scanner(System.in);
        CarregarEventos();
        CarregarUsuarios();
    }

    // Método para salvar eventos em um arquivo
    private void SalvarEventos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("events.data"))) {
            for (Evento evento : eventos) {
                writer.println(evento.ToFileString());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar eventos: " + e.getMessage());
        }
    }

    // Método para carregar eventos a partir de um arquivo
    private void CarregarEventos() {
        try (BufferedReader reader = new BufferedReader(new FileReader("events.data"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                eventos.add(Evento.FromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar eventos: " + e.getMessage());
            // Se o arquivo não existir, crie um novo arquivo
            CriarArquivo("events.data");
        }
    }

    // Método para criar um arquivo se não existir
    private void CriarArquivo(String nomeArquivo) {
        try {
            File arquivo = new File(nomeArquivo);
            if (arquivo.createNewFile()) {
                System.out.println("Arquivo '" + nomeArquivo + "' criado com sucesso.");
            } else {
                System.out.println("O arquivo '" + nomeArquivo + "' já existe.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao criar o arquivo '" + nomeArquivo + "': " + e.getMessage());
        }
    }

    // Método para cadastrar um evento
    public void CadastrarEvento() {
        System.out.println("Cadastro de Evento");

        // Solicitar informações do evento
        System.out.print("Nome do Evento: ");
        String nome = scanner.nextLine();

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        System.out.println("Categoria (festas, eventos esportivos, shows): ");
        String categoria = scanner.nextLine();

        while (!categoria.equals("festas") && !categoria.equals("eventos esportivos") && !categoria.equals("shows")) {
            System.out.println("Categoria inválida! Escolha entre festas, eventos esportivos e shows: ");
            categoria = scanner.nextLine();
        }

        System.out.print("Horário de Início (Formato: dd/MM/yyyy HH:mm): ");
        String horarioInicioStr = scanner.nextLine();
        LocalDateTime horarioInicio = LocalDateTime.parse(horarioInicioStr,
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        System.out.print("Horário de Término (Formato: dd/MM/yyyy HH:mm): ");
        String horarioFimStr = scanner.nextLine();
        LocalDateTime horarioFim = LocalDateTime.parse(horarioFimStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        Evento evento = new Evento(nome, endereco, cidade, categoria, horarioInicio, horarioFim, descricao);
        eventos.add(evento);

        SalvarEventos(); // Após cadastrar, salva os eventos

        System.out.println("Evento cadastrado com sucesso!");
    }

    // Método para exibir todos os eventos cadastrados que ainda não ocorreram
    public void ExibirEventos() {
        LocalDateTime agora = LocalDateTime.now();
        System.out.println("Eventos cadastrados que ainda não ocorreram:");

        eventos.stream()
                .filter(evento -> evento.GetHorarioInicio().isAfter(agora)) // Eventos com horário de início posterior
                                                                            // ao horário atual
                .sorted(Comparator.comparing(Evento::GetHorarioInicio))
                .forEach(evento -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    System.out.println("Nome: " + evento.GetNome());
                    System.out.println("Endereço: " + evento.GetEndereco());
                    System.out.println("Cidade: " + evento.GetCidade());
                    System.out.println("Categoria: " + evento.GetCategoria());
                    System.out.println("Horário de Início: " + evento.GetHorarioInicio().format(formatter));
                    System.out.println("Horário de Término: " + evento.GetHorarioFim().format(formatter));
                    System.out.println("Descrição: " + evento.GetDescricao());
                    System.out.println();
                });
    }

    // Método para exibir eventos que já ocorreram
    public void ExibirEventosPassados() {
        LocalDateTime agora = LocalDateTime.now();
        System.out.println("Eventos que já ocorreram:");

        eventos.stream()
                .filter(evento -> evento.GetHorarioFim().isBefore(agora)) // Eventos com horário de término anterior ao
                                                                          // horário atual
                .sorted(Comparator.comparing(Evento::GetHorarioFim).reversed()) // Ordena do mais recente para o mais
                                                                                // antigo
                .forEach(evento -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    System.out.println("Nome: " + evento.GetNome());
                    System.out.println("Endereço: " + evento.GetEndereco());
                    System.out.println("Cidade: " + evento.GetCidade());
                    System.out.println("Categoria: " + evento.GetCategoria());
                    System.out.println("Horário de Início: " + evento.GetHorarioInicio().format(formatter));
                    System.out.println("Horário de Término: " + evento.GetHorarioFim().format(formatter));
                    System.out.println("Descrição: " + evento.GetDescricao());
                    System.out.println();
                });
    }

    // Método para verificar se há eventos ocorrendo no momento e exibi-los
    public void VerificarEventosOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        System.out.println("Eventos ocorrendo agora:");

        eventos.stream()
                .filter(evento -> evento.GetHorarioInicio().isBefore(agora) && evento.GetHorarioFim().isAfter(agora)) // Eventos
                                                                                                                      // dentro
                                                                                                                      // do
                                                                                                                      // horário
                                                                                                                      // atual
                .sorted(Comparator.comparing(Evento::GetHorarioInicio))
                .forEach(evento -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    System.out.println("Nome: " + evento.GetNome());
                    System.out.println("Endereço: " + evento.GetEndereco());
                    System.out.println("Cidade: " + evento.GetCidade());
                    System.out.println("Categoria: " + evento.GetCategoria());
                    System.out.println("Horário de Início: " + evento.GetHorarioInicio().format(formatter));
                    System.out.println("Horário de Término: " + evento.GetHorarioFim().format(formatter));
                    System.out.println("Descrição: " + evento.GetDescricao());
                    System.out.println();
                });
    }

    // Método para marcar presença em um evento
    // Método para marcar presença em um evento
    public void MarcarPresenca() {
        if (eventos.isEmpty()) {
            System.out.println("Não há eventos disponíveis para marcar presença.");
            return;
        }

        LocalDateTime agora = LocalDateTime.now();
        List<Evento> eventosFuturos = eventos.stream()
                .filter(evento -> evento.GetHorarioInicio().isAfter(agora))
                .collect(Collectors.toList());

        if (eventosFuturos.isEmpty()) {
            System.out.println("Não há eventos futuros disponíveis para marcar presença.");
            return;
        }

        // Obter a cidade do usuário atual - você pode solicitar ao usuário ou obter de
        // alguma outra fonte
        System.out.print("Digite a sua cidade: ");
        String cidadeUsuario = scanner.nextLine();

        // Filtrar os eventos futuros disponíveis apenas para a cidade do usuário
        eventosFuturos = eventosFuturos.stream()
                .filter(evento -> evento.GetCidade().equalsIgnoreCase(cidadeUsuario))
                .collect(Collectors.toList());

        if (eventosFuturos.isEmpty()) {
            System.out.println("Não há eventos futuros disponíveis na sua cidade para marcar presença.");
            return;
        }

        System.out.println("Escolha o evento para marcar presença:");

        for (int i = 0; i < eventosFuturos.size(); i++) {
            System.out.println((i + 1) + ". " + eventosFuturos.get(i).GetNome());
        }

        System.out.print("Digite o número do evento ou 0 para voltar ao menu principal: ");
        int numeroEvento = Integer.parseInt(scanner.nextLine());

        if (numeroEvento == 0) {
            return;
        }

        if (numeroEvento >= 1 && numeroEvento <= eventosFuturos.size()) {
            Evento eventoSelecionado = eventosFuturos.get(numeroEvento - 1);

            // Exibir os usuários disponíveis para marcação de presença
            System.out.println("Escolha um usuário para marcar presença:");
            for (int i = 0; i < usuarios.size(); i++) {
                // Filtrar usuários apenas para a mesma cidade do evento
                if (usuarios.get(i).GetCidade().equalsIgnoreCase(cidadeUsuario)) {
                    System.out.println((i + 1) + ". " + usuarios.get(i).GetNomeCompleto());
                }
            }

            System.out.println("0. Criar novo usuário");
            System.out.print("Digite o número do usuário ou 0 para criar um novo: ");
            int numeroUsuario = Integer.parseInt(scanner.nextLine());

            if (numeroUsuario == 0) {
                CadastrarUsuario(); // Criar um novo usuário
                return;
            }

            if (numeroUsuario >= 1 && numeroUsuario <= usuarios.size()) {
                Usuario usuarioSelecionado = usuarios.get(numeroUsuario - 1);
                eventoSelecionado.AdicionarParticipante(usuarioSelecionado);
                SalvarEventos(); // Após marcar presença, salva os eventos
                System.out.println("Presença marcada com sucesso para o evento: " + eventoSelecionado.GetNome());
            } else {
                System.out.println("Número do usuário inválido.");
            }
        } else {
            System.out.println("Número do evento inválido.");
        }
    }

    // Método para cancelar a presença em um evento
    public void CancelarPresenca() {
        if (eventos.isEmpty()) {
            System.out.println("Não há eventos disponíveis para cancelar presença.");
            return;
        }

        LocalDateTime agora = LocalDateTime.now();
        List<Evento> eventosFuturos = eventos.stream()
                .filter(evento -> evento.GetHorarioInicio().isAfter(agora))
                .collect(Collectors.toList());

        if (eventosFuturos.isEmpty()) {
            System.out.println("Não há eventos futuros disponíveis para cancelar presença.");
            return;
        }

        System.out.println("Escolha o evento para cancelar presença:");
        for (int i = 0; i < eventosFuturos.size(); i++) {
            System.out.println((i + 1) + ". " + eventosFuturos.get(i).GetNome());
        }

        System.out.print("Digite o número do evento ou 0 para voltar ao menu principal: ");
        int numeroEvento = Integer.parseInt(scanner.nextLine());

        if (numeroEvento == 0) {
            return;
        }

        if (numeroEvento >= 1 && numeroEvento <= eventosFuturos.size()) {
            Evento eventoSelecionado = eventosFuturos.get(numeroEvento - 1);

            // Exibir os participantes do evento
            List<Usuario> participantes = eventoSelecionado.GetParticipantes();
            if (participantes.isEmpty()) {
                System.out.println("Não há participantes neste evento.");
                return;
            }

            System.out.println("Participantes do evento '" + eventoSelecionado.GetNome() + "':");
            for (int i = 0; i < participantes.size(); i++) {
                System.out.println((i + 1) + ". " + participantes.get(i).GetNomeCompleto());
            }

            System.out.print("Digite o número do participante para cancelar presença ou 0 para voltar: ");
            int numeroParticipante = Integer.parseInt(scanner.nextLine());

            if (numeroParticipante == 0) {
                return;
            }

            if (numeroParticipante >= 1 && numeroParticipante <= participantes.size()) {
                Usuario participanteSelecionado = participantes.get(numeroParticipante - 1);
                eventoSelecionado.RemoverParticipante(participanteSelecionado);
                SalvarEventos(); // Após cancelar presença, salva os eventos
                System.out.println("Presença cancelada com sucesso para o participante: "
                        + participanteSelecionado.GetNomeCompleto());
            } else {
                System.out.println("Número do participante inválido.");
            }
        } else {
            System.out.println("Número do evento inválido.");
        }
    }

    // Método para retornar a lista de eventos
    public List<Evento> GetEventos() {
        return eventos;
    }

    // Método para retornar a lista de usuários
    public List<Usuario> GetUsuarios() {
        return usuarios;
    }

    // Método para salvar usuários em um arquivo
    private void SalvarUsuarios() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.data"))) {
            for (Usuario usuario : usuarios) {
                writer.println(usuario.ToFileString());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }

    // Método para gerar id não sendo chamado..
    // Método para gerar um novo ID unico para usuario
    /*
     * private int gerarNovoIdUsuario() {
     * // verifica qual o maior ID atualmente em uso
     * int maiorId = usuarios.stream()
     * .mapToInt(Usuario::getId)
     * .max().orElse(0);
     * 
     * return maiorId + 1;
     * }
     */

    // Método para carregar usuários a partir de um arquivo
    private void CarregarUsuarios() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.data"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                usuarios.add(Usuario.FromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar usuários: " + e.getMessage());
            // Se o arquivo não existir, crie um novo arquivo
            CriarArquivo("users.data");
        }
    }

    // Método para consultar usuários pelo ID
    public void ConsultarUsuarioPorId(int id) {
        Usuario usuarioEncontrado = usuarios.stream()
                .filter(usuario -> usuario.GetId() == id)
                .findFirst()
                .orElse(null);

        if (usuarioEncontrado != null) {
            System.out.println("Usuário encontrado:");
            System.out.println("ID: " + usuarioEncontrado.GetId());
            System.out.println("Nome: " + usuarioEncontrado.GetNomeCompleto());
            System.out.println("Email: " + usuarioEncontrado.GetEmail());
            System.out.println("Cidade: " + usuarioEncontrado.GetCidade());
        } else {
            System.out.println("Usuário com o ID " + id + " não encontrado.");
        }
    }

    // Método para cadastrar um novo usuário
    public void CadastrarUsuario() {
        System.out.println("Cadastro de Usuário");

        // Solicitar informações do usuário
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Sobrenome: ");
        String sobrenome = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        // Gera novo id unico para cada usuario / se bugar resolver esse problema de não
        // usar
        // int novoId = gerarNovoIdUsuario();

        Usuario novoUsuario = new Usuario(nome, sobrenome, email, cidade);
        usuarios.add(novoUsuario);

        SalvarUsuarios(); // Após cadastrar, salva os usuários

        System.out.println("Usuário cadastrado com sucesso!");
    }

    // Método para consultar usuários pelo nome
    public void ConsultarUsuarioPorNome(String nome) {
        List<Usuario> usuariosEncontrados = usuarios.stream()
                .filter(usuario -> usuario.GetNomeCompleto().equalsIgnoreCase(nome))
                .collect(Collectors.toList());

        if (!usuariosEncontrados.isEmpty()) {
            System.out.println("Usuários encontrados com o nome '" + nome + "':");
            for (Usuario usuario : usuariosEncontrados) {
                System.out.println("ID: " + usuario.GetId());
                System.out.println("Nome: " + usuario.GetNomeCompleto());
                System.out.println("Email: " + usuario.GetEmail());
                System.out.println("Cidade: " + usuario.GetCidade());
                System.out.println();
            }
        } else {
            System.out.println("Nenhum usuário encontrado com o nome '" + nome + "'.");
        }
    }

    // Método principal para consultar usuários
    public void ConsultarUsuarios() {
        System.out.println("Consulta de Usuário");
        System.out.println("Escolha uma opção:");
        System.out.println("1. Pesquisar por ID");
        System.out.println("2. Pesquisar por Nome");
        System.out.println("3.Listar todos os Usuários");

        int opcao = Integer.parseInt(scanner.nextLine());

        switch (opcao) {
            case 1:
                System.out.print("Digite o ID do usuário: ");
                int id = Integer.parseInt(scanner.nextLine());
                ConsultarUsuarioPorId(id);
                break;
            case 2:
                System.out.print("Digite o nome e sobrenome do usuário: ");
                String nome = scanner.nextLine();
                ConsultarUsuarioPorNome(nome);
                break;
            case 3:
                ListarTodosUsuarios();
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    // Método para listar todos os usuários
    public void ListarTodosUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("Não há usuários cadastrados.");
            return;
        }

        System.out.println("Lista de todos os usuários:");
        for (Usuario usuario : usuarios) {
            System.out.println("ID: " + usuario.GetId());
            System.out.println("Nome: " + usuario.GetNomeCompleto());
            System.out.println("Email: " + usuario.GetEmail());
            System.out.println("Cidade: " + usuario.GetCidade());
            System.out.println();
        }
    }

}