/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/de/willuhn/jameica/xmlrpc/server/Attic/MyWebServer.java,v $
 * $Revision: 1.2 $
 * $Date: 2006/10/31 17:44:20 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

import org.apache.xmlrpc.webserver.WebServer;

import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.xmlrpc.Settings;
import de.willuhn.logging.Logger;

/**
 * Wir leiten vom XMLRPC-Webserver ab, um ihn besser customizen zu koennen.
 */
public class MyWebServer extends WebServer
{

  /**
   * ct.
   */
  public MyWebServer()
  {
    super(Settings.getPort());
    Logger.info("started webserver at port " + Settings.getPort());
    Logger.info("  ssl enabled : " + Settings.getUseSSL());
    Logger.info("  auth enabled: " + Settings.getUseAuth());
  }

  /**
   * @see org.apache.xmlrpc.webserver.WebServer#createServerSocket(int, int, java.net.InetAddress)
   */
  protected ServerSocket createServerSocket(int pPort, int backlog, InetAddress addr) throws IOException
  {
    if (Settings.getUseSSL())
    {
      try
      {
        SSLContext context = Application.getSSLFactory().getSSLContext();
        SSLServerSocketFactory factory = context.getServerSocketFactory();
        return factory.createServerSocket(pPort,backlog,addr);
      }
      catch (IOException ioe)
      {
        throw ioe;
      }
      catch (Exception e)
      {
        Logger.error("unable to create ssl socket",e);
        throw new IOException("unable to create ssl socket");
      }
    }
    return super.createServerSocket(pPort, backlog, addr);
  }
}


/*********************************************************************
 * $Log: MyWebServer.java,v $
 * Revision 1.2  2006/10/31 17:44:20  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/31 17:06:26  willuhn
 * @N GUI to configure xml-rpc
 *
 **********************************************************************/