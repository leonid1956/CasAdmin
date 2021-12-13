/*
 * CasAdminView.java
 */
package casadmin;

import java.awt.Dimension;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Properties;
import java.util.Vector;
import javax.swing.*;
import oracle.jdbc.OracleTypes;
import org.jdesktop.application.ResourceMap;

/**
 * The application's main frame.
 */
public class CasAdminView extends FrameView {

    public CasAdminView(SingleFrameApplication app) {
        super(app);
        initComponents();

        (new File(Constants.temporaryDir)).mkdir();


        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

/*
        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
*/
        // Odłączamy Proxy
        
        java.net.ProxySelector.setDefault(null);
        // Plik ustawień
        try {
           // Otwieramy dostęp do pliku
           GlobalData.settings = new Properties();
           GlobalData.settings.load(new FileInputStream("Settings.ini"));
        }catch (Exception e) {}
        // Odczytujemy ostatnio używany serwer
        String tmp = GlobalData.settings.getProperty("UsersPanel.startShipmentsDate","2006/01/01");

        GlobalData.startShipmentsDate = YPFunctions.StringToDate(tmp);

        GlobalData.settings.setProperty("UsersPanel.startShipmentsDate","2006/01/01");
        // zapisujemy na dysk
        try {
                GlobalData.settings.save(new FileOutputStream("Settings.ini"), "");
        }catch (Exception e) {}





        // Pokazujemy HelloPanel, która służy jako informacyjna
        showPanel(new HelloPanel(),Constants.gifStyle.hello);
        // Tworzymy objekt klasy Timer dla tego żeby pokazać okno logowania
        // wtędy, kiedy głowny ekran aplikacji już pokaże się na ekranie.
        // Jak by my tego nie zrobili za pomocą objektu klasy Timer,
        // to musieli by najpierw pokazać dialog logowania, a
        // tylko póżniej już pojawił by się ekran aplikacji.
        loginTimer = new Timer(200, new ActionListener() {
            // Definiujemy zdarzenie Timer'a
            public void actionPerformed(ActionEvent e) {
                showLogin();
            }
        });
        // Uruchamiamy timer. Skutkiem będzie pojawienie za 200 milisekund
        // ekranu logowania, a konstruktor głównego okna CasAdminView już
        // skończył pracę i głowne okno zostało wyświetlone.
        loginTimer.start();
    }

    void setProgressPos(final int i) {
        Runnable doSetProgressBarValue = new Runnable() {
            public void run() {
                    progressBar.setValue(i);
            }
        };
        SwingUtilities.invokeLater(doSetProgressBarValue);
    }
    void setMessageText(final String mess) {
        Runnable doSetMessageText = new Runnable() {
            public void run() {
                    statusMessageLabel.setText(mess);
            }
        };
        SwingUtilities.invokeLater(doSetMessageText);
    }


    void swingworkerStartProgress(final boolean p_bIntermediate,
                                  final int p_iMinProgressBar,
                                  final int p_iMaxProgressBar,
                                  final String p_sMessage) {
        Runnable DoStartProgress = new Runnable() {
            public void run() {
               if (!busyIconTimer.isRunning()) {
                   statusAnimationLabel.setIcon(busyIcons[0]);
                   busyIconIndex = 0;
                   busyIconTimer.start();
               }
               progressBar.setIndeterminate(p_bIntermediate);
               progressBar.setMinimum(p_iMinProgressBar);
               progressBar.setMaximum(p_iMaxProgressBar);
               progressBar.setVisible(true);
               statusMessageLabel.setText(p_sMessage);
            }
        };
        SwingUtilities.invokeLater(DoStartProgress);
    }

    void swingworkerStopProgress(final String p_sMessage) {
        Runnable DoStopProgress = new Runnable() {
            public void run() {
               if (!busyIconTimer.isRunning()) {
                   statusAnimationLabel.setIcon(busyIcons[0]);
                   busyIconIndex = 0;
                   busyIconTimer.start();
               }
               busyIconTimer.stop();
               statusAnimationLabel.setIcon(idleIcon);
               progressBar.setValue(0);
               progressBar.setVisible(false);
               statusMessageLabel.setText(p_sMessage);
            }
        };
        SwingUtilities.invokeLater(DoStopProgress);
    }

    /*
    * Procedura jest uruchamiana za pomocą timer'a automatycznie na starcie programu
    * i może być uruchomiona (jeśli użytkownik pomylił się w pierwszy raz)
    * za pomocą menu.
    */
    private void showLogin() {
        // praca timer'a już nam nie potrzebna - timer swoją pracę już wykonał.
        // Procedura showLogin może być wykonywana za pomocą głownego menu,
        // dla tego wykonujemy stop timer'a tylo w przypadku kiedy on jest
        // uruchomiony
        if (loginTimer.isRunning()) {
            loginTimer.stop();
        }
        // Pokazujemy dialog logowania
        LoginDialog frame = new LoginDialog(CasAdminApp.getApplication().getMainFrame(), true);
        frame.setVisible(true);
        // Jeśli użytkownik wycofał sie z procedury logowania
        int retStatus = frame.getReturnStatus();
        if (retStatus == frame.RET_CANCEL) {
            menuBar.setVisible(false);    
            // Zamykamy aplikację.
                YPFunctions.applicationExit();
                // Zamykanie aplikacji zajmuje czas, dla tego powinniśmy
                // wycofać się z procedury
                return;
        }
            // Przygotowujemy tablicę do cache'owania
        GlobalData.v_allCustomers = new Vector();
        GlobalData.v_allDepots = new Vector();
        GlobalData.v_allUsers = new Vector();

        // udostępniamy cała funkcjonalność oprócz ponownego zalogowania
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            menuBar.getMenu(i).setEnabled(true);
            for (int j = 0; j < menuBar.getMenu(i).getItemCount(); j++) {
                menuBar.getMenu(i).getItem(j).setEnabled(true);
            }
        }
        miLogin.setEnabled(false);
        // błokujemy dostęp do niektórych punktów menu dla ID_GRANT >= 3 ( REPORT )
        if (Integer.parseInt(GlobalData.m_sGrant) > 2) {
            serverMenu.setEnabled(false);
            miNewClient.setEnabled(false);
            miDeleteClient.setEnabled(false);
            miSetSynchro.setEnabled(false);
            miSetActive.setEnabled(false);
            miChangePassword.setEnabled(false);
        }

