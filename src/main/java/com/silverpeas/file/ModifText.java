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
package com.silverpeas.file;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifText extends ModifFile {

  /**
   * @constructor prend en param�tre le chemin du fichier a modifier
   */
  public ModifText(String path) throws Exception {
    super.setPath(path);
  }

  /**
   * ajoute des modifications sous forme de tableau de chaine "key=replace"
   */
  public void setModif(String str[]) throws Exception {
    for (int i = 0; i < str.length; i++) {
      this.addModification(str[i]);
    }
  }

  /**
   * analyse une ligne du fichier
   */
  protected void analyseLigne(DataOutput out, String pBuf) {

    String resStr = pBuf;
    String tmpStr = null;
    String modif = null;

    int index, res;
    Iterator im = listeModifications.iterator();

    while (im.hasNext()) {
      Object modifObj = im.next();
      if (modifObj instanceof ElementMultiValues) {
        ElementMultiValues emv = ((ElementMultiValues) modifObj);
        Pattern pattern = Pattern.compile(emv.getSearch());
        Matcher match = pattern.matcher(resStr);
        if (match.matches()) {
          modif = (String) emv.getIterator().next();
          tmpStr = resStr.substring(0, match.start(0));
          tmpStr += modif;
          tmpStr += resStr.substring(match.end(0));
          resStr = tmpStr;
        }
      } else {
        ElementModif em = ((ElementModif) modifObj);
        // remplace uniquement la derniere occurence de chaque ligne
        res = resStr.lastIndexOf(em.getSearch());
        if (res != -1) {
          modif = em.getModif();
          tmpStr = new String(resStr.substring(0, res).concat(modif).concat(
              resStr
              .substring((res + em.getSearch().length()), resStr.length())));
          resStr = tmpStr;
        }
      }
    }
    try {
      if (tmpStr != null) {
        out.writeBytes(tmpStr);
      } else {
        out.writeBytes(pBuf);
      }
      out.writeBytes(System.getProperty("line.separator"));
    } catch (Exception e) {
      System.out.println("probleme d'ecriture est survenue");
    }
  }

  /**
   * lance la modification du fichier Attention la modification s'effectue par ligne du fichier
   */
  @Override
  public void executeModification() throws Exception {

    DataInput dataInput;
    DataOutput dataOutput;
    String line;

    File tmpFile = new File(System.getProperty("java.io.tmpdir")
        + File.separator + "ModifText.sh");
    File inFile = new File(path);

    FileInputStream dis = new FileInputStream(path);
    FileOutputStream dos = new FileOutputStream(tmpFile);

    dataInput = new DataInputStream(dis);
    dataOutput = new DataOutputStream(dos);

    while ((line = dataInput.readLine()) != null) {
      analyseLigne(dataOutput, line);
    }

    dis.close();
    dos.close();
    inFile.delete();
    FileUtil.copyFile(tmpFile, inFile);
    tmpFile.delete();

    // Specifique Linux/Solaris: on met le fichier de script en executable
    if (path.endsWith(".sh") || path.endsWith(".csh") || path.endsWith(".ksh")) {
      String[] commande = new String[3];
      commande[0] = "/bin/chmod";
      commande[1] = "755";
      commande[2] = new String(path);
      Runtime.getRuntime().exec(commande);
    }
  }

  public static void main(String[] args) {
    ModifText mp;
    String[] strs = { "comments=ouais", "beanId=12385" };
    System.out.println("test des modification de fichier XML");
    try {
      mp = new ModifText("c:\\toto\\SilverpeasSettings.xml");
      mp.createFileBak(null);
      mp.addModification("%AUTHENTIFICATION%", "c:\toto\titi");
      mp.executeModification();

    } catch (Exception e) {
      e.printStackTrace();

    }
  }
}