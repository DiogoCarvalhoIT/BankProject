package com.bnpp.bank.Client.Controller;

import com.bnpp.bank.Account.Persistence.AccountEntity;
import com.bnpp.bank.Card.Persistence.CardEntity;
import com.bnpp.bank.Transacoes.Persistence.TransacaoEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.bnpp.bank.Client.Persistence.ClientEntity;
import com.bnpp.bank.Client.Service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@RestController
// @RequestMapping(path = "api/v1/clients")
public class ClientController {
    // responsavel por criar clientes, etc...

    ClientService clientService;

    @Autowired // vai ser instanciado e injetado no construtor
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }






    // ---------------------------------------------------------------------------------------------------------------- //
    // -------------------------------------- LOGIN ------------------------------------------------------------------- //
    // ---------------------------------------------------------------------------------------------------------------- //
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView viewPaginaLogin() {
        ModelAndView modelAndView = new ModelAndView("menuLogin");
        return modelAndView;
    }

    @RequestMapping("/")
    public ModelAndView paginaInicial() {
        ModelAndView modelAndView = new ModelAndView("menuInicial");
        return modelAndView;
    }


    @PostMapping("/loginVerificacao")
    public ModelAndView loginVerificacao(@RequestParam("nif") long nif, @RequestParam("senha") String senha,
                                         Model model, HttpSession session) {

        ClientEntity client = clientService.isClientValid(nif, senha);

        if (client != null) {
            session.setAttribute("nif", nif);
            session.setAttribute("clienteAtivo", client);
            model.addAttribute("client", client);
            ModelAndView clientDashboardView = new ModelAndView("menuOpcoes");
            return clientDashboardView;
        } else {
            ModelAndView clientDashboardView = new ModelAndView("menuLogin");
            model.addAttribute("error", "NIF ou Senha errada!");
            return clientDashboardView;
        }
    }

    @GetMapping("/voltarDashboardCliente")
    public ModelAndView voltarDashboardCliente(Model model, HttpSession session) {

        ClientEntity client = clientService.getClient((Long) session.getAttribute("nif"));
        model.addAttribute("client", client);
        ModelAndView modelAndView = new ModelAndView("menuOpcoes");
        return modelAndView;

    }

    // ---------------------------------------------------------------------------------------------------------------- //
    // -------------------------------- Criar Conta de Cliente -------------------------------------------------------- //
    // ---------------------------------------------------------------------------------------------------------------- //


    @GetMapping("/createClient")
    public ModelAndView creationForm() {
        ModelAndView modelAndView = new ModelAndView("menuCriarConta");
        return modelAndView;
    }

    @PostMapping("/criarNovoCliente")
    public ModelAndView criarNovoCliente(

            //@RequestParam int idade,
            @RequestParam String dataNascimento,
            @RequestParam int telefone,
            @RequestParam String email,
            @RequestParam String nomeCompleto,
            @RequestParam long nif,
            @RequestParam String profissao,
            @RequestParam String password,
            @RequestParam int telemovel,
            Model model) {

        LocalDate dataNascimentoNova = LocalDate.parse(dataNascimento);
        Period cálculoIdade = Period.between(dataNascimentoNova, LocalDate.now());
        int idade = cálculoIdade.getYears();
        String verificacaoNif = String.valueOf(nif);

        ClientEntity client = clientService.getClient(nif);

        if(client != null) {
         model.addAttribute("error", "O NIF que tentou inserir já se encontra com uma conta criada.");
            return creationForm();
        }
        else if(verificacaoNif.length() !=9) {
            model.addAttribute("error", "O NIF necessita de ter nove digitos para ser válido.");
            return creationForm();
        }
        else {
            clientService.criarNovoCliente(nif, nomeCompleto, password, dataNascimento, idade, telefone, telemovel, email, profissao);
            return viewPaginaLogin();

        }



    }


    // ------------------------------------------------------------------------------------------------------------------------ //
    // ------------------------------------- Alterar Informações Cliente ------------------------------------------------------ //
    // ------------------------------------------------------------------------------------------------------------------------ //


    @GetMapping("/alterarInformacoes")
    public ModelAndView alterarInformacoes(HttpSession session) {
        if(clientService.validarNifSessao(session)) {
        ModelAndView modelAndView = new ModelAndView("menuAlterarInformacoes");
        ClientEntity client = clientService.getClient((Long) session.getAttribute("nif"));
        modelAndView.addObject("client", client);
        return modelAndView; }
        else {
            return new ModelAndView("redirect:/login");
        }
    }


    @PostMapping("/updateUser")
    public ModelAndView updateUser(@RequestParam(value = "email", required = false) String email,
                             @RequestParam(name = "nome-completo", required = false) String nomeCompleto,
                             @RequestParam(name = "senha", required = false) String senha,
                             //@RequestParam(name = "data-nascimento", required = false) String dataNascimento,
                             //@RequestParam(name = "idade", required = false) Integer idade,
                             @RequestParam(name = "telefone", required = false) Integer telefone,
                             @RequestParam(name = "telemovel", required = false) Integer telemovel,
                             @RequestParam(name = "profissao", required = false) String profissao,
                             Model model, HttpSession session) {


        ClientEntity client = clientService.getClient((Long) session.getAttribute("nif"));
        model.addAttribute("client", client);



        if(email != null && !email.isEmpty()) {
            clientService.atualizarEmailCliente(client, email);
        }

        if (nomeCompleto != null && !nomeCompleto.isEmpty()) {
            clientService.atualizarNomeCliente(client, nomeCompleto);
        }

        if (senha != null && !senha.isEmpty()) {
            clientService.atualizarSenhaCliente(client, senha);
        }

        /*if (dataNascimento != null && !dataNascimento.isEmpty()) {
            clientService.atualizarDataNascimentoCliente(client, dataNascimento);
        }
        if (idade != null) {
            clientService.atualizarIdade(client, idade);
        }*/

        if (telefone != null) {
            clientService.atualizarTelefone(client, telefone);

        }

        if (telemovel != null) {
            clientService.atualizarTelemovel(client, telemovel);

        }

        if (profissao != null && !profissao.isEmpty()) {
            clientService.atualizarProfissao(client, profissao);

        }


        model.addAttribute("successMessage", "User information updated successfully.");
        ModelAndView modelAndView = new ModelAndView("menuOpcoes");
        return modelAndView;
    }


    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return viewPaginaLogin();
    }












    // --------------------------------------------------------------------------------------------------------- //

/*
    @RequestMapping("/opcoes")
    public ModelAndView opcoes() {
        ModelAndView modelAndView = new ModelAndView("menuOpcoes");
        return modelAndView;
    }


    @RequestMapping("/clientDashboard")
    public ModelAndView loginFom() {
        ModelAndView modelAndView = new ModelAndView("menuInformacoesAtuaisCliente");
        return modelAndView;
    }

 */
}




