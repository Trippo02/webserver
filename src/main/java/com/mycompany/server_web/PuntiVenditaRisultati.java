/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server_web;
/**
 *
 * @author Leonardo
 */
import java.util.*;
import java.io.*;
import java.math.BigDecimal;

public class PuntiVenditaRisultati{
    int size;
    ArrayList<PuntiVendita> risultati;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<PuntiVendita> getRisultati() {
        return risultati;
    }

    public void setRisultati(ArrayList<PuntiVendita> risultati) {
        this.risultati = risultati;
    }
}