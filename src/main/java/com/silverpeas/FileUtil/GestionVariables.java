package com.silverpeas.FileUtil;

/**
 * Titre : gestion de variable
 * Description : gestion de variable et de resolution de string.
 * Copyright :    Copyright (c) 2001
 * Société :
 * @author Thomas pellegrin
 * @version 1.0
 */
import bsh.Interpreter;
import java.util.Hashtable;

public class GestionVariables {

  /**
   * Hashtable contenant toutes les variables et leurs valeurs
   */
  private Hashtable listeVariables;

  /**
   * @constructor construtor principale de la classe
   */
  public GestionVariables() {
    listeVariables = new Hashtable();
  }

  /**
   * ajout d'une variable dans la base
   */
  public void addVariable(String pName, String pValue) {
    listeVariables.put(pName, pValue);
  }

  /**
   * modification d'une variable déja existante
   */
  public void modifyVariable(String pName, String pValue) {
    listeVariables.remove(pName);
    listeVariables.put(pName, pValue);
  }

  /**
   * resolution de string les variables doivent être de la forme ${variable}
   * il n'y a pas de contrainte aux niveaux du nombre de variables utilisées
   * ex: path=c:\tmp
   *     rep=\lib\
   * ${path}{$rep}\toto ->c:\tmp\lib\toto
   */
  public String resolveString(String pStr) throws Exception {
    String newString = "";
    int index = pStr.indexOf("${");

    if (index != -1) {
      int index_fin;
      String tmp = pStr;
      while (index != -1) {
        newString = newString.concat(tmp.substring(0, index));
        index_fin = tmp.indexOf('}');

        newString = newString.concat(this.getValue(tmp.substring(index + 2, index_fin)));
        tmp = tmp.substring(index_fin + 1);
        index = tmp.indexOf("${");
      }
      newString = newString.concat(tmp);
      return newString;
    }
    else {
      return pStr;
    }
  }

  /**
   *  résolution des variables d'une string
   *  puis evaluation dynamique d'une string de la forme $eval{{.....}}
   */
  public String resolveAndEvalString(String pStr) throws Exception {

    int index = pStr.indexOf("$eval{{");

    if (index == -1) {
      // chaine classique statique
      return resolveString(pStr);

    }
    else {
      // chaine à évaluation dynamique
      if (index != 0) {
        throw new Exception("(unable to evaluate " + pStr + " because string is not beginning with \"$eval{{\" sequence.");
      }

      int index_fin = pStr.indexOf("}}");

      if (index_fin != pStr.length() - 2) {
        throw new Exception("(unable to evaluate " + pStr + " because string is not endding with \"}}\" sequence.");
      }

      String resolvedString = pStr.substring(0, index_fin);
      resolvedString = resolvedString.substring(7);
      resolvedString = resolveString(resolvedString);

      // évaluation dynamique
      Interpreter bsh = new bsh.Interpreter();

// System.out.println("valeur avant substitution="+resolvedString);

      bsh.set("value", new String());
      bsh.eval(resolvedString);
      String evaluatedString = (String) bsh.get("value");

// System.out.println("valeur apres substitution="+evaluatedString);

      return evaluatedString;
    } // if
  }

  /**
   * @return la valeur de la variable
   */
  public String getValue(String pVar) throws Exception {
    String tmp = (String) listeVariables.get(pVar);

    if (tmp == null) {
      throw new Exception("La variable :\"" + pVar + "\" n'existe pas dans la base");
    }
    return tmp;
  }

  public static void main(String[] args) {
    GestionVariables gv;
    System.out.println("test des variables");
    try {
      gv = new GestionVariables();
      gv.addVariable(new String("path"), "c:\\thomas");
      gv.addVariable(new String("rep"), "\\lib\\tutu\\");
      System.out.println("value:" + gv.getValue("rep"));
      System.out.println("resultat:" + gv.resolveString("proper${path}ties.tmp"));

    }
    catch (Exception e) {
      e.printStackTrace();

    }
  }
}