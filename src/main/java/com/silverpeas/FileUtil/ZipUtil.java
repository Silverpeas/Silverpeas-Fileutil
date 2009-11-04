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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtil {

  /**
   * ---------------------------------------------------------------------
   * 
   * @param fromFile
   * @param toDir
   * @throws IOException
   * @throws ZipException
   * @see
   */
  public static void unzip(String fromFile, String toDir) throws Exception {
    unzip(new File(fromFile), new File(toDir));
  }

  /**
   * ---------------------------------------------------------------------
   * 
   * @param from
   * @param to
   * @throws IOException
   * @throws IllegalArgumentException
   * @see
   */
  public static void unzip(File fromZipFile, File toDir) throws Exception {
    if (!toDir.exists()) {
      toDir.mkdirs();
    }
    ZipFile zipFile = new ZipFile(fromZipFile);
    ZipInputStream in = new ZipInputStream(new FileInputStream(fromZipFile));
    ZipEntry entry;
    while ((entry = in.getNextEntry()) != null) {
      extractFile(zipFile, entry, toDir);
    }
    in.close();
    zipFile.close();
  }

  /**
   * ---------------------------------------------------------------------
   * 
   * @param fromZipFile
   * @param fileName
   * @param toDir
   * @throws Exception
   * @see
   */
  public static void extractFile(String fromZipFile, String fileName,
      String toDir) throws Exception {
    ZipFile zipFile = new ZipFile(fromZipFile);
    File dir = new File(toDir);
    if (!dir.exists()) {
      dir.mkdirs();
    } else if (dir.isFile()) {
      throw new Exception("destination cannot be a file");
    }
    if (zipFile.getEntry(fileName) == null) {
      throw new Exception("file \"" + fileName + "\" not found into \""
          + fromZipFile + "\".");
    }
    extractFile(zipFile, zipFile.getEntry(fileName), dir);
  }

  /**
   * ---------------------------------------------------------------------
   * 
   * @param zipFile
   * @param zipEntry
   * @param toDir
   * @throws Exception
   * @see
   */
  private static void extractFile(ZipFile zipFile, ZipEntry zipEntry, File toDir)
      throws Exception {
    byte[] data = new byte[(int) zipEntry.getSize()];
    DataInputStream in = new DataInputStream(zipFile.getInputStream(zipEntry));
    in.readFully(data);
    File toFile = new File(toDir.getAbsolutePath() + File.separator
        + zipEntry.getName());
    if (zipEntry.isDirectory()) {
      toFile.mkdirs();
    } else if (!toFile.exists()) {
      toFile.getParentFile().mkdirs();
      DataOutputStream out = new DataOutputStream(new FileOutputStream(toFile));
      out.write(data);
      out.close();
    }
    in.close();
  }

}
