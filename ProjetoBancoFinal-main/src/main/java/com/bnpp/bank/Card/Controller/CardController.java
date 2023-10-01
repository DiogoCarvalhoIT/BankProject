

package com.bnpp.bank.Card.Controller;

import com.bnpp.bank.Account.Persistence.AccountEntity;

import com.bnpp.bank.Account.Persistence.AccountRepository;
import com.bnpp.bank.Card.Model.Card;
import com.bnpp.bank.Card.Persistence.CardEntity;
import com.bnpp.bank.Card.Persistence.CardRepository;
import com.bnpp.bank.Card.Service.CardService;
import com.bnpp.bank.Client.Persistence.ClientEntity;
import com.bnpp.bank.Client.Service.ClientService;
import com.bnpp.bank.Transacoes.Persistence.TransacaoEntity;
import com.bnpp.bank.Transacoes.Persistence.TransacaoRepository;
import com.bnpp.bank.Transacoes.Service.TransacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.Banner;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
public class CardController {

    CardService cardService;
    ClientService clientService;
    TransacaoService transacaoService;
    TransacaoRepository transacaoRepository;
    AccountRepository accountRepository;
    CardRepository cardRepository;


    //@Autowired
    public CardController(CardService cardService, ClientService clientService, TransacaoService transacaoService, AccountRepository accountRepository, TransacaoRepository transacaoRepository, CardRepository cardRepository) {
        this.cardService = cardService;
        this.clientService = clientService;
        this.transacaoService = transacaoService;
        this.accountRepository = accountRepository;
        this.transacaoRepository = transacaoRepository;
        this.cardRepository = cardRepository;
    }

    @GetMapping("/criarCartaoDebito")
    public ModelAndView criarCartaoDebito(HttpSession session, Model model) {



        if(cardService.validarNifSessao(session)) {

            AccountEntity conta = (AccountEntity) session.getAttribute("contaCliente");
            List<CardEntity> cartoesAssociadosConta = cardRepository.findCardEntityByNumeroConta(conta.getNumeroConta());
            model.addAttribute("cartoesAssociadosConta", cartoesAssociadosConta);

        ModelAndView viewCriarCartaoDebito = new ModelAndView("menuCriarCartaoDebito");
        return viewCriarCartaoDebito; }
        else {
            return new ModelAndView("redirect:/login");
        }


    }

    @PostMapping("/submeterCartaoDebito")
    public ModelAndView submeterCartaoDebito (@RequestParam int pinCD, HttpSession session, Model model) {

        AccountEntity conta = (AccountEntity) session.getAttribute("contaCliente");
        ClientEntity cliente = (ClientEntity) session.getAttribute("clienteAtivo");
        String verificacaoPinCD = String.valueOf(pinCD);

        if(cardService.listaCartoesDebitoAssociadosConta(conta.getNumeroConta()).size() >= 4) {
            model.addAttribute("error", "A conta já se encontra com, pelo menos, quatro cartões de débito.");
            return criarCartaoDebito(session, model);
        }
        else if (cardService.listaCartoesDebitoAssociadosCliente(cliente.getNif()).size() >= 1) {
            model.addAttribute("error", "O cliente já se encontra com um cartão de débito associado.");
            return criarCartaoDebito(session, model);
        }
        else if (verificacaoPinCD.length() !=4) {
            model.addAttribute("error", "O PIN tem que conter quatro dígitos.");
            return criarCartaoDebito(session, model);
        }
        else {
            cardService.criarNovoCartao(cliente.getNif(), conta.getNumeroConta(), "Débito", pinCD, null, null);
            List<CardEntity> cartoesCliente = cardService.listaCartoesAssociados(conta.getNumeroConta());
            List<TransacaoEntity> listaTransacoes = transacaoService.listaTransacoes(conta.getNumeroConta());
            List<TransacaoEntity> listaTransacaoRecebidas = transacaoService.listaTransacoesRecebidas(conta.getNumeroConta());

            session.setAttribute("contaCliente", conta);
            session.setAttribute("cartoesCliente", cartoesCliente);
            session.setAttribute("listaTransacoes", listaTransacoes);
            session.setAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);

            model.addAttribute("contaCliente", conta);
            model.addAttribute("cartoesCliente", cartoesCliente);
            model.addAttribute("listaTransacoes", listaTransacoes);
            model.addAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);

