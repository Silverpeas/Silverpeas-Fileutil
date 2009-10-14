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
import java.util.Enumeration;
import java.util.zip.*;

/**
 * @Description :
 * 
 * @Copyright : Copyright (c) 2001
 * @Société : Silverpeas
 * @author STR
 * @version 1.0
 */
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

  /*
   * public static void unzip( File fromZipFile, File toDir ) throws
   * ZipException, IOException { byte[] buffer = new byte[4096]; int bytes_read;
   * // check input file if ( !fromZipFile.exists() ) { throw new IOException(
   * fromZipFile + " does not exist." ); } if ( !fromZipFile.canRead() ) { throw
   * new IOException( fromZipFile + " read protected." ); } // create directory
   * if ( !toDir.exists() ) { toDir.mkdirs(); } ZipInputStream in = new
   * ZipInputStream( new FileInputStream( fromZipFile ) ); ZipEntry entry;
   * FileOutputStream out; String toName; File fout; while ( ( entry =
   * in.getNextEntry() ) != null ) { toName = toDir.getAbsolutePath() +
   * File.separator + entry.getName(); fout = new File( toName ); // if (
   * fout.exists() ) { // don't overwrite existing files and continue //
   * System.err.println( "File '" + toName + "' already exisits." ); //
   * continue; // } if ( entry.isDirectory() ) { // create directory for
   * directory entries if ( !fout.mkdirs() ) { throw new IOException(
   * "Unable to create directory: " + toName ); } } else { // write file out =
   * new FileOutputStream( toName ); while ( ( bytes_read = in.read( buffer ) )
   * != -1 ) { out.write( buffer, 0, bytes_read ); } out.close(); } }
   * in.close(); }
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
