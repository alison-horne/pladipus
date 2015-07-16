/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.pladipus.view.dialogs.run;

import com.compomics.pladipus.core.control.distribution.communication.interpreter.impl.XMLTemplateInterpreter;
import com.compomics.pladipus.core.control.runtime.steploader.StepLoadingException;
import com.compomics.pladipus.core.control.updates.ProcessingBeanUpdater;
import com.compomics.pladipus.core.model.prerequisite.Prerequisite;
import com.compomics.pladipus.core.model.prerequisite.PrerequisiteParameter;
import com.compomics.pladipus.core.model.processing.templates.PladipusProcessingTemplate;
import com.compomics.pladipus.core.model.processing.templates.ProcessingParameterTemplate;
import com.compomics.pladipus.view.util.renderer.xmlEditorKit.XMLEditorKit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import no.uib.jsparklines.extra.NimbusCheckBoxRenderer;
import org.xml.sax.SAXException;

/**
 *
 * @author Kenneth Verheggen
 */
public class RunCreationDialog extends javax.swing.JDialog {

    /**
     * the run owner
     */
    private final String user;
    /**
     * the temporary template to display
     */
    private PladipusProcessingTemplate template;
    /**
     *
     * the template file chooser
     */
    private final JFileChooser fileChooser = new JFileChooser();
    /**
     * the last selected folder
     */
    private File lastSelectedFolder = new File(System.getProperty("user.home") + "/.compomics/pladipus");
    /**
     * the installed processes file chooser
     */
    private final TreeMap<String, String> installedProcessStepClasses;
    /**
     * the preference to send the jobs for this run to
     */
    private Prerequisite prerequisite = new Prerequisite();
    /**
     * boolean indicating the run can be added to the database
     */
    private boolean confirmed;

