package classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Evento {

    // Variaveis
    private String nome;
    private String endereco;
    private String cidade;
    private String categoria;
    private LocalDateTime horarioInicio;
    private LocalDateTime horarioFim;
    private String descricao;
    private List<Usuario> participantes;

    // Construtor
    public Evento(String nome, String endereco, String cidade, String categoria, LocalDateTime horarioInicio,
            LocalDateTime horarioFim,
            String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = cidade;
        this.categoria = categoria;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.descricao = descricao;
        this.participantes = new ArrayList<>();
    }

    // Getters e setters
    public String GetNome() {
        return nome;
    }

    public String GetEndereco() {
        return endereco;
    }

    public String GetCidade() {
        return cidade;
    }

    public String GetCategoria() {
        return categoria;
    }

    public LocalDateTime GetHorarioInicio() {
        return horarioInicio;
    }

    public LocalDateTime GetHorarioFim() {
        return horarioFim;
    }

    public String GetDescricao() {
        return descricao;
    }

    public List<Usuario> GetParticipantes() {
        return participantes;
    }

    public void AdicionarParticipante(Usuario usuario) {
        participantes.add(usuario);
    }

    public void RemoverParticipante(Usuario usuario) {
        participantes.remove(usuario);
    }

    // Método para converter o evento para uma string no formato de linha do arquivo
    public String toFileString() {
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        return nome + ";" + endereco + ";" + cidade + ";" + categoria + ";"
                + horarioInicio.format(formatterDate) + " " + horarioInicio.format(formatterTime) + ";"
                + horarioFim.format(formatterDate) + " " + horarioFim.format(formatterTime) + ";" + descricao;
    }

    // Método para criar um evento a partir de uma linha do arquivo
    public static Evento fromFileString(String line) {
        String[] parts = line.split(";");
        String nome = parts[0];
        String endereco = parts[1];
        String cidade = parts[2];
        String categoria = parts[3];
        LocalDateTime horarioInicio = LocalDateTime.parse(parts[4].trim(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        LocalDateTime horarioFim = LocalDateTime.parse(parts[5].trim(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        // DateTimeFormatter formatterDateTime =
        // DateTimeFormatter.ofPattern("dd/MM/yyyyHH:mm");
        // LocalDateTime horarioInicio = LocalDateTime.parse(parts[4],
        // formatterDateTime);
        // LocalDateTime horarioFim = LocalDateTime.parse(parts[5], formatterDateTime);
        String descricao = parts[6];
        return new Evento(nome, endereco, cidade, categoria, horarioInicio, horarioFim, descricao);
    }
}