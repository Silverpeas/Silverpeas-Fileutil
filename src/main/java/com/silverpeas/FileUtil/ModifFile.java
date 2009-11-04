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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public abstract class ModifFile {

  /**
   * Chaine du fichier a modifier
   */
  protected String path = null;
  /**
   * tableau des modification a effectuer les objets contenue dans ce tableau
   * sont de type ElementModif ou ElementMultiValues
   */
  protected ArrayList listeModifications;
  /**
   * isModified indique si le fichier a ete modifie
   */
  protected boolean isModified = false;

  /**
   * @constructor constructeur de la class
   */
  public ModifFile() {
    listeModifications = new ArrayList();
  }

  /**
   * @constructor prend en param�tre le chemin du fichier a modifier
   */
  public ModifFile(String path) throws Exception {
    listeModifications = new ArrayList();
    setPath(path);
  }

  /**
   * met a jour le chemin du fichier a modifier
   */
  protected void setPath(String src) throws Exception {
    File file = new File(src);
    if (file.exists() != true) {
      throw new Exception("Le fichier \"" + src + "\" n'existe pas");
    } else {
      path = src;
    }
  }

  /**
   * copy un fichier passe en parametre dans le fichier out
   */
  public static void copyFile(File in, File out) throws Exception {
    byte[] buf = new byte[1024];
    FileInputStream sIn = new FileInputStream(in);
    FileOutputStream sOut = new FileOutputStream(out);
    int i = 0;
    while ((i = sIn.read(buf)) != -1) {
      sOut.write(buf, 0, i);
    }
    sIn.close();
    sOut.close();
  }

  /**
   * ajoute une modification au fichier les parametres: chaine de recherche,
   * chaine de remplacement
   */
  public void addModification(String pSearch, String pModif) {
    listeModifications.add(new ElementModif(pSearch, pModif));
  }

  /**
   * ajoute une modification au fichier parametre: un ElementModif
   */
  public void addModification(ElementModif em) {
    listeModifications.add(em);
  }

  /**
   * ajoute une modification au fichier parametre: chaine de type "key=value"
   */
  public void addModification(String pModif) throws Exception {
    int index = pModif.lastIndexOf("=");
    if (index != -1) {
      listeModifications.add(new ElementModif(pModif.substring(0, index - 1),
          pModif.substring(index + 1, pModif.length())));
    } else {
      throw new Exception("la chaine \"" + pModif
          + "\" n'est pas standard \"key=modif\"");
    }
  }

  /**
   * creer une copy de fichier *.bak par defaut (si parametre est a null) sinon
   * la valeur du parametre passe.
   */
  public void createFileBak(String str) throws Exception {
    File file = new File(path);
    File newFile;
    if (file.exists() && file.isFile()) {
      if (str == null) {
        newFile = new File(path + ".bak");
      } else {
        newFile = new File(str);
      }
      copyFile(file, newFile);
    } else {
      throw new Exception("Le fichier :\"" + path
          + "\" n'existe pas ou n'est pas un fichier");
    }
  }

  /**
   * methode abstraite pour la modification du fichier
   */
  protected abstract void executeModification() throws Exception;
}
