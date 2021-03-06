/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.Movie;
import hr.algebra.model.User;
import hr.algebra.parsers.rss.MovieParser;
import hr.algebra.utils.MessageUtils;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.SwingWorker;

/**
 *
 * @author bisiv
 */
public class AdminUI extends javax.swing.JFrame {

    private Repository repository;
    private DefaultListModel<Movie> movieModel;

    private final JDialog loadingDialog = new JDialog(this, false);
    private static final String LOADING_GIF_PATH = "assets/graphics/loading.gif";

    private static final String DIR = "assets";
    private static User USER;

    /**
     * Creates new form AdminUI
     *
     * @param user
     */
    public AdminUI(User user) {
        AdminUI.USER = user;

        initComponents();
        init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        lsMovies = new javax.swing.JList<>();
        btnUpload = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmiAddAdmin = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jmiLogout = new javax.swing.JMenuItem();
        jmiExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jmiEditProfile = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SneakPeekAdmin");
        setPreferredSize(new java.awt.Dimension(1200, 780));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setViewportView(lsMovies);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1184, 670));

        btnUpload.setText("Upload");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });
        getContentPane().add(btnUpload, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 680, 120, 30));

        btnClear.setBackground(new java.awt.Color(255, 0, 0));
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        getContentPane().add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 680, 120, 30));

        jMenu1.setText("File");

        jmiAddAdmin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jmiAddAdmin.setText("Add admin");
        jmiAddAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAddAdminActionPerformed(evt);
            }
        });
        jMenu1.add(jmiAddAdmin);
        jMenu1.add(jSeparator1);

        jmiLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jmiLogout.setText("Logout");
        jmiLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiLogoutActionPerformed(evt);
            }
        });
        jMenu1.add(jmiLogout);

        jmiExit.setText("Exit");
        jmiExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiExitActionPerformed(evt);
            }
        });
        jMenu1.add(jmiExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jmiEditProfile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jmiEditProfile.setText("Edit profile");
        jmiEditProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiEditProfileActionPerformed(evt);
            }
        });
        jMenu2.add(jmiEditProfile);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jmiLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiLogoutActionPerformed

        new Login().setVisible(true);
        this.dispose();

    }//GEN-LAST:event_jmiLogoutActionPerformed

    private void jmiExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_jmiExitActionPerformed

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed

        startThread();

    }//GEN-LAST:event_btnUploadActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed

        try {
            repository.clearDatabase();

            File file = new File(DIR);

            for (File f : file.listFiles()) {
                if (!f.isDirectory()) {
                    f.delete();
                }
            }

            loadModel();
        } catch (Exception ex) {
            MessageUtils.showErrorMessage("Error", "Cannot clear database");
            Logger.getLogger(AdminUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnClearActionPerformed

    private void jmiAddAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAddAdminActionPerformed
        new RegisterDialog(this, true, true).setVisible(true);
    }//GEN-LAST:event_jmiAddAdminActionPerformed

    private void jmiEditProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiEditProfileActionPerformed
        new ProfileDialog(this, true, USER).setVisible(true);
    }//GEN-LAST:event_jmiEditProfileActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new AdminUI(USER).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnUpload;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuItem jmiAddAdmin;
    private javax.swing.JMenuItem jmiEditProfile;
    private javax.swing.JMenuItem jmiExit;
    private javax.swing.JMenuItem jmiLogout;
    private javax.swing.JList<Movie> lsMovies;
    // End of variables declaration//GEN-END:variables

    private void init() {
        try {
            repository = RepositoryFactory.getRepository();
            movieModel = new DefaultListModel<>();
            loadModel();
        } catch (Exception e) {
            MessageUtils.showErrorMessage("Unrecovarable error", "Cannot initiate the admin form");
        }
    }

    private void loadModel() throws Exception {
        List<Movie> movies = repository.selectMovies();
        movieModel.clear();
        movies.forEach(m -> movieModel.addElement(m));
        lsMovies.setModel(movieModel);
    }

    private void startThread() {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                btnUpload.setEnabled(false);
                createLoadingAnimation();
                uploadMovies();
                return null;
            }

            ;

            @Override
            protected void done() {
                try {
                    btnUpload.setEnabled(true);
                    loadingDialog.dispose();
                    loadModel();
                } catch (Exception ex) {
                    Logger.getLogger(AdminUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };

        worker.execute();
    }

    private void uploadMovies() {
        try {
            List<Movie> movies = MovieParser.parse();
            repository.createMovies(movies);
        } catch (Exception ex) {
            Logger.getLogger(AdminUI.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to upload movies");
        }
    }

    private void createLoadingAnimation() {

        ImageIcon loadingAnimation = new ImageIcon(LOADING_GIF_PATH);

        JLabel loading = new JLabel(loadingAnimation, JLabel.CENTER);

        loadingDialog.add(loading);
        loadingDialog.setSize(this.getWidth() - 20, this.getHeight() - 110);
        loadingDialog.setLocationRelativeTo(null);

        loadingDialog.setUndecorated(true);
        loadingDialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        loadingDialog.setOpacity(0.6f);

        loadingDialog.setVisible(true);

    }

    public void setUser(User user) {
        USER = user;
    }
}