        miImportPC.setEnabled(Integer.parseInt(GlobalData.m_sGrant) == 1);
        miSqlQuery.setEnabled(Integer.parseInt(GlobalData.m_sGrant) == 1);
        miDelShipment.setEnabled(Integer.parseInt(GlobalData.m_sGrant) == 1);

        String title = this.getFrame().getTitle() + " w. " + 
                org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap().getString(
                "Application.version") + " na " + GlobalData.m_sServerDisplayName + " [ " + YPOracleSession.oraUrl+" ]";
        this.getFrame().setTitle(title);
    }


    public void setGif(String gif) {
        if (! Constants.bShowAnimatedIcon)
          return;
        try {
           GlobalData.lastIcon = menuIcon.getIcon();
           menuIcon.setIcon(getContext().getResourceMap(this.getClass()).getIcon(gif));
        } catch (Exception e){}
    }

    public void restoreGif() {
        if (! Constants.bShowAnimatedIcon)
          return;
        menuIcon.setIcon(GlobalData.lastIcon);
    }




    @Action
    public void showAboutBox() {
        // Pokazujemy AboutBox
        YPFunctions.setAboutGif();
        if (aboutBox == null) {
            JFrame mainFrame = CasAdminApp.getApplication().getMainFrame();
            aboutBox = new CasAdminAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        CasAdminApp.getApplication().show(aboutBox);
        YPFunctions.restoreGif();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        menuIcon = new javax.swing.JMenu();
        javax.swing.JMenu adminMenu = new javax.swing.JMenu();
        miLogin = new javax.swing.JMenuItem();
        miMainForm = new javax.swing.JMenuItem();
        miChangePassword = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        serverMenu = new javax.swing.JMenu();
        miUsers = new javax.swing.JMenuItem();
        miDefaultPrices = new javax.swing.JMenuItem();
        miEditDefaultPrice = new javax.swing.JMenuItem();
        miTablesViews = new javax.swing.JMenuItem();
        miDepotExceptions = new javax.swing.JMenuItem();
        miImportPC = new javax.swing.JMenuItem();
        miSqlQuery = new javax.swing.JMenuItem();
        clientsMenu = new javax.swing.JMenu();
        miExistingClients = new javax.swing.JMenuItem();
        miNewClient = new javax.swing.JMenuItem();
        miDeleteClient = new javax.swing.JMenuItem();
        miSetSynchro = new javax.swing.JMenuItem();
        miSetActive = new javax.swing.JMenuItem();
        shipmentMenu = new javax.swing.JMenu();
        miClientsActivity = new javax.swing.JMenuItem();
        miWykazPrzesylek = new javax.swing.JMenuItem();
        miDelShipment = new javax.swing.JMenuItem();
        seekMenu = new javax.swing.JMenu();
        miSeekShipment = new javax.swing.JMenuItem();
        logMenu = new javax.swing.JMenu();
        miLogComm = new javax.swing.JMenuItem();
        miLogSynchro = new javax.swing.JMenuItem();
        miLog103 = new javax.swing.JMenuItem();
        miEnvironment = new javax.swing.JMenuItem();
        miMonitorComm = new javax.swing.JMenuItem();
        reportsMenu = new javax.swing.JMenu();
        miLastParcel = new javax.swing.JMenuItem();
        miShipmentsCount = new javax.swing.JMenuItem();
        miShipCountByIdCust = new javax.swing.JMenuItem();
        miGateCas = new javax.swing.JMenuItem();
        miGateCasErrors = new javax.swing.JMenuItem();
        administrowanieMenu = new javax.swing.JMenu();
        miNewVersion = new javax.swing.JMenuItem();
        miPropositions = new javax.swing.JMenuItem();
        miHistory = new javax.swing.JMenuItem();
        optionsMenu = new javax.swing.JMenu();
        miSettings = new javax.swing.JMenuItem();
        javax.swing.JMenu aboutMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getResourceMap(CasAdminView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.LINE_AXIS));

        menuBar.setBackground(resourceMap.getColor("menuBar.background")); // NOI18N
        menuBar.setName("menuBar"); // NOI18N

        menuIcon.setBackground(resourceMap.getColor("menuIcon.background")); // NOI18N
        menuIcon.setText(resourceMap.getString("menuIcon.text")); // NOI18N
        menuIcon.setName("menuIcon"); // NOI18N
        menuBar.add(menuIcon);

        adminMenu.setBackground(resourceMap.getColor("adminMenu.background")); // NOI18N
        adminMenu.setText(resourceMap.getString("adminMenu.text")); // NOI18N
        adminMenu.setName("adminMenu"); // NOI18N

        miLogin.setBackground(resourceMap.getColor("miLogin.background")); // NOI18N
        miLogin.setText(resourceMap.getString("miLogin.text")); // NOI18N
        miLogin.setName("miLogin"); // NOI18N
        miLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLoginActionPerformed(evt);
            }
        });
        adminMenu.add(miLogin);

        miMainForm.setBackground(resourceMap.getColor("miMainForm.background")); // NOI18N
        miMainForm.setText(resourceMap.getString("miMainForm.text")); // NOI18N
        miMainForm.setName("miMainForm"); // NOI18N
        miMainForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMainFormActionPerformed(evt);
            }
        });
        adminMenu.add(miMainForm);

        miChangePassword.setBackground(resourceMap.getColor("miChangePassword.background")); // NOI18N
        miChangePassword.setText(resourceMap.getString("miChangePassword.text")); // NOI18N
        miChangePassword.setName("miChangePassword"); // NOI18N
        miChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miChangePasswordActionPerformed(evt);
            }
        });
        adminMenu.add(miChangePassword);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(casadmin.CasAdminApp.class).getContext().getActionMap(CasAdminView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setBackground(resourceMap.getColor("exitMenuItem.background")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        adminMenu.add(exitMenuItem);

        menuBar.add(adminMenu);

        serverMenu.setBackground(resourceMap.getColor("serverMenu.background")); // NOI18N
        serverMenu.setText(resourceMap.getString("serverMenu.text")); // NOI18N
        serverMenu.setName("serverMenu"); // NOI18N

        miUsers.setBackground(resourceMap.getColor("miUsers.background")); // NOI18N
        miUsers.setText(resourceMap.getString("miUsers.text")); // NOI18N
        miUsers.setName("miUsers"); // NOI18N
        miUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miUsersActionPerformed(evt);
            }
        });
        serverMenu.add(miUsers);

        miDefaultPrices.setBackground(resourceMap.getColor("miDefaultPrices.background")); // NOI18N
        miDefaultPrices.setText(resourceMap.getString("miDefaultPrices.text")); // NOI18N
        miDefaultPrices.setName("miDefaultPrices"); // NOI18N
        miDefaultPrices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miDefaultPricesActionPerformed(evt);
            }
        });
        serverMenu.add(miDefaultPrices);

        miEditDefaultPrice.setBackground(resourceMap.getColor("miEditDefaultPrice.background")); // NOI18N
        miEditDefaultPrice.setText(resourceMap.getString("miEditDefaultPrice.text")); // NOI18N
        miEditDefaultPrice.setName("miEditDefaultPrice"); // NOI18N
        miEditDefaultPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miEditDefaultPriceActionPerformed(evt);
            }
        });
        serverMenu.add(miEditDefaultPrice);

        miTablesViews.setBackground(resourceMap.getColor("miTablesViews.background")); // NOI18N
        miTablesViews.setText(resourceMap.getString("miTablesViews.text")); // NOI18N
        miTablesViews.setName("miTablesViews"); // NOI18N
        miTablesViews.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miTablesViewsActionPerformed(evt);
            }
        });
        serverMenu.add(miTablesViews);

        miDepotExceptions.setBackground(resourceMap.getColor("miDepotExceptions.background")); // NOI18N
        miDepotExceptions.setText(resourceMap.getString("miDepotExceptions.text")); // NOI18N
        miDepotExceptions.setName("miDepotExceptions"); // NOI18N
        miDepotExceptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miDepotExceptionsActionPerformed(evt);
            }
        });
        serverMenu.add(miDepotExceptions);

        miImportPC.setBackground(resourceMap.getColor("miImportPC.background")); // NOI18N
        miImportPC.setText(resourceMap.getString("miImportPC.text")); // NOI18N
        miImportPC.setName("miImportPC"); // NOI18N
        miImportPC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miImportPCActionPerformed(evt);
            }
        });
        serverMenu.add(miImportPC);

        miSqlQuery.setBackground(resourceMap.getColor("miSqlQuery.background")); // NOI18N
        miSqlQuery.setText(resourceMap.getString("miSqlQuery.text")); // NOI18N
        miSqlQuery.setName("miSqlQuery"); // NOI18N
        miSqlQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSqlQueryActionPerformed(evt);
            }
        });
        serverMenu.add(miSqlQuery);

        menuBar.add(serverMenu);

        clientsMenu.setBackground(resourceMap.getColor("clientsMenu.background")); // NOI18N
        clientsMenu.setText(resourceMap.getString("clientsMenu.text")); // NOI18N
        clientsMenu.setName("clientsMenu"); // NOI18N

        miExistingClients.setBackground(resourceMap.getColor("miExistingClients.background")); // NOI18N
        miExistingClients.setText(resourceMap.getString("miExistingClients.text")); // NOI18N
        miExistingClients.setName("miExistingClients"); // NOI18N
        miExistingClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExistingClientsActionPerformed(evt);
            }
        });
        clientsMenu.add(miExistingClients);

        miNewClient.setBackground(resourceMap.getColor("miNewClient.background")); // NOI18N
        miNewClient.setText(resourceMap.getString("miNewClient.text")); // NOI18N
        miNewClient.setName("miNewClient"); // NOI18N
        miNewClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miNewClientActionPerformed(evt);
            }
        });
        clientsMenu.add(miNewClient);

        miDeleteClient.setBackground(resourceMap.getColor("miDeleteClient.background")); // NOI18N
        miDeleteClient.setText(resourceMap.getString("miDeleteClient.text")); // NOI18N
        miDeleteClient.setName("miDeleteClient"); // NOI18N
        miDeleteClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miDeleteClientActionPerformed(evt);
            }
        });
        clientsMenu.add(miDeleteClient);

        miSetSynchro.setBackground(resourceMap.getColor("miSetSynchro.background")); // NOI18N
        miSetSynchro.setText(resourceMap.getString("miSetSynchro.text")); // NOI18N
        miSetSynchro.setName("miSetSynchro"); // NOI18N
        miSetSynchro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSetSynchroActionPerformed(evt);
            }
        });
        clientsMenu.add(miSetSynchro);

        miSetActive.setBackground(resourceMap.getColor("miSetActive.background")); // NOI18N
        miSetActive.setText(resourceMap.getString("miSetActive.text")); // NOI18N
        miSetActive.setAutoscrolls(true);
        miSetActive.setName("miSetActive"); // NOI18N
        miSetActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSetActiveActionPerformed(evt);
            }
        });
        clientsMenu.add(miSetActive);

        menuBar.add(clientsMenu);

        shipmentMenu.setBackground(resourceMap.getColor("shipmentMenu.background")); // NOI18N
        shipmentMenu.setText(resourceMap.getString("shipmentMenu.text")); // NOI18N
        shipmentMenu.setName("shipmentMenu"); // NOI18N

        miClientsActivity.setBackground(resourceMap.getColor("miClientsActivity.background")); // NOI18N
        miClientsActivity.setText(resourceMap.getString("miClientsActivity.text")); // NOI18N
        miClientsActivity.setName("miClientsActivity"); // NOI18N
        miClientsActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miClientsActivityActionPerformed(evt);
            }
        });
        shipmentMenu.add(miClientsActivity);

        miWykazPrzesylek.setBackground(resourceMap.getColor("miWykazPrzesylek.background")); // NOI18N
        miWykazPrzesylek.setText(resourceMap.getString("miWykazPrzesylek.text")); // NOI18N
        miWykazPrzesylek.setName("miWykazPrzesylek"); // NOI18N
        miWykazPrzesylek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miWykazPrzesylekActionPerformed(evt);
            }
        });
        shipmentMenu.add(miWykazPrzesylek);

        miDelShipment.setBackground(resourceMap.getColor("miDelShipment.background")); // NOI18N
        miDelShipment.setText(resourceMap.getString("miDelShipment.text")); // NOI18N
        miDelShipment.setName("miDelShipment"); // NOI18N
        miDelShipment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miDelShipmentActionPerformed(evt);
            }
        });
        shipmentMenu.add(miDelShipment);

        menuBar.add(shipmentMenu);

        seekMenu.setBackground(resourceMap.getColor("seekMenu.background")); // NOI18N
        seekMenu.setText(resourceMap.getString("seekMenu.text")); // NOI18N
        seekMenu.setName("seekMenu"); // NOI18N

        miSeekShipment.setBackground(resourceMap.getColor("miSeekShipment.background")); // NOI18N
        miSeekShipment.setText(resourceMap.getString("miSeekShipment.text")); // NOI18N
        miSeekShipment.setName("miSeekShipment"); // NOI18N
        miSeekShipment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSeekShipmentActionPerformed(evt);
            }
        });
        seekMenu.add(miSeekShipment);

        menuBar.add(seekMenu);

        logMenu.setBackground(resourceMap.getColor("logMenu.background")); // NOI18N
        logMenu.setText(resourceMap.getString("logMenu.text")); // NOI18N
        logMenu.setName("logMenu"); // NOI18N

        miLogComm.setBackground(resourceMap.getColor("miLogComm.background")); // NOI18N
        miLogComm.setText(resourceMap.getString("miLogComm.text")); // NOI18N
        miLogComm.setName("miLogComm"); // NOI18N
        miLogComm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLogCommActionPerformed(evt);
            }
        });
        logMenu.add(miLogComm);

        miLogSynchro.setBackground(resourceMap.getColor("miLogSynchro.background")); // NOI18N
        miLogSynchro.setText(resourceMap.getString("miLogSynchro.text")); // NOI18N
        miLogSynchro.setName("miLogSynchro"); // NOI18N
        miLogSynchro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLogSynchroActionPerformed(evt);
            }
        });
        logMenu.add(miLogSynchro);

        miLog103.setBackground(resourceMap.getColor("miLog103.background")); // NOI18N
        miLog103.setText(resourceMap.getString("miLog103.text")); // NOI18N
        miLog103.setName("miLog103"); // NOI18N
        miLog103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLog103ActionPerformed(evt);
            }
        });
        logMenu.add(miLog103);

        miEnvironment.setBackground(resourceMap.getColor("miEnvironment.background")); // NOI18N
        miEnvironment.setText(resourceMap.getString("miEnvironment.text")); // NOI18N
        miEnvironment.setName("miEnvironment"); // NOI18N
        miEnvironment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miEnvironmentActionPerformed(evt);
            }
        });
        logMenu.add(miEnvironment);

        miMonitorComm.setBackground(resourceMap.getColor("miMonitorComm.background")); // NOI18N
        miMonitorComm.setText(resourceMap.getString("miMonitorComm.text")); // NOI18N
        miMonitorComm.setName("miMonitorComm"); // NOI18N
        miMonitorComm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMonitorCommActionPerformed(evt);
            }
        });
        logMenu.add(miMonitorComm);

        menuBar.add(logMenu);

        reportsMenu.setBackground(resourceMap.getColor("reportsMenu.background")); // NOI18N
        reportsMenu.setText(resourceMap.getString("reportsMenu.text")); // NOI18N
        reportsMenu.setName("reportsMenu"); // NOI18N

        miLastParcel.setBackground(resourceMap.getColor("miLastParcel.background")); // NOI18N
        miLastParcel.setText(resourceMap.getString("miLastParcel.text")); // NOI18N
        miLastParcel.setName("miLastParcel"); // NOI18N
        miLastParcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLastParcelActionPerformed(evt);
            }
        });
        reportsMenu.add(miLastParcel);

        miShipmentsCount.setBackground(resourceMap.getColor("miShipmentsCount.background")); // NOI18N
        miShipmentsCount.setText(resourceMap.getString("miShipmentsCount.text")); // NOI18N
        miShipmentsCount.setName("miShipmentsCount"); // NOI18N
        miShipmentsCount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miShipmentsCountActionPerformed(evt);
            }
        });
        reportsMenu.add(miShipmentsCount);

        miShipCountByIdCust.setBackground(resourceMap.getColor("miShipCountByIdCust.background")); // NOI18N
        miShipCountByIdCust.setText(resourceMap.getString("miShipCountByIdCust.text")); // NOI18N
        miShipCountByIdCust.setName("miShipCountByIdCust"); // NOI18N
        miShipCountByIdCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miShipCountByIdCustActionPerformed(evt);
            }
        });
        reportsMenu.add(miShipCountByIdCust);

        miGateCas.setBackground(resourceMap.getColor("miGateCas.background")); // NOI18N
        miGateCas.setActionCommand(resourceMap.getString("miGateCas.actionCommand")); // NOI18N
        miGateCas.setLabel(resourceMap.getString("miGateCas.label")); // NOI18N
        miGateCas.setName("miGateCas"); // NOI18N
        miGateCas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miGateCasActionPerformed(evt);
            }
        });
        reportsMenu.add(miGateCas);
        miGateCas.getAccessibleContext().setAccessibleName(resourceMap.getString("miGateCas.AccessibleContext.accessibleName")); // NOI18N

        miGateCasErrors.setBackground(resourceMap.getColor("miGateCasErrors.background")); // NOI18N
        miGateCasErrors.setText(resourceMap.getString("miGateCasErrors.text")); // NOI18N
        miGateCasErrors.setName("miGateCasErrors"); // NOI18N
        miGateCasErrors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miGateCasErrorsActionPerformed(evt);
            }
        });
        reportsMenu.add(miGateCasErrors);

        menuBar.add(reportsMenu);

        administrowanieMenu.setBackground(resourceMap.getColor("administrowanieMenu.background")); // NOI18N
        administrowanieMenu.setText(resourceMap.getString("administrowanieMenu.text")); // NOI18N
        administrowanieMenu.setName("administrowanieMenu"); // NOI18N

        miNewVersion.setBackground(resourceMap.getColor("miNewVersion.background")); // NOI18N
        miNewVersion.setText(resourceMap.getString("miNewVersion.text")); // NOI18N
        miNewVersion.setName("miNewVersion"); // NOI18N
        miNewVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miNewVersionActionPerformed(evt);
            }
        });
        administrowanieMenu.add(miNewVersion);

        miPropositions.setBackground(resourceMap.getColor("miPropositions.background")); // NOI18N
        miPropositions.setText(resourceMap.getString("miPropositions.text")); // NOI18N
        miPropositions.setName("miPropositions"); // NOI18N
        miPropositions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPropositionsActionPerformed(evt);
            }
        });
        administrowanieMenu.add(miPropositions);

        miHistory.setBackground(resourceMap.getColor("miHistory.background")); // NOI18N
        miHistory.setText(resourceMap.getString("miHistory.text")); // NOI18N
        miHistory.setAutoscrolls(true);
        miHistory.setName("miHistory"); // NOI18N
        miHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miHistoryActionPerformed(evt);
            }
        });
        administrowanieMenu.add(miHistory);

        menuBar.add(administrowanieMenu);

        optionsMenu.setBackground(resourceMap.getColor("optionsMenu.background")); // NOI18N
        optionsMenu.setText(resourceMap.getString("optionsMenu.text")); // NOI18N
        optionsMenu.setName("optionsMenu"); // NOI18N

        miSettings.setBackground(resourceMap.getColor("miSettings.background")); // NOI18N
        miSettings.setText(resourceMap.getString("miSettings.text")); // NOI18N
        miSettings.setName("miSettings"); // NOI18N
        optionsMenu.add(miSettings);

        menuBar.add(optionsMenu);

        aboutMenu.setBackground(resourceMap.getColor("aboutMenu.background")); // NOI18N
        aboutMenu.setText(resourceMap.getString("aboutMenu.text")); // NOI18N
        aboutMenu.setName("aboutMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setBackground(resourceMap.getColor("aboutMenuItem.background")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        aboutMenu.add(aboutMenuItem);

        menuBar.add(aboutMenu);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(466, 30));

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 355, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

     /**
     * Procedura służy do pokazywania panelu klasy JPanel
     * w miejscu mainPanel
     */
    public void showPanel(javax.swing.JPanel p, Constants.gifStyle gs) {
        // Bieżąca widoczna panel jest przechowywana w zmiennej
        // currentPanel. żeby pokazać nowy objekt klasy JPanel,
        // musimy najpierw usunąć z mainPanel bieżącą panel.
        boolean bFirstStart = currentPanel == null;
        if (currentPanel != null) {
            mainPanel.remove(currentPanel);
            currentPanel = null;
        }
        currentPanel = p;
        mainPanel.add(currentPanel);

//        JOptionPane.showMessageDialog(null,p.getClass().toString());

        
//        mainPanel.repaint(); - ???
        // Metoda this.getFrame().repaint() dla czegoś nie przerysowuje
        // nowej zawartości panelu mainPanel. Nie wiem w czym sprawa,
        // ale udało się mnie zrobić tak :

        Dimension d = this.getFrame().getSize();
        d.height--;
        this.getFrame().setSize(d);
        d.height++;
        this.getFrame().setSize(d);

        if (! bFirstStart) {
            switch (gs){
                case about    :  YPFunctions.setAboutGif();    break;
                case wait     :  YPFunctions.setWaitGif();     break;
                case edit     :  YPFunctions.setEditGif();     break;
                case bigerror :  YPFunctions.setBigErrorGif(); break;
                case error    :  YPFunctions.setErrorGif();    break;
                case hello    :  YPFunctions.setHelloGif();    break;
                case ok       :  YPFunctions.setOkGif();       break;
                case process  :  YPFunctions.setProcessGif();  break;
                case read     :  YPFunctions.setReadGif();     break;
                case talk     :  YPFunctions.setTalkGif();     break;
                default       :  YPFunctions.setWaitGif();     break;
            }
        }
 }

