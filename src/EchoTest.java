import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**********************************************************************
 * $Source: /cvsroot/jameica/jameica.xmlrpc/src/EchoTest.java,v $
 * $Revision: 1.6 $
 * $Date: 2006/11/07 23:57:16 $
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
   * @throws Exception
   */
  public static void main(String[] args) throws Exception
  {
    // Die URL, unter der Jameica auf XML-RPC Anfragen reagiert.
    // Der Port 8888 kann ueber Datei->Einstellungen->XML-RPC festgelegt werden
    // Wenn die Jameica-Installation auf einem anderen Rechner laeuft, kann
    // auch eine andere IP verwendet werden.
    // Ist in Jameica die Verwendung von SSL fuer XML-RPC-Anfragen aktiv,
    // muss die URL mit "https" beginnen
    String url = "https://127.0.0.1:8888/xmlrpc";

    // Client-Config erzeugen
    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    
    // Jameica-Masterpasswort
    // Falls in Jameica die Abfrage des Passwortes bei XML-RPC-Anfragen aktiviert ist
    config.setBasicPassword("geheim");
    
    // Ein Username muss leider angegeben werden, wenn in Jameica die
    // Passwort-Kontrolle aktiv ist. Sonst wirft Apache XML-RPC einen Fehler.
    // Geprueft wird der Username aber nicht, man kann also einen beliebigen
    // Wert angeben
    config.setBasicUserName("egal");
    
    // optional: Freischaltung von Apache-spezifischen Erweiterungen.
    // Damit lassen sich z.Bsp. auch Objekte des Typs java.io.Serializable uebertragen
    config.setEnabledForExtensions(true);
    
    // Angabe der Server-URL.
    // "https" falls in Jameica Verschluesselung fuer XML-RPC aktiviert wurde
    config.setServerURL(new URL(url));

    if (url.startsWith("https"))
    {
      initSSL();
    }
    
    // Client erzeugen und Config uebernehmen
    XmlRpcClient client = new XmlRpcClient();
    client.setConfig(config);

    // Echo-Testservice aufrufen
    // Wir uebergeben als Parameter ein "Hello World"
    System.out.println("Test 1:");
    String echo = (String) client.execute("jameica.xmlrpc.echo.echo",new String[]{"Hello World"});
    System.out.println(echo);

    // Methode "list" auf dem Service "hibiscus.xmlrpc.konto" ausfuehren.
    // Freigegebene Services siehe Jameica: Datei->Einstellungen->XML-RPC
    // Der Parameter "(Object[]) null" muss angegeben werden, auch wenn
    // die Methode keine Parameter erwartet.
    System.out.println("Test 2:");
    Object[] konten = (Object[]) client.execute("hibiscus.xmlrpc.konto.list",(Object[]) null);
    
    for (int i=0;i<konten.length;++i)
    {
      System.out.println(konten[i]);
    }

    // Methode "list" auf dem Service "hibiscus.xmlrpc.ueberweisung" ausfuehren.
    System.out.println("Test 3:");
    Object[] ueberweisungen = (Object[]) client.execute("hibiscus.xmlrpc.ueberweisung.list",(Object[]) null);
    
    for (int i=0;i<ueberweisungen.length;++i)
    {
      System.out.println(ueberweisungen[i]);
    }
  
    // Neue Ueberweisung anlegen
    System.out.println("Test 4:");
    Object[] params = new Object[]
      {
        "0",                // ID des Kontos, auf dem die Ueberweisung ausgefuehrt werden soll
        "123456789",        // Kontonummer des Empfaengers
        "12345678",         // BLZ des Empfaenger-Kontos
        "Max Mustermann",   // Name des Empfaengers
        "Das ist ein Test", // Verwendungszweck
        new Double(1.00),   // Betrag (1,- EUR)
      };
    Object returnCode = (Object) client.execute("hibiscus.xmlrpc.ueberweisung.create",params);
    System.out.println(returnCode);

  }


  
  /**
   * Falls SSL akrtiv ist, initialisiert diese Funktion Java fuer die Verwendung von HTTPs.
   * @throws Exception
   */
  private static void initSSL() throws Exception
  {
    // Hinweis: Die folgenden Zeilen Code sind nur fuer Testzwecke gedacht.
    // Im produktiven Einsatz sollte man selbstverstaendlich pruefen, ob
    // die Zertifikate in Ordnung sind und ob der Hostname des Rechners
    // mit dem des Zertifikates uebereinstimmt. Um den Code hier jedoch
    // zu verkuerzen, akzeptieren wir alle Zertifikate.
    
    // Wenn SSL aktiv ist, muessen wir dem SSL-Zertifikat von Jameica vertrauen.
    // Da Java das nicht von allein macht, muessen wir das tun: Wir implementieren
    // hierzu einen TrustManager, der alle Zertifikate akzeptiert und verwenden
    // diesen, um den SSLContext zu initialisieren.
    TrustManager trustAll = new X509TrustManager()
    {
       /**
       * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
       */
      public java.security.cert.X509Certificate[] getAcceptedIssuers()
      {
        return null;
      }
      
      /**
       * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
       */
      public void checkClientTrusted (X509Certificate[] certs, String authType) {}
      
      /**
       * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
       */
      public void checkServerTrusted(X509Certificate[] certs, String authType) {}
    };
    
    // Initialisieren des SSLContext mit unserem eigenen TrustManager
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, new TrustManager[]{trustAll}, new SecureRandom());
    
    // Jetzt muessen wir Java noch mitteilen, dass fuer HTTPS-Verbindungen
    // bitte unser SSLContext verwendet werden soll.
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    
    // Und als ob das alles noch nicht geung ist, muss der Hostname, der
    // in dem Zertifikat steht auch noch mit dem tatsaechlichen Hostnamen
    // des Rechners uebereinstimmen. Fuer diesen Fall kann man auch
    // noch den HostnameVerifier in Java ersetzen, damit dieser auch
    // dann Zertifikate akzeptiert, wenn der Hostname nicht mit dem
    // CN-Namen des Zertifikats uebereinstimmt.
    
    HostnameVerifier verifier = new HostnameVerifier() {
    
      public boolean verify(String arg0, SSLSession arg1)
      {
        // Wir sagen immer ja.
        return true;
      }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(verifier); 

  }
}


/*********************************************************************
 * $Log: EchoTest.java,v $
 * Revision 1.6  2006/11/07 23:57:16  willuhn
 * @N Testcode erweitert
 *
 * Revision 1.5  2006/11/01 00:49:30  willuhn
 * @N Beispiel-Code dokumentiert
 *
 * Revision 1.4  2006/10/31 17:06:26  willuhn
 * @N GUI to configure xml-rpc
 *
 * Revision 1.3  2006/10/31 01:43:08  willuhn
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/28 01:05:37  willuhn
 * @N add bindings on demand
 *
 * Revision 1.1  2006/10/26 23:54:15  willuhn
 * @N added needed jars
 * @N first working version
 *
 **********************************************************************/