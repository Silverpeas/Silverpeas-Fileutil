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
/*--- formatted by Jindent 2.1, (www.c-lab.de/~jindent) 
 ---*/

package com.silverpeas.FileUtil;

import java.io.*;

/**
 * @Description :
 * 
 * @Copyright : Copyright (c) 2001
 * @Société : Silverpeas
 * @author STR
 * @version 1.0
 */
public class FileUtil {

  /**
   * ---------------------------------------------------------------------
   * 
   * @see
   */
  private FileUtil() {
  }

  /**
   * Utility method to copy a file from one directory to another
   */
  public static void copyFile(String from, String to) throws IOException {
    copyFile(new File(from), new File(to));
  }

  /**
   * Utility method to copy a file from one directory to another
   */
  public static void copyFile(String fileName, String fromDir, String toDir)
      throws IOException {
    copyFile(new File(fromDir + File.separator + fileName), new File(toDir
        + File.separator + fileName));
  }

  /**
   * Utility method to copy a file from one directory to another
   */
  public static void copyFile(File from, File to) throws IOException {
    if (!from.canRead()) {
      throw new IOException("Cannot read file '" + from + "'.");
    }
    if (to.exists() && (!to.canWrite())) {
      throw new IOException("Cannot write to file '" + to + "'.");
    } else {
      to.getParentFile().mkdirs();
    }
    int fileSize = (int) from.length();
    byte[] data = new byte[fileSize];
    FileInputStream in = new FileInputStream(from);
    in.read(data);
    FileOutputStream out = new FileOutputStream(to);
    out.write(data);
    in.close();
    out.close();
    data = null;
  }

  /**
   * ---------------------------------------------------------------------
   * 
   * @param from
   * @param to
   * @throws IOException
   * @see
   */
  public static void copyDir(String from, String to) throws IOException {
    copyDir(new File(from), new File(to));
  }

  /**
   * ---------------------------------------------------------------------
   * 
   * @param from
   * @param to
   * @throws IOException
   *           if from doesn't exist
   * @see
   */
  public static void copyDir(File from, File to) throws IOException {
    if (!from.exists()) {
      throw new IOException("Cannot found file or directory \"" + from + "\".");
    }
    if (from.isFile()) {
      copyFile(from, to);
    } else {
      if (to.isFile())
        throw new IOException("cannot copy directory \"" + from
            + "\" into the file" + to + "\".");
      to.mkdirs();
      String[] childs = from.list();
      if (childs != null) {
        for (int i = 0; i < childs.length; i++) {
          File childFrom = new File(from.getAbsolutePath() + File.separator
              + childs[i]);
          File childTo = new File(to.getAbsolutePath() + File.separator
              + childs[i]);
          copyDir(childFrom, childTo);
        }
      }
    }
  }

  /**
   * ---------------------------------------------------------------------
   * 
   * @param file
   * @param c
   * @return
   * @throws FileNotFoundException
   * @see
   */
  public static InputStream getInputStream(File file, Class c)
      throws FileNotFoundException {
    InputStream rtn;
    String s;
    if (file != null) {
      try {
        return new FileInputStream(file);
      } catch (FileNotFoundException e) {
        s = file.toString();
        int i = s.indexOf(File.separator);
        if (i >= 0) {
          s = s.substring(i);
          s = StringUtil.sReplace("\\", "/", s);
          if ((rtn = c.getResourceAsStream(s)) != null) {
            return rtn;
          }
        }
        throw e;
      }
    }
    return null;
  }

  /**
   * Utility method to get extension of a file return empty String if @file
   * doesn't exist or if @file doesn't have extension
   */
  public static String getExtension(File file) throws IOException {
    if (!file.isFile()) {
      return "";
    } else {
      String name = file.getName();
      int i = name.lastIndexOf(".");
      if (i == -1) {
        return "";
      } else {
        return name.substring(i + 1, name.length());
      }
    }
  }

  /**
   * Utility method to get extension of a file return empty String if @file
   * doesn't have extension
   */
  public static String getExtension(String file) throws IOException {
    int i = file.lastIndexOf(".");
    if (i == -1) {
      return "";
    } else {
      return file.substring(i + 1, file.length());
    }
  }

  /**
   * ---------------------------------------------------------------------
   * 
   * @param _file
   * @see
   */
  public static void deleteFiles(String _file) {

    // suppression récursive des fils
    File file = new File(_file);
    String[] childs = file.list();
    if (childs != null) {
      for (int i = 0; i < childs.length; i++) {
        deleteFiles(_file + File.separator + childs[i]);
      }
    }

    // suppression de la racine
    file.delete();
  }

  /**
   * ---------------------------------------------------------------------
   * 
   * @param _file
   * @see
   */
  public static void deleteFilesOnExit(String _file) {

    // suppression récursive des fils
    File file = new File(_file);
    // suppression de la racine
    file.deleteOnExit();
    String[] childs = file.list();
    if (childs != null) {
      for (int i = 0; i < childs.length; i++) {
        deleteFiles(_file + File.separator + childs[i]);
      }
    }

  }

}
