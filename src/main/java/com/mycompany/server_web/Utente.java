/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server_web;

/**
 *
 * @author PC
 */
import java.util.*;
import java.io.*;
import java.math.BigDecimal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Famiglia
 */
public class Utente{
    String nome;
    String cognome;

    public String getNomeUtente() {
        return nome;
    }

    public void setNomeUtente(String nome) {
        this.nome = nome;
    }

    public String getCognomeUtente() {
        return cognome;
    }

    public void setCognomeUtente(String cognome) {
        this.cognome = cognome;
    }
}
