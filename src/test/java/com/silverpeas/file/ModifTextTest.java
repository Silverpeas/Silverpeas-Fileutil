package com.silverpeas.file;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static  org.junit.Assert.*;

/**
 *
 * @author ehugonnet
 */
public class ModifTextTest {

  private static String base = System.getProperty("basedir");

  @Test
  public void testModificationWithRegexp() throws Exception {
    File testFile = new File(base + File.separatorChar + "target" + File.separatorChar + "test-classes"
            + File.separatorChar + "run.bat");
    ModifText mp = new ModifText(testFile.getPath());
    mp.createFileBak(null);
    RegexpElementMotif elt = new RegexpElementMotif("-Xms[0-9]+m");
    elt.setRemplacement("-Xms512m");
    mp.addModification(elt);
    mp.executeModification();
    List lines = FileUtils.readLines(testFile);
    assertNotNull(lines);
    assertEquals(3, lines.size());
    assertEquals("set JAVA_OPTS=%JAVA_OPTS% -Xms512m -Xmx256m", (String)lines.get(2));

  }

  @Test
  public void testDoubleModificationWithRegexp() throws Exception {
    File testFile = new File(base + File.separatorChar + "target" + File.separatorChar + "test-classes"
            + File.separatorChar + "run.bat");
    ModifText mp = new ModifText(testFile.getPath());
    mp.createFileBak(null);
    RegexpElementMotif elt1 = new RegexpElementMotif("-Xms[0-9]+m");
    elt1.setRemplacement("-Xms512m");
    RegexpElementMotif elt2 = new RegexpElementMotif("-Xmx[0-9]+m");
    elt2.setRemplacement("-Xmx512m");
    mp.addModification(elt1);
    mp.addModification(elt2);
    mp.executeModification();
    List lines = FileUtils.readLines(testFile);
    assertNotNull(lines);
    assertEquals(3, lines.size());
    assertEquals("set JAVA_OPTS=%JAVA_OPTS% -Xms512m -Xmx512m", (String)lines.get(2));

  }

  @Test
  public void testModificationWithRegexpMultimatch() throws Exception {
    File testFile = new File(base + File.separatorChar + "target" + File.separatorChar + "test-classes"
            + File.separatorChar + "run.txt");
    ModifText mp = new ModifText(testFile.getPath());
    mp.createFileBak(null);
    RegexpElementMotif elt1 = new RegexpElementMotif("-Xms[0-9]+m");
    elt1.setRemplacement("-Xms512m");
    RegexpElementMotif elt2 = new RegexpElementMotif("-Xmx[0-9]+m");
    elt2.setRemplacement("-Xmx512m");
    mp.addModification(elt1);
    mp.addModification(elt2);
    mp.executeModification();
    List lines = FileUtils.readLines(testFile);
    assertNotNull(lines);
    assertEquals(3, lines.size());
    assertEquals("set JAVA_OPTS=%JAVA_OPTS% -Xms512m -Xmx512m -Xms512m", (String)lines.get(2));

  }
}