private void miNewClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miNewClientActionPerformed
    // Wybrany punkt menu "Dodaj klienta"
    // Proponujemy wprowadzić numer SAP klienta, którego chcemy założyć jako nowego klienta CAS
    String sap = YPFunctions.showInputDialog("Wprowadż numer SAP nowego klienta", "Numer SAP klienta");
    // Numer SAP powinien być 7-znakowy numeryczny. Sprawdzamy
    if ((sap == null) || (sap.length() != 7) || (!YPFunctions.isInteger(sap))) {
        YPFunctions.showErrorMessage("Nie prawidłowy numer SAP");
//        JOptionPane.showMessageDialog(null, "Nie prawidłowy numer SAP", "Błąd", JOptionPane.ERROR_MESSAGE);
        return;
    }
    // Teraz sprawdzamy w bazie danych. Funkcja Search_sap zwraca 0 jeśli istnieje
    // klient z takim numerem SAP lub kod błędu.
    Integer result = ((BigDecimal) GlobalData.oraSession.executeFunction(
            "MGR_DEFINE.SEARCH_SAP(?)",
            new Vector(Arrays.asList(Integer.parseInt(sap))),
            OracleTypes.NUMBER)).intValue();
    if (result != 0) {
        // Brak takiego numeru SAP w bazie danych
        YPFunctions.showErrorMessage("Brak numeru SAP w bazie danych");
//        JOptionPane.showMessageDialog(null, "Brak numeru SAP w bazie danych", "Błąd", JOptionPane.ERROR_MESSAGE);
        return;
    }
    // Pokazujemy panel dla zakładania nowego klienta CAS, przekazująć konstruktoru numer SAP.
    showPanel(new NewUserPanel(sap),Constants.gifStyle.edit);
}//GEN-LAST:event_miNewClientActionPerformed

