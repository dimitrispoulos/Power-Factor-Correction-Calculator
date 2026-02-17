/*
    Project: Power Factor Correction (PFC) Calculator
    Description: A Java Swing-based engineering application for Power Factor Correction analysis. Designed for industrial electrical systems, it provides required capacitor bank sizing for Single-Phase and Three-Phase systems while evaluating line current loss reduction.
    Author: Dimitrios Poulos
*/


package PFCCalculator;


import java.io.IOException;
import javax.swing.JOptionPane;




public class PFCApp extends javax.swing.JFrame
{
    // Global Variables
    javax.swing.border.Border originalBorder;    // Variable for the default border style of text fields
    javax.swing.JTextField selectedField = null;
    PFCCalculation calculator = new PFCCalculation();
    
    public PFCApp()
    {
        initComponents();
        
        
        // Icon for Application
        try
        {
            java.awt.Image icon = javax.imageio.ImageIO.read(getClass().getResource("capacitor.png"));
            this.setIconImage(icon);
        }
        catch (IOException ex)
        {
            System.out.println("Icon is missing: " + ex.getMessage());    // Error message if the icon file is missing
        }
        
        
        
        this.setResizable(false);    // Application window cannot be resized
        this.setLocationRelativeTo(null);    // Application window is centered on the screen
        
        
        
        // Text panes become transparent in order to match the background
        txtpnlCalculationResults.setBackground(new java.awt.Color(0,0,0,0));
        txtpnlGuide.setBackground(new java.awt.Color(0,0,0,0));
        txtpnlAbout.setBackground(new java.awt.Color(0,0,0,0));
        txtpnlTheory.setBackground(new java.awt.Color(0,0,0,0));
        
        
        
        txtpnlCalculationResults.setCaretColor(new java.awt.Color(0,0,0,0));    // The cursor becomes invisible in the results text pane
        
        
        
        originalBorder = txtP.getBorder();    // originalBorder stores the border of active power text field
        
        // This border is used for all text fields
        txtP.setBorder(originalBorder);
        txtVoltage.setBorder(originalBorder);
        txtFrequency.setBorder(originalBorder);
        txtInitialValue.setBorder(originalBorder);
        txtTargetValue.setBorder(originalBorder);
        
        
        
        pnlConnectionType.setVisible(false);    // Connection type panel (Delta/Star) is hidden until Three-Phase is selected
        
        
        
        // This listener identifies which text field the user wants to type into.  This allows the virtual keypad to know where to input the numbers
        java.awt.event.FocusListener buttonPress = new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                selectedField = (javax.swing.JTextField) evt.getSource();    // Stores the current text field that gained focus
            }
        };

        
        // Attachment of focus listener to all text input fields
        txtP.addFocusListener(buttonPress);
        txtVoltage.addFocusListener(buttonPress);
        txtFrequency.addFocusListener(buttonPress);
        txtInitialValue.addFocusListener(buttonPress);
        txtTargetValue.addFocusListener(buttonPress);
        
        
        
        txtpnlCalculationResults.setText("<html><div style='text-align: center; padding-top: 120px;'><i style='color: gray; font-size: 10px;'>Results will be shown here</i></div></html>");
        
        
        
        // Use of HTML in order to define the content of the "Theory" tab
        String theoryText = "<html>";
               theoryText += "<body style='font-family: sans-serif; padding: 20px; background-color: transparent;'>";
               theoryText += "<div style='text-align: center; width: 100%;'>";
               theoryText += "<h1 style='color: green; font-size: 18px;'> Power Factor Correction: Technical Analysis </h1>";
               theoryText += "<hr style='width: 95%; border: 0.5px solid gray;'>";
               theoryText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 15px;'>";
               theoryText += "<b style='font-size: 14px; color: blue;'> 1. Definitions of Power Components </b>";
               theoryText += "<ul style='font-size: 12px; line-height: 1.4; margin-bottom: 5px;'>";
               theoryText += "<li><b>Active Power (P):</b> Measured in <b>kW (W:Watt)</b>. It is the real power that performs actual work in the system, such as generating heat, light, or mechanical torque.</li>";
               theoryText += "</ul>";
               theoryText += "<p style='font-size: 15px; background-color: white; padding: 12px; border-left: 5px solid blue; text-align: center;'>";
               theoryText += "<i>P = V &middot; I &middot; cos&phi;</i></p>";
               theoryText += "<ul style='font-size: 12px; line-height: 1.4; margin-bottom: 5px;'>";
               theoryText += "<li><b>Reactive Power (Q):</b> Measured in <b>kVAr (VAr:Volt-Ampere reactive)</b>. This power oscillates between the source and the load. It is essential for creating magnetic fields in inductive loads (motors, transformers) but does not perform useful work.</li>";
               theoryText += "</ul>";
               theoryText += "<p style='font-size: 15px; background-color: white; padding: 12px; border-left: 5px solid blue; text-align: center;'>";
               theoryText += "<i>Q = V &middot; I &middot; sin&phi;</i></p>";
               theoryText += "<ul style='font-size: 12px; line-height: 1.4; margin-bottom: 5px;'>";
               theoryText += "<li><b>Apparent Power (S):</b> Measured in <b>kVA (VA:Volt-Ampere)</b>. The vector sum of P and Q. It represents the total capacity the grid must provide to support the load.</li>";
               theoryText += "</ul>";
               theoryText += "<p style='font-size: 15px; background-color: white; padding: 12px; border-left: 5px solid blue; text-align: center;'>";
               theoryText += "<i>S = &radic;(P<sup>2</sup> + Q<sup>2</sup>)</i></p>";
               theoryText += "<ul style='font-size: 12px; line-height: 1.4; margin-bottom: 5px;'>";
               theoryText += "<li><b>Power Factor (cos&phi;):</b> A dimensionless ratio between 0 and 1. It indicates how effectively the incoming power is converted into useful work. It is the ratio of Active Power to Apparent Power.</li>";
               theoryText += "</ul>";
               theoryText += "<p style='font-size: 15px; background-color: white; padding: 12px; border-left: 5px solid blue; text-align: center;'>";
               theoryText += "<i>cos&phi; = P / S</i></p>";
               theoryText += "</div>";
               theoryText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               theoryText += "<b style='font-size: 14px; color: blue;'> 2. Reactive Compensation Requirement (Qc) </b>";
               theoryText += "<p style='font-size: 12px;'>The capacitive power needed to improve the power factor:</p>";
               theoryText += "<p style='font-size: 15px; background-color: white; padding: 12px; border-left: 5px solid blue; text-align: center;'>";
               theoryText += "<i>Q<sub>c</sub> = P &middot; [tan(&phi;<sub>1</sub>) - tan(&phi;<sub>2</sub>)]</i><br>";
               theoryText += "<i>Q<sub>c</sub> = Q<sub>1</sub> - Q<sub>2</sub></i></p>";
               theoryText += "</div>";
               theoryText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               theoryText += "<b style='font-size: 14px; color: blue;'> 3. Physical Capacitance Determination (C) </b>";
               theoryText += "<p style='font-size: 12px;'>Formula to derive capacitance in Microfarads (&mu;F):</p>";
               theoryText += "<p style='font-size: 15px; background-color: white; padding: 12px; border-left: 5px solid blue; text-align: center;'>";
               theoryText += "<i>C = (Q<sub>c</sub> &middot; 10<sup>9</sup>) / (2 &middot; &pi; &middot; f &middot; V<sup>2</sup>)</i></p>";
               theoryText += "</div>";
               theoryText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               theoryText += "<b style='font-size: 14px; color: blue;'> 4. Three-Phase Power Dynamics </b>";
               theoryText += "<p style='font-size: 12px;'>In three-phase systems, we distinguish between Line and Phase quantities:</p>";
               theoryText += "<ul style='font-size: 12px; line-height: 1.4;'>";
               theoryText += "<li><b>Line Voltage (V<sub>L</sub>):</b> The potential difference between any two phases of the system.</li>";
               theoryText += "<li><b>Phase Voltage (V<sub>ph</sub>):</b> The potential difference between any single phase and the neutral point of the system.</li>";
               theoryText += "<li><b>Line Current (I<sub>L</sub>):</b> The intensity of the electric current flowing through any one of the phases connecting the source to the load.</li>";
               theoryText += "<li><b>Phase Current (I<sub>ph</sub>):</b> The intensity of the electric current flowing through each individual phase.</li>";
               theoryText += "</ul>";
               theoryText += "<p style='font-size: 15px; background-color: white; padding: 12px; border-left: 5px solid blue; text-align: center;'>";
               theoryText += "<i>P = &radic;3 &middot; V<sub>L</sub> &middot; I<sub>L</sub> &middot; cos&phi;</i><br>";
               theoryText += "<i>Q = &radic;3 &middot; V<sub>L</sub> &middot; I<sub>L</sub> &middot; sin&phi;</i><br>";
               theoryText += "<i>S = &radic;3 &middot; V<sub>L</sub> &middot; I<sub>L</sub></i></p>";
               theoryText += "</div>";
               theoryText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               theoryText += "<b style='font-size: 14px; color: blue;'> 5. Connection Topologies: Delta (&Delta;) vs Star (Y) </b>";
               theoryText += "<ul style='font-size: 12px; line-height: 1.6;'>";
               theoryText += "<li><b>Delta (&Delta;):</b> Phase voltage equals line voltage (V<sub>ph</sub> = V<sub>L</sub>). Line current is &radic;3 times the phase current (I<sub>L</sub> = &radic;3 &middot; I<sub>ph</sub>).</li>";
               theoryText += "<li><b>Star (Y):</b> Phase voltage is reduced (V<sub>ph</sub> = V<sub>L</sub> / &radic;3). Line current equals phase current (I<sub>L</sub> = I<sub>ph</sub>)</li>";
               theoryText += "</ul>";
               theoryText += "<p style='font-size: 13px; background-color: lightyellow; padding: 12px; border-left: 5px solid orange; text-align: center;'>";
               theoryText += "<b>Technical Implication:</b> Star connection requires <b>3 times more capacitance</b> than Delta for the same Q<sub>c</sub>.";
               theoryText += "</p></div>";
               theoryText += "</div>";
               theoryText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               theoryText += "<b style='font-size: 14px; color: blue;'> 6. Line Current Calculation (I) </b>";
               theoryText += "<p style='font-size: 12px;'>The RMS current drawn from the source depends on the system type:</p>";
               theoryText += "<p style='font-size: 15px; background-color: white; padding: 12px; border-left: 5px solid blue; text-align: center;'>";
               theoryText += "<b>Single-Phase:</b> <i>I = P / (V &middot; cos&phi;)</i><br>";
               theoryText += "<b>Three-Phase:</b> <i>I<sub>L</sub> = P / (&radic;3 &middot; V<sub>L</sub> &middot; cos&phi;)</i></p>";
               theoryText += "</div>";
               theoryText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               theoryText += "<b style='font-size: 14px; color: blue;'> 7. Benefits: Line Current Reduction (%) </b>";
               theoryText += "<p style='font-size: 12px;'>Improving the power factor directly reduces the line current, leading to lower thermal losses and released system capacity:</p>";
               theoryText += "<p style='font-size: 15px; background-color: white; padding: 12px; border-left: 5px solid blue; text-align: center;'>";
               theoryText += "<i>Reduction (%) = [1-(cos&phi;<sub>1</sub>/cos&phi;<sub>2</sub>)<sup>2</sup>] &middot; 100</i></p>";
               theoryText += "</body></html>";
                
        txtpnlTheory.setText(theoryText);    // The text is applied to "Theory" panel
        txtpnlTheory.setCaretPosition(0);    // Ensures that the text pane starts at the beginning top of the text
        
        
        
        // Use of HTML in order to define the content of the "Guide" tab
        String guideText = "<html>";
               guideText += "<body style='font-family: sans-serif; padding: 20px; background-color: transparent;'>";
               guideText += "<div style='text-align: center; width: 100%;'>";
               guideText += "<h1 style='color: green; font-size: 18px;'> Application Guide </h1>";
               guideText += "<hr style='width: 95%; border: 0.5px solid gray;'>";
               guideText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 15px;'>";
               guideText += "<p style='font-size: 12px; margin-bottom: 5px;'> Follow the steps below to accomplish your study: </p>";
               guideText += "</div>";
               guideText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               guideText += "<b style='font-size: 14px; color: blue;'> STEP 1: Grid Parameter Specification </b>";
               guideText += "<ul style='font-size: 12px; line-height: 1.4; margin-bottom: 5px;'>";
               guideText += "<li>In the <b>Active Power</b> field, enter the system's Active Power in <b>kW</b>.</li>";
               guideText += "<li>In the <b>Voltage</b> field, enter line's Voltage in <b>Volts</b>.</li>";
               guideText += "<li>In the <b>Frequency</b> field, define network's Frequency in <b>Hz</b>.</li>";
               guideText += "</ul>";
               guideText += "</div>";
               guideText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               guideText += "<b style='font-size: 14px; color: blue;'> STEP 2: Initial State Specification </b>";
               guideText += "<ul style='font-size: 12px; line-height: 1.4; margin-bottom: 5px;'>";
               guideText += "<li>Determine whether the input data is based on the <b>Initial Power Factor (cos&phi;)</b> or the <b>Initial Reactive Power (Q)</b> in kVAr.</li>";
               guideText += "<li>Press the corresponding button based on your selection and enter the value in the field.</li>";
               guideText += "</ul>";
               guideText += "</div>";
               guideText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               guideText += "<b style='font-size: 14px; color: blue;'> STEP 3: Target State Specification </b>";
               guideText += "<ul style='font-size: 12px; line-height: 1.4; margin-bottom: 5px;'>";
               guideText += "<li>Determine whether the input data is based on the <b>Target Power Factor (cos&phi;)</b> or the <b>Target Reactive Power (Q)</b> in kVAr.</li>";
               guideText += "<li>Press the corresponding button based on your selection and enter the value in the field.</li>";
               guideText += "</ul>";
               guideText += "</div>";
               guideText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               guideText += "<b style='font-size: 14px; color: blue;'> STEP 4: System Type Details Configuration </b>";
               guideText += "<ul style='font-size: 12px; line-height: 1.4; margin-bottom: 5px;'>";
               guideText += "<li>Select <b>Single-Phase:</b> For simple single-phase loads or <b>Three-Phase:</b> For three-phase motors and industrial loads.</li>";
               guideText += "<li>If the load is three-phase, select whether the capacitors are connected in <b>Delta (&Delta;)</b> or <b>Star (Y)</b></li>";
               guideText += "</ul>";
               guideText += "</div>";
               guideText += "<div style='text-align: left; display: inline-block; width: 95%; margin-top: 10px;'>";
               guideText += "<b style='font-size: 14px; color: blue;'> STEP 5: Calculation & Depiction of Results </b>";
               guideText += "<ul style='font-size: 12px; line-height: 1.4; margin-bottom: 5px;'>";
               guideText += "<li>Press <b>CALCULATE</b>. All results will be displayed in the box called <b>Calculation Summary</b>.</li>";
               guideText += "<li>If the user wishes to delete all data and restore the program to its original form, they can use the <b>RESET ALL</b> button.</li>";
               guideText += "</ul>";
               guideText += "</div>";
               guideText += "</div></body></html>";
               
        txtpnlGuide.setText(guideText);    // The text is applied to "Guide" panel
        txtpnlGuide.setCaretPosition(0);    // Ensures that the text pane starts at the beginning top of the text
        
        
        
        // Use of HTML in order to define the content of the "About" tab
        String aboutText = "<html>";
               aboutText += "<div style='text-align: center; width: 100%; font-family: sans-serif; padding-top: 130px;'>";
               aboutText += "<h2 style='color: #2c3e50; font-size: 25px'>Power Factor Correction Calculator</h2>";
               aboutText += "<center><hr style='width: 75%; border-top: 1px solid #bdc3c7;'></center>";
               aboutText += "<p style='font-size: 14px;'><b>Developer:</b> Dimitrios Poulos </p>";
               aboutText += "<p style='font-size: 11px; color: #34495e; margin-top: 0px;'><i>Electrical & Computer Engineering Student</i></p>";
               aboutText += "<br><br>";
               aboutText += "<p style='font-size: 13px;'><b>Version:</b> 1.0.0 (2026)</p>";
               aboutText += "<br><br>";
               aboutText += "<p style='font-size: 10px; color: gray;'>Built with Java Swing & NetBeans IDE</p>";
               aboutText += "</div>";
               aboutText += "</html>";
               
        txtpnlAbout.setText(aboutText);    // The text is applied to "Theory" panel

    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jButton1 = new javax.swing.JButton();
        pnlCalculator = new javax.swing.JPanel();
        pnlCalculationResults = new javax.swing.JPanel();
        scrlpnlCaclulationResults = new javax.swing.JScrollPane();
        txtpnlCalculationResults = new javax.swing.JTextPane();
        pnlGeneralData = new javax.swing.JPanel();
        lblP = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtP = new javax.swing.JTextField();
        txtVoltage = new javax.swing.JTextField();
        txtFrequency = new javax.swing.JTextField();
        pnlInitialState = new javax.swing.JPanel();
        rbInitialPF = new javax.swing.JRadioButton();
        rbInitialQ = new javax.swing.JRadioButton();
        txtInitialValue = new javax.swing.JTextField();
        pnlTargetState = new javax.swing.JPanel();
        rbTargetPF = new javax.swing.JRadioButton();
        rbTargetQ = new javax.swing.JRadioButton();
        txtTargetValue = new javax.swing.JTextField();
        btnCalculate = new javax.swing.JButton();
        pnlSystemType = new javax.swing.JPanel();
        rbIsSinglePhase = new javax.swing.JRadioButton();
        rbIsThreePhase = new javax.swing.JRadioButton();
        pnlConnectionType = new javax.swing.JPanel();
        rbIsDelta = new javax.swing.JRadioButton();
        rbIsStar = new javax.swing.JRadioButton();
        btnResetAll = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btn1 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn0 = new javax.swing.JButton();
        btnDot = new javax.swing.JButton();
        btnDEL = new javax.swing.JButton();
        btnAC = new javax.swing.JButton();
        pnlTheory = new javax.swing.JPanel();
        btnBack1 = new javax.swing.JButton();
        scrlpnlTheory = new javax.swing.JScrollPane();
        txtpnlTheory = new javax.swing.JTextPane();
        pnlGuide = new javax.swing.JPanel();
        scrlpnlGuide = new javax.swing.JScrollPane();
        txtpnlGuide = new javax.swing.JTextPane();
        btnBack2 = new javax.swing.JButton();
        pnlAbout = new javax.swing.JPanel();
        txtpnlAbout = new javax.swing.JTextPane();
        btnBack3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnHelp = new javax.swing.JMenu();
        mnitmTheory = new javax.swing.JMenuItem();
        mnitmGuide = new javax.swing.JMenuItem();
        mnAbout = new javax.swing.JMenu();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PFC-Calculator");
        getContentPane().setLayout(new java.awt.CardLayout());

        pnlCalculationResults.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Calculation Results", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        scrlpnlCaclulationResults.setBorder(null);

        txtpnlCalculationResults.setEditable(false);
        txtpnlCalculationResults.setContentType("text/html"); // NOI18N
        txtpnlCalculationResults.setOpaque(false);
        scrlpnlCaclulationResults.setViewportView(txtpnlCalculationResults);

        javax.swing.GroupLayout pnlCalculationResultsLayout = new javax.swing.GroupLayout(pnlCalculationResults);
        pnlCalculationResults.setLayout(pnlCalculationResultsLayout);
        pnlCalculationResultsLayout.setHorizontalGroup(
            pnlCalculationResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCalculationResultsLayout.createSequentialGroup()
                .addComponent(scrlpnlCaclulationResults, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlCalculationResultsLayout.setVerticalGroup(
            pnlCalculationResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCalculationResultsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrlpnlCaclulationResults, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
        );

        pnlGeneralData.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "General Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        lblP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblP.setText("Active Power (kW):");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Voltage (Volts):");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Frequency (Hz):");

        txtP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtP.setToolTipText("Enter Active Power (in kW)");
        txtP.setPreferredSize(new java.awt.Dimension(80, 30));
        txtP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPActionPerformed(evt);
            }
        });

        txtVoltage.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtVoltage.setToolTipText("Enter Line's Voltage (in Volts)");
        txtVoltage.setPreferredSize(new java.awt.Dimension(80, 30));
        txtVoltage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVoltageActionPerformed(evt);
            }
        });

        txtFrequency.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFrequency.setToolTipText("Enter Frequency (in Hz)");
        txtFrequency.setPreferredSize(new java.awt.Dimension(80, 30));
        txtFrequency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFrequencyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlGeneralDataLayout = new javax.swing.GroupLayout(pnlGeneralData);
        pnlGeneralData.setLayout(pnlGeneralDataLayout);
        pnlGeneralDataLayout.setHorizontalGroup(
            pnlGeneralDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGeneralDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlGeneralDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnlGeneralDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtP, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVoltage, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlGeneralDataLayout.setVerticalGroup(
            pnlGeneralDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGeneralDataLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlGeneralDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblP)
                    .addComponent(txtP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlGeneralDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtVoltage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlGeneralDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlInitialState.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Initial State", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        buttonGroup1.add(rbInitialPF);
        rbInitialPF.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rbInitialPF.setText("Initial PF");
        rbInitialPF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbInitialPFActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbInitialQ);
        rbInitialQ.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rbInitialQ.setText("Initial Q (kVAr)");

        txtInitialValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtInitialValue.setToolTipText("Enter Initial Power Factor (cosφ) or Initial Reactive Power (Q)");
        txtInitialValue.setPreferredSize(new java.awt.Dimension(80, 30));
        txtInitialValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInitialValueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInitialStateLayout = new javax.swing.GroupLayout(pnlInitialState);
        pnlInitialState.setLayout(pnlInitialStateLayout);
        pnlInitialStateLayout.setHorizontalGroup(
            pnlInitialStateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtInitialValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlInitialStateLayout.createSequentialGroup()
                .addGroup(pnlInitialStateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInitialStateLayout.createSequentialGroup()
                        .addComponent(rbInitialPF, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInitialStateLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(rbInitialQ)))
                .addContainerGap())
        );
        pnlInitialStateLayout.setVerticalGroup(
            pnlInitialStateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInitialStateLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbInitialPF)
                .addGap(18, 18, 18)
                .addComponent(rbInitialQ)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtInitialValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlTargetState.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Target State", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        buttonGroup2.add(rbTargetPF);
        rbTargetPF.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rbTargetPF.setText("Target PF");

        buttonGroup2.add(rbTargetQ);
        rbTargetQ.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rbTargetQ.setText("Target Q (kVAr)");

        txtTargetValue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTargetValue.setToolTipText("Enter Target Power Factor (cosφ) or Target Reactive Power (Q)");
        txtTargetValue.setPreferredSize(new java.awt.Dimension(80, 30));
        txtTargetValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTargetValueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTargetStateLayout = new javax.swing.GroupLayout(pnlTargetState);
        pnlTargetState.setLayout(pnlTargetStateLayout);
        pnlTargetStateLayout.setHorizontalGroup(
            pnlTargetStateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTargetStateLayout.createSequentialGroup()
                .addGroup(pnlTargetStateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTargetStateLayout.createSequentialGroup()
                        .addComponent(rbTargetPF, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(rbTargetQ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(txtTargetValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlTargetStateLayout.setVerticalGroup(
            pnlTargetStateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTargetStateLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbTargetPF)
                .addGap(18, 18, 18)
                .addComponent(rbTargetQ)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTargetValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnCalculate.setBackground(new java.awt.Color(40, 130, 180));
        btnCalculate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCalculate.setForeground(new java.awt.Color(255, 255, 255));
        btnCalculate.setText("CALCULATE");
        btnCalculate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });

        pnlSystemType.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "System Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        buttonGroup3.add(rbIsSinglePhase);
        rbIsSinglePhase.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rbIsSinglePhase.setText("Single-Phase System");
        rbIsSinglePhase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbIsSinglePhaseActionPerformed(evt);
            }
        });

        buttonGroup3.add(rbIsThreePhase);
        rbIsThreePhase.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rbIsThreePhase.setText("Three-Phase System");
        rbIsThreePhase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbIsThreePhaseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSystemTypeLayout = new javax.swing.GroupLayout(pnlSystemType);
        pnlSystemType.setLayout(pnlSystemTypeLayout);
        pnlSystemTypeLayout.setHorizontalGroup(
            pnlSystemTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSystemTypeLayout.createSequentialGroup()
                .addGroup(pnlSystemTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rbIsSinglePhase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbIsThreePhase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 82, Short.MAX_VALUE))
        );
        pnlSystemTypeLayout.setVerticalGroup(
            pnlSystemTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSystemTypeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rbIsSinglePhase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbIsThreePhase))
        );

        pnlConnectionType.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Connection Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        buttonGroup4.add(rbIsDelta);
        rbIsDelta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rbIsDelta.setText("Delta (Δ)");

        buttonGroup4.add(rbIsStar);
        rbIsStar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rbIsStar.setText("Star (Υ)");

        javax.swing.GroupLayout pnlConnectionTypeLayout = new javax.swing.GroupLayout(pnlConnectionType);
        pnlConnectionType.setLayout(pnlConnectionTypeLayout);
        pnlConnectionTypeLayout.setHorizontalGroup(
            pnlConnectionTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConnectionTypeLayout.createSequentialGroup()
                .addComponent(rbIsDelta, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbIsStar, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlConnectionTypeLayout.setVerticalGroup(
            pnlConnectionTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConnectionTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlConnectionTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbIsDelta)
                    .addComponent(rbIsStar))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        btnResetAll.setBackground(new java.awt.Color(190, 60, 40));
        btnResetAll.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnResetAll.setForeground(new java.awt.Color(255, 255, 255));
        btnResetAll.setText("RESET ALL");
        btnResetAll.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetAll.setPreferredSize(new java.awt.Dimension(80, 25));
        btnResetAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetAllActionPerformed(evt);
            }
        });

        btn7.setBackground(new java.awt.Color(45, 65, 80));
        btn7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn7.setForeground(new java.awt.Color(255, 255, 255));
        btn7.setText("7");
        btn7.setFocusable(false);
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7ActionPerformed(evt);
            }
        });

        btn8.setBackground(new java.awt.Color(45, 65, 80));
        btn8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn8.setForeground(new java.awt.Color(255, 255, 255));
        btn8.setText("8");
        btn8.setFocusable(false);
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8ActionPerformed(evt);
            }
        });

        btn9.setBackground(new java.awt.Color(45, 65, 80));
        btn9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn9.setForeground(new java.awt.Color(255, 255, 255));
        btn9.setText("9");
        btn9.setFocusable(false);
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn9ActionPerformed(evt);
            }
        });

        btn4.setBackground(new java.awt.Color(45, 65, 80));
        btn4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn4.setForeground(new java.awt.Color(255, 255, 255));
        btn4.setText("4");
        btn4.setFocusable(false);
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });

        btn5.setBackground(new java.awt.Color(45, 65, 80));
        btn5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn5.setForeground(new java.awt.Color(255, 255, 255));
        btn5.setText("5");
        btn5.setFocusable(false);
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5ActionPerformed(evt);
            }
        });

        btn6.setBackground(new java.awt.Color(45, 65, 80));
        btn6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn6.setForeground(new java.awt.Color(255, 255, 255));
        btn6.setText("6");
        btn6.setFocusable(false);
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn6ActionPerformed(evt);
            }
        });

        btn1.setBackground(new java.awt.Color(45, 65, 80));
        btn1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn1.setForeground(new java.awt.Color(255, 255, 255));
        btn1.setText("1");
        btn1.setFocusable(false);
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });

        btn2.setBackground(new java.awt.Color(45, 65, 80));
        btn2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 255, 255));
        btn2.setText("2");
        btn2.setFocusable(false);
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });

        btn3.setBackground(new java.awt.Color(45, 65, 80));
        btn3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn3.setForeground(new java.awt.Color(255, 255, 255));
        btn3.setText("3");
        btn3.setFocusable(false);
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });

        btn0.setBackground(new java.awt.Color(45, 65, 80));
        btn0.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn0.setForeground(new java.awt.Color(255, 255, 255));
        btn0.setText("0");
        btn0.setFocusable(false);
        btn0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn0ActionPerformed(evt);
            }
        });

        btnDot.setBackground(new java.awt.Color(0, 0, 0));
        btnDot.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnDot.setForeground(new java.awt.Color(255, 255, 255));
        btnDot.setText(".");
        btnDot.setFocusable(false);
        btnDot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDotActionPerformed(evt);
            }
        });

        btnDEL.setBackground(new java.awt.Color(190, 60, 40));
        btnDEL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnDEL.setForeground(new java.awt.Color(255, 255, 255));
        btnDEL.setText("DEL");
        btnDEL.setFocusable(false);
        btnDEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDELActionPerformed(evt);
            }
        });

        btnAC.setBackground(new java.awt.Color(190, 60, 40));
        btnAC.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnAC.setForeground(new java.awt.Color(255, 255, 255));
        btnAC.setText("AC");
        btnAC.setFocusable(false);
        btnAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnACActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCalculatorLayout = new javax.swing.GroupLayout(pnlCalculator);
        pnlCalculator.setLayout(pnlCalculatorLayout);
        pnlCalculatorLayout.setHorizontalGroup(
            pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCalculatorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCalculatorLayout.createSequentialGroup()
                        .addComponent(pnlSystemType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlConnectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlCalculatorLayout.createSequentialGroup()
                        .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCalculatorLayout.createSequentialGroup()
                                .addComponent(pnlGeneralData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pnlInitialState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pnlTargetState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pnlCalculationResults, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCalculatorLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(pnlCalculatorLayout.createSequentialGroup()
                                                .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlCalculatorLayout.createSequentialGroup()
                                                    .addComponent(btn0, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(btnDot, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(btnDEL, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(pnlCalculatorLayout.createSequentialGroup()
                                                    .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(pnlCalculatorLayout.createSequentialGroup()
                                                .addComponent(btn7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btn8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addContainerGap())
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCalculatorLayout.createSequentialGroup()
                                        .addGap(119, 119, 119)
                                        .addComponent(btnAC, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCalculatorLayout.createSequentialGroup()
                                    .addComponent(btnCalculate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap()))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCalculatorLayout.createSequentialGroup()
                                .addComponent(btnResetAll, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
        );
        pnlCalculatorLayout.setVerticalGroup(
            pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCalculatorLayout.createSequentialGroup()
                .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCalculatorLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(btnCalculate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnResetAll, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(175, 175, 175)
                        .addComponent(btnAC))
                    .addGroup(pnlCalculatorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlCalculationResults, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCalculatorLayout.createSequentialGroup()
                        .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlGeneralData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlInitialState, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlTargetState, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pnlSystemType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlConnectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlCalculatorLayout.createSequentialGroup()
                        .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn7)
                            .addComponent(btn8)
                            .addComponent(btn9))
                        .addGap(35, 35, 35)
                        .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn4)
                            .addComponent(btn5)
                            .addComponent(btn6))
                        .addGap(35, 35, 35)
                        .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn1)
                            .addComponent(btn2)
                            .addComponent(btn3))
                        .addGap(35, 35, 35)
                        .addGroup(pnlCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn0)
                            .addComponent(btnDot)
                            .addComponent(btnDEL, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(pnlCalculator, "cardCalculator");

        btnBack1.setText("Back");
        btnBack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBack1ActionPerformed(evt);
            }
        });

        txtpnlTheory.setEditable(false);
        txtpnlTheory.setBorder(null);
        txtpnlTheory.setContentType("text/html"); // NOI18N
        txtpnlTheory.setOpaque(false);
        scrlpnlTheory.setViewportView(txtpnlTheory);

        javax.swing.GroupLayout pnlTheoryLayout = new javax.swing.GroupLayout(pnlTheory);
        pnlTheory.setLayout(pnlTheoryLayout);
        pnlTheoryLayout.setHorizontalGroup(
            pnlTheoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTheoryLayout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(btnBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(175, Short.MAX_VALUE))
            .addGroup(pnlTheoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrlpnlTheory)
                .addContainerGap())
        );
        pnlTheoryLayout.setVerticalGroup(
            pnlTheoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTheoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrlpnlTheory, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(btnBack1)
                .addGap(5, 5, 5))
        );

        getContentPane().add(pnlTheory, "cardTheory");

        scrlpnlGuide.setBackground(new java.awt.Color(242, 242, 242));
        scrlpnlGuide.setBorder(null);

        txtpnlGuide.setEditable(false);
        txtpnlGuide.setContentType("text/html"); // NOI18N
        txtpnlGuide.setOpaque(false);
        scrlpnlGuide.setViewportView(txtpnlGuide);

        btnBack2.setText("Back");
        btnBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBack2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlGuideLayout = new javax.swing.GroupLayout(pnlGuide);
        pnlGuide.setLayout(pnlGuideLayout);
        pnlGuideLayout.setHorizontalGroup(
            pnlGuideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGuideLayout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(btnBack2, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(175, Short.MAX_VALUE))
            .addGroup(pnlGuideLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrlpnlGuide)
                .addContainerGap())
        );
        pnlGuideLayout.setVerticalGroup(
            pnlGuideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGuideLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrlpnlGuide)
                .addGap(10, 10, 10)
                .addComponent(btnBack2)
                .addGap(5, 5, 5))
        );

        getContentPane().add(pnlGuide, "cardGuide");

        pnlAbout.setOpaque(false);

        txtpnlAbout.setEditable(false);
        txtpnlAbout.setBackground(new java.awt.Color(255, 255, 255));
        txtpnlAbout.setBorder(null);
        txtpnlAbout.setContentType("text/html"); // NOI18N
        txtpnlAbout.setOpaque(false);

        btnBack3.setText("Back");
        btnBack3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBack3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAboutLayout = new javax.swing.GroupLayout(pnlAbout);
        pnlAbout.setLayout(pnlAboutLayout);
        pnlAboutLayout.setHorizontalGroup(
            pnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAboutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtpnlAbout, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pnlAboutLayout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(btnBack3, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlAboutLayout.setVerticalGroup(
            pnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAboutLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(txtpnlAbout, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnBack3)
                .addGap(5, 5, 5))
        );

        getContentPane().add(pnlAbout, "cardAbout");

        mnHelp.setText("Help");

        mnitmTheory.setText("Theory");
        mnitmTheory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitmTheoryActionPerformed(evt);
            }
        });
        mnHelp.add(mnitmTheory);

        mnitmGuide.setText("Guide");
        mnitmGuide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitmGuideActionPerformed(evt);
            }
        });
        mnHelp.add(mnitmGuide);

        jMenuBar1.add(mnHelp);

        mnAbout.setText("About");
        mnAbout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnAboutMouseClicked(evt);
            }
        });
        jMenuBar1.add(mnAbout);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnACActionPerformed
        // Function of "AC" (All Clear) in virtual keypad
        if(selectedField != null)    // Checks if a text field is currently selected to avoid errors
        {
            selectedField.setText("");    // Clears the text of the focused field
        }
    }//GEN-LAST:event_btnACActionPerformed

    private void btnDELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDELActionPerformed
        // Function of "DEL" (Delete) in virtual keypad
        if(selectedField != null)    // Checks if a text field is currently selected to avoid errors
        {
            String currentText = selectedField.getText();    // Gets the current text from the focused field
            if(currentText.length() > 0)    // Field must not be empty in order to delete something in it
            {
                String newText = currentText.substring(0, currentText.length()-1);    // Create a new string excluding the last character
                selectedField.setText(newText);    // Sets the new text in the text field
            }
        }
    }//GEN-LAST:event_btnDELActionPerformed

    private void btnDotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDotActionPerformed
        // Function of "." (Dot) in virtual keypad
        if(selectedField != null)    // Checks if a text field is currently selected to avoid errors
        {
            if(selectedField.getText().contains(".") == false)    // Checks if there are multiple decimal points in the same number
            {
                enterNumber(".");    // Adds the dot only if it does not already exist (use of "enterNumber" method)
            }
        }
    }//GEN-LAST:event_btnDotActionPerformed

    private void btn0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn0ActionPerformed
        // Function of "0" in virtual keypad
        enterNumber("0");    // Adds number 0 in the text field (use of "enterNumber" method)
    }//GEN-LAST:event_btn0ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        // Function of "3" in virtual keypad
        enterNumber("3");    // Adds number 3 in the text field (use of "enterNumber" method)
    }//GEN-LAST:event_btn3ActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        // Function of "2" in virtual keypad
        enterNumber("2");    // Adds number 2 in the text field (use of "enterNumber" method)
    }//GEN-LAST:event_btn2ActionPerformed

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        // Function of "1" in virtual keypad 
        enterNumber("1");    // Adds number 1 in the text field (use of "enterNumber" method)
    }//GEN-LAST:event_btn1ActionPerformed

    private void btn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6ActionPerformed
        // Function of "6" in virtual keypad
        enterNumber("6");    // Adds number 6 in the text field (use of "enterNumber" method)
    }//GEN-LAST:event_btn6ActionPerformed

    private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
        // Function of "5" in virtual keypad
        enterNumber("5");    // Adds number 5 in the text field (use of "enterNumber" method)
    }//GEN-LAST:event_btn5ActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        // Function of "4" in virtual keypad
        enterNumber("4");    // Adds number 4 in the text field (use of "enterNumber" method)
    }//GEN-LAST:event_btn4ActionPerformed

    private void btn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9ActionPerformed
        // Function of "9" in virtual keypad
        enterNumber("9");    // Adds number 9 in the text field (use of "enterNumber" method)
    }//GEN-LAST:event_btn9ActionPerformed

    private void btn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8ActionPerformed
        // Function of "8" in virtual keypad
        enterNumber("8");    // Adds number 8 in the text field (use of "enterNumber" method)
    }//GEN-LAST:event_btn8ActionPerformed

    private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
        // Function of "7" in virtual keypad
        enterNumber("7");    // Adds number 7 in the text field (use of "enterNumber" method)
    }//GEN-LAST:event_btn7ActionPerformed

    private void btnResetAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetAllActionPerformed
        // Function of "Reset All" button
        
        // Clears all input text fields in order to start a new calculation
        txtP.setText("");
        txtVoltage.setText("");
        txtFrequency.setText("");
        txtInitialValue.setText("");
        txtTargetValue.setText("");

        // Restores text field borders to original style in order to clear any red error highlights
        txtP.setBorder(originalBorder);
        txtVoltage.setBorder(originalBorder);
        txtFrequency.setBorder(originalBorder);
        txtInitialValue.setBorder(originalBorder);
        txtTargetValue.setBorder(originalBorder);

        // Resets selections for all radio button groups
        buttonGroup1.clearSelection();
        buttonGroup2.clearSelection();
        buttonGroup3.clearSelection();
        buttonGroup4.clearSelection();

        txtpnlCalculationResults.setText("<html><div style='text-align: center; padding-top: 120px;'><i style='color: gray; font-size: 10px;'>Results will be shown here</i></div></html>");    // The original message is shown again
        
        txtP.requestFocus();    // The cursor is automatically placed in the Active Power text field

        // Prevention of problems due to visual changes
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_btnResetAllActionPerformed

    private void rbIsThreePhaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbIsThreePhaseActionPerformed
        // Function of Three-Phase radio button
        pnlConnectionType.setVisible(true);    // The connection type panel (Delta or Star) is revealed
        
        // Prevention of problems due to visual changes
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_rbIsThreePhaseActionPerformed

    private void rbIsSinglePhaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbIsSinglePhaseActionPerformed
        // Function of Single-Phase radio button
        pnlConnectionType.setVisible(false);    // The connection type panel (Delta or Star) is hidden
        
        // Prevention of problems due to visual changes
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_rbIsSinglePhaseActionPerformed

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        try
        {
            boolean isAllValid = true;    // Flag for tracking the overall validation status
            
            // Each text field is checked individually
            if(validateField(txtP) == false)
            {
                isAllValid = false;
            }
            if(validateField(txtVoltage) == false)
            {
                isAllValid = false;
            }
            if(validateField(txtFrequency) == false)
            {
                isAllValid = false;
            }
            if(validateField(txtInitialValue) == false)
            {
                isAllValid = false;
            }
            if(validateField(txtTargetValue) == false)
            {
                isAllValid = false;
            }

            // If even one of them is invalid, calculation stops
            if(isAllValid == false)
            {
                javax.swing.JOptionPane.showMessageDialog(this, "Please correct the fields outlined in red.", "Input Error", 0);
                return;    // Calculation stops
            }

            if((rbInitialPF.isSelected()==false) && (rbInitialQ.isSelected()==false))    // An input for the Initial State must be selected
            {
                throw new IllegalArgumentException("Please choose whether you are giving Initial PF or Initial Q!");
            }

            if((rbTargetPF.isSelected()==false) && (rbTargetQ.isSelected()==false))    // An input for the Target State must be selected
            {
                throw new IllegalArgumentException("Please choose whether you are giving Target PF or Target Q!");
            }
            
            if((rbIsThreePhase.isSelected()==false) && (rbIsSinglePhase.isSelected()==false))    // An input for the system type must be selected
            {
                throw new IllegalArgumentException("Please select System Type (Single-phase or Three-phase)!");
            }

            if((rbIsThreePhase.isSelected()==true) && ((rbIsDelta.isSelected()==false) && (rbIsStar.isSelected()==false)))    // An input for the connection type (for Three-Phase) must be selected
            {
                throw new IllegalArgumentException("Please select connection (Star or Delta)!");
            }

            // Convertion of string inputs to doubles, replacing commas with dots for universal compatibility
            double P = Double.parseDouble(txtP.getText().replace(',', '.'));
            double voltage = Double.parseDouble(txtVoltage.getText().replace(',', '.'));
            double frequency = Double.parseDouble(txtFrequency.getText().replace(',', '.'));
            double initialValue = Double.parseDouble(txtInitialValue.getText().replace(',', '.'));
            double targetValue = Double.parseDouble(txtTargetValue.getText().replace(',', '.'));

            
            // Based on user's choices the calculation method alters
            if(rbInitialPF.isSelected())
            {
                calculator.calculateCurrentStateFromPF(P, Double.parseDouble(txtInitialValue.getText().replace(',', '.')));
            }
            else
            {
                calculator.calculateCurrentStateFromQ(P, Double.parseDouble(txtInitialValue.getText().replace(',', '.')));
            }

            if(rbTargetPF.isSelected())
            {
                calculator.calculateDesiredStateFromPF(Double.parseDouble(txtTargetValue.getText().replace(',', '.')));
            }
            else
            {
                calculator.calculateTargetStateFromQ(Double.parseDouble(txtTargetValue.getText().replace(',', '.')));
            }

            
            if(P <= 0)    // Active Power must be a positive number
            {
                txtP.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));    // The border of text field becomes red to help the user locate the problem  
                throw new IllegalArgumentException("Active Power (P) must be a positive number (> 0)!");
            }

            if(voltage <= 0)    // Voltage must be a positive number
            {
                txtVoltage.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                throw new IllegalArgumentException("Voltage (V) must be a positive number (> 0)!");
            }

            if(frequency <= 0)    // Frequency must be a positive number
            {
                txtInitialValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                throw new IllegalArgumentException("Frequency (f) must be a positive number (> 0)!");
            }

        if(rbInitialPF.isSelected())
        {
            if((initialValue<=0) || (initialValue>=1))
            {
                txtInitialValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                throw new IllegalArgumentException("Initial PF (cosφ) must be between 0 and 1!");
            }
            else if(rbInitialQ.isSelected() == true)    // If Initial Value from user is Reactive Power
            {
                if(initialValue < 0)    // Reactive Power cannot be negative
                {
                    txtInitialValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                    throw new IllegalArgumentException("Initial Reactive Power (Q) must be a positive number (>0)!");
                }
            }
        }

            if(rbTargetPF.isSelected() == true)    // If Target Value from user is PF
            {
                if((targetValue<=0) || (targetValue>=1))
                {
                    txtTargetValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                    throw new IllegalArgumentException("Target PF (cosφ) must be between 0 and 1!");
                }
                if((rbInitialPF.isSelected()==true) && (targetValue<initialValue))    // PF Correction occurs only if target PF is a bigger number than Initial PF
                {
                    txtTargetValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                    throw new IllegalArgumentException("Target PF must exceed the Initial PF value!");
                }
            }
            else if(rbTargetQ.isSelected() == true)    // If Initial Value from user is Reactive Power
            {
                if(targetValue < 0)
                {
                    txtTargetValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                    throw new IllegalArgumentException("Target Reactive Power (Q) must be a positive number (>0)!");
                }
            }

            if(rbInitialPF.isSelected() == true)    // If Initial Value from user is PF
            {
                if((initialValue<=0) || (initialValue>=1))    // PF cannot be negative or exceed 1
                {
                    txtInitialValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                    throw new IllegalArgumentException("Initial PF (cosφ) must be between 0 and 1!");
                }
            }
            else if(rbInitialQ.isSelected() == true)    // If Initial Value from user is Reactive Power
            {
                if(initialValue < 0)    // Reactive Power cannot be negative
                {
                    txtInitialValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                    throw new IllegalArgumentException("Initial Reactive Power (Q) must be a positive number (>0)!");
                }
            }

            if(rbTargetPF.isSelected() == true)    // If Target Value from user is PF
            {
                if((targetValue<=0) || (targetValue>=1))
                {
                    txtTargetValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                    throw new IllegalArgumentException("Target PF (cosφ) must be between 0 and 1!");
                }
                if((rbInitialPF.isSelected()==true) && (targetValue<initialValue))    // PF Correction occurs only if target PF is a bigger number than Initial PF
                {
                    txtTargetValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                    throw new IllegalArgumentException("Target PF must exceed the Initial PF value!");
                }
            }
            else if(rbTargetQ.isSelected() == true)    // If Initial Value from user is Reactive Power
            {
                if(targetValue < 0)
                {
                    txtTargetValue.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2));
                    throw new IllegalArgumentException("Target Reactive Power (Q) must be a positive number (>0)!");
                }
            }

            
            // Calculation of required capacitor (from method "calculateCapacitor")
            calculator.calculateCapacitor(voltage, frequency, rbIsThreePhase.isSelected(), rbIsDelta.isSelected(), rbIsStar.isSelected());

            
            // Color for current reduction
            String reductionColor;
            if(calculator.currentReduction > 5)
            {
                reductionColor = "green";    // Green color for significant reduction
            }
            else
            {
                reductionColor = "orange";    // Orange color for limited reduction
            }

            
            // Use of HTML in order to define the content of the "Calculation Results" text pane
            String htmlResult = "<html>";
                   htmlResult += "<body style='font-family: sans-serif; font-size: 12px;'>";
                   htmlResult += "<table width='100%'  cellpadding='5'>";
                   htmlResult += "<tr>";
                   htmlResult += "<td width='50%'  valign='top'  style='border-right:1px solid gray;'>";
                   htmlResult += "<b style='color:blue; font-size:15px;'> SYSTEM DATA </b>"; 
                   htmlResult += "<hr><br>";
                   htmlResult += "<span style='color:orange;'>System Type:</span> <b>";
                   if(rbIsThreePhase.isSelected() == true)
                   {
                       htmlResult += "Three-phase";
                   }
                   else
                   {
                       htmlResult += "Single-phase";
                   }
                   if(rbIsThreePhase.isSelected() == true)
                   {
                       htmlResult += "</b><br>";
                       htmlResult += "<span style='color:#800000;'>Connection Type:</span> <b>";
                       if(rbIsDelta.isSelected() == true)
                       {
                           htmlResult += "Delta (Δ)";
                       }
                       else if(rbIsStar.isSelected() == true)
                       {
                           htmlResult += "Star (Y)";
                       }
                   }
                   htmlResult += "</b>";
                   htmlResult += "<br><hr><br>";
                   htmlResult += "<span style='color:gray;'> Active Power: </span><b>" + txtP.getText() + " kW</b><br>";
                   htmlResult += "<span style='color:gray;'> Voltage: </span><b>" + txtVoltage.getText() + " Volts</b><br>";
                   htmlResult += "<span style='color:gray;'> Frequency: </span><b>" + txtFrequency.getText() + " Hz</b><br>";
                   htmlResult += "<span style='color:gray;'> Initial Power Factor (cosφ): </span><b>" + String.format("%.3f", calculator.initialPF) + "</b><br>";
                   htmlResult += "<span style='color:gray;'> Initial Reactive Power: </span><b>" + String.format("%.3f", calculator.initialQ) + " kVAr</b><br>";
                   htmlResult += "<span style='color:gray;'> Initial Current: </span><b>" + String.format("%.3f", calculator.initialI) + " A</b><br>";
                   htmlResult += "</td>";
                   htmlResult += "<td width='50%'  valign='top'  style='padding-left: 15px;'>";
                   htmlResult += "<b style='color:blue; font-size:15px;'> RESULTS </b>";
                   htmlResult += "<br><hr><br>";
                   htmlResult += "<span style='color:gray;'> Target Power Factor (cosφ): </span><b>" + String.format("%.3f", calculator.targetPF) + "</b><br>";
                   htmlResult += "<span style='color:gray;'> Target Reactive Power: </span><b>" + String.format("%.3f", calculator.targetQ) + " kVAr</b><br>";
                   htmlResult += "<span style='color:gray;'> Target Current: </span><b>" + String.format("%.3f", calculator.targetI) + " A</b><br>";
                   htmlResult += "<span style='color:gray;'> Current Loss Reduction: </span><b style='color: " + reductionColor + ";'>" + String.format("%.2f", calculator.currentReduction) + "%</b>&darr;<br>";
                   htmlResult += "<hr>";
                   htmlResult += "<span style='color:gray;'> Capacitor's Reactive Power: </span><b style='color: red;'>" + String.format("%.3f", calculator.capacitorQ) + " kVAr</b><br>";
                   htmlResult += "<span style='color:gray;'> Capacitance: </span><b style='color: red;'>" + String.format("%.3f", calculator.capacitanceuF) + " μF</b><br>";
                   htmlResult += "</td>";
                   htmlResult += "</table>";
                   htmlResult += "</html>";

            txtpnlCalculationResults.setText(htmlResult);    // The text is applied to "Calculation Results" panel
        }

        // Catch block to handle any errors occured
        catch(IllegalArgumentException e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage() , "Missing Choice", 0);
        }
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void txtTargetValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTargetValueActionPerformed
        btnCalculate.doClick();    // If user presses "Enter", calculation will run
    }//GEN-LAST:event_txtTargetValueActionPerformed

    private void txtInitialValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInitialValueActionPerformed
        btnCalculate.doClick();    // If user presses "Enter", calculation will run
    }//GEN-LAST:event_txtInitialValueActionPerformed

    private void rbInitialPFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbInitialPFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbInitialPFActionPerformed

    private void txtFrequencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFrequencyActionPerformed
        btnCalculate.doClick();    // If user presses "Enter", calculation will run
    }//GEN-LAST:event_txtFrequencyActionPerformed

    private void txtVoltageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVoltageActionPerformed
        btnCalculate.doClick();    // If user presses "Enter", calculation will run
    }//GEN-LAST:event_txtVoltageActionPerformed

    private void txtPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPActionPerformed
        btnCalculate.doClick();    // If user presses "Enter", calculation will run
    }//GEN-LAST:event_txtPActionPerformed

    private void mnAboutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnAboutMouseClicked
        // Gets the layout manager responsible for switching between application screens
        java.awt.CardLayout card = (java.awt.CardLayout) getContentPane().getLayout();

        card.show(getContentPane(), "cardAbout");    // The panel with card name "cardAbout" is shown
    }//GEN-LAST:event_mnAboutMouseClicked

    private void btnBack3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBack3ActionPerformed
        // Gets the layout manager responsible for switching between application screens
        java.awt.CardLayout card = (java.awt.CardLayout) getContentPane().getLayout();

        card.show(getContentPane(), "cardCalculator");    // The panel with card name "cardCalculator" is shown. This is the main page
    }//GEN-LAST:event_btnBack3ActionPerformed

    private void mnitmTheoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitmTheoryActionPerformed
        // Gets the layout manager responsible for switching between application screens
        java.awt.CardLayout card = (java.awt.CardLayout) getContentPane().getLayout();

        card.show(getContentPane(), "cardTheory");    // The panel with card name "cardTheory" is shown
    }//GEN-LAST:event_mnitmTheoryActionPerformed

    private void mnitmGuideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitmGuideActionPerformed
        // Gets the layout manager responsible for switching between application screens
        java.awt.CardLayout card = (java.awt.CardLayout) getContentPane().getLayout();
        
        card.show(getContentPane(), "cardGuide");    // The panel with card name "cardGuide" is shown
    }//GEN-LAST:event_mnitmGuideActionPerformed

    private void btnBack1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBack1ActionPerformed
        java.awt.CardLayout card = (java.awt.CardLayout) getContentPane().getLayout();

        card.show(getContentPane(), "cardCalculator");
    }//GEN-LAST:event_btnBack1ActionPerformed

    private void btnBack2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBack2ActionPerformed
        java.awt.CardLayout card = (java.awt.CardLayout) getContentPane().getLayout();
        
        card.show(getContentPane(), "cardCalculator");
    }//GEN-LAST:event_btnBack2ActionPerformed

    
    
    
    
    // Method (boolean) to validate if the input in a text field is empty
    private boolean validateField(javax.swing.JTextField txtField)
    {
        try
        {
            String input = txtField.getText().replace(',', '.');
            double value = Double.parseDouble(input);    // If there is not a number, parsing fails
            
            // If parsing succeeds, border is reset and true is returned
            txtField.setBorder(originalBorder);
            return true;
        }
        
        catch(NumberFormatException e)
        {
            // Border becomes red if the input is empty or is not a number
            javax.swing.border.Border redLine = javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2);
            javax.swing.border.Border padding = javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2);
            txtField.setBorder(javax.swing.BorderFactory.createCompoundBorder(redLine, padding));
            return false;
        }
    }
    
    
    
    
    // Method (void) to add a number from virtual keypad to a text field
    private void enterNumber(String num)
    {
        if(selectedField != null)    // A text field must be selected
        {
            selectedField.setText(selectedField.getText() + num);    // Append number to current text of the field
        }
    }
    
    
    
    
    public static void main(String args[])
    {
        try
        {
            // Sets "Nimbus" theme for the application
            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception ex)
        {
            System.out.println("Nimbus is not found!");
        }
        
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new PFCApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn0;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnAC;
    private javax.swing.JButton btnBack1;
    private javax.swing.JButton btnBack2;
    private javax.swing.JButton btnBack3;
    private javax.swing.JButton btnCalculate;
    private javax.swing.JButton btnDEL;
    private javax.swing.JButton btnDot;
    private javax.swing.JButton btnResetAll;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel lblP;
    private javax.swing.JMenu mnAbout;
    private javax.swing.JMenu mnHelp;
    private javax.swing.JMenuItem mnitmGuide;
    private javax.swing.JMenuItem mnitmTheory;
    private javax.swing.JPanel pnlAbout;
    private javax.swing.JPanel pnlCalculationResults;
    private javax.swing.JPanel pnlCalculator;
    private javax.swing.JPanel pnlConnectionType;
    private javax.swing.JPanel pnlGeneralData;
    private javax.swing.JPanel pnlGuide;
    private javax.swing.JPanel pnlInitialState;
    private javax.swing.JPanel pnlSystemType;
    private javax.swing.JPanel pnlTargetState;
    private javax.swing.JPanel pnlTheory;
    private javax.swing.JRadioButton rbInitialPF;
    private javax.swing.JRadioButton rbInitialQ;
    private javax.swing.JRadioButton rbIsDelta;
    private javax.swing.JRadioButton rbIsSinglePhase;
    private javax.swing.JRadioButton rbIsStar;
    private javax.swing.JRadioButton rbIsThreePhase;
    private javax.swing.JRadioButton rbTargetPF;
    private javax.swing.JRadioButton rbTargetQ;
    private javax.swing.JScrollPane scrlpnlCaclulationResults;
    private javax.swing.JScrollPane scrlpnlGuide;
    private javax.swing.JScrollPane scrlpnlTheory;
    private javax.swing.JTextField txtFrequency;
    private javax.swing.JTextField txtInitialValue;
    private javax.swing.JTextField txtP;
    private javax.swing.JTextField txtTargetValue;
    private javax.swing.JTextField txtVoltage;
    private javax.swing.JTextPane txtpnlAbout;
    private javax.swing.JTextPane txtpnlCalculationResults;
    private javax.swing.JTextPane txtpnlGuide;
    private javax.swing.JTextPane txtpnlTheory;
    // End of variables declaration//GEN-END:variables
}