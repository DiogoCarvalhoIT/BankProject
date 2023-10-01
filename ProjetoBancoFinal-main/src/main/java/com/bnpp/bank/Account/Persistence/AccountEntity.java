package com.bnpp.bank.Account.Persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity(name = "Accounts") // Modela para uma tabela
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity implements Serializable {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
@Column(nullable = false, updatable = false)
 private long numeroConta;

@Column(nullable = false)
private long titularPrincipal;

@ElementCollection
private List<Long> titularesSecundarios;
 
@Column(nullable = false)
 private double saldoConta;

 public AccountEntity(long titularPrincipal, double saldoConta) {
  this.titularPrincipal = titularPrincipal;
  this.saldoConta = saldoConta;
 }

 public void adicionarTitularSecundario(Long titularSecundario) {
  titularesSecundarios.add(titularSecundario);
 }


}