private void miMainFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMainFormActionPerformed
     // Wybrany punkt menu "Strona główna"
    showPanel(new HelloPanel(),Constants.gifStyle.wait);
}//GEN-LAST:event_miMainFormActionPerformed

private void miLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLoginActionPerformed
     // Wybrany punkt menu "Zaloguj się"
    showLogin();
}//GEN-LAST:event_miLoginActionPerformed

private void miWykazPrzesylekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miWykazPrzesylekActionPerformed
    // Wybrany punkt menu "Wykaz przesyłek"
    showPanel(new ShipmentsPanel(),Constants.gifStyle.wait);
}//GEN-LAST:event_miWykazPrzesylekActionPerformed

private void miClientsActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miClientsActivityActionPerformed
    // Wybrany punkt menu "Aktywność klientów"
    showPanel(new ShipmentActivityPanel(),Constants.gifStyle.wait);
}//GEN-LAST:event_miClientsActivityActionPerformed

private void miDeleteClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miDeleteClientActionPerformed
    // Wybrany punkt menu "Usuń klienta"
    // Proponujemy zaznaczyć klientów do usuwania z bazy danych
    SelectCustomerDialog frame = new SelectCustomerDialog(null, true, false, "","");
    frame.setTitle("Zaznacz ID_CUST klientów, którzycz chcesz usunąć");
    frame.setVisible(true);
    String deletedIdCust = ((SelectCustomerDialog) frame).selectedID_CUST;
    if (deletedIdCust.isEmpty()) {
        // Nie zaznaczono żadnych klientów, wychodzimy z metody
        return;
    }
    // Wymagamy potwierdzenia
    if (javax.swing.JOptionPane.YES_OPTION == YPFunctions.showQuestionMessage(
            "Czy naprawdę chcesz usunąć dane klientów \n" +
            deletedIdCust +
            " ?",
            "Usuwanie klientów CAS !!!")) {
        // Pokazujemy kursor oczekiwania
        this.getFrame().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        // Lista klientów do usunięcia sawarta w stringu deletedIdCust
        // ma wygląd podobny do "2435,4521,1098", dla tego rozdzielamy 
        // na "tokeny"
        Vector<String> tokens = YPFunctions.getTokensAsVector(deletedIdCust, ",");
        // Dla każdego klienta osobno uruchamiamy proceduru Oracle DELETE_CUST.
        for (int i = 0; i < tokens.size(); i++) {
            // Ilość parametrów procedury = 1, będzie to kolejny "token"
            Vector pars = new Vector(Arrays.asList(Integer.parseInt(tokens.elementAt(i))));
            // Wykonanie procedury
            GlobalData.oraSession.executeProcedure(
                    "MGR_DEFINE.DELETE_CUST(?)", pars);
        }
        // Przywracamy domyślny kursor
        this.getFrame().setCursor(java.awt.Cursor.getDefaultCursor());
        // Komunikat o wykonaniu
        YPFunctions.showMessage("Klienci\n" + deletedIdCust + "\nzostały usunięte", "Usuwanie klientów");
    }
}//GEN-LAST:event_miDeleteClientActionPerformed

