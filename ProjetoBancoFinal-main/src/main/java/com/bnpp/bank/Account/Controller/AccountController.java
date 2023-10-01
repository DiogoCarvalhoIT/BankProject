

package com.bnpp.bank.Account.Controller;
 
 import com.bnpp.bank.Card.Persistence.CardEntity;
 import com.bnpp.bank.Card.Service.CardService;
 import com.bnpp.bank.Client.Model.Client;
 import com.bnpp.bank.Client.Persistence.ClientEntity;
 import com.bnpp.bank.Client.Service.ClientService;
 import com.bnpp.bank.Transacoes.Persistence.TransacaoEntity;
 import com.bnpp.bank.Transacoes.Service.TransacaoService;
 import jakarta.servlet.http.HttpServletRequest;
 import jakarta.servlet.http.HttpSession;
 import org.springframework.boot.Banner;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.web.servlet.ModelAndView;

 import com.bnpp.bank.Account.Persistence.AccountEntity;
 import com.bnpp.bank.Account.Service.AccountService;
 
 import java.util.List;
 
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.ui.Model;

@RestController
//@RequestMapping(path = "api/v1/clients")
 public class AccountController {
// responsavel por criar clientes, etc...
 
 AccountService accountService;
 CardService cardService;
 TransacaoService transacaoService;
 ClientService clientService;
 
 @Autowired // vai ser instanciado e injetado no construtor
 public AccountController(AccountService accountService, CardService cardService, TransacaoService transacaoService, ClientService clientService) {
 this.accountService = accountService;
 this.cardService = cardService;
 this.transacaoService = transacaoService;
 this.clientService = clientService;

 }


     // --------------------------------------------------------------------------------------------------------- //
     // ------------------------------------ CRIAÇÃO DE UMA CONTA   --------------------------------------------- //
     // --------------------------------------------------------------------------------------------------------- //

    /* display da pagina html */
    /* "locahost:8080/criarConta" */
    @GetMapping("/criarContaBancaria")
    public ModelAndView criarContaBancaria() {
        ModelAndView viewCriarConta = new ModelAndView("criarConta");
        return viewCriarConta;
    }

    /* método que cria o cliente */
    @PostMapping("/criarNovaConta")
    public ModelAndView criarNovaConta(
            @RequestParam double saldoConta,
            HttpSession session,
            Model model,
            HttpServletRequest request) {

            long nifTitularPrincipal = (long) session.getAttribute("nif");
            session = request.getSession();
            ClientEntity client = (ClientEntity) session.getAttribute("clienteAtivo");

            /*if(saldoConta >= 50 && client.getIdade() >= 18) {
                accountService.criarConta(nifTitularPrincipal, saldoConta);

                model.addAttribute("client", client);
                ModelAndView modelAndView = new ModelAndView("menuOpcoes");
                return modelAndView;
            } else {
                // todo meter erro caso o saldo seja menor
                return criarContaBancaria();

            }*/

            if(saldoConta < 50) {
                model.addAttribute("error", "O saldo inicial tem que ser superior a 50€.");
                return criarContaBancaria();
            }
            else if (client.getIdade() < 18) {
                model.addAttribute("error", "A idade mínima para criar uma conta bancária é 18.");
                return criarContaBancaria();
            }
            else {
                accountService.criarConta(nifTitularPrincipal, saldoConta);
                model.addAttribute("client", client);
                ModelAndView modelAndView = new ModelAndView("menuOpcoes");
                return modelAndView;
            }


    }

     // --------------------------------------------------------------------------------------------------------- //
     // ------------------------------------ Display das Contas do Cliente   ------------------------------------ //
     // --------------------------------------------------------------------------------------------------------- //

     @RequestMapping(value = "/contasBancariasCliente", method = RequestMethod.GET)
     public ModelAndView contasBancariasCliente(Model model, HttpServletRequest request, HttpSession session) {

        if(accountService.validarNifSessao(session)) {
         ModelAndView viewContasBancariasCliente = new ModelAndView("menuDisplayContaCliente");
         session = request.getSession();
         long nifAtual = (long) session.getAttribute("nif");
         List<AccountEntity> contasCliente = accountService.getAccounts(nifAtual);
         List<AccountEntity> contasSecundariasCliente = accountService.encontrarContasSecundarios(nifAtual);
         viewContasBancariasCliente.addObject("contasCliente", contasCliente);
         viewContasBancariasCliente.addObject("contasSecundariasCliente", contasSecundariasCliente);
         return viewContasBancariasCliente; }
        else {
            return new ModelAndView("redirect:/login");
        }
     }

     @PostMapping("/contaSelecionada")
     public ModelAndView contaBancariaSelecionada(@RequestParam String numeroConta, Model model, HttpSession session) {


        Long numeroContaAtual = Long.valueOf(numeroConta);

        AccountEntity contaCliente = accountService.getAccountsNifNumeroConta((Long) session.getAttribute("nif"), numeroContaAtual);

        if(contaCliente == null) {
            AccountEntity contaSecundariaCliente = accountService.encontrarContasTitularSecundario((Long) session.getAttribute("nif"));
            List<CardEntity> cartoesCliente = cardService.listaCartoesAssociados(contaSecundariaCliente.getNumeroConta());
            List<TransacaoEntity> listaTransacoes = transacaoService.listaTransacoes(contaSecundariaCliente.getNumeroConta());
            List<TransacaoEntity> listaTransacaoRecebidas = transacaoService.listaTransacoesRecebidas(numeroContaAtual);

            model.addAttribute("contaCliente", contaSecundariaCliente);
            model.addAttribute("cartoesCliente", cartoesCliente);
            model.addAttribute("listaTransacoes", listaTransacoes);
            model.addAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);
            session.setAttribute("contaCliente", contaSecundariaCliente);
            session.setAttribute("cartoesCliente", cartoesCliente);
            session.setAttribute("listaTransacoes", listaTransacoes);
            session.setAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);

            ModelAndView modelAndView = new ModelAndView("menuDashboardConta");
            return  modelAndView;
        } else{
            List<CardEntity> cartoesCliente = cardService.listaCartoesAssociados(numeroContaAtual);
            List<TransacaoEntity> listaTransacoes = transacaoService.listaTransacoes(numeroContaAtual);
            List<TransacaoEntity> listaTransacaoRecebidas = transacaoService.listaTransacoesRecebidas(numeroContaAtual);

            model.addAttribute("contaCliente", contaCliente);
            model.addAttribute("cartoesCliente", cartoesCliente);
            model.addAttribute("listaTransacoes", listaTransacoes);
            model.addAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);
            session.setAttribute("contaCliente", contaCliente);
            session.setAttribute("cartoesCliente", cartoesCliente);
            session.setAttribute("listaTransacoes", listaTransacoes);
            session.setAttribute("listaTransacoesRecebidas",listaTransacaoRecebidas);

            ModelAndView modelAndView = new ModelAndView("menuDashboardConta");
            return  modelAndView;
        }
     }

     @GetMapping("/viewMenuTitularesSecundarios")
     public ModelAndView viewMenuTitularesSecundarios(HttpSession session, Model model) {
        if(accountService.validarNifSessao(session)) {
        AccountEntity conta = (AccountEntity) session.getAttribute("contaCliente");
        model.addAttribute("contaCliente", conta);
        session.setAttribute("contaCliente", conta);
         List<Long> listaTitularesSecundarios = conta.getTitularesSecundarios();
         model.addAttribute("listaTitularesSecundarios", listaTitularesSecundarios);
        ModelAndView modelAndView = new ModelAndView("menuTitularesSecundarios");
         return modelAndView; }
        else {
            return new ModelAndView("redirect:/login");
        }
     }

     @PostMapping("/adicionarTitularSecundario") //todo ADICIONAR TITULAR SECUNDARIO FALTA HTML DISTO
     public ModelAndView adicionarTitularSecundario(@RequestParam Long nifTitularSecundario, HttpSession session, Model model) {

        AccountEntity conta = (AccountEntity) session.getAttribute("contaCliente");

         if(conta.getTitularesSecundarios().size() >=4 ) {
             model.addAttribute("error", "Esta conta já possui o máximo, quatro, titulares secundários.");
             return viewMenuTitularesSecundarios(session, model);
         }
         else if (conta.getTitularesSecundarios().contains(nifTitularSecundario)) {
             model.addAttribute("error", "Esta conta já tem este titular como titular secundário.");
             return viewMenuTitularesSecundarios(session, model);
         }
         else if (clientService.getClient(nifTitularSecundario) == null) {
             model.addAttribute("error", "O titular que está a tentar adicionar não se encontra com conta criada.");
             return viewMenuTitularesSecundarios(session, model);
         }
         else if (conta.getTitularPrincipal() == nifTitularSecundario) {
             model.addAttribute("error", "Este titular já se encontra como titular desta conta.");
             return viewMenuTitularesSecundarios(session, model);
         }
         else {
             accountService.adicionarTitularSecundario(conta , nifTitularSecundario);
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

    @PostMapping("/removerTitularSecundario")
    public ModelAndView removerTitularSecundario(@RequestParam Long nifTitularSecundarioRemover, HttpSession session, Model model) {

        AccountEntity conta = (AccountEntity) session.getAttribute("contaCliente");
        accountService.removerTitularSecundario(conta, nifTitularSecundarioRemover);

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

    /*
    @PostMapping("/removerCartao")
    public ModelAndView removerCartao(@RequestParam Long cartaoRemover, HttpSession session, Model model) {

        AccountEntity conta = (AccountEntity) session.getAttribute("contaCliente");
        accountService.eliminarCartao(cartaoRemover);
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
    }*/

    @RequestMapping("/voltarDashboardConta")
    public ModelAndView voltarDashboardConta(Model model, HttpSession session) {

        AccountEntity conta = (AccountEntity) session.getAttribute("contaCliente");

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