    /**
     * Creates new form RunCreationDialog
     */
    public RunCreationDialog(java.awt.Frame parent, String user, boolean modal) throws ParserConfigurationException, IOException, SAXException {
        super(parent, modal);
        initComponents();

        this.user = user;
        this.setTitle("Run Creation Wizard");
        //transparent viewports
        spnlParameters.getViewport().setOpaque(false);
        spnlPreview.getViewport().setOpaque(false);
        //preview xmlEditorKit
        epnlPreviewXML.setEditorKit(new XMLEditorKit());
        epnlPreviewXML.setEditable(true);
        //step loading
        ProcessingBeanUpdater updater = ProcessingBeanUpdater.getInstance();
        installedProcessStepClasses = updater.getInstalledProcessStepClasses();
        //combobox
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (String aClass : installedProcessStepClasses.keySet()) {
            model.addElement(aClass);
        }
        //fill parameters
        tblParameters.getColumn(tblParameters.getColumnName(2)).setCellRenderer(new NimbusCheckBoxRenderer());
        tblParameters.getModel().addTableModelListener(
                new TableModelListener() {
                    public void tableChanged(TableModelEvent evt) {
                        //read all the table values, turn them into proper parameters and fling them into the model, then refresh
                        DefaultTableModel model = (DefaultTableModel) tblParameters.getModel();
                        for (int i = 0; i < model.getRowCount(); i++) {
                            try {
                                String parameterName = String.valueOf(model.getValueAt(i, 0));
                                if (!parameterName.isEmpty()) {
                                    String parameterValue;
                                    if (model.getValueAt(i, 1) != null) {
                                        parameterValue = String.valueOf(model.getValueAt(i, 1));
                                    } else {
                                        parameterValue = "";
                                    }
                                    boolean runParameter;
                                    if (model.getValueAt(i, 1) != null) {
                                        runParameter = (Boolean) model.getValueAt(i, 2);
                                    } else {
                                        runParameter = false;
                                    }
                                    ProcessingParameterTemplate processingParameter = new ProcessingParameterTemplate(parameterName, parameterValue);
                                    template.getRunParameters().remove(parameterName);
                                    template.getJobParameters().remove(parameterName);
                                    if (runParameter) {
                                        template.addRunParameter(processingParameter);
                                    } else {
                                        template.addJobParameter(processingParameter);
                                    }
                                }
                            } catch (NullPointerException e) {
                                //todo refactor to avoid this
                            }
                        }
                        refreshPreview();
                    }
                });

        tfRunName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent arg0) {
                super.focusLost(arg0);
                String text = tfRunName.getText();
                template.setName(text);
                refreshPreview();
            }
        });

        cbSteps.setModel(model);

        liSteps.setModel(new DefaultListModel());

        template = new PladipusProcessingTemplate("Default Run Name", user, 4, prerequisite);

        //add prerequisite buttons
        btnGroupOSArch.add(rdbLinux32);
        btnGroupOSArch.add(rdbLinux64);
        btnGroupOSArch.add(rdbWindows32);
        btnGroupOSArch.add(rdbWindows64);

        //add icons to up and down buttons
        ImageIcon imageUp = new ImageIcon(
                getClass().getResource(
                        "/images/icons/arrowUp.png"));
        btnUp.setIcon(imageUp);
        ImageIcon imageDown = new ImageIcon(
                getClass().getResource(
                        "/images/icons/arrowDown.png"));
        btnDown.setIcon(imageDown);
        btnUp.repaint();
        btnDown.repaint();
        refreshPreview();

    }

    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupOSArch = new javax.swing.ButtonGroup();
        pnlMain = new javax.swing.JPanel();
        btnCreateRun = new javax.swing.JButton();
        pnlSteps = new javax.swing.JPanel();
        liSteps = new javax.swing.JList();
        cbSteps = new javax.swing.JComboBox();
        btnAddStep = new javax.swing.JButton();
        btnRemoveStep = new javax.swing.JButton();
        lbPreSet = new javax.swing.JLabel();
        btnPreSetDbSearch = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        pnlRunName = new javax.swing.JPanel();
        tfRunName = new javax.swing.JTextField();
        pnlParameters = new javax.swing.JPanel();
        spnlParameters = new javax.swing.JScrollPane();
        tblParameters = new javax.swing.JTable();
        btnAddParameter = new javax.swing.JButton();
        btnRemoveParameter = new javax.swing.JButton();
        lblParameterRemark = new javax.swing.JLabel();
        pnlPreview = new javax.swing.JPanel();
        spnlPreview = new javax.swing.JScrollPane();
        epnlPreviewXML = new javax.swing.JEditorPane();
        btnCancel = new javax.swing.JButton();
        mbMain = new javax.swing.JMenuBar();
        miFile = new javax.swing.JMenu();
        miImport = new javax.swing.JMenuItem();
        miExit = new javax.swing.JMenuItem();
        miPreferences = new javax.swing.JMenu();
        miClearPreferences = new javax.swing.JMenuItem();
        sprOS = new javax.swing.JPopupMenu.Separator();
        rdbWindows64 = new javax.swing.JRadioButtonMenuItem();
        rdbWindows32 = new javax.swing.JRadioButtonMenuItem();
        rdbLinux64 = new javax.swing.JRadioButtonMenuItem();
        rdbLinux32 = new javax.swing.JRadioButtonMenuItem();
        rdbNoOs = new javax.swing.JRadioButtonMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miSetCores = new javax.swing.JMenuItem();
        miSetMemory = new javax.swing.JMenuItem();
        miSetDiskSpace = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlMain.setBackground(new java.awt.Color(255, 255, 255));

        btnCreateRun.setText("Create");
        btnCreateRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateRunActionPerformed(evt);
            }
        });

        pnlSteps.setBackground(new java.awt.Color(255, 255, 255));
        pnlSteps.setBorder(javax.swing.BorderFactory.createTitledBorder("Steps"));

        liSteps.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        liSteps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        cbSteps.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnAddStep.setText("+");
        btnAddStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStepActionPerformed(evt);
            }
        });

        btnRemoveStep.setText("-");
        btnRemoveStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveStepActionPerformed(evt);
            }
        });

        lbPreSet.setText("Presets :");

        btnPreSetDbSearch.setBackground(new java.awt.Color(255, 255, 255));
        btnPreSetDbSearch.setForeground(java.awt.SystemColor.textHighlight);
        btnPreSetDbSearch.setText("Database Search");
        btnPreSetDbSearch.setBorder(null);
        btnPreSetDbSearch.setBorderPainted(false);
        btnPreSetDbSearch.setContentAreaFilled(false);
        btnPreSetDbSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreSetDbSearchActionPerformed(evt);
            }
        });

        btnUp.setBorder(null);
        btnUp.setBorderPainted(false);
        btnUp.setOpaque(false);
        btnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpActionPerformed(evt);
            }
        });

        btnDown.setBorder(null);
        btnDown.setBorderPainted(false);
        btnDown.setOpaque(false);
        btnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlStepsLayout = new javax.swing.GroupLayout(pnlSteps);
        pnlSteps.setLayout(pnlStepsLayout);
        pnlStepsLayout.setHorizontalGroup(
            pnlStepsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStepsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStepsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlStepsLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(lbPreSet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPreSetDbSearch))
                    .addGroup(pnlStepsLayout.createSequentialGroup()
                        .addComponent(liSteps, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlStepsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDown, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRemoveStep, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)))
                    .addGroup(pnlStepsLayout.createSequentialGroup()
                        .addComponent(cbSteps, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddStep)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlStepsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddStep, btnDown, btnRemoveStep, btnUp});

        pnlStepsLayout.setVerticalGroup(
            pnlStepsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStepsLayout.createSequentialGroup()
                .addGroup(pnlStepsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPreSet)
                    .addComponent(btnPreSetDbSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStepsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddStep))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStepsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlStepsLayout.createSequentialGroup()
                        .addComponent(btnRemoveStep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                        .addComponent(btnUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(liSteps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlStepsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDown, btnUp});

        pnlRunName.setBackground(new java.awt.Color(255, 255, 255));
        pnlRunName.setBorder(javax.swing.BorderFactory.createTitledBorder("Run"));

        tfRunName.setText("Default Run Name");
        tfRunName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfRunNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRunNameLayout = new javax.swing.GroupLayout(pnlRunName);
        pnlRunName.setLayout(pnlRunNameLayout);
        pnlRunNameLayout.setHorizontalGroup(
            pnlRunNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRunNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tfRunName)
                .addGap(19, 19, 19))
        );
        pnlRunNameLayout.setVerticalGroup(
            pnlRunNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRunNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tfRunName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pnlParameters.setBackground(new java.awt.Color(255, 255, 255));
        pnlParameters.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));

        spnlParameters.setBackground(new java.awt.Color(255, 255, 255));
        spnlParameters.setOpaque(false);

        tblParameters.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        tblParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Parameter", "Value", "Run *"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblParameters.setOpaque(false);
        spnlParameters.setViewportView(tblParameters);

        btnAddParameter.setText("+");
        btnAddParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddParameterActionPerformed(evt);
            }
        });

        btnRemoveParameter.setText("-");
        btnRemoveParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveParameterActionPerformed(evt);
            }
        });

        lblParameterRemark.setText("* Run parameters will be available to every job belonging to this template");

        javax.swing.GroupLayout pnlParametersLayout = new javax.swing.GroupLayout(pnlParameters);
        pnlParameters.setLayout(pnlParametersLayout);
        pnlParametersLayout.setHorizontalGroup(
            pnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParametersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlParametersLayout.createSequentialGroup()
                        .addComponent(spnlParameters, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRemoveParameter)
                            .addComponent(btnAddParameter)))
                    .addComponent(lblParameterRemark))
                .addContainerGap())
        );

        pnlParametersLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddParameter, btnRemoveParameter});

        pnlParametersLayout.setVerticalGroup(
            pnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParametersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnlParameters, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(pnlParametersLayout.createSequentialGroup()
                        .addComponent(btnAddParameter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveParameter)
                        .addGap(0, 95, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblParameterRemark)
                .addGap(9, 9, 9))
        );

        pnlPreview.setBackground(new java.awt.Color(255, 255, 255));
        pnlPreview.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));

        spnlPreview.setBorder(null);

        epnlPreviewXML.setBorder(null);
        spnlPreview.setViewportView(epnlPreviewXML);

        javax.swing.GroupLayout pnlPreviewLayout = new javax.swing.GroupLayout(pnlPreview);
        pnlPreview.setLayout(pnlPreviewLayout);
        pnlPreviewLayout.setHorizontalGroup(
            pnlPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPreviewLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spnlPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlPreviewLayout.setVerticalGroup(
            pnlPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPreviewLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spnlPreview)
                .addContainerGap())
        );

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCreateRun))
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pnlSteps, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(pnlParameters, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlRunName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlMainLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCancel, btnCreateRun});

        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addComponent(pnlRunName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlSteps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlParameters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreateRun)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        miFile.setText("File");

        miImport.setText("Import...");
        miImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miImportActionPerformed(evt);
            }
        });
        miFile.add(miImport);

        miExit.setText("Exit");
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        miFile.add(miExit);

        mbMain.add(miFile);

        miPreferences.setText("Preferences");

        miClearPreferences.setText("Clear all preferences");
        miClearPreferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miClearPreferencesActionPerformed(evt);
            }
        });
        miPreferences.add(miClearPreferences);
        miPreferences.add(sprOS);

        rdbWindows64.setText("Windows 64-bit");
        rdbWindows64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbWindows64ActionPerformed(evt);
            }
        });
        miPreferences.add(rdbWindows64);

        rdbWindows32.setText("Windows 32-bit");
        rdbWindows32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbWindows32ActionPerformed(evt);
            }
        });
        miPreferences.add(rdbWindows32);

        rdbLinux64.setText("Linux 64-bit");
        rdbLinux64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbLinux64ActionPerformed(evt);
            }
        });
        miPreferences.add(rdbLinux64);

        rdbLinux32.setText("Linux 32-bit");
        rdbLinux32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbLinux32ActionPerformed(evt);
            }
        });
        miPreferences.add(rdbLinux32);

        rdbNoOs.setText("None");
        rdbNoOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbNoOsActionPerformed(evt);
            }
        });
        miPreferences.add(rdbNoOs);
        miPreferences.add(jSeparator2);

        miSetCores.setText("Set minimal cores (1)");
        miSetCores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSetCoresActionPerformed(evt);
            }
        });
        miPreferences.add(miSetCores);

        miSetMemory.setText("Set minimal RAM (0 GB)");
        miSetMemory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSetMemoryActionPerformed(evt);
            }
        });
        miPreferences.add(miSetMemory);

        miSetDiskSpace.setText("Set minimal disk  (0 GB)");
        miSetDiskSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSetDiskSpaceActionPerformed(evt);
            }
        });
        miPreferences.add(miSetDiskSpace);

        mbMain.add(miPreferences);

        setJMenuBar(mbMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshPreview() {
        if (template != null) {
            //check the step order
            template.clearProcessSteps();
            DefaultListModel model = (DefaultListModel) liSteps.getModel();
            Enumeration elements = model.elements();
            while (elements.hasMoreElements()) {
                template.addProcessingStep(installedProcessStepClasses.get(String.valueOf(elements.nextElement())));
            }
            epnlPreviewXML.setEditorKit(new XMLEditorKit());
            epnlPreviewXML.setText(template.toXML());
        }
    }

    private void btnCreateRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateRunActionPerformed
        //save the XML to a file = start a filechooser?
        fileChooser.setCurrentDirectory(lastSelectedFolder);
        fileChooser.setDialogTitle("Specify output file");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            lastSelectedFolder = fileToSave.getParentFile();
            if (fileToSave.exists()) {
                int dialogResult = JOptionPane.showConfirmDialog(this, fileToSave.getName() + " already exists, do you want to overwrite the file?");
                if (dialogResult != JOptionPane.YES_OPTION) {
                    return;

                }
            }
            try (FileWriter xmlOut = new FileWriter(fileToSave);
                    FileWriter tsvOut = new FileWriter(new File(fileToSave.getAbsolutePath() + ".tsv"))) {
                //save the template
                xmlOut.append(template.toXML().replace(">", ">" + System.lineSeparator())).flush();
                if (template.getAllProcessingParameters().size() > 0) {
                    //save the parameter configuration tsv file
                    StringBuilder headers = new StringBuilder();
                    for (String aHeader : template.getJobParameters().keySet()) {
                        headers.append(aHeader).append("\t");
                    }
                    //remove the last tab
                    headers.setLength(headers.length() - 1);
                    headers.append(System.lineSeparator());
                    tsvOut.append(headers).flush();
                }
                this.setVisible(false);
                confirmed = true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "An error occurred during exporting : " + ex.getMessage(),
                        "Inane error",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnCreateRunActionPerformed


    private void btnAddParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddParameterActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblParameters.getModel();
        if (model == null) {
            model = new DefaultTableModel();
            tblParameters.setModel(model);
        }
        model.addRow(new Object[]{"", "", true});
    }//GEN-LAST:event_btnAddParameterActionPerformed

    private void btnRemoveParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveParameterActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblParameters.getModel();
        if (model != null) {
            for (int selectedRow : tblParameters.getSelectedRows()) {
                String parameterName = String.valueOf(model.getValueAt(selectedRow, 0));
                template.getJobParameters().remove(parameterName);
                template.getRunParameters().remove(parameterName);
                model.removeRow(selectedRow);
            }
            refreshPreview();
        }
    }//GEN-LAST:event_btnRemoveParameterActionPerformed

    private void btnRemoveStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveStepActionPerformed
        for (Object aSelectedStep : liSteps.getSelectedValuesList()) {
            String step = String.valueOf(aSelectedStep);
            String className = installedProcessStepClasses.get(step);
            if (template.getProcessingSteps().contains(className)) {
                template.removeProcessingStep(className);
                ((DefaultListModel) liSteps.getModel()).removeElement(step);
            }
        }
        refreshPreview();
    }//GEN-LAST:event_btnRemoveStepActionPerformed

    private void btnAddStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStepActionPerformed
        if (template != null) {
            for (Object aSelectedStep : cbSteps.getSelectedObjects()) {
                String step = String.valueOf(aSelectedStep);
                String className = installedProcessStepClasses.get(step);
                if (!template.getProcessingSteps().contains(className)) {
                    template.addProcessingStep(className);
                    ((DefaultListModel) liSteps.getModel()).addElement(step);
                }
            }
        }
        refreshPreview();
    }//GEN-LAST:event_btnAddStepActionPerformed

    private void tfRunNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfRunNameActionPerformed
        refreshPreview();
    }//GEN-LAST:event_tfRunNameActionPerformed

    private void btnPreSetDbSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreSetDbSearchActionPerformed

        DefaultListModel model = (DefaultListModel) liSteps.getModel();
        model.removeAllElements();
        String[] steps = new String[]{"InitialisingStep", "SearchGUIStep", "PeptideShakerStep", "TempCleaningStep"};
        template = new PladipusProcessingTemplate(tfRunName.getText(), user, 4);

        for (String aStep : steps) {
            model.addElement(aStep);
            template.addProcessingStep(installedProcessStepClasses.get(aStep));
        }

        DefaultTableModel tModel = (DefaultTableModel) tblParameters.getModel();
        tModel.setRowCount(0);
        tModel.addRow(new Object[]{"fastafile", "", true});
        tModel.addRow(new Object[]{"searchGUI", "", true});
        tModel.addRow(new Object[]{"peptideShaker", "", true});
        tModel.addRow(new Object[]{"outputFolder", "", true});
        tModel.addRow(new Object[]{"searchengines", "", true});
        tModel.addRow(new Object[]{"peakfile", "", false});
        tModel.addRow(new Object[]{"parameterfile", "", false});
        tModel.addRow(new Object[]{"assay", "", false});

    }//GEN-LAST:event_btnPreSetDbSearchActionPerformed

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_miExitActionPerformed

    private void clearOSArchPrerequisites() {
        prerequisite.removePrerequisite(PrerequisiteParameter.OS_WINDOWS);
        rdbLinux32.setSelected(false);
        rdbWindows32.setSelected(false);
        rdbLinux64.setSelected(false);
        rdbWindows64.setSelected(false);
        prerequisite.removePrerequisite(PrerequisiteParameter.OS_LINUX);
        prerequisite.removePrerequisite(PrerequisiteParameter.ARCH_64);
        prerequisite.removePrerequisite(PrerequisiteParameter.ARCH_32);
    }


    private void rdbWindows64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbWindows64ActionPerformed
        clearOSArchPrerequisites();
        if (rdbWindows64.isSelected()) {
            prerequisite.addPrerequisite(PrerequisiteParameter.OS_WINDOWS);
            prerequisite.addPrerequisite(PrerequisiteParameter.ARCH_64);
        }
        refreshPreview();
    }//GEN-LAST:event_rdbWindows64ActionPerformed

    private void rdbWindows32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbWindows32ActionPerformed
        clearOSArchPrerequisites();
        if (rdbWindows32.isSelected()) {
            prerequisite.addPrerequisite(PrerequisiteParameter.OS_WINDOWS);
            prerequisite.addPrerequisite(PrerequisiteParameter.ARCH_32);
        }
        refreshPreview();
    }//GEN-LAST:event_rdbWindows32ActionPerformed

    private void rdbLinux64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbLinux64ActionPerformed
        clearOSArchPrerequisites();
        if (rdbLinux64.isSelected()) {
            prerequisite.addPrerequisite(PrerequisiteParameter.OS_LINUX);
            prerequisite.addPrerequisite(PrerequisiteParameter.ARCH_64);
        }
        refreshPreview();
    }//GEN-LAST:event_rdbLinux64ActionPerformed

    private void rdbLinux32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbLinux32ActionPerformed
        clearOSArchPrerequisites();
        if (rdbLinux32.isSelected()) {
            prerequisite.addPrerequisite(PrerequisiteParameter.OS_LINUX);
            prerequisite.addPrerequisite(PrerequisiteParameter.ARCH_32);
        }
        refreshPreview();
    }//GEN-LAST:event_rdbLinux32ActionPerformed

    private void miSetCoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSetCoresActionPerformed
        String message = JOptionPane.showInputDialog(null, "Please specify the minimal amount of cores");
        try {
            int cores = Integer.parseInt(message);
            prerequisite.removePrerequisite(PrerequisiteParameter.CORES);
            prerequisite.addPrerequisite(PrerequisiteParameter.CORES, message);
            if (cores == -1) {
                miSetCores.setText("Set minimal cores");
            }
            miSetCores.setText("Set minimal cores (" + cores + ")");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please insert a valid number", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
        refreshPreview();
    }//GEN-LAST:event_miSetCoresActionPerformed

    private void miSetMemoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSetMemoryActionPerformed
        String message = JOptionPane.showInputDialog(null, "Please specify the minimal amount of RAM (in GB)");
        try {
            long RAM = Long.parseLong(message);
            prerequisite.removePrerequisite(PrerequisiteParameter.MEMORY);
            prerequisite.addPrerequisite(PrerequisiteParameter.MEMORY, String.valueOf(RAM));
            if (RAM == -1) {
                miSetMemory.setText("Set minimal RAM");
            }
            miSetMemory.setText("Set minimal RAM (" + message + " GB)");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please insert a valid number", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
        refreshPreview();
    }//GEN-LAST:event_miSetMemoryActionPerformed

    private void miSetDiskSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSetDiskSpaceActionPerformed
        String message = JOptionPane.showInputDialog(null, "Please specify the minimal amount of disk space (in GB)");
        try {
            long diskSpace = Long.parseLong(message);
            prerequisite.removePrerequisite(PrerequisiteParameter.DISKSPACE);
            prerequisite.addPrerequisite(PrerequisiteParameter.DISKSPACE, String.valueOf(diskSpace));
            if (diskSpace == -1) {
                miSetDiskSpace.setText("Set minimal disk");
            }
            miSetDiskSpace.setText("Set minimal disk (" + message + " GB)");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please insert a valid number", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
        refreshPreview();
    }//GEN-LAST:event_miSetDiskSpaceActionPerformed

    private void miClearPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miClearPreferencesActionPerformed
        for (PrerequisiteParameter aParamter : PrerequisiteParameter.values()) {
            prerequisite.removePrerequisite(aParamter);
        }
        miSetDiskSpace.setText("Set minimal disk");
        miSetMemory.setText("Set minimal RAM");
        miSetCores.setText("Set minimal cores");
        refreshPreview();
    }//GEN-LAST:event_miClearPreferencesActionPerformed

    private void miImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miImportActionPerformed
        fileChooser.setCurrentDirectory(lastSelectedFolder);
        fileChooser.setDialogTitle("Select template file to import...");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try {

                PladipusProcessingTemplate convertXMLtoTemplate = XMLTemplateInterpreter.getInstance().convertXMLtoTemplate(fileToOpen);
                //check for unknown steps
                ArrayList<String> unknownSteps = new ArrayList<>();
                for (String aStep : convertXMLtoTemplate.getProcessingSteps()) {
                    if (!installedProcessStepClasses.values().contains(aStep)) {
                        unknownSteps.add(aStep);
                    }
                }

                if (!unknownSteps.isEmpty()) {
                    StringBuilder unknownStepMessage = new StringBuilder("The following classes are not correctly installed : ").append(System.lineSeparator());
                    for (String anUnknownStep : unknownSteps) {
                        unknownStepMessage.append(anUnknownStep).append(System.lineSeparator());
                    }
                    JOptionPane.showMessageDialog(this,
                            unknownStepMessage.toString(),
                            "Your import seems corrupted...",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                synchronized (template) {
                    //set the lists and table and run name
                    template = convertXMLtoTemplate;
                    DefaultListModel liModel = (DefaultListModel) liSteps.getModel();
                    DefaultTableModel tbModel = (DefaultTableModel) tblParameters.getModel();

                    tfRunName.setText(template.getName());
                    liModel.removeAllElements();
                    for (String aStep : template.getProcessingSteps()) {
                        liModel.addElement(aStep.substring(aStep.lastIndexOf(".") + 1));
                    }
                    tbModel.setRowCount(0);

                    TreeMap<String, ProcessingParameterTemplate> temp = new TreeMap<>();
                    temp.putAll(template.getRunParameters());
                    for (ProcessingParameterTemplate aRunParamter : temp.values()) {
                        tbModel.addRow(new Object[]{aRunParamter.getName(), aRunParamter.getValue(), true});
                    }
                    temp.clear();
                    temp.putAll(template.getJobParameters());
                    for (ProcessingParameterTemplate aJobParameter : temp.values()) {
                        tbModel.addRow(new Object[]{aJobParameter.getName(), aJobParameter.getValue(), false});
                    }
                    //also do the prerequisites

                    prerequisite = template.getMachinePrerequisite();
                    refreshPrerequisites();
                }
            } catch (IOException | StepLoadingException | ParserConfigurationException | SAXException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "An error occurred during importing : " + ex.getMessage(),
                        "Your import seems corrupted...",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        refreshPreview();
    }//GEN-LAST:event_miImportActionPerformed

    private void refreshPrerequisites() {
        ArrayList<PrerequisiteParameter> prerequisiteList = prerequisite.getPrerequisiteList();
        boolean arch32 = false;
        boolean arch64 = false;
        boolean windows = false;
        boolean linux = false;
        for (PrerequisiteParameter aParameter : prerequisiteList) {
            if (aParameter.equals(PrerequisiteParameter.ARCH_32)) {
                arch32 = true;
            } else if (aParameter.equals(PrerequisiteParameter.ARCH_64)) {
                arch64 = true;
            } else if (aParameter.equals(PrerequisiteParameter.OS_LINUX)) {
                linux = true;
            } else if (aParameter.equals(PrerequisiteParameter.OS_WINDOWS)) {
                windows = true;
            } else if (aParameter.equals(PrerequisiteParameter.CORES)) {
                miSetCores.setText("Set minimal cores (" + aParameter.getOptionValue() + ")");
            } else if (aParameter.equals(PrerequisiteParameter.MEMORY)) {
                miSetMemory.setText("Set minimal RAM (" + aParameter.getOptionValue() + " GB)");
            } else if (aParameter.equals(PrerequisiteParameter.DISKSPACE)) {
                miSetDiskSpace.setText("Set minimal disk (" + aParameter.getOptionValue() + " GB)");
            }
        }
        if (arch64) {
            if (windows) {
                rdbWindows64.setEnabled(true);
            }
            if (linux) {
                rdbLinux64.setEnabled(true);
            }
        } else if (arch32) {
            if (windows) {
                rdbWindows32.setEnabled(true);
            }
            if (linux) {
                rdbLinux32.setEnabled(true);
            }
        }
    }


    private void rdbNoOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbNoOsActionPerformed
        clearOSArchPrerequisites();
        refreshPreview();
    }//GEN-LAST:event_rdbNoOsActionPerformed

    private void swapElements(int pos1, int pos2) {
        if (pos1 != pos2) {
            DefaultListModel listModel = (DefaultListModel) liSteps.getModel();
            String tmp = String.valueOf(listModel.get(pos1));
            listModel.set(pos1, listModel.get(pos2));
            listModel.set(pos2, tmp);
        }
    }
    private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
        int indexOfSelected = liSteps.getSelectedIndex();
        if (indexOfSelected > 0) {
            swapElements(indexOfSelected, indexOfSelected - 1);
            indexOfSelected = indexOfSelected - 1;
            liSteps.setSelectedIndex(indexOfSelected);
            liSteps.updateUI();
            refreshPreview();
        }
    }//GEN-LAST:event_btnUpActionPerformed

    private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
        int indexOfSelected = liSteps.getSelectedIndex();
        DefaultListModel listModel = (DefaultListModel) liSteps.getModel();
        if (indexOfSelected < listModel.getSize() - 1) {
            swapElements(indexOfSelected, indexOfSelected + 1);
            indexOfSelected = indexOfSelected + 1;
            liSteps.setSelectedIndex(indexOfSelected);
            liSteps.updateUI();
            refreshPreview();
        }
    }//GEN-LAST:event_btnDownActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddParameter;
    private javax.swing.JButton btnAddStep;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCreateRun;
    private javax.swing.JButton btnDown;
    private javax.swing.ButtonGroup btnGroupOSArch;
    private javax.swing.JButton btnPreSetDbSearch;
    private javax.swing.JButton btnRemoveParameter;
    private javax.swing.JButton btnRemoveStep;
    private javax.swing.JButton btnUp;
    private javax.swing.JComboBox cbSteps;
    private javax.swing.JEditorPane epnlPreviewXML;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JLabel lbPreSet;
    private javax.swing.JLabel lblParameterRemark;
    private javax.swing.JList liSteps;
    private javax.swing.JMenuBar mbMain;
    private javax.swing.JMenuItem miClearPreferences;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenu miFile;
    private javax.swing.JMenuItem miImport;
    private javax.swing.JMenu miPreferences;
    private javax.swing.JMenuItem miSetCores;
    private javax.swing.JMenuItem miSetDiskSpace;
    private javax.swing.JMenuItem miSetMemory;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlParameters;
    private javax.swing.JPanel pnlPreview;
    private javax.swing.JPanel pnlRunName;
    private javax.swing.JPanel pnlSteps;
    private javax.swing.JRadioButtonMenuItem rdbLinux32;
    private javax.swing.JRadioButtonMenuItem rdbLinux64;
    private javax.swing.JRadioButtonMenuItem rdbNoOs;
    private javax.swing.JRadioButtonMenuItem rdbWindows32;
    private javax.swing.JRadioButtonMenuItem rdbWindows64;
    private javax.swing.JScrollPane spnlParameters;
    private javax.swing.JScrollPane spnlPreview;
    private javax.swing.JPopupMenu.Separator sprOS;
    private javax.swing.JTable tblParameters;
    private javax.swing.JTextField tfRunName;
    // End of variables declaration//GEN-END:variables

    public PladipusProcessingTemplate getProcessingTemplate() {
        return template;
    }
}
