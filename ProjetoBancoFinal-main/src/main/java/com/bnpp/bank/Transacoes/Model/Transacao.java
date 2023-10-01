package com.bnpp.bank.Transacoes.Model;

public class Transacao {

    int id;
    String tipoOperacao;
    int numeroCartao;
    int numeroConta;
    String momentoTransacao;
    long montante;

    int destinatario;

    public Transacao() {
    }

    public Transacao(String tipoOperacao, int numeroCartao, int numeroConta, String momentoTransacao, long montante, int destinatario) {
        this.tipoOperacao = tipoOperacao;
        this.numeroCartao = numeroCartao;
        this.numeroConta = numeroConta;
        this.momentoTransacao = momentoTransacao;
        this.montante = montante;
        this.destinatario = destinatario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public int getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(int numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(int numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getMomentoTransacao() {
        return momentoTransacao;
    }

    public void setMomentoTransacao(String momentoTransacao) {
        this.momentoTransacao = momentoTransacao;
    }

    public long getMontante() {
        return montante;
    }

    public void setMontante(long montante) {
        this.montante = montante;
    }

    public int getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(int destinatario) {
        this.destinatario = destinatario;
    }

}