private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
    // Wybrany punkt menu "Koniec"
    YPFunctions.applicationExit();
}//GEN-LAST:event_exitMenuItemActionPerformed

private void miSeekShipmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSeekShipmentActionPerformed
    // Wybrany punkt menu "Szukaj"->"Przesyłka lub paczka"
    // Proponujemy wprowadzić ciąg znaków dla wyszukiwania
    String ret = YPFunctions.showInputDialog("Wprowadż numer przesyłki lub paczki do przeszukiwania", "Przesyłka lub paczka");
    // Sprawdzamy poprawność
    if ((ret == null) || (ret.length() < 9) || (ret.length() > 35)) {
        return;
    }
    // Przechodzimy do dużych liter
    ret = ret.toUpperCase();
    String query;
    if (ret.length() <= 11) {
        // Wprowadzony ciąg może być tylko numerem przesyłki
        // Szukamy po tabeli Data_shipments
        query = "select id_cust,no_no from data_shipments where no_no=? and is_deleted='N'";
    } else {
        // Inaczej jest to numer paczki
        if (ret.substring(0, 2).equals("JJ")) {
            // Jeśli pierwsze 2 znaki - to "JJ" - usuwamy jeden znak - tak trzeba
            ret = ret.substring(1);
        }
        // Szukamy po tabeli data_shipments używająć wyszukiwania po tabeli
        // data_parcels
        query = "select id_cust, no_no from data_shipments where no_no = " +
                "(select no_no from data_parcels where no_parcel=?) and is_deleted='N'";
    }
    // Właśnie wyszukiwanie
    Vector<String> v =
                GlobalData.oraSession.selectFirstRecordToVector(query,
                                         new Vector(Arrays.asList(ret)));
