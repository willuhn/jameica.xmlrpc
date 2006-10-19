/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/Plugin.java,v $
 * $Revision: 1.2 $
 * $Date: 2006/10/19 16:08:30 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc;

import java.io.File;

import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.util.ApplicationException;

/**
 * @author willuhn
 */
public class Plugin extends AbstractPlugin
{

  /**
   * @param file
   */
  public Plugin(File file)
  {
    super(file);
  }

  /**
   * @see de.willuhn.jameica.plugin.AbstractPlugin#init()
   */
  public void init() throws ApplicationException
  {
  }

  /**
   * @see de.willuhn.jameica.plugin.AbstractPlugin#install()
   */
  public void install() throws ApplicationException
  {
  }

  /**
   * @see de.willuhn.jameica.plugin.AbstractPlugin#shutDown()
   */
  public void shutDown()
  {
  }

  /**
   * @see de.willuhn.jameica.plugin.AbstractPlugin#update(double)
   */
  public void update(double oldVersion) throws ApplicationException
  {
  }

}


/*********************************************************************
 * $Log: Plugin.java,v $
 * Revision 1.2  2006/10/19 16:08:30  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/19 15:27:01  willuhn
 * @N initial checkin
 *
 *********************************************************************/