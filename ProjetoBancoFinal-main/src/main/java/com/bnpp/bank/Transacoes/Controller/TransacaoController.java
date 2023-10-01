package com.bnpp.bank.Transacoes.Controller;
import com.bnpp.bank.Account.Persistence.AccountEntity;
import com.bnpp.bank.Account.Service.AccountService;
import com.bnpp.bank.Card.Persistence.CardEntity;
import com.bnpp.bank.Card.Service.CardService;
import com.bnpp.bank.Transacoes.Persistence.TransacaoEntity;
import com.bnpp.bank.Transacoes.Persistence.TransacaoRepository;
import com.bnpp.bank.Transacoes.Service.TransacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
public class TransacaoController {

    TransacaoService transacaoService;
    AccountService accountService;
    CardService cardService;

    TransacaoRepository transacaoRepository;

    // ------------------------------------------------------------------ //

    @Autowired
    public TransacaoController(TransacaoService transacaoService, AccountService accountService, TransacaoRepository transacaoRepository, CardService cardService) {
        this.transacaoService = transacaoService;
        this.accountService = accountService;
        this.transacaoRepository = transacaoRepository;
        this.cardService = cardService;
    }

    // ------------------------------------------------------------------ //



    @GetMapping("/menuTransferencia")
    public ModelAndView menuTransferencia() {
        ModelAndView modelAndView = new ModelAndView("menuTransferencia");
        return modelAndView;
    }

    @PostMapping("/realizarTransferencia")
    public ModelAndView realizarTransferencia(
            @RequestParam double montante,
            @RequestParam long numeroConta,
            HttpSession session,
            Model model) {

        AccountEntity contaCliente = (AccountEntity) session.getAttribute("contaCliente");
        /*if(montante <= 200 && contaCliente.getSaldoConta() >= montante && transacaoService.verificarExistenciaConta(numeroConta) != null) {
        transacaoService.realizarTransferencia(contaCliente, numeroConta, montante);
        //transacaoService.registarTransacao("Transferencia", null, contaCliente.getNumeroConta(), montante, new Date(),numeroConta);
        // -------- FALTA VER PARA ONDE MANDAR DEPOIS DE CONCLUIR A TRANSFERENCIA --------- //
        ModelAndView viewDashboardConta = new ModelAndView("menuDashboardConta");
        return viewDashboardConta; }
        else {
            //todo falta meter erro aqui
            return menuTransferencia();
        }*/

        if (contaCliente.getSaldoConta() < montante) {
            model.addAttribute("error", "Montante a transferir excedeu o saldo da conta.");
            return menuTransferencia();
        }
        else if(montante > 200) {
            model.addAttribute("error", "Montante a transferir excedeu o limite de 200€.");
            return menuTransferencia();
        }
        else if (transacaoService.verificarExistenciaConta(numeroConta) == null) {
            model.addAttribute("error", "A qual para a qual pretende realizar a transferência não existe.");
            return menuTransferencia();
        }
        else {
            transacaoService.realizarTransferencia(contaCliente, numeroConta, montante);
            List<CardEntity> cartoesCliente = cardService.listaCartoesAssociados(contaCliente.getNumeroConta());
            List<TransacaoEntity> listaTransacoes = transacaoService.listaTransacoes(contaCliente.getNumeroConta());
            List<TransacaoEntity> listaTransacaoRecebidas = transacaoService.listaTransacoesRecebidas(contaCliente.getNumeroConta());

            session.setAttribute("contaCliente", contaCliente);
            session.setAttribute("cartoesCliente", cartoesCliente);
            session.setAttribute("listaTransacoes", listaTransacoes);
            session.setAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);



            model.addAttribute("contaCliente", contaCliente);
            model.addAttribute("cartoesCliente", cartoesCliente);
            model.addAttribute("listaTransacoes", listaTransacoes);
            model.addAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);

            ModelAndView viewDashboardConta = new ModelAndView("menuDashboardConta");
            return viewDashboardConta;
        }


    }
}




    // ------------------------------------------------------------------------------------------------------------------------------------------------------ //
    // --------------------------------------------------------APAGAR O QUE ESTÁ EM BAIXO DISTO???----------------------------------------------------------- //
    // ------------------------------------------------------------------------------------------------------------------------------------------------------ //

/*


    @GetMapping("/menuDeposito")
    public ModelAndView menuDeposito() {
        ModelAndView modelAndView = new ModelAndView("menuDeposito");
        return modelAndView;
    }


    @PostMapping("/realizarDeposito")
    public ModelAndView realizarDeposito(
            @RequestParam double montante,
            HttpSession session,
            Model model
    ) {
        AccountEntity contaCliente = (AccountEntity) session.getAttribute("contaCliente");

        if(montante >200) {
            model.addAttribute("error", "Montante excedeu o limite de 200€ permitido para depósitos.");
            return menuDeposito();
        } else {
            transacaoService.realizarDeposito(contaCliente, montante);
            ModelAndView viewDashboardConta = new ModelAndView("menuDashboardConta");
            return viewDashboardConta;
        }


    }

    @RequestMapping(value = "/menuLevantamento", method = RequestMethod.GET)
    public ModelAndView menuLevantamento() {
        ModelAndView modelAndView = new ModelAndView("menuLevantamento");
        return modelAndView;
    }

    @PostMapping("/realizarLevantamento")
    public ModelAndView realizarLevantamento(
            @RequestParam double montante,
            HttpSession session,
            Model model
    ) {

        AccountEntity contaCliente = (AccountEntity) session.getAttribute("contaCliente");
        LocalDateTime currentDate = LocalDateTime.now();

        List<TransacaoEntity> transacoes = transacaoRepository.findTransacaoEntitiesByNumeroContaAndMomentoTransacaoOn(contaCliente.getNumeroConta(), currentDate);

        Double montanteDiarioLevantado = transacoes.stream()
                .filter(t -> t.getTipoOperacao().equals("Levantamento"))
                .mapToDouble(TransacaoEntity::getMontante)
                .sum();

        if(montante >= 200) {
            model.addAttribute("error", "Montante excedeu o limite de 200€ permitido para levantamentos.");
            return menuLevantamento();
        }
        else if (contaCliente.getSaldoConta()<= montante) {
            model.addAttribute("error", "Montante a levantar é superior ao seu saldo de conta.");
            return menuLevantamento();
        }
        else if(montanteDiarioLevantado >= 400) {
            model.addAttribute("error", "Montante excedeu o limite diário de 400€ permitido para levantamentos.");
            return menuLevantamento();
        }
        else {
            transacaoService.realizarLevantamento(contaCliente, montante);
            List<CardEntity> cartoesCliente = cardService.listaCartoesAssociados(contaCliente.getNumeroConta());
            List<TransacaoEntity> listaTransacoes = transacaoService.listaTransacoes(contaCliente.getNumeroConta());
            List<TransacaoEntity> listaTransacaoRecebidas = transacaoService.listaTransacoesRecebidas(contaCliente.getNumeroConta());

            session.setAttribute("contaCliente", contaCliente);
            session.setAttribute("cartoesCliente", cartoesCliente);
            session.setAttribute("listaTransacoes", listaTransacoes);
            session.setAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);



            model.addAttribute("contaCliente", contaCliente);
            model.addAttribute("cartoesCliente", cartoesCliente);
            model.addAttribute("listaTransacoes", listaTransacoes);
            model.addAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);

            ModelAndView viewDashboardConta = new ModelAndView("menuDashboardConta");
            return viewDashboardConta;

        }
    }*/