/*
    if (v.isEmpty())
    {
        // nie znaleziono
        YPFunctions.showErrorMessage("Brak przesyłki lub paczki w bazie danych eCas");
        return;
    }
    YPFunctions.showPanelAsModalDialog(new ShipmentsPanel(v.elementAt(0), v.elementAt(1)), "Przegląd danych o przesyłkach",
                                                   new Dimension(1017,700));
 */ 
    if (!v.isEmpty())
    {
       YPFunctions.showPanelAsModalDialog(new ShipmentsPanel(v.elementAt(0), v.elementAt(1)), "Przegląd danych o przesyłkach",
                                                   new Dimension(1017,700));
    }
    else
    {
        if (GlobalData.returnsStarted)
        {
            int recCount = Integer.parseInt(GlobalData.oraSession.selectString("Select count(*) from wspolne_dane_terminali.RETURN_SHIPMENTS where no_no=?",
                        new Vector(Arrays.asList(ret)), "0"));
            if (recCount > 0)            
               YPFunctions.showPanelAsModalDialog(new ReturnsPanel(ret),
                                               "Przegląd danych o zwrotach",
                                               new Dimension(1017,400));
            else
               YPFunctions.showErrorMessage("Brak przesyłki lub paczki w bazie danych eCas");
        }
    }
}//GEN-LAST:event_miSeekShipmentActionPerformed

