
package com.bnpp.bank.Card.Persistence;


import com.bnpp.bank.Card.Model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Integer> {

        CardEntity save(CardEntity card);
        List<CardEntity> findCardEntityByNumeroConta(Long numeroConta);
        CardEntity findCardEntitiesByNumeroCartaoAndPin(Long numeroCartao, Long pin);
        CardEntity findCardEntityByNumeroCartao(Long numeroCartao);

        CardEntity findCardEntityByNumeroCartaoAndPin(Long numeroCartao, Long pin);
        List<CardEntity> findCardEntityByNumeroContaAndTitularCartaoAndTipoCartao (Long numeroConta, Long titularCartao, String tipoCartao);

        List<CardEntity> findCardEntityByTitularCartaoAndTipoCartao (Long titularCartao, String tipoCartao);
        List<CardEntity> findCardEntityByNumeroContaAndTipoCartao (Long numeroConta, String tipoCartao);
        List<CardEntity> findCardEntityByTitularCartao(Long titularCartao);

        List<CardEntity> findCardEntitiesByTitularCartaoAndNumeroConta(Long nif, Long numeroConta);

        CardEntity deleteByNumeroCartao(Long numeroCartao);

        //void deleteAll(List<CardEntity> cards);

}



    //CardEntity save(CardEntity cardEntity);





