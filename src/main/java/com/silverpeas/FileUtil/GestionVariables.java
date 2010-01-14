/**
 * Copyright (C) 2000 - 2009 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.silverpeas.FileUtil;

import bsh.Interpreter;
import java.util.Hashtable;
import java.util.Map.Entry;

public class GestionVariables {

  /**
   * Hashtable contenant toutes les variables et leurs valeurs
   */
  private Hashtable<String, String> listeVariables;

  /**
   * @constructor construtor principale de la classe
   */
  public GestionVariables() {
    listeVariables = new Hashtable();
    for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
      listeVariables.put(entry.getKey().toString(), entry.getValue().toString());
    }
    for (Entry<String, String> entry : System.getenv().entrySet()) {
      addVariable(entry.getKey(), entry.getValue());
    }
  }

  /**
   * ajout d'une variable dans la base
   */
  public void addVariable(String pName, String pValue) {
    listeVariables.put(pName, pValue);
  }

  /**
   * modification d'une variable deja existante
   */
  public void modifyVariable(String pName, String pValue) {
    listeVariables.remove(pName);
    listeVariables.put(pName, pValue);
  }

  /**
   * resolution de string les variables doivent etre de la forme ${variable} il n'y a pas de
   * contrainte aux niveaux du nombre de variables utilis�es ex: path=c:\tmp rep=\lib\
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

        newString = newString.concat(this.getValue(tmp.substring(index + 2,
            index_fin)));
        tmp = tmp.substring(index_fin + 1);
        index = tmp.indexOf("${");
      }
      newString = newString.concat(tmp);
      return newString;
    } else {
      return pStr;
    }
  }

  /**
   * resolution des variables d'une string puis evaluation dynamique d'une string de la forme
   * $eval{{.....}}
   */
  public String resolveAndEvalString(String pStr) throws Exception {
    int index = pStr.indexOf("$eval{{");
    if (index == -1) {
      return resolveString(pStr);
    } else {
      if (index != 0) {
        throw new Exception("(unable to evaluate " + pStr
            + " because string is not beginning with \"$eval{{\" sequence.");
      }

      int index_fin = pStr.indexOf("}}");

      if (index_fin != pStr.length() - 2) {
        throw new Exception("(unable to evaluate " + pStr
            + " because string is not endding with \"}}\" sequence.");
      }

      String resolvedString = pStr.substring(0, index_fin);
      resolvedString = resolvedString.substring(7);
      resolvedString = resolveString(resolvedString);

      // evaluation dynamique
      Interpreter bsh = new bsh.Interpreter();
      bsh.set("value", new String());
      bsh.eval(resolvedString);
      String evaluatedString = (String) bsh.get("value");
      return evaluatedString;
    }
  }

  /**
   * @return la valeur de la variable
   */
  public String getValue(String pVar) throws Exception {
    String tmp = listeVariables.get(pVar);
    if (tmp == null) {
      throw new Exception("La variable :\"" + pVar
          + "\" n'existe pas dans la base");
    }
    return tmp;
  }
}
