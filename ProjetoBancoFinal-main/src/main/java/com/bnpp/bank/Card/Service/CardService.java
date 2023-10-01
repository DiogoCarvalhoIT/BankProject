
package com.bnpp.bank.Card.Service;
import com.bnpp.bank.Account.Persistence.AccountEntity;
import com.bnpp.bank.Account.Persistence.AccountRepository;
import com.bnpp.bank.Card.Controller.CardController;
import com.bnpp.bank.Card.Model.Card;
import com.bnpp.bank.Card.Persistence.CardEntity;
import com.bnpp.bank.Card.Persistence.CardRepository;
import com.bnpp.bank.Transacoes.Persistence.TransacaoEntity;
import com.bnpp.bank.Transacoes.Persistence.TransacaoRepository;
import com.bnpp.bank.Transacoes.Service.TransacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class CardService {

    CardRepository cardRepository;
    AccountRepository accountRepository;
    TransacaoService transacaoService;
    TransacaoRepository transacaoRepository;



    //@Autowired
    @Autowired
    public CardService(CardRepository cardRepository, AccountRepository accountRepository, TransacaoService transacaoService, TransacaoRepository transacaoRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.transacaoService = transacaoService;
        this.transacaoRepository = transacaoRepository;
    }

    // ----------------------------------------------------------------------------- //
    // ------------------------------- Select || Cartões ----------------------------//
    // ----------------------------------------------------------------------------- //
    public CardEntity cartaoAssociado(Long numeroCartao, Long pin){
        return cardRepository.findCardEntitiesByNumeroCartaoAndPin(numeroCartao, pin);
    };

    public List<CardEntity> listaCartoesAssociados(Long numeroConta) {
        return cardRepository.findCardEntityByNumeroConta(numeroConta);
    }

    public List<CardEntity> listaCartoesDebitoAssociadosContaCliente(Long numeroConta, Long titularCartao) {
        return cardRepository.findCardEntityByNumeroContaAndTitularCartaoAndTipoCartao(numeroConta, titularCartao, "Débito");
    }

    public List<CardEntity> listaCartoesCreditoAssociadosConta(Long numeroConta) {
        return cardRepository.findCardEntityByNumeroContaAndTipoCartao(numeroConta, "Crédito");
    }

    public List<CardEntity> listaCartoesDebitoAssociadosConta(Long numeroConta) {
        return cardRepository.findCardEntityByNumeroContaAndTipoCartao(numeroConta, "Débito");
    }

    public List<CardEntity> listaCartoesCreditoAssociadosCliente(Long titularCartao) {
        return cardRepository.findCardEntityByTitularCartaoAndTipoCartao(titularCartao, "Crédito");
    }

    public List<CardEntity> listaCartoesDebitoAssociadosCliente(Long titularCartao) {
        return cardRepository.findCardEntityByTitularCartaoAndTipoCartao(titularCartao, "Débito");
    }


    // ----------------------------------------------------------------------------- //
    //------------------------------ Criar Novo Cartão Débito ----------------------//
    // ----------------------------------------------------------------------------- //
    public void criarNovoCartao(long titularPrincipal, Long numeroConta, String tipoCartao, int pin, Double plafondAtual, Double plafondMensal) {
        CardEntity card = new CardEntity(numeroConta, titularPrincipal, tipoCartao, pin, plafondAtual, plafondMensal);
        cardRepository.save(card);
    }

    public void atmDeposito(AccountEntity conta, Double montante, Long numeroCartao) {
        CardEntity cartaoUsado = cardRepository.findCardEntityByNumeroCartao(numeroCartao);

        if("Débito".equals(cartaoUsado.getTipoCartao())) {
            conta.setSaldoConta(conta.getSaldoConta() + montante);
            accountRepository.save(conta);
            transacaoService.registarTransacao("Deposito", numeroCartao, conta.getNumeroConta(), montante, new Date(), null);
        }
        else if ("Crédito".equals(cartaoUsado.getTipoCartao())) {
            cartaoUsado.setPlafondAtual(cartaoUsado.getPlafondAtual() + montante);
            cardRepository.save(cartaoUsado);
            transacaoService.registarTransacao("Deposito", numeroCartao, conta.getNumeroConta(), montante, new Date(), null);
        }
    }

    public void atmLevantamento(AccountEntity conta, Long numeroCartao, Double montante) {

        CardEntity cartaoUsado = cardRepository.findCardEntityByNumeroCartao(numeroCartao);

        if("Débito".equals(cartaoUsado.getTipoCartao())) {
            conta.setSaldoConta(conta.getSaldoConta() - montante);
            accountRepository.save(conta);
            transacaoService.registarTransacao("Levantamento", numeroCartao, conta.getNumeroConta(), montante, new Date(), null);
        }
        else if ("Crédito".equals(cartaoUsado.getTipoCartao())) {
            cartaoUsado.setPlafondAtual(cartaoUsado.getPlafondAtual() - montante);
            cardRepository.save(cartaoUsado);
            transacaoService.registarTransacao("Levantamento", numeroCartao, conta.getNumeroConta(), montante, new Date(), null);
        }

    }






    //, METER AQUI CONTA A RECEER
    public void atmTransferencia(Long numeroCartao, Double montante, Long destinatario, AccountEntity conta) {

        AccountEntity contaDestinatario = accountRepository.findAccountEntityByNumeroConta(destinatario);
        CardEntity cartaoUsado = cardRepository.findCardEntityByNumeroCartao(numeroCartao);

        if("Débito".equals(cartaoUsado.getTipoCartao())) {
            conta.setSaldoConta(conta.getSaldoConta() - montante);
            contaDestinatario.setSaldoConta(contaDestinatario.getSaldoConta() + montante);
            accountRepository.save(conta);
            accountRepository.save(contaDestinatario);
            transacaoService.registarTransacao("Transferencia", numeroCartao, conta.getNumeroConta(), montante, new Date(),destinatario);
        }

        else if("Crédito".equals(cartaoUsado.getTipoCartao())) {
            cartaoUsado.setPlafondAtual(cartaoUsado.getPlafondAtual() - montante);
            contaDestinatario.setSaldoConta(contaDestinatario.getSaldoConta() + montante);
            cardRepository.save(cartaoUsado);
            accountRepository.save(contaDestinatario);
            transacaoService.registarTransacao("Transferencia", numeroCartao, conta.getNumeroConta(), montante, new Date(),destinatario);
        }

    }

    //List<CardEntity> cartoesDebito = cardService.listaCartoesAssociados(numeroConta);
    //session.setAttribute("cartoesDebito",cartoesDebito);

    // ----------------------------------------------------------------------------- //
    //------------------------------ Apagar Cartao -------------------.------------- //
    // ----------------------------------------------------------------------------- //


    public boolean validarCartaoSessao(HttpSession session) {
        if(session.getAttribute("cartaoATM") != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validarNifSessao(HttpSession session) {
        if(session.getAttribute("nif") != null) {
            return true;
        } else {
            return false;
        }
    }

    public void eliminarCartao(Long numeroCartao) {
        CardEntity cartao = cardRepository.findCardEntityByNumeroCartao(numeroCartao);
        cardRepository.delete(cartao);
    }

    public CardEntity encontrarCartao(long numeroCartao) {
        return cardRepository.findCardEntityByNumeroCartao(numeroCartao);
    }
}




