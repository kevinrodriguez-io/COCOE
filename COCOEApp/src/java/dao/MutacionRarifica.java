package dao;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author COCOE
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class MutacionRarifica {
    private String alfa;
    private String beta;
    public MutacionRarifica(){}
    public MutacionRarifica(String alfa, String beta) {
        this.alfa = alfa;
        this.beta = beta;
    }
}
