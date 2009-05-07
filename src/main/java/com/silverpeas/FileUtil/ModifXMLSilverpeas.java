package com.silverpeas.FileUtil;

/**
 * Titre :
 * Description :
 * Copyright :    Copyright (c) 2001
 * Société :
 * @author thomas pellegrin
 * @version 1.0
 */
import java.io.*;
import java.io.Reader.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;

public class ModifXMLSilverpeas extends ModifFile {

  public ModifXMLSilverpeas(String path) throws Exception {
    super(path);
  }

  public void setModif(String str[]) throws Exception {
    for (int i = 0; i < str.length; i++) {
      this.addModification(str[i]);
    }
  }

  /**
   * ajoute une modification au fichier paramétre: un ElementMultiValues
   */
  public void addModification(ElementMultiValues em) throws Exception {
    listeModifications.add(em);
  }

  private void traiteChildren(List children) throws Exception {

    for (int i = 0; i < children.size(); i++) {
      Element currentNode = (Element) children.get(i);
      if (currentNode.getName().compareTo("param-name") == 0) {
        String value = currentNode.getTextTrim();
        ElementModif em;
        Iterator listeModif = listeModifications.iterator();
        while (listeModif.hasNext()) {
          em = ((ElementModif) listeModif.next());
          if (em.getSearch().compareTo(value) == 0) {

            if (em instanceof ElementMultiValues) {
              //pour l'instant on sauvegarde le fichier si il y a des ElementMultiValues
              if (!isModified) {
                isModified = true;
                BackupFile bf = new BackupFile(new File(path));
                bf.makeBackup();
              }
              Iterator iterMultiVal = ((ElementMultiValues) em).getIterator();
              List listeParamValue = currentNode.getParentElement().getChildren("param-value");
              Iterator iListeParamValue = listeParamValue.iterator();

              while (iterMultiVal.hasNext()) {
                if (iListeParamValue.hasNext()) {
                  Object o = iListeParamValue.next();
                  ((Element) o).setText((String) iterMultiVal.next());
                }
                else {
                  Element newElement = new Element("param-value");
                  newElement.setText((String) iterMultiVal.next());
                  currentNode.getParentElement().addContent(newElement);
                }
              }

            }
            else {
              //((Element)currentNode).getParent().getChild("param-value").setText(em.getModif());
              String val = ((Element) currentNode).getParentElement().getChild("param-value").getTextTrim();
              if (!val.equals(em.getModif())) {
                if (!isModified) {
                  isModified = true;
                  BackupFile bf = new BackupFile(new File(path));
                  bf.makeBackup();
                }
                ((Element) currentNode).getParentElement().getChild("param-value").setText(em.getModif());
              }
            }
          }
        }
      }
      // appel récursif , car le noeud courant peut avoir des enfants
      List chil = currentNode.getChildren();
      if (chil != null) {
        traiteChildren(chil);
      }

    }
  }

  public void executeModification() throws Exception {

    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(new File(path));
    Element root = doc.getRootElement();
    List listeParam = root.getChildren("param");
    traiteChildren(root.getChildren());
    FileOutputStream out = new FileOutputStream(path);

    Format format = Format.getPrettyFormat();
    format.setIndent("\t");
    format.setTextMode(Format.TextMode.TRIM);
    /*XMLOutputter serializer = new XMLOutputter("\t",true);
    serializer.setTrimText(true);*/
    XMLOutputter serializer = new XMLOutputter(format);
    serializer.output(doc, out);
    out.flush();
    out.close();
  }

  public static void main(String[] args) {
    ModifXMLSilverpeas mp;
    ElementMultiValues emv;

    System.out.println("test des modification de fichier XML");
    try {
      mp = new ModifXMLSilverpeas("c:\\lib\\test.xml");
      mp.createFileBak(null);
      mp.addModification("AddressBooksTitle", "ouais et toi");
      emv = new ElementMultiValues("AddressBooksTitle");
      String[] test = {"toto", "titi"};
      emv.addValue("toto");
      emv.addValue("titi");
      mp.addModification(emv);
      mp.executeModification();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}