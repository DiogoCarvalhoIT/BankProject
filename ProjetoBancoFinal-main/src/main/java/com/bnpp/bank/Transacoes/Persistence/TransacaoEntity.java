package com.bnpp.bank.Transacoes.Persistence;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "Transacoes")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // todo mudar para indetity
    @Column(nullable = false,updatable = false)
    Long id;
    @Column(nullable = false, updatable = false)
    String tipoOperacao;
    @Column
    Long numeroCartao;
    @Column(nullable = false, updatable = false)
    Long numeroConta;

    @Column(nullable = false, updatable = false)
    Double montante;

    @Column(nullable = false, updatable = false)
    Date momentoTransacao;
    @Column
    Long destinatario;

    public TransacaoEntity(String tipoOperacao, Long numeroCartao, Long numeroConta, Double montante, Date momentoTransacao, Long destinatario) {
        this.tipoOperacao = tipoOperacao;
        this.numeroCartao = numeroCartao;
        this.numeroConta = numeroConta;
        this.montante = montante;
        this.momentoTransacao = momentoTransacao;
        this.destinatario = destinatario;
    }
}
