/*--- formatted by Jindent 2.1, (www.c-lab.de/~jindent) ---*/

package com.silverpeas.FileUtil;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

/**
 * Titre :        MajDateFile
 * Description :  Tools de production des releases primaires
 * Copyright :    Copyright (c)
 * Société :      Stratélia
 * @author        Pellegrin Thomas
 * @version 1.0
 */
public class UpgradeDateFiles {
	private Date dateMaj = null;
	public int nbDocuments = -1;
	public int nbRepertoires = -1;

	/**
	 * ---------------------------------------------------------------------
	 * @see
	 */
	public UpgradeDateFiles() throws Exception {
		dateMaj = new Date();
		nbDocuments = 0;
		nbRepertoires = 0;
	}

	/**
	 * met a jour la valeur de la date de mise à jour à l'aide de la string
	 * passée en paramétre (ex: 10/10/2001/22:12).
	 */
	public void setUpgradeDate( String pDate ) throws Exception {
		SimpleDateFormat formatDate = new SimpleDateFormat( "dd/MM/yyyy/hh:mm" );
		ParsePosition pos = new ParsePosition( 0 );
		dateMaj = formatDate.parse( pDate, pos );
	}

	/**
	 * recupere un clone de la date de mise à jour
	 */
	public Date getUpgradeDate() throws Exception {
		return ( Date ) dateMaj.clone();
	}

	/**
	 * fonction : qui met à jour tous les fichiers et repertoire du repertoire
	 * passer en paramétre
	 */
	private void majDirectory( File pRep ) throws Exception {
		File[] listFiles;
		if ( pRep.isDirectory() ) {
			nbRepertoires++;
			pRep.setLastModified( dateMaj.getTime() );
			listFiles = pRep.listFiles();
			for ( int i = 0; i < listFiles.length; i++ ) {
				if ( listFiles[i].isDirectory() ) {
					majDirectory( listFiles[i] );
				} else {
					listFiles[i].setLastModified( dateMaj.getTime() );
					nbDocuments++;
				}
			}
		} else {
			throw new IllegalArgumentException( "la mise à jour ne concerne pas un repertoire" );
		}
	}

	/**
	 * méthode pour la mise à jour de toute l'arborescence à partir du chemin
	 * donné en paramétre
	 */
	public void upgradeFiles( String pPathName ) throws Exception {
		File tmpFile;
		try {
			tmpFile = new File( pPathName );
			tmpFile.setLastModified( dateMaj.getTime() );
			if ( tmpFile.isDirectory() ) {
				majDirectory( tmpFile );
			} else {
				nbDocuments++;
			}
		} catch ( Exception ex ) {
			throw new Exception( "Un problème est apparue lors de la mise à jour d'un fichier nb:" + nbRepertoires + ex );
		}
	}

	/**
	 * main pour tester la classe MajDateFile
	 */

	/*
	 * public static void main(String[] args)  {
	 *
	 * if(args.length==2){
	 * try {
	 * MajDateFile maj;
	 * System.out.println("lancement de la mise à jour");
	 * System.out.println("Attention: il peut y avoir une variation de plus ou moins une heure suivant l'heure d'été actuel de WINDOWS");
	 * maj = new MajDateFile();
	 * maj.setDateMaj(args[0]);
	 * maj.majFiles(args[1]);
	 * System.out.println("mise à jour terminé-> nb  repertoires:"+maj.nbRepertoires+" documents:"+maj.nbDocuments);
	 * } catch (Exception e) {
	 * System.out.println("error : " + e.toString());
	 * }
	 * }else{
	 * System.out.println("[jour/mois/année/heure:minute] [path]");
	 * }
	 *
	 * }
	 */


}
