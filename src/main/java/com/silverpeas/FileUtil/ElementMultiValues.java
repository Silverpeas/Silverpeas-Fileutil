package com.silverpeas.FileUtil;

/**
 * Titre : Element de modification
 * Description : Element de modification à plusieurs valeurs
 * Copyright :    Copyright (c) 2001
 * Société :
 * @author Thomas pellegrin
 * @version 1.0
 */


import java.util.*;

 public class ElementMultiValues extends ElementModif{
	 /**
	  * tableau de chaine des valeurs
	  */
	 private  ArrayList listeModifications;


	 /**
	  * @Constructor : prend en paramétre la chaine de recherche
	  */
	  public ElementMultiValues(String pSearch){
		super(pSearch,null);
		listeModifications= new ArrayList();
	  }

	  /**
	  * met à jour le tableau des valeurs
	  */
	  public void addValue(String pValues){
		 listeModifications.add(pValues);
	  }

	  /**
	  * @return un objet iterator des valeurs
	  */
	  public Iterator getIterator(){
		 return listeModifications.iterator();
	  }


	}