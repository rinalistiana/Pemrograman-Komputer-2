/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Notebook
 */
public class PMDialog extends javax.swing.JDialog {

    /**
     * Creates new form pmDialog
     *
     * @param parent
     * @param user
     */
    private final String pengirim;
    private final String kepada;

    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public PMDialog(ChatClient parent, Socket socket, ObjectInputStream input, ObjectOutputStream output, String pengirim, String kepada) {
        super(parent, false);

        this.socket = socket;
        this.input = input;
        this.output = output;
        this.pengirim = pengirim;
        this.kepada = kepada;
        setLocationRelativeTo(parent);
        initComponents();
    }

    void display(String res) {
        viewTextArea.setText(viewTextArea.getText() + res + "\n");
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        viewTextArea = new javax.swing.JTextArea();
        postTextField = new javax.swing.JTextField();
        postButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Private Message");

        viewTextArea.setEditable(false);
        viewTextArea.setColumns(20);
        viewTextArea.setLineWrap(true);
        viewTextArea.setRows(5);
        jScrollPane1.setViewportView(viewTextArea);

        postTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postTextFieldActionPerformed(evt);
            }
        });

        postButton.setText("Kirim");
        postButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(postTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(postButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(postTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(postButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void postButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postButtonActionPerformed
        // TODO add your handling code here:
        try {
            String message = "postPrivateText~" + pengirim + "~" + postTextField.getText() + "~" + kepada + "~\n";
            display(pengirim + ": " + postTextField.getText() + "\n");
            output.writeObject(message);
            postTextField.setText("");
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_postButtonActionPerformed

    private void postTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postTextFieldActionPerformed
        // TODO add your handling code here:
        postButtonActionPerformed(evt);
    }//GEN-LAST:event_postTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton postButton;
    private javax.swing.JTextField postTextField;
    private javax.swing.JTextArea viewTextArea;
    // End of variables declaration//GEN-END:variables

}
