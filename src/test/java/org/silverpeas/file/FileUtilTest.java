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
 * FLOSS exception.  You should have received a copy of the text describing
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

package org.silverpeas.file;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ehugonnet
 */
public class FileUtilTest {

  public FileUtilTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void simpleTest() throws Exception {
    GestionVariables gv = new GestionVariables(new Properties());
    gv.addVariable("path", "c:\\thomas");
    gv.addVariable("rep", "\\lib\\tutu\\");
    assertEquals("\\lib\\tutu\\", gv.getValue("rep"));
    assertEquals("properc:\\thomasties.tmp", gv.resolveString("proper${path}ties.tmp"));

  }

  @Test
  public void simpleTestWithEnvironmentVariable() throws Exception {
    GestionVariables gv = new GestionVariables(new Properties());
    gv.addVariable("path", "c:\\thomas");
    gv.addVariable("rep", "\\lib\\tutu\\");
    assertEquals(System.getenv("JAVA_HOME"), gv.getValue("JAVA_HOME"));
    assertEquals(System.getenv("JAVA_HOME") + "\\properties.tmp", gv.resolveString("${JAVA_HOME}\\properties.tmp"));
  }
}