private void miChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miChangePasswordActionPerformed
    showPanel(new CasAdminLogPanel(),Constants.gifStyle.read);

}//GEN-LAST:event_miChangePasswordActionPerformed

private void miDefaultPricesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miDefaultPricesActionPerformed
    // Wybrany punkt menu "Słowniki/Cenniki domyślne"
    showPanel(new StandardPricesPanel(),Constants.gifStyle.read);
}//GEN-LAST:event_miDefaultPricesActionPerformed

private void miUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miUsersActionPerformed
      // Wybrany punkt menu "Użytkownycy systemowe"
      showPanel(new SystemUsersPanel(),Constants.gifStyle.read);
}//GEN-LAST:event_miUsersActionPerformed

private void miExistingClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExistingClientsActionPerformed
     // Wybrany punkt menu "Istnijące klienci"
    showPanel(new UsersPanel(),Constants.gifStyle.wait);
}//GEN-LAST:event_miExistingClientsActionPerformed

private void miLogCommActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLogCommActionPerformed
    showPanel(new ShowLog("comm"),Constants.gifStyle.read);
}//GEN-LAST:event_miLogCommActionPerformed

private void miLogSynchroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLogSynchroActionPerformed
    showPanel(new ShowLog("synchro"),Constants.gifStyle.read);
}//GEN-LAST:event_miLogSynchroActionPerformed

private void miLog103ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLog103ActionPerformed
    showPanel(new ShowLog("err103"),Constants.gifStyle.read);
}//GEN-LAST:event_miLog103ActionPerformed

private void miEnvironmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEnvironmentActionPerformed
    showPanel(new ShowLog("env"),Constants.gifStyle.read);
}//GEN-LAST:event_miEnvironmentActionPerformed

private void miMonitorCommActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMonitorCommActionPerformed
    showPanel(new ShowLog("monitor"),Constants.gifStyle.read);
}//GEN-LAST:event_miMonitorCommActionPerformed

