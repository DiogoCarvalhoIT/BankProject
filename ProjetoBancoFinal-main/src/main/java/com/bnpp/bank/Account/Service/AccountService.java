
package com.bnpp.bank.Account.Service;

import com.bnpp.bank.Card.Model.Card;
import com.bnpp.bank.Card.Persistence.CardEntity;
import com.bnpp.bank.Card.Persistence.CardRepository;
import com.bnpp.bank.Client.Persistence.ClientEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;

import com.bnpp.bank.Account.Persistence.AccountEntity;
import com.bnpp.bank.Account.Persistence.AccountRepository;

import java.util.Collections;
import java.util.List;

//declarar repositorio ClientRepository clientRepository
  
@Service // assim ja sabe onde encontrar o ClientService
public class AccountService {
 
 private AccountRepository accountRepository;
 private CardRepository cardRepository;


 
 @Autowired
 public AccountService(AccountRepository accountRepository, CardRepository cardRepository) {
 this.accountRepository = accountRepository;
 this.cardRepository = cardRepository;
 }

 /* ----------------------------- Criar Conta Bancaria ----------------------------- */
 public void criarConta(long nifTitularPrincipal, Double saldoConta) {

 AccountEntity conta = new AccountEntity(
         nifTitularPrincipal,
         saldoConta);

    //conta.adicionarTitularSecundario(titularSecundario);
    accountRepository.save(conta);
 
 }

 /* ----------------------------- Encontrar Contas Bancarias ----------------------------- */

  public List<AccountEntity> getAccounts(long nifTitularPrincipal) {
   return accountRepository.findAccountEntityByTitularPrincipal(nifTitularPrincipal);
  }

  public List<AccountEntity> encontrarContasSecundarios(Long nif) {
   return accountRepository.findAccountEntityByTitularesSecundarios(nif);
  }

  public  AccountEntity encontrarContasTitularSecundario(Long nif) {
   return accountRepository.findEntityByTitularesSecundarios(nif);
  }

  public AccountEntity getAccountsNifNumeroConta(long nifTitularPrincipal, long numeroConta) {
  return accountRepository.findAccountEntitiesByTitularPrincipalAndNumeroConta(nifTitularPrincipal, numeroConta);
 }

 /*--------------------------- Titulares Secundarios ---------------------- */

 public void adicionarTitularSecundario(AccountEntity conta, long nifTitularSecundario) {
   conta.adicionarTitularSecundario(nifTitularSecundario);
   accountRepository.save(conta);
 }

 public void removerTitularSecundario(AccountEntity conta, Long nifTitularSecundario) {
  List<Long> listaTitularesSecundarios = conta.getTitularesSecundarios();
  listaTitularesSecundarios.remove(nifTitularSecundario);
  conta.setTitularesSecundarios(listaTitularesSecundarios);

  if(cardRepository.findCardEntitiesByTitularCartaoAndNumeroConta(nifTitularSecundario, conta.getNumeroConta()) != null){
     List<CardEntity> cartoesTitularSecundario = cardRepository.findCardEntitiesByTitularCartaoAndNumeroConta(nifTitularSecundario, conta.getNumeroConta());
     cardRepository.deleteAll(cartoesTitularSecundario);
    accountRepository.save(conta);
  } else {
   accountRepository.save(conta);
  }
 }



 public boolean validarNifSessao(HttpSession session) {
  if(session.getAttribute("nif") != null) {
   return true;
  } else {
   return false;
  }
 }





 }
 