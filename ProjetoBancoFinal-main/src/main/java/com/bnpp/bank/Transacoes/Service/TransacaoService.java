package com.bnpp.bank.Transacoes.Service;

import com.bnpp.bank.Account.Persistence.AccountEntity;
import com.bnpp.bank.Account.Persistence.AccountRepository;
import com.bnpp.bank.Transacoes.Persistence.TransacaoEntity;
import com.bnpp.bank.Transacoes.Persistence.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class TransacaoService {

    private TransacaoRepository transacaoRepository;
    private AccountRepository accountRepository;

    // ------------------------------------------------------------------ //
    @Autowired
    public TransacaoService(TransacaoRepository transacaoRepository,AccountRepository accountRepository) {
        this.transacaoRepository = transacaoRepository;
        this.accountRepository = accountRepository;
    }

    // ------------------------------------------------------------------ //

    public void realizarTransferencia(AccountEntity contaAtual, long destinatario,double montante) {
        AccountEntity contaDestinatario = accountRepository.findAccountEntityByNumeroConta(destinatario);

        contaAtual.setSaldoConta(contaAtual.getSaldoConta() - montante);
        accountRepository.save(contaAtual);

        contaDestinatario.setSaldoConta(contaDestinatario.getSaldoConta() + montante);
        accountRepository.save(contaDestinatario);

        registarTransacao("Transferencia",null, contaAtual.getNumeroConta(), montante, new Date(), destinatario);
    }

    public void realizarDeposito(AccountEntity contaAtual, double montante) {
        contaAtual.setSaldoConta(contaAtual.getSaldoConta() + montante);
        accountRepository.save(contaAtual);
        registarTransacao("Deposito", null, contaAtual.getNumeroConta(), montante, new Date(), null);
    }

    public void realizarLevantamento(AccountEntity contaAtual, double montante) {

        contaAtual.setSaldoConta(contaAtual.getSaldoConta() - montante);
        accountRepository.save(contaAtual);
        registarTransacao("Levantamento", null, contaAtual.getNumeroConta(), montante, new Date(), null);
    }

    public TransacaoEntity registarTransacao(String tipoOperacao, Long numeroCartao, Long numeroConta, Double montante, Date momentoTransacao, Long destinatario) {

        TransacaoEntity transacao = new TransacaoEntity(tipoOperacao,
                numeroCartao,
                numeroConta,
                montante,
                momentoTransacao,
                destinatario);

        return transacaoRepository.save(transacao);
    }

    public List<TransacaoEntity> listaTransacoes(long numeroConta) {
        return transacaoRepository.findTransacaoEntitiesByNumeroConta(numeroConta);
    }


    /*public List<TransacaoEntity> listaTransacoes(long numeroConta) {
        List<TransacaoEntity> listaTransacoes = transacaoRepository.findTransacaoEntitiesByNumeroConta(numeroConta);
        if(listaTransacoes == null) {
            List<TransacaoEntity> listaTransacoesNova = new List<TransacaoEntity>();
            return new List<TransacaoEntity>;
        } else {
            return listaTransacoes;
        }
    }*/

    public List <TransacaoEntity> listaTransacoesRecebidas(Long numeroConta) {
        return transacaoRepository.findTransacaoEntitiesByDestinatario(numeroConta);
    }

    public List<TransacaoEntity> listaTransacoesAtm(long numeroCartao) {
        return transacaoRepository.findTransacaoEntitiesByNumeroCartao(numeroCartao);
    }

    public AccountEntity verificarExistenciaConta(Long numeroContaDestinataria) {
        return accountRepository.findAccountEntityByNumeroConta(numeroContaDestinataria);
    }



}
