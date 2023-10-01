package com.bnpp.bank.Client.Service;



import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.bnpp.bank.Client.Model.Client;
//import org.springframework.web.bind.annotation.GetMapping;
import com.bnpp.bank.Client.Persistence.ClientEntity;
import com.bnpp.bank.Client.Persistence.ClientRepository;

import java.util.List;

//declarar repositorio ClientRepository clientRepository

@Service // assim ja sabe onde encontrar o ClientService
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /*
    public void saveClientChange(ClientEntity client) {
        clientRepository.save(client);
    }
    */

    public ClientEntity getClient(long nif) {
        return clientRepository.findEntityByNif(nif);
    } //se der null

    public void criarNovoCliente(Long nif, String nomeCompleto, String senha,
            String dataNascimento, int idade, int telefone, int telemovel, String email, String profissao) {
        ClientEntity client = new ClientEntity(nif, nomeCompleto, senha, dataNascimento, idade, telefone, telemovel, email, profissao);
        clientRepository.save(client);

    }

    public ClientEntity isClientValid(long nif, String senha) {
        return clientRepository.findEntityByNifAndSenha(nif, senha);
    }

    public void atualizarNomeCliente(ClientEntity cliente, String nomeNovo) {
        cliente.setNomeCompleto(nomeNovo);
        clientRepository.save(cliente);
    }
    
    public void atualizarSenhaCliente(ClientEntity cliente, String senhaNova) {
        cliente.setSenha(senhaNova);
        clientRepository.save(cliente);
    }

    public void atualizarEmailCliente(ClientEntity cliente, String email) {
        cliente.setEmail(email);
        clientRepository.save(cliente);
    }


    public void atualizarTelefone(ClientEntity cliente, int telefoneNovo) {
        cliente.setTelefone(telefoneNovo);
        clientRepository.save(cliente);
    }

    public void atualizarTelemovel(ClientEntity cliente, int telemovelNovo) {
        cliente.setTelemovel(telemovelNovo);
        clientRepository.save(cliente);
    }

    public void atualizarProfissao(ClientEntity cliente, String profissao) {
        cliente.setProfissao(profissao);
        clientRepository.save(cliente);
    }


    public boolean validarNifSessao(HttpSession session) {
        if(session.getAttribute("nif") != null) {
            return true;
        } else {
            return false;
        }
    }

    public void eliminarCliente(Long nif) {
        ClientEntity cliente = clientRepository.findEntityByNif(nif);
        clientRepository.delete(cliente);
    }



















}

