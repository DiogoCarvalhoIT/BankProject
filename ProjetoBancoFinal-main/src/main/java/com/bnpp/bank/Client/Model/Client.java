package com.bnpp.bank.Client.Model;



public class Client {

    private Long nif;
    private String nomeCompleto;
    private String senha;
    private String dataNascimento;
    private int idade;
    private int telefone;
    private int telemovel;
    private String email;
    private String profissao;

    public Client(String nomeCompleto, Long nif, String senha, String dataNascimento, int idade, int telefone,
            int telemovel, String email, String profissao) {
        this.nomeCompleto = nomeCompleto;
        this.nif = nif;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.idade = idade;
        this.telefone = telefone;
        this.telemovel = telemovel;
        this.email = email;
        this.profissao = profissao;
    }

    // RequiredByJPA
    public Client() {
    }

    public Long getNif() {
        return nif;
    }

    public void setNif(Long nif) {
        this.nif = nif;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    public int getTelemovel() {
        return telemovel;
    }

    public void setTelemovel(int telemovel) {
        this.telemovel = telemovel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

}
