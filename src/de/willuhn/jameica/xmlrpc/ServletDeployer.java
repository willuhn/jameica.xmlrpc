/**********************************************************************
 *
 * Copyright (c) by Olaf Willuhn
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.xmlrpc;

import org.eclipse.jetty.security.LoginService;

import de.willuhn.jameica.webadmin.deploy.AbstractServletDeployer;
import de.willuhn.jameica.webadmin.server.JameicaLoginService;
import de.willuhn.jameica.xmlrpc.server.XmlRpcServlet;

/**
 * Deployer fuer das XML-RPC-Servlet.
 */
public class ServletDeployer extends AbstractServletDeployer
{
  /**
   * @see de.willuhn.jameica.webadmin.deploy.AbstractServletDeployer#getContext()
   */
  protected String getContext()
  {
    return "/xmlrpc";
  }

  /**
   * @see de.willuhn.jameica.webadmin.deploy.AbstractWebAppDeployer#getSecurityRoles()
   */
  protected String[] getSecurityRoles()
  {
    return new String[]{"admin"};
  }
  
  /**
   * @see de.willuhn.jameica.webadmin.deploy.AbstractServletDeployer#getLoginService()
   */
  @Override
  protected LoginService getLoginService()
  {
    return new JameicaLoginService()
    {
      /**
       * @see de.willuhn.jameica.webadmin.server.JameicaLoginService#getName()
       */
      public String getName()
      {
        // Name des Realms koennte man mal noch konfigurierbar machen
        // Aber aus Gruenden der Abwaertskompatibilitaet (zur alten
        // jameica.xmlrpc-Version) lass ich das mal stehen.
        return "XML-RPC";
      }
      
    };
  }

  /**
   * @see de.willuhn.jameica.webadmin.deploy.AbstractServletDeployer#getServletClass()
   */
  protected Class getServletClass()
  {
    return XmlRpcServlet.class;
  }

}
