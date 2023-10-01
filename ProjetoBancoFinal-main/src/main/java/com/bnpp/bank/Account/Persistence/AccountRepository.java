
 package com.bnpp.bank.Account.Persistence;

 import com.bnpp.bank.Card.Persistence.CardEntity;
 import com.bnpp.bank.Client.Persistence.ClientEntity;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;

 import java.util.List;


 @Repository
 public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

     AccountEntity save(AccountEntity conta);

     List<AccountEntity> findAccountEntityByTitularesSecundarios(Long nif);

     List<AccountEntity> findAccountEntityByTitularPrincipal(long nif);

     AccountEntity findAccountEntityByNumeroConta(long numeroConta);

     AccountEntity findEntityByTitularesSecundarios(Long nif);



     AccountEntity findAccountEntitiesByTitularPrincipalAndNumeroConta(long nif, long numeroConta);

     //AccountEntity findAccountEntityByNumeroCartao(Long numeroCartao);

    //AccountEntity findEntityByNifAndSenha(long nif, String senha);
}