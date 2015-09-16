package com.compomics.pladipus.view.panels.impl;

import com.compomics.pladipus.core.control.distribution.service.queue.jmx.operation.impl.QueryOperation;
import com.compomics.pladipus.core.model.queue.CompomicsQueue;
import com.compomics.pladipus.view.panels.UpdatingPanel;
import java.io.IOException;
import javax.management.MalformedObjectNameException;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kenneth Verheggen
 */
public class PladipusInformationPanel extends javax.swing.JPanel implements UpdatingPanel {

    /**
     * Loads the information on pladipus in the background
     */
    private UpdateWorker updateWorker;

    /**
     * Creates new form PladipusInformationPanel
     */
    public PladipusInformationPanel() {
        initComponents();
        spnlMain.getViewport().setOpaque(false);
        tblPladipusInfo.getTableHeader().setReorderingAllowed(false);
    }

    @Override
    public void activate() {
        updateWorker = new UpdateWorker();
        updateWorker.execute();
    }

    @Override
    public void deactivate() {
        if (updateWorker != null) {
            updateWorker.done();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spnlMain = new javax.swing.JScrollPane();
        tblPladipusInfo = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        tblPladipusInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Active Consumers", null},
                {"Current job count", null},
                {"Overall job count", null}
            },
            new String [] {
                "Parameter", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPladipusInfo.setOpaque(false);
        spnlMain.setViewportView(tblPladipusInfo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane spnlMain;
    private javax.swing.JTable tblPladipusInfo;
    // End of variables declaration//GEN-END:variables

    private class UpdateWorker extends SwingWorker<Integer, Integer> {

        private boolean isDone = false;

        @Override
        protected Integer doInBackground() throws Exception {
            while (!isDone) {
                try {
                    Thread.sleep(1000);
                    updateTable();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }

        @Override
        protected void done() {
            isDone = true;
        }

        private void updateTable() throws IOException, MalformedObjectNameException, Exception {
            JTable table = PladipusInformationPanel.this.tblPladipusInfo;
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            if (model == null) {
                model = new DefaultTableModel();
            }

            QueryOperation operation = new QueryOperation();

            model.setValueAt(operation.getConsumerCount(CompomicsQueue.JOB), 0, 1);

            model.setValueAt(operation.getCurrentQueueSize(CompomicsQueue.JOB), 1, 1);

            model.setValueAt(operation.getCumulativeQueueSize(CompomicsQueue.JOB), 2, 1);

            table.setModel(model);
        }
    }

}
