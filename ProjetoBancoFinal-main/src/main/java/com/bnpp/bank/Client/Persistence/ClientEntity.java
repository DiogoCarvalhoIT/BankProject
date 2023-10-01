package com.bnpp.bank.Client.Persistence;

import org.springframework.core.annotation.Order;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity(name = "Clients") // Modela para uma tabela
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity implements Serializable {

        @Id
        @Column(nullable = false, updatable = false, name = "nif")
        private Long nif;

        @Column(nullable = false, name = "nome_completo")
        @OrderColumn()
        private String nomeCompleto;

        @Column(nullable = false, name = "senha")
        private String senha;

        @Column(nullable = false, updatable = false, name = "data_nascimento")
        private String dataNascimento;

        @Column(nullable = false, name = "idade")
        private int idade;

        @Column(nullable = false, name = "telefone")
        private int telefone;

        @Column(nullable = false, name = "telemovel")
        private int telemovel;

        @Column(nullable = false, name = "email")
        private String email;

        @Column(nullable = false, name = "profissao")
        private String profissao;



}
