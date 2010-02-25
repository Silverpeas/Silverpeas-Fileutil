/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.silverpeas.file;

import java.util.regex.Pattern;

/**
 * @author ehugonnet
 */
public class RegexpElementMotif extends ElementModif {
  /**
   * tableau de chaine des valeurs
   */
  private Pattern pattern;
  private String remplacement;

  /**
   * @Constructor : prend en parametre la chaine de recherche
   */
  public RegexpElementMotif(String pSearch) {
    super(pSearch, null);
    pattern = Pattern.compile(pSearch);
  }

  public Pattern getPattern() {
    return this.pattern;
  }

  /**
   * met a jour le tableau des valeurs
   */
  public void setRemplacement(String pValues) {
    remplacement = pValues;
  }

  /**
   * met a jour le tableau des valeurs
   */
  public String getRemplacement() {
    return remplacement;
  }

}