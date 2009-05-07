/*--- formatted by Jindent 2.1, (www.c-lab.de/~jindent) ---*/

package com.silverpeas.FileUtil;

import java.io.File;
import java.util.*;

/**
* @Description :
*
* @Copyright   : Copyright (c) 2001
* @Société     : Silverpeas
* @author STR
* @version 1.0
*/
public class BackupFile {
	private static final String EXT = ".bak~";
	private File file = null;
	private File[] listFileBackup = null;
	private File firstFileBackup = null;
	private File lastFileBackup = null;
	private int nbFileBackup = 0;
	private int lastNumFileBackup = 0;

	// ---------------------------------------------------------------------

	/**
	*
	* @param pathname
	* @see
	*/
	public BackupFile( File pathname ) throws Exception {
		file = pathname;
		if ( !file.exists() ) {
			throw new Exception( "file not found : " + file.getAbsolutePath() );
		}
		refresh();
	}

	// ---------------------------------------------------------------------

	/**
	* @throws Exception
	* @see
	*/
	public void makeBackup() throws Exception {
//		refresh();
//		FileUtil.copyFile( file, new File( file + EXT + ( lastNumFileBackup + 1 ) ) );
//		refresh();
	}

	// ---------------------------------------------------------------------

	/**
	* @throws Exception
	* @see
	*/
	private void refresh() throws Exception {
		setList();
		setFirst();
		setLast();
	}

	// ---------------------------------------------------------------------

	/**
	* @return
	* @throws Exception
	* @see
	*/
	public File[] getListBackup() throws Exception {
		return listFileBackup;
	}

	// ---------------------------------------------------------------------

	/**
	* @return
	* @throws Exception
	* @see
	*/
	public File getFirstBackup() throws Exception {
		return firstFileBackup;
	}

	// ---------------------------------------------------------------------

	/**
	* @return
	* @throws Exception
	* @see
	*/
	public File getLastBackup() throws Exception {
		return lastFileBackup;
	}

	// ---------------------------------------------------------------------

	/**
	* @return
	* @throws Exception
	* @see
	*/
	public int getNumberBackup() throws Exception {
		return nbFileBackup;
	}

	// ---------------------------------------------------------------------

	/**
	* @return
	* @throws Exception
	* @see
	*/
	public boolean existBackup() throws Exception {
		return ( nbFileBackup != 0 );
	}

	// ---------------------------------------------------------------------

	/**
	* @throws Exception
	* @see
	*/
	private void setList() throws Exception {
		File[] listeAll = file.getParentFile().listFiles();
		ArrayList listeGood = new ArrayList();
		for ( int i = 0; i < listeAll.length; i++ ) {
			File tmpFile = listeAll[i];
			if ( tmpFile.isFile() && ( tmpFile.getName().indexOf( file.getName() ) != -1 ) && tmpFile.getName().indexOf( EXT ) != -1 ) {
				listeGood.add( tmpFile );
			}
		}
		File[] listeF = new File[listeGood.size()];
		for ( int i = 0; i < listeF.length; i++ ) {
			listeF[i] = ( File ) listeGood.get( i );
		}
		listFileBackup = listeF;
		nbFileBackup = listFileBackup.length;
	}

	// ---------------------------------------------------------------------

	/**
	* @throws Exception
	* @see
	*/
	private void setFirst() throws Exception {
		File f = null;
		if ( this.existBackup() ) {
			f = listFileBackup[0];
			for ( int i = 0; i < nbFileBackup; i++ ) {
				if ( listFileBackup[i].lastModified() < f.lastModified() ) {
					f = listFileBackup[i];
				}
			}
		}
		firstFileBackup = f;
	}

	// ---------------------------------------------------------------------

	/**
	* @throws Exception
	* @see
	*/
	private void setLast() throws Exception {
		lastFileBackup = null;
		if ( this.existBackup() ) {
			lastFileBackup = listFileBackup[0];
			for ( int i = 0; i < nbFileBackup; i++ ) {
				if ( listFileBackup[i].lastModified() > lastFileBackup.lastModified() ) {
					lastFileBackup = listFileBackup[i];
				}
			}
		}
		lastNumFileBackup = -1;
		if ( this.existBackup() ) {
			String name = lastFileBackup.getName();
			String endName = name.substring( name.lastIndexOf( EXT ) + EXT.length(), name.length() );
			int i = 1;
			while ( i <= endName.length() ) {
				try {
					int num = Integer.parseInt( endName.substring( 0, i ) );
					lastNumFileBackup = num;
					i++;
				} catch ( Exception e ) {
					i = endName.length() + 2;
				}
			}
		}
	}

	// ---------------------------------------------------------------------

	/**
	* @param args
	* @throws Exception
	* @see
	*/
	public static void main( String[] args ) throws Exception {
		BackupFile bf = new BackupFile( new File( args[0] ) );
		bf.makeBackup();
		File f[] = bf.getListBackup();
		for ( int i = 0; i < f.length; i++ ) {
			System.out.println( f[i].getAbsolutePath() );
		}
		System.out.println( "last : " + bf.getLastBackup().getAbsolutePath() );
		System.out.println( "nb last : " + bf.lastNumFileBackup );
	}

}
