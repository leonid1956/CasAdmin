package casadmin;

import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import javax.swing.Icon;

public class GlobalData {
    /**
    * <p><strong>Login, hasło, uprawnienia i serwer</strong> , do którego jesteśmy zalogowani</p>
    */
    public static String m_sDomainUserLogin,
                         m_sComputerName,
                         m_sServerDisplayName,
                         m_sUserLogin,
                         m_sGrant,
                         m_sServer;
    public static Vector<Vector> v_allCustomers;
    public static Vector<String> v_allDepots;
    public static Vector<String> v_allUsers;
    public static Icon lastIcon;
    public static Properties settings;
    public static Date startShipmentsDate;
    
    public static boolean returnsStarted;
    public static boolean connectB2BStarted;

    /**
    * <p>Globalny egzemplarz sesji <strong>bazy danych Oracle</strong>.
    * Tworzy się jeden raz przy logowaniu do aplikacji - procedura <strong>showLogin()</strong>
    * w modułu <strong>CasAdminView.java</strong></p>
    */
    public static YPOracleSession oraSession;
}
