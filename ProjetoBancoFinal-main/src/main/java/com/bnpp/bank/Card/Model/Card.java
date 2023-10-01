
package com.bnpp.bank.Card.Model;

public class Card { // todo rever a utilizacao da classe

    private int numeroCartao;
    private int numeroConta;
    private String tipoCartao;
    private long pin;


    public Card() {
    }

    public Card(int numeroCartao, int numeroConta, String tipoCartao, long pin) {
        this.numeroCartao = numeroCartao;
        this.numeroConta = numeroConta;
        this.tipoCartao = tipoCartao;
        this.pin = pin;
    }

    public int getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(int numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public long getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(int numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(String tipoCartao) {
        this.tipoCartao = tipoCartao;
    }

    public long getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}
