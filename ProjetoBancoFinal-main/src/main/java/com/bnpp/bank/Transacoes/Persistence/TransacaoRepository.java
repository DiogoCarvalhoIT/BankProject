package com.bnpp.bank.Transacoes.Persistence;

import com.bnpp.bank.Card.Persistence.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {

    TransacaoEntity save(TransacaoEntity transacao);

    List<TransacaoEntity> findTransacaoEntitiesByNumeroConta(Long numeroConta);
    List<TransacaoEntity> findTransacaoEntitiesByNumeroCartao(Long numeroCartao);

    List<TransacaoEntity> findTransacaoEntitiesByDestinatario(Long destinatario);

    List<TransacaoEntity> findTransacaoEntitiesByNumeroContaAndMomentoTransacaoBetween(Long numeroConta, Date startOfDay, Date endOfDay);

    default List<TransacaoEntity> findTransacaoEntitiesByNumeroContaAndMomentoTransacaoOn(Long numeroConta, LocalDateTime data) {
        LocalDateTime momentoExato = data;
        ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(momentoExato);
        Date startDate = Date.from(data.toLocalDate().atStartOfDay().toInstant(offset));
        Date endDate = Date.from(data.toLocalDate().atTime(23, 59, 59).toInstant(offset));
        return findTransacaoEntitiesByNumeroContaAndMomentoTransacaoBetween(numeroConta, startDate, endDate);

    }


}
