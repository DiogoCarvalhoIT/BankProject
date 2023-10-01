
package com.bnpp.bank.Card.Persistence;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Entity(name = "Cartoes")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardEntity implements Serializable {

    private static final long serialVersionUID = 2142804344907462586L;
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private Long numeroCartao;
    @Column(nullable = false, updatable = false)
    private Long numeroConta;
    @Column(nullable = false, updatable = false)
    private Long titularCartao;
    @Column(nullable = false, updatable = false)
    private String tipoCartao;
    @Column(nullable = false)
    private long pin;
    @Column
    private Double plafondAtual;
    @Column
    private Double plafondMensal;

    public CardEntity(Long numeroConta, Long titularCartao, String tipoCartao, long pin, Double plafondAtual, Double plafondMensal) {
        this.numeroConta = numeroConta;
        this.titularCartao = titularCartao;
        this.tipoCartao = tipoCartao;
        this.pin = pin;
        this.plafondAtual = plafondAtual;
        this.plafondMensal = plafondMensal;
    }
}
