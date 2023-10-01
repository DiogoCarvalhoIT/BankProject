package com.bnpp.bank.Client.Persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

  @Override
  ClientEntity save(ClientEntity client);

  //ClientEntity save(ClientEntity client, long nif);

  ClientEntity findEntityByNifAndSenha(long nif, String senha);
  ClientEntity findEntityByNif(long nif);



}
