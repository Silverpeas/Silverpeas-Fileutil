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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TODO : replace with commons-io
 * @author ehugonnet
 */
public class FileUtil {

  private FileUtil() {
  }

  /**
   * Utility method to copy a file from one directory to another
   */
  public static void copyFile(final String fromPath, final String toPath) throws IOException {
    copyFile(new File(fromPath), new File(toPath));
  }

  /**
   * Utility method to copy a file from one directory to another
   */
  public static void copyFile(final String fileName, final String fromDir, final String toDir)
      throws IOException {
    copyFile(new File(fromDir + File.separator + fileName), new File(toDir
        + File.separator + fileName));
  }

  /**
   * Utility method to copy a file from one directory to another
   */
  public static void copyFile(final File from, final File toDir) throws IOException {
    if (!from.canRead()) {
      throw new IOException("Cannot read file '" + from + "'.");
    }
    if (toDir.exists() && (!toDir.canWrite())) {
      throw new IOException("Cannot write to file '" + toDir + "'.");
    }
    toDir.getParentFile().mkdirs();
    final int fileSize = (int) from.length();
    byte[] data = new byte[fileSize];
    FileInputStream in = new FileInputStream(from);
    in.read(data);
    FileOutputStream out = new FileOutputStream(toDir);
    out.write(data);
    in.close();
    out.close();
    data = null;
  }

  /**
   * ---------------------------------------------------------------------
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
   * @param from
   * @param to
   * @throws IOException if from doesn't exist
   * @see
   */
  public static void copyDir(File from, File to) throws IOException {
    if (!from.exists()) {
      throw new IOException("Cannot found file or directory \"" + from + "\".");
    }
    if (from.isFile()) {
      copyFile(from, to);
    } else {
      if (to.isFile()) {
        throw new IOException("cannot copy directory \"" + from
            + "\" into the file" + to + "\".");
      }
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
   * Utility method to get extension of a file return empty String if @file doesn't exist or if @file
   * doesn't have extension
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
   * Utility method to get extension of a file return empty String if @file doesn't have extension
   */
  public static String getExtension(String file) throws IOException {
    int i = file.lastIndexOf(".");
    if (i == -1) {
      return "";
    } else {
      return file.substring(i + 1, file.length());
    }
  }

  public static void deleteFiles(String _file) {
    // Deleting the children recursively
    File file = new File(_file);
    String[] childs = file.list();
    if (childs != null) {
      for (int i = 0; i < childs.length; i++) {
        deleteFiles(_file + File.separator + childs[i]);
      }
    }
    file.delete();
  }

  public static void deleteFilesOnExit(String _file) {
    // Deleting the children recursively
    File file = new File(_file);
    file.deleteOnExit();
    String[] childs = file.list();
    if (childs != null) {
      for (int i = 0; i < childs.length; i++) {
        deleteFiles(_file + File.separator + childs[i]);
      }
    }
  }
}
