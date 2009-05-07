package com.silverpeas.FileUtil;

/**
 * Titre : modification simple
 * Description : Element pour realiser une modification de fichier
 * Copyright :    Copyright (c) 2001
 * Société :
 * @author Thomas pellegrin
 * @version 1.0
 */


public class ElementModif{

	  /**
	   * Chaine de recherche
	   */
	  private String search;

	  /**
	   * Chaine de la modification a effectuer
	   */
	  private String modif;

	  /**
	   * Constructeur : demande la chaine de recherche et la modification
	   */
	  public ElementModif(String pSearch,String pModif){
		search=pSearch;
		modif=pModif;
	  }

	  /**
	   * @return la modification à effectuer
	   */
	  public String getModif(){
		return modif;
	  }

	  /**
	   * @return la chaine de recherche
	   */
	  public String getSearch(){
		return search;
	  }
  }