import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/EchoTest.java,v $
 * $Revision: 1.2 $
 * $Date: 2006/10/28 01:05:37 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

public class EchoTest
{

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    config.setServerURL(new URL("http://127.0.0.1:8889/xmlrpc"));
    XmlRpcClient client = new XmlRpcClient();
    client.setConfig(config);
    System.out.println(client.execute("de.willuhn.jameica.xmlrpc.rmi.EchoService.echo", new String[]{"Hallo Echo"}));
  }

}


/*********************************************************************
 * $Log: EchoTest.java,v $
 * Revision 1.2  2006/10/28 01:05:37  willuhn
 * @N add bindings on demand
 *
 * Revision 1.1  2006/10/26 23:54:15  willuhn
 * @N added needed jars
 * @N first working version
 *
 **********************************************************************/