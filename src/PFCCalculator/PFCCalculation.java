/*
    Project: Power Factor Correction (PFC) Calculator
    Description: A Java Swing-based engineering application for Power Factor Correction analysis. Designed for industrial electrical systems, it provides required capacitor bank sizing for Single-Phase and Three-Phase systems while evaluating line current loss reduction.
    Author: Dimitrios Poulos
*/


package PFCCalculator;




public class PFCCalculation
{
    // Variable declaration
    public double P;    // Active Power in kW
    public double initialQ;    // Initial Reactive Power (kVAr)
    public double targetQ;    // Target Reactive Power (kVAr)
    public double capacitorQ;    // Capacitor's Reactive Power which must be provided (kVAr)
    public double capacitorQVAr;    // The former Reactive Power in VAr
    
    public double initialPF;    // Initial Power Factor
    public double targetPF;    // Target Power Factor
    public double phi1;    // Initial Phase angle (in radians)
    public double phi2;    // Target Phase angle (in radians)
    
    public double initialI;
    public double targetI;
    public double currentReduction;
    
    public double voltage;    // Voltage (Volts)
    public double frequency;    // Frequency (Hz)
    public double capacitanceF;    // Capacitor's Capacitance (Farads)
    public double capacitanceuF;    // The former Capacitance in microFarads (uF)
    
    
    
    
    
    // Calculation of Initial State from Active Power and Initial Power Factor
    // Formula: Q = P*tan(phi)
    public void calculateCurrentStateFromPF(double inputP, double inputInitialPF)
    {
        this.P = inputP;
        this.initialPF = inputInitialPF;
        this.phi1 = Math.acos(initialPF);    // Calculation of the initial phase angle
        this.initialQ = P*Math.tan(phi1);    // Calculation of the current Reactive Power
    }
    
    
    // Calculation of Initial State from Active Power and Initial Reactive Power
    // Formula: phi = atan(Q/P) and then PF = cos(phi)
    public void calculateCurrentStateFromQ(double inputP, double inputInitialQ)
    {
        this.P = inputP;
        this.initialQ = inputInitialQ;
        this.phi1 = Math.atan(initialQ/P);
        this.initialPF = Math.cos(phi1);
    }
    
    
    
    // Calculation of Target State from Active Power and Target Power Factor
    // Formula: Q = P*tan(phi)
    public void calculateDesiredStateFromPF(double inputTargetPF)
    {
        this.targetPF = inputTargetPF;
        this.phi2 = Math.acos(targetPF);
        this.targetQ = P*Math.tan(phi2);
    }
    
    
    // Calculation of Target State from Active Power and Target Reactive Power
    // Formula: phi = atan(Q/P) and then PF = cos(phi)
    public void calculateTargetStateFromQ(double inputTargetQ)
    {
        this.targetQ = inputTargetQ;
        this.phi2 = Math.atan(targetQ/this.P);
        this.targetPF = Math.cos(phi2);
    }
    
    
    
    public void calculateCapacitor(double inputVoltage, double inputFrequency, boolean isThreePhase, boolean isDelta, boolean isStar)
    {
        this.voltage = inputVoltage;
        this.frequency = inputFrequency;
        double omega = 2*Math.PI*frequency;    // Angular frequency (Formula: omega = 2*pi*f)
        
        
        this.capacitorQ = initialQ-targetQ;    // Calculation of required capacitor's Reactive Power
        this.capacitorQVAr = capacitorQ*1000.0;    // Conversion of capacitor's Reactive Power from kVAr to VAr
        
        
        if(isThreePhase == true)    // Current calculation for Three-Phase system
        {
            // Formula: I = P/(root(3)*V*cosphi)
            this.initialI = (P*1000.0)/(Math.sqrt(3)*voltage*initialPF);
            this.targetI = (P*1000.0)/(Math.sqrt(3)*voltage*targetPF);
        }
        else    // Current calculation for Single-Phase system
        {
            // Formula: I = P/(V*cosphi)
            this.initialI = (P*1000.0)/(voltage*initialPF);
            this.targetI = (P*1000.0)/(voltage*targetPF);
        }
        
        this.currentReduction = ((initialI-targetI)/initialI)*100.0;    // Calculation of reduction in line current (percentage)
        
        
        if(isThreePhase == true)    // Capacitance calculation for Three-Phase system
        {
            if((isDelta==true) && (isStar==false))    // Capacitance calculation for Delta connection
            {
                this.capacitanceF = capacitorQVAr/(3*omega*Math.pow(voltage, 2));    // Formula: C = Qc/(3*omega*V^2)
            }
            else if((isDelta==false) && (isStar==true))    // Capacitance calculation for Star connection
            {
                this.capacitanceF = capacitorQVAr/(omega*Math.pow(voltage, 2));    // Formula: C = Qc/(omega*V^2)
            }
        }
        
        else if(isThreePhase == false)    // Capacitance calculation for Single-Phase system
        {
            this.capacitanceF = capacitorQVAr/(omega*Math.pow(voltage, 2));    // Calculation of Capacitance in Farads. Formula: C = Qc/(omega*V^2)
        }
        
        this.capacitanceuF = capacitanceF*1000000.0;    // Conversion of capacitance from F to uF
    }
}