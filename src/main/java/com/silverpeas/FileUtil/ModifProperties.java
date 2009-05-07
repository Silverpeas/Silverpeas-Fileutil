package com.silverpeas.FileUtil;

/**
 * Titre : Modification de fichier properties
 * Description : modifi des fichiers properties
 * Copyright :    Copyright (c) 2001
 * Société :
 * @author thomas pellegrin
 * @version 1.0
 */

import java.io.*;
import java.util.*;


public class ModifProperties extends ModifFile {

  /**
   * message de la nouvelle mise à jour du fichier
   */
  private String messageProperties=null;

  /**
   * @constructor prend en paramétre le fichier à modifier
   */
  public ModifProperties(String path) throws Exception{
	  this.setPath(path);
  }

/**
 * met à jour le message d'information du propertie
 */
  public void setMessageProperties(String str){
	  messageProperties=str;
  }

  /**
   * @return le message de mise à jour du properties
   */
  public String getMessageProperties(){
	  return messageProperties;
  }

  /**
   * lance la modification du fichier properties
   */
  public void executeModification() throws Exception{
	FileInputStream  inputFile;
	FileOutputStream  outputFile;
	Iterator i;
	int index;
	ElementModif em;

	File file= new File(path);

	if (file.exists()){
	   inputFile= new FileInputStream(file);
	   Properties pro = new Properties();
	   pro.load(inputFile);
	   inputFile.close();
	   i=listeModifications.iterator();

	   while (i.hasNext()){
		  em=((ElementModif)i.next());
		  if ( pro.containsKey(em.getSearch())){
			 if (!em.getModif().equals(pro.getProperty(em.getSearch()))) {
				if (!isModified) {
					isModified=true;
					BackupFile bf = new BackupFile(new File(path));
					bf.makeBackup();
				}
				pro.setProperty(em.getSearch(),em.getModif());
			 }
		  } else{
			 throw new Exception("ATTENTION key:\""+em.getSearch()+"\" non existante dans le fichier propertie: "+path);
		  }
	   }
	   outputFile = new FileOutputStream(path);
	   pro.store(outputFile,messageProperties);
	} else {
	  throw new Exception("ATTENTION le fichier:"+path+" n'existe pas");
	}
  }


  public static void main(String[] args){
	  ModifProperties mp;
	  String[] strs = {"bonjour=different"};
	   try{
		mp = new ModifProperties("c:\\lib\\test.properties");
		mp.createFileBak(null);
		mp.executeModification();
	  }catch(Exception e){
		System.out.println(e);
	  }
  }



}