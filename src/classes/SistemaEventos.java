package classes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
                writer.println(evento.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar eventos: " + e.getMessage());
        }
    }

    // Método para salvar usuários em um arquivo
    private void SalvarUsuarios() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.data"))) {
            for (Usuario usuario : usuarios) {
                writer.println(usuario.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }

    // Método para carregar usuários a partir de um arquivo
    private void CarregarUsuarios() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.data"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                usuarios.add(Usuario.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar usuários: " + e.getMessage());

        }
    }

    // Método para carregar eventos a partir de um arquivo
    private void CarregarEventos() {
        try (BufferedReader reader = new BufferedReader(new FileReader("events.data"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                eventos.add(Evento.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar eventos: " + e.getMessage());
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
        // Se não houver eventos disponíveis, retorna
        if (eventos.isEmpty()) {
            System.out.println("Não há eventos disponíveis para marcar presença.");
            return;
        }

        LocalDateTime agora = LocalDateTime.now();
        // Filtra os eventos futuros
        List<Evento> eventosFuturos = eventos.stream()
                .filter(evento -> evento.GetHorarioInicio().isAfter(agora))
                .collect(Collectors.toList());

        // Se não houver eventos futuros, retorna
        if (eventosFuturos.isEmpty()) {
            System.out.println("Não há eventos futuros disponíveis para marcar presença.");
            return;
        }

        // Solicita a cidade do usuário
        System.out.print("Digite a sua cidade: ");
        String cidadeUsuario = scanner.nextLine();

        // Filtra os eventos futuros disponíveis apenas para a cidade do usuário
        eventosFuturos = eventosFuturos.stream()
                .filter(evento -> evento.GetCidade().equalsIgnoreCase(cidadeUsuario))
                .collect(Collectors.toList());

        // Se não houver eventos futuros disponíveis na cidade do usuário, retorna
        if (eventosFuturos.isEmpty()) {
            System.out.println("Não há eventos futuros disponíveis na sua cidade para marcar presença.");
            return;
        }

        // Exibe os eventos futuros disponíveis
        System.out.println("Escolha o evento para marcar presença:");

        for (int i = 0; i < eventosFuturos.size(); i++) {
            System.out.println((i + 1) + ". " + eventosFuturos.get(i).GetNome());
        }

        System.out.print("Digite o número do evento ou 0 para voltar ao menu principal: ");
        int numeroEvento = Integer.parseInt(scanner.nextLine());

        // Verifica se o número do evento é válido
        if (numeroEvento == 0 || !(numeroEvento >= 1 && numeroEvento <= eventosFuturos.size())) {
            return;
        }

        Evento eventoSelecionado = eventosFuturos.get(numeroEvento - 1);

        // Exibe as opções de pesquisa de usuário
        System.out.println("Escolha a opção de pesquisa de usuário:");
        System.out.println("1. Pesquisar por ID");
        System.out.println("2. Pesquisar por Nome + Sobrenome");
        System.out.println("3. Pesquisar todos usuários");
        System.out.print("Digite a opção: ");
        int opcaoPesquisa = Integer.parseInt(scanner.nextLine());

        Usuario usuarioSelecionado;

        switch (opcaoPesquisa) {
            case 1:
                System.out.print("Digite o ID do usuário: ");
                int idUsuario = Integer.parseInt(scanner.nextLine());
                Usuario usuarioPorId = usuarios.stream()
                        .filter(usuario -> usuario.GetId() == idUsuario)
                        .findFirst()
                        .orElse(null);

                if (usuarioPorId != null) {
                    // Verificar se o usuário já está presente no evento
                    if (eventoSelecionado.GetParticipantes().contains(usuarioPorId)) {
                        System.out.println("O usuário já está presente neste evento.");
                        return;
                    }

                    // Exibe o nome associado ao ID e solicita confirmação
                    System.out.println("Usuário encontrado:");
                    System.out.println("Nome: " + usuarioPorId.GetNomeCompleto());
                    System.out.print("Confirmar presença para este usuário? (S/N): ");
                    String confirmacao = scanner.nextLine();

                    if (confirmacao.equalsIgnoreCase("S")) {
                        eventoSelecionado.AdicionarParticipante(usuarioPorId);
                        SalvarEventos();
                        SalvarUsuarios();
                        System.out
                                .println("Presença marcada com sucesso para o evento: " + eventoSelecionado.GetNome());
                    } else {
                        System.out.println("Presença não confirmada.");
                    }
                } else {
                    System.out.println("Usuário com o ID " + idUsuario + " não encontrado.");
                }
                break;

            case 2:
                System.out.print("Digite o nome + sobrenome do usuário: ");
                String nomeSobrenome = scanner.nextLine();
                List<Usuario> usuariosPorNome = usuarios.stream()
                        .filter(usuario -> usuario.GetNomeCompleto().equalsIgnoreCase(nomeSobrenome))
                        .collect(Collectors.toList());

                if (!usuariosPorNome.isEmpty()) {
                    System.out.println("Usuários encontrados com o nome '" + nomeSobrenome + "':");
                    for (int i = 0; i < usuariosPorNome.size(); i++) {
                        System.out.println((i + 1) + ". " + usuariosPorNome.get(i).GetNomeCompleto());
                    }

                    System.out.print("Digite o número do usuário ou 0 para cancelar: ");
                    int numeroUsuario = Integer.parseInt(scanner.nextLine());

                    if (!usuariosPorNome.isEmpty() && numeroUsuario >= 1 && numeroUsuario <= usuariosPorNome.size()) {
                        usuarioSelecionado = usuariosPorNome.get(numeroUsuario - 1);

                        // Verificar se o usuário já está presente no evento
                        if (eventoSelecionado.GetParticipantes().contains(usuarioSelecionado)) {
                            System.out.println("O usuário já está presente neste evento.");
                            return;
                        }

                        // Exibe o nome selecionado e solicita confirmação
                        System.out.println("Nome: " + usuarioSelecionado.GetNomeCompleto());
                        System.out.print("Confirmar presença para este usuário? (S/N): ");
                        String confirmacao = scanner.nextLine();

                        if (confirmacao.equalsIgnoreCase("S")) {
                            eventoSelecionado.AdicionarParticipante(usuarioSelecionado);
                            SalvarEventos();
                            System.out.println(
                                    "Presença marcada com sucesso para o evento: " + eventoSelecionado.GetNome());
                        } else {
                            System.out.println("Presença não confirmada.");
                        }
                    } else {
                        System.out.println("Número do usuário inválido.");
                    }
                } else {
                    System.out.println("Nenhum usuário encontrado com o nome '" + nomeSobrenome + "'.");
                }
                break;

            case 3:
                System.out.println("Lista de todos os usuários da cidade '" + cidadeUsuario + "':");
                List<Usuario> usuariosNaCidade = usuarios.stream()
                        .filter(usuario -> usuario.GetCidade().equalsIgnoreCase(cidadeUsuario))
                        .collect(Collectors.toList());

                if (!usuariosNaCidade.isEmpty()) {
                    for (int i = 0; i < usuariosNaCidade.size(); i++) {
                        System.out.println((i + 1) + ". " + usuariosNaCidade.get(i).GetNomeCompleto());
                    }

                    System.out.print("Digite o número do usuário ou 0 para cancelar: ");
                    int numeroUsuarioCidade = Integer.parseInt(scanner.nextLine());

                    if (numeroUsuarioCidade >= 1 && numeroUsuarioCidade <= usuariosNaCidade.size()) {
                        Usuario usuarioSelecionadoCidade = usuariosNaCidade.get(numeroUsuarioCidade - 1);
                        // Verificar se o usuário já está presente no evento
                        String nomeUsuario = usuarioSelecionadoCidade.GetNomeCompleto();
                        boolean usuarioPresente = eventoSelecionado.GetParticipantes().stream()
                                .anyMatch(participante -> participante.GetNomeCompleto().equals(nomeUsuario));
                        if (!usuarioPresente) {
                            if (!VerificarPresencaUsuario(usuarioSelecionadoCidade.GetId())) {
                                // O usuário não está presente no evento, então podemos adicioná-lo
                                eventoSelecionado.AdicionarParticipante(usuarioSelecionadoCidade);
                                PegarPresenca(usuarioSelecionadoCidade, eventoSelecionado); // Registrar presença no
                                                                                            // arquivo
                                SalvarEventos();
                                SalvarUsuarios(); // Salvar alterações na lista de usuários
                                System.out.println(
                                        "Presença marcada com sucesso para o evento: " + eventoSelecionado.GetNome());
                            } else {
                                System.out.println("O usuário já está presente neste evento.");
                            }
                        } else {
                            System.out.println("O usuário já está presente neste evento.");
                        }
                    } else {
                        System.out.println("Número do usuário inválido.");
                    }
                } else {
                    System.out.println("Nenhum usuário encontrado na cidade '" + cidadeUsuario + "'.");
                }
                break;

            default:
                System.out.println("Opção de pesquisa inválida.");
                break;
        }

    }

    public void PegarPresenca(Usuario usuario, Evento eventoSelecionado) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("presenca.data", true))) {
            // Escrever as informações da presença no arquivo no formato desejado
            writer.println("ID do Usuário: " + usuario.GetId());
            writer.println("Nome do Usuário: " + usuario.GetNomeCompleto());
            writer.println("Cidade do Usuário: " + usuario.GetCidade());
            writer.println("Evento: " + eventoSelecionado.GetNome());
            writer.println("Horário de Início do Evento: " +
                    eventoSelecionado.GetHorarioInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            writer.println(); // Adiciona uma linha em branco para separar as entradas no arquivo
            System.out.println("Presença registrada com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao registrar presença: " + e.getMessage());
        }
    }

    public boolean VerificarPresencaUsuario(int idUsuario) {
        boolean presente = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("presenca.data"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Verifica se a linha contém o ID do usuário
                if (line.startsWith("ID do Usuário:")) {
                    int idLido = Integer.parseInt(line.substring("ID do Usuário: ".length()).trim());
                    // Se o ID lido for igual ao ID do usuário que estamos procurando
                    if (idLido == idUsuario) {
                        presente = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de presença: " + e.getMessage());
        }
        return presente;
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
            System.out.println(
                    (i + 1) + ". " + eventosFuturos.get(i).GetNome() + " - " + eventosFuturos.get(i).GetCidade());
        }

        System.out.print("Digite o número do evento ou 0 para voltar ao menu principal: ");
        int numeroEvento = Integer.parseInt(scanner.nextLine());

        if (numeroEvento == 0) {
            return;
        }

        if (numeroEvento >= 1 && numeroEvento <= eventosFuturos.size()) {
            Evento eventoSelecionado = eventosFuturos.get(numeroEvento - 1);

            // Verificar se o usuário está presente neste evento
            System.out.print("Digite o ID do usuário para cancelar presença: ");
            int idUsuario = Integer.parseInt(scanner.nextLine());
            boolean usuarioPresente = VerificarPresencaUsuario(idUsuario);

            if (!usuarioPresente) {
                System.out.println("O usuário não está presente neste evento.");
                return;
            }

            // Verificar se o evento pertence à mesma cidade do usuário
            if (!eventoSelecionado.GetCidade().equalsIgnoreCase(usuarios.stream()
                    .filter(u -> u.GetId() == idUsuario)
                    .findFirst()
                    .orElse(new Usuario("", "", "", ""))
                    .GetCidade())) {
                System.out.println("O evento selecionado não pertence à mesma cidade do usuário.");
                return;
            }

            // Excluir todas as informações relacionadas ao usuário do arquivo
            // "presenca.data"
            try {
                File inputFile = new File("presenca.data");
                File tempFile = new File("temp.data");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String lineToRemove = "ID do Usuário: " + idUsuario;
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    if (currentLine.equals(lineToRemove)) {
                        // Pular todas as linhas relacionadas ao usuário
                        for (int i = 0; i < 4; i++) {
                            reader.readLine(); // Pular as próximas três linhas
                        }
                        continue;
                    }
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
                writer.close();
                reader.close();

                Files.move(tempFile.toPath(), inputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Presença cancelada com sucesso para o usuário com ID: " + idUsuario);
            } catch (IOException e) {
                System.out.println("Erro ao atualizar o arquivo de presença: " + e.getMessage());
                e.printStackTrace(); // Imprime o stack trace completo para depuração
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