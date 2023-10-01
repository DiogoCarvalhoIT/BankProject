
 package com.bnpp.bank.Account.Model;


 import java.util.List;

 public class Account {

private long numeroConta;
private long titularPrincipal;
private double saldoConta;
private List<Long> titularesSecundarios;

     public Account(long numeroConta, long titularPrincipal, double saldoConta, String titularesSecundarios) {
         this.numeroConta = numeroConta;
         this.titularPrincipal = titularPrincipal;
         this.saldoConta = saldoConta;
     }

     public Account() {
     }

     public long getNumeroConta() {
         return numeroConta;
     }

     public void setNumeroConta(long numeroConta) {
         this.numeroConta = numeroConta;
     }

     public long getTitularPrincipal() {
         return titularPrincipal;
     }

     public void setTitularPrincipal(long titularPrincipal) {
         this.titularPrincipal = titularPrincipal;
     }

     public double getSaldoConta() {
         return saldoConta;
     }

     public void setSaldoConta(double saldoConta) {
         this.saldoConta = saldoConta;
     }

     public List<Long> getTitularesSecundarios() {
         return titularesSecundarios;
     }

     public void setTitularesSecundarios(List<Long> titularesSecundarios) {
         this.titularesSecundarios = titularesSecundarios;
     }


 }