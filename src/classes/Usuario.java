package classes;

class Usuario {

    // Variaveis
    private static int proximoId = 1; // variavel para controlar o proximo id a ser incrementado
    private int id; // ID unico do usuario
    private String nome;
    private String sobrenome;
    private String email;
    private String cidade;

    // Construtor
    public Usuario(String nome, String sobrenome, String email, String cidade) {
        id = proximoId++;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.cidade = cidade;
    }

    // Getters

    public int GetId() {
        return id;
    }

    public String GetNomeCompleto() {
        return nome + " " + sobrenome;
    }

    public String GetEmail() {
        return email;
    }

    public String GetCidade() {
        return cidade;
    }

    // Método para converter o usuário para uma string no formato de linha do
    // arquivo
    public String ToFileString() {
        return nome + ";" + sobrenome + ";" + email + ";" + cidade;
    }

    // Método para criar um usuário a partir de uma linha do arquivo
    public static Usuario FromFileString(String line) {
        String[] parts = line.split(";");
        String nome = parts[0];
        String sobrenome = parts[1];
        String email = parts[2];
        String cidade = parts[3];
        return new Usuario(nome, sobrenome, email, cidade);
    }

}