            ModelAndView viewDashboardConta = new ModelAndView("menuDashboardConta");
            return viewDashboardConta;
        }


    }

    @PostMapping("/submeterCartaoCredito") // todo criar isto no html
    public ModelAndView submeterCartaoCredito(@RequestParam int pinCC, @RequestParam Double plafondMensal,HttpSession session, Model model) {
        AccountEntity conta = (AccountEntity) session.getAttribute("contaCliente");
        ClientEntity cliente = (ClientEntity) session.getAttribute("clienteAtivo");
        String verificacaoPinCC = String.valueOf(pinCC);


        if(cardService.listaCartoesCreditoAssociadosConta(conta.getNumeroConta()).size() >= 2) {
            model.addAttribute("error", "A conta já se encontra com, pelo menos, dois cartões de crédito.");
            return criarCartaoDebito(session, model);
        }
        else if (cardService.listaCartoesCreditoAssociadosCliente(cliente.getNif()).size() >=1) {
            model.addAttribute("error", "O cliente já se encontra com um cartão de crédito associado.");
            return criarCartaoDebito(session, model);
        }
        else if (verificacaoPinCC.length() !=4) {
            model.addAttribute("error", "O PIN tem que conter quatro dígitos.");
            return criarCartaoDebito(session, model);
        }
        else {

            Double plafondAtual = plafondMensal;

            cardService.criarNovoCartao(cliente.getNif(), conta.getNumeroConta(), "Crédito", pinCC, plafondAtual, plafondMensal);

            List<CardEntity> cartoesCliente = cardService.listaCartoesAssociados(conta.getNumeroConta());
            List<TransacaoEntity> listaTransacoes = transacaoService.listaTransacoes(conta.getNumeroConta());
            List<TransacaoEntity> listaTransacaoRecebidas = transacaoService.listaTransacoesRecebidas(conta.getNumeroConta());

            session.setAttribute("contaCliente", conta);
            session.setAttribute("cartoesCliente", cartoesCliente);
            session.setAttribute("listaTransacoes", listaTransacoes);
            session.setAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);



            model.addAttribute("contaCliente", conta);
            model.addAttribute("cartoesCliente", cartoesCliente);
            model.addAttribute("listaTransacoes", listaTransacoes);
            model.addAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);

            ModelAndView viewDashboardConta = new ModelAndView("menuDashboardConta");
            return viewDashboardConta;
        }
    }

    @PostMapping("/removerCartao")
    public ModelAndView removerCartao(@RequestParam Long cartaoRemover, Model model, HttpSession session) {

        AccountEntity conta = (AccountEntity) session.getAttribute("contaCliente");

        if(cardService.encontrarCartao(cartaoRemover) == null){
            model.addAttribute("error", "O cartão que pretende eliminar não existe.");
            return criarCartaoDebito(session, model);
        }
        else {
            cardService.eliminarCartao(cartaoRemover);

            List<CardEntity> cartoesCliente = cardService.listaCartoesAssociados(conta.getNumeroConta());
            List<TransacaoEntity> listaTransacoes = transacaoService.listaTransacoes(conta.getNumeroConta());
            List<TransacaoEntity> listaTransacaoRecebidas = transacaoService.listaTransacoesRecebidas(conta.getNumeroConta());

            session.setAttribute("contaCliente", conta);
            session.setAttribute("cartoesCliente", cartoesCliente);
            session.setAttribute("listaTransacoes", listaTransacoes);
            session.setAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);



            model.addAttribute("contaCliente", conta);
            model.addAttribute("cartoesCliente", cartoesCliente);
            model.addAttribute("listaTransacoes", listaTransacoes);
            model.addAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);

            ModelAndView viewDashboardConta = new ModelAndView("menuDashboardConta");
            return viewDashboardConta;

        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------ //
    // ------------------------------------------------------------------------------------------------------------------------------------------------------ //
    // ---------------------------------------------------------------- ATM --------------------------------------------------------------------------------- //
    // ------------------------------------------------------------------------------------------------------------------------------------------------------ //
    // ------------------------------------------------------------------------------------------------------------------------------------------------------ //

    @RequestMapping("/atm")
    public ModelAndView atm() {
        ModelAndView modelAndView = new ModelAndView("ATMpaginaInicial");
        return modelAndView;
    }

    @PostMapping("/atmVerificao")
    public ModelAndView atmVerificacao(@RequestParam("numeroCartao") Long numeroCartao,
                                       @RequestParam("pin") Long pin,
                                       HttpSession session,
                                       Model model) {
        CardEntity cartao = cardService.cartaoAssociado(numeroCartao, pin);

        if(cartao != null) {
            session.setAttribute("cartaoATM", cartao);
            //model.addAttribute("cartaoATM", cartao);
            ModelAndView modelAndView = new ModelAndView("ATMoperacoes");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("ATMpaginaInicial");
            model.addAttribute("error", "Numero de Cartão ou PIN inválido!");
            return modelAndView;
        }
    }

    @GetMapping("/atmIrLevantamentos")
    public ModelAndView viewAtmLevantamentos(HttpSession session) {
        if(cardService.validarCartaoSessao(session)) {
        ModelAndView viewAtmLevantamentos = new ModelAndView("ATMlevantamento");
        return viewAtmLevantamentos;}
        else {
            return new ModelAndView("redirect:/login");
        }
    }

    @PostMapping("realizarLevantamentoATM")
    public ModelAndView realizarLevantamentoATM(@RequestParam double montante,
                                                HttpSession session,
                                                Model model) {

        CardEntity cartaoATM = (CardEntity) session.getAttribute("cartaoATM");
        CardEntity cartaoAtualizado = cardRepository.findCardEntityByNumeroCartao(cartaoATM.getNumeroCartao());
        AccountEntity conta = accountRepository.findAccountEntityByNumeroConta(cartaoATM.getNumeroConta());
        LocalDateTime currentDate = LocalDateTime.now();
        List<TransacaoEntity> transacoes = transacaoRepository.findTransacaoEntitiesByNumeroContaAndMomentoTransacaoOn(conta.getNumeroConta(), currentDate);
        Double montanteDiarioLevantado = transacoes.stream()
                .filter(t -> t.getTipoOperacao().equals("Levantamento"))
                .mapToDouble(TransacaoEntity::getMontante)
                .sum();

        if("Débito".equals(cartaoAtualizado.getTipoCartao())) {

            if(conta.getSaldoConta() < montante){
                model.addAttribute("error", "Montante a levantar excedeu o saldo da conta.");
                return viewAtmLevantamentos(session);

            }
            else if (montante > 200) {
                model.addAttribute("error", "Montante a levantar excedeu o limite de 200€.");
                return viewAtmLevantamentos(session);
            }
            else if (montanteDiarioLevantado >= 400) {
                model.addAttribute("error", "Já ultrapassou o limite diário de 400€.");
                return viewAtmLevantamentos(session);
            }
            else {
                cardService.atmLevantamento(conta, cartaoAtualizado.getNumeroCartao(), montante);
                session.setAttribute("cartaoATM", cartaoAtualizado);
                return viewAtmOperacoes(session);
            }
        }

        else {
            if(cartaoAtualizado.getPlafondAtual() < montante) {
                model.addAttribute("error", "Montante a levantar excedeu o plafond atual do cartão de crédito.");
                return viewAtmLevantamentos(session);
            }
            else if (montante > 200) {
                model.addAttribute("error", "Montante a levantar excedeu o limite de 200€.");
                return viewAtmLevantamentos(session);
            }
            else if (montanteDiarioLevantado >= 400) {
                model.addAttribute("error", "Já ultrapassou o limite diário de 400€.");
                return viewAtmLevantamentos(session);
            }
            else {
                cardService.atmLevantamento(conta, cartaoAtualizado.getNumeroCartao(), montante);
                session.setAttribute("cartaoATM", cartaoAtualizado);
                return viewAtmOperacoes(session);
            }
        }
    }

    @GetMapping("/atmIrDepositos")
    public ModelAndView viewAtmDepositos(HttpSession session) {
        if(cardService.validarCartaoSessao(session)) {
        ModelAndView viewAtmDepositos = new ModelAndView("ATMdeposito");
        return viewAtmDepositos;}
        else {
            return new ModelAndView("redirect:/login");
        }
    }

    @PostMapping("realizarDepositoATM")
    public ModelAndView realizarDepositoATM(@RequestParam double montante,
                                            HttpSession session,
                                            Model model) {

        CardEntity cartaoATM = (CardEntity) session.getAttribute("cartaoATM");
        CardEntity cartaoAtualizado = cardRepository.findCardEntityByNumeroCartao(cartaoATM.getNumeroCartao());
        AccountEntity conta = accountRepository.findAccountEntityByNumeroConta(cartaoATM.getNumeroConta());

        if("Crédito".equals(cartaoAtualizado.getTipoCartao())) {

            if(cartaoAtualizado.getPlafondAtual() < montante) {
                model.addAttribute("error", "Montante a depositar excedeu o plafond atual do cartão de crédito.");
                return viewAtmDepositos(session);
            }
            else if (montante > 200) {
                model.addAttribute("error", "Montante a depositar excedeu o limite de 200€.");
                return viewAtmDepositos(session);
            }
            else if (cartaoAtualizado.getPlafondMensal() < (cartaoAtualizado.getPlafondAtual() + montante)) {
                model.addAttribute("error", "Não pode exceder o valor do plafond mensal.");
                System.out.println(cartaoAtualizado.getPlafondAtual() + montante);
                return viewAtmDepositos(session);
            }
            else {
                cardService.atmDeposito(conta, montante, cartaoAtualizado.getNumeroCartao());
                session.setAttribute("cartaoATM", cartaoAtualizado);
                return viewAtmOperacoes(session);
            }
        }
        else {
            if(montante > 200) {
                model.addAttribute("error", "Montante a depositar excedeu o limite de 200€.");
                return viewAtmDepositos(session);
            }
            else {
                cardService.atmDeposito(conta, montante, cartaoAtualizado.getNumeroCartao());
                session.setAttribute("cartaoATM", cartaoAtualizado);
                return viewAtmOperacoes(session);
            }
        }

    }

    @GetMapping("/atmIrTransferencias")
    public ModelAndView viewAtmTransferencias(HttpSession session) {
        if(cardService.validarCartaoSessao(session)) {
            CardEntity cartaoATM = (CardEntity) session.getAttribute("cartaoATM");
            //System.out.println(cartaoATM.getTipoCartao());
        ModelAndView viewAtmTransferencias = new ModelAndView("ATMtransferencia");
        return viewAtmTransferencias; }
        else {
            return new ModelAndView("redirect:/login");
        }
    }

    @PostMapping("realizarTransferenciaATM")
    public ModelAndView realizarTransferenciaATM(@RequestParam double montante,
                                                 @RequestParam long destinatario,
                                                 HttpSession session, Model model) {

        CardEntity cartaoATM = (CardEntity) session.getAttribute("cartaoATM");
        CardEntity cartaoAtualizado = cardRepository.findCardEntityByNumeroCartao(cartaoATM.getNumeroCartao());
        AccountEntity conta = accountRepository.findAccountEntityByNumeroConta(cartaoATM.getNumeroConta());

        if("Débito".equals(cartaoAtualizado.getTipoCartao())) {

            if(conta.getSaldoConta() < montante) {
                model.addAttribute("error", "Montante a transferir excedeu o saldo da conta.");
                return viewAtmTransferencias(session);
            }
            else if (montante > 200) {
                model.addAttribute("error", "Montante a transferir excedeu o limite de 200€.");
                return viewAtmTransferencias(session);
            }
            else if (transacaoService.verificarExistenciaConta(destinatario) == null) {
                model.addAttribute("error", "A qual para a qual pretende realizar a transferência não existe.");
                return viewAtmTransferencias(session);
            }
            else {
                cardService.atmTransferencia((long) cartaoAtualizado.getNumeroCartao(), montante,destinatario, conta);
                session.setAttribute("cartaoATM", cartaoAtualizado);
                return viewAtmOperacoes(session);
            }
        }

        else {
            if(cartaoAtualizado.getPlafondAtual() < montante) {
                model.addAttribute("error", "Montante a transferir excedeu o plafond atual do cartão de crédito.");
                return viewAtmDepositos(session);
            }
            else if (montante > 200) {
                model.addAttribute("error", "Montante a transferir excedeu o limite de 200€.");
                return viewAtmDepositos(session);
            }
            else if (transacaoService.verificarExistenciaConta(destinatario) == null) {
                model.addAttribute("error", "A qual para a qual pretende realizar a transferência não existe.");
                return viewAtmTransferencias(session);
            }
            else {
                cardService.atmTransferencia((long) cartaoAtualizado.getNumeroCartao(), montante,destinatario, conta);
                session.setAttribute("cartaoATM", cartaoAtualizado);
                return viewAtmOperacoes(session);
            }
        }




    }

    @GetMapping("/voltarATMoperacoes")
    public ModelAndView viewAtmOperacoes(HttpSession session) {
        if(cardService.validarCartaoSessao(session)) {
        ModelAndView viewAtmOperacoes = new ModelAndView("ATMoperacoes");
        return viewAtmOperacoes;}
        else {return new ModelAndView("redirect:/login");
        }
    }

    @GetMapping("/atmIrConsultas")
    public ModelAndView viewAtmConsultas(Model model, HttpSession session) {

        if(cardService.validarCartaoSessao(session)) {
        ModelAndView viewAtmConsultas = new ModelAndView("ATMmovimentos");
        CardEntity card = (CardEntity) session.getAttribute("cartaoATM");
        List<TransacaoEntity> listaTransacoesAtm = transacaoService.listaTransacoesAtm(card.getNumeroCartao());

        model.addAttribute("listaTransacoesAtm", listaTransacoesAtm);
        session.setAttribute("listaTransacoesAtm",listaTransacoesAtm);
        return viewAtmConsultas; }
        else {
            return new ModelAndView("redirect:/login");
        }
    }

    @PostMapping("/displayMovimentosCartao")
    public ModelAndView displayMovimentosCartao(Model model, HttpSession session) {

        CardEntity card = (CardEntity) session.getAttribute("cartaoATM");
        List<TransacaoEntity> listaTransacoesAtm = transacaoService.listaTransacoesAtm(card.getNumeroCartao());
        return  viewAtmOperacoes(session);
    }
}