private void miDepotExceptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miDepotExceptionsActionPerformed
       showPanel(new ReAddressPanel(),Constants.gifStyle.edit);
}//GEN-LAST:event_miDepotExceptionsActionPerformed

private void miTablesViewsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTablesViewsActionPerformed
       showPanel(new CasTablesPanel(),Constants.gifStyle.read);
}//GEN-LAST:event_miTablesViewsActionPerformed

private void miSetSynchroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSetSynchroActionPerformed
       showPanel(new SetSynchroPanel(),Constants.gifStyle.edit);
}//GEN-LAST:event_miSetSynchroActionPerformed

private void miSetActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSetActiveActionPerformed
       showPanel(new SetActivationPanel(),Constants.gifStyle.edit);
}//GEN-LAST:event_miSetActiveActionPerformed

private void miDelShipmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miDelShipmentActionPerformed
       showPanel(new DelShipmentPanel(),Constants.gifStyle.edit);
}//GEN-LAST:event_miDelShipmentActionPerformed

private void miNewVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miNewVersionActionPerformed
      showPanel(new UpdateVersionPanel(),Constants.gifStyle.wait);
}//GEN-LAST:event_miNewVersionActionPerformed

private void miHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miHistoryActionPerformed
    showPanel(new HistoryPanel(),Constants.gifStyle.talk);
}//GEN-LAST:event_miHistoryActionPerformed

private void miPropositionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPropositionsActionPerformed
    showPanel(new PropositionsPanel(),Constants.gifStyle.talk);
}//GEN-LAST:event_miPropositionsActionPerformed

private void miEditDefaultPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEditDefaultPriceActionPerformed
    showPanel(new EditStandardPricePanel(),Constants.gifStyle.edit);
}//GEN-LAST:event_miEditDefaultPriceActionPerformed

private void miLastParcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLastParcelActionPerformed
    showPanel(new ReportLastParcelPanel(),Constants.gifStyle.edit);
}//GEN-LAST:event_miLastParcelActionPerformed

private void miShipmentsCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miShipmentsCountActionPerformed
    showPanel(new ReportShipmentsCountPanel(),Constants.gifStyle.edit);
}//GEN-LAST:event_miShipmentsCountActionPerformed

private void miShipCountByIdCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miShipCountByIdCustActionPerformed
    showPanel(new ReportCountByIdCustPanel(),Constants.gifStyle.edit);

}//GEN-LAST:event_miShipCountByIdCustActionPerformed

private void miImportPCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miImportPCActionPerformed
    showPanel(new ImportPcPanel(), Constants.gifStyle.process);
}//GEN-LAST:event_miImportPCActionPerformed

private void miSqlQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSqlQueryActionPerformed
    showPanel(new SqlQueriesPanel(), Constants.gifStyle.wait);
}//GEN-LAST:event_miSqlQueryActionPerformed

private void miGateCasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miGateCasActionPerformed
    showPanel(new ReportGatewayPanel(), Constants.gifStyle.talk);
}//GEN-LAST:event_miGateCasActionPerformed

private void miGateCasErrorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miGateCasErrorsActionPerformed
    showPanel(new ReportGatewayErrorsPanel(), Constants.gifStyle.talk);
}//GEN-LAST:event_miGateCasErrorsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu administrowanieMenu;
    private javax.swing.JMenu clientsMenu;
    private javax.swing.JMenu logMenu;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuIcon;
    private javax.swing.JMenuItem miChangePassword;
    private javax.swing.JMenuItem miClientsActivity;
    private javax.swing.JMenuItem miDefaultPrices;
    private javax.swing.JMenuItem miDelShipment;
    private javax.swing.JMenuItem miDeleteClient;
    private javax.swing.JMenuItem miDepotExceptions;
    private javax.swing.JMenuItem miEditDefaultPrice;
    private javax.swing.JMenuItem miEnvironment;
    private javax.swing.JMenuItem miExistingClients;
    private javax.swing.JMenuItem miGateCas;
    private javax.swing.JMenuItem miGateCasErrors;
    private javax.swing.JMenuItem miHistory;
    private javax.swing.JMenuItem miImportPC;
    private javax.swing.JMenuItem miLastParcel;
    private javax.swing.JMenuItem miLog103;
    private javax.swing.JMenuItem miLogComm;
    private javax.swing.JMenuItem miLogSynchro;
    private javax.swing.JMenuItem miLogin;
    private javax.swing.JMenuItem miMainForm;
    private javax.swing.JMenuItem miMonitorComm;
    private javax.swing.JMenuItem miNewClient;
    private javax.swing.JMenuItem miNewVersion;
    private javax.swing.JMenuItem miPropositions;
    private javax.swing.JMenuItem miSeekShipment;
    private javax.swing.JMenuItem miSetActive;
    private javax.swing.JMenuItem miSetSynchro;
    private javax.swing.JMenuItem miSettings;
    private javax.swing.JMenuItem miShipCountByIdCust;
    private javax.swing.JMenuItem miShipmentsCount;
    private javax.swing.JMenuItem miSqlQuery;
    private javax.swing.JMenuItem miTablesViews;
    private javax.swing.JMenuItem miUsers;
    private javax.swing.JMenuItem miWykazPrzesylek;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenu reportsMenu;
    private javax.swing.JMenu seekMenu;
    private javax.swing.JMenu serverMenu;
    private javax.swing.JMenu shipmentMenu;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    
    private javax.swing.JPanel currentPanel;
    private final Timer loginTimer;
    private JDialog aboutBox;

}
