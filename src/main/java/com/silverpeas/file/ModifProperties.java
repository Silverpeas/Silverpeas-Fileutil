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
import java.util.Iterator;
import java.util.Properties;

public class ModifProperties extends ModifFile {

  /**
   * message de la nouvelle mise a jour du fichier
   */
  private String messageProperties = null;

  /**
   * @constructor prend en parametre le fichier a modifier
   */
  public ModifProperties(String path) throws Exception {
    this.setPath(path);
  }

  /**
   * met a jour le message d'information du propertie
   */
  public void setMessageProperties(String str) {
    messageProperties = str;
  }

  /**
   * @return le message de mise a jour du properties
   */
  public String getMessageProperties() {
    return messageProperties;
  }

  /**
   * lance la modification du fichier properties
   */
  @Override
  public void executeModification() throws Exception {
    FileInputStream inputFile;
    FileOutputStream outputFile;
    Iterator i;
    int index;
    ElementModif em;

    File file = new File(path);

    if (file.exists()) {
      inputFile = new FileInputStream(file);
      Properties pro = new Properties();
      pro.load(inputFile);
      inputFile.close();
      i = listeModifications.iterator();

      while (i.hasNext()) {
        em = ((ElementModif) i.next());
        if (pro.containsKey(em.getSearch())) {
          if (!em.getModif().equals(pro.getProperty(em.getSearch()))) {
            if (!isModified) {
              isModified = true;
              BackupFile bf = new BackupFile(new File(path));
              bf.makeBackup();
            }
            pro.setProperty(em.getSearch(), em.getModif());
          }
        } else {
          throw new Exception("ATTENTION key:\"" + em.getSearch()
              + "\" non existante dans le fichier propertie: " + path);
        }
      }
      outputFile = new FileOutputStream(path);
      pro.store(outputFile, messageProperties);
    } else {
      throw new Exception("ATTENTION le fichier:" + path + " n'existe pas");
    }
  }

  public static void main(String[] args) {
    ModifProperties mp;
    String[] strs = { "bonjour=different" };
    try {
      mp = new ModifProperties("c:\\lib\\test.properties");
      mp.createFileBak(null);
      mp.executeModification();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

}