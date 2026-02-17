# Power-Factor-Correction-Calculator

The **PFC Analysis Tool** is a specialized engineering software developed to simulate, analyze, and optimize electrical power systems. It solves the **Reactive Power Compensation** problem by calculating the exact capacitor bank size ($Q\_c$) required to shift the power factor from an initial state $\\cos \\phi\_{initial}$ to a target state $\\cos \\phi\_{target}$.

Designed for educational and industrial analysis, the application supports both **Single-Phase** and **Three-Phase** topologies, implementing vector analysis and adhering to standard power quality principles.


-----
-----


## Calculation Results
The tool generates a comprehensive technical report, including:
* **Target State:** Target Reactive Power ($Q\_{target}$) and Target Power Factor ($\\cos\\phi\_{target}$) are displayed.
* **Required Capacitor's Reactive Power ($Q\_c$):** Total required capacitor's reactive power in $kVAr$.
* **Capacitance per Branch ($C$):** Precise required value in $\\mu F$.
* **Loss Reduction Percentage:** Estimated percentage reduction in line current and ohmic ($I^2R$) losses.
* **RMS Current Comparison:** Pre-calculation vs Post-calculation current levels.


-----


## User Experience \& Functional Options
The application is built around user-defined parameters that simulate real-world industrial scenarios.

### 1) State Definition (Initial vs Target)
The core logic requires the user to define two distinct operating points:
* **Initial State ($\\cos\\phi\_{initial}$, $Q\_{initial}$):** Represents the current, uncompensated status of the load. This is typically an inductive state with a low power factor (e.g., 0.70), resulting in high current demand and energy waste. This state should be improved.
* **Target State ($\\cos\\phi\_{target}$, $Q\_{target}$):** Represents the desired efficiency goal. The user selects the target power factor (bigger number than Initial PF) to meet utility regulations, reduce losses and result in better performance in general.

### 2) System Type \& Connection Type Configuration
* **System Type:** Toggle between **Single-Phase** and **Three-Phase** systems.
* **Connection Type:** For Three-Phase analysis, users must select between **Delta ($\\Delta$)** and **Star ($Y$)** capacitor bank configurations. This choice is vital as it alters the voltage stress on the components and the resulting Farad requirements.

### 3) Smart Validation \& Defensive Logic
* **Input Validation:** The tool prevents unphysical inputs (e.g. Target PF lower than Initial PF, or values $< 0$).
* **Visual Alerts:** Whenever an input is false, the borders of text fields (in which a problem is detected) become red in order to help the user find the location of the issue.


-----


## Mathematical Logic
### 1) The Power Triangle Analysis
Every inductive load (motors, transformers) consumes two types of power:
* **Active Power ($P$):** This power performs actual work in the system, such as generating heat, light, or mechanical torque.(measured in $kW$).
* **Reactive Power ($Q$):** This power oscillates between the source and the load. It is essential for creating magnetic fields but does not perform useful work (measured in $kVAr$).
The formula is defined by the **Power Triangle** (geometry):

$$S = \sqrt{P^2 + Q^2} \quad \text{and} \quad PF = \cos \phi = \frac{P}{S}$$

### 2) Power Formulas & Line Current ($I_{RMS}$)
The application calculates the line current drawn from the grid. The calculation method depends on the System Type. Since Active Power ($P$) is constant and does not change during the correction process, improving the Power Factor ($PF-\cos\phi$) directly reduces the current.
* **Single-Phase System:**
  
$$P = V \cdot I \cdot PF \implies I = \frac{P}{V \cdot PF}$$

* **Three-Phase System:**
  
$$P = \sqrt{3} \cdot V_L \cdot I_L \cdot PF \implies I_L = \frac{P}{\sqrt{3} \cdot V_L \cdot PF}$$

### 3) Calculating Capacitor's Reactive Power ($Q_c$)
This value represents the exact amount of reactive power the capacitor bank must "generate" to cancel out the inductive component of the load.<br>
To improve the Power Factor from an initial value $\cos\phi_{initial}$ to a target $\cos\phi_{target}$, the application calculates the required **Capacitor's Reactive Power ($Q_c$)**.
The formula is derived from the difference in the tangents of the phase angles:

$$Q_c = P \cdot [ \tan(\arccos(PF_{initial})) - \tan(\arccos(PF_{target}))]$$

Alternatively, if the reactive power components are known directly, the formula which can be used is:

$$Q_c = Q_{initial} - Q_{target}$$

### 4) Capacitor's Sizing
The tool calculates the capacitance ($C$) required per branch. This value (which is determined by the connection topology), depends on the angular frequency ($\omega$), as well as on Voltage.

$$\text{Angular Frequency (rad/s): }\omega = 2 \pi f$$

* **Delta Connection ($\Delta$):** Phase voltage equals line voltage ($V_{ph} = V_L$). Line current is $\sqrt{3}$ times the phase current ($I_L = \sqrt{3}·I_{ph}$).

$$C_{\Delta} = \frac{Q_c}{3 \cdot \omega \cdot V_{L}^2}$$

* **Star Connection ($Y$):** Phase voltage is reduced ($V_{ph} = V_L / \sqrt{3}$). Line current equals phase current ($I_L = I_{ph}$)
 
$$C_{Y} = \frac{Q_c}{\omega \cdot V_{L}^2}$$

### 5) Ohmic Loss Reduction
A primary benefit of Power Factor Correction is the reduction of thermal losses in cables and transformers. This is a major advantage, since current causes significant damage in cables which results in losses. These losses are governed by Joule's First Law:

$$P_{loss} = I^2 \cdot R$$

Because losses are proportional to the **square** of the current, even a small improvement in Power Factor will result in beneficial energy savings. The app calculates this reduction (percentage) using the formula:

$$\text{Loss Reduction} = \left[ 1 - \left( \frac{PF_{initial}}{PF_{target}} \right)^2 \right] \cdot 100 \, \%$$


-----


## Interface Preview
### 1) Calculation Results
The interface displays a complete calculation for a **Three-Phase Delta System**. The left panel summarizes the system parameters (Initial State), while the right panel calculates and displays Target State, the required Capacitor Bank (**26.794 kVAr**), the physical Capacitance ($537 \mu F$), and the achieved Loss Reduction (**22.22%**). The example was executed with inputs: **Active Power ($P$) = $50 kW$, Voltage ($V$) = $230 V$, Frequency ($f$) = $50 Hz$, Initial $PF$ = $0.70$, and Target $PF$ = $0.90$.**

![Calculation Results](assets/screenshot1.png)

### 2. Smart Validation System
The application features defensive programming to ensure data validity. In this example, the tool detects a missing value in the "Target State" section. The specific field is highlighted with a **red border**, and an error dialog is displayed in order to guide the user to correct the input before proceeding.

![Validation Logic](assets/screenshot2.png)


-----


## Software
* **Language:** ***Java (JDK 8+)***
* **GUI Framework:** ***Java Swing***
* **UI Design:** ***Nimbus Look \& Feel***
* **Development Environment:** ***Apache NetBeans IDE***


---


## Project's Files Structure
* `src/PFCCalculator/PFCApp.java`: The main GUI layer and event handling.
* `src/PFCCalculator/PFCCalculation.java`: The core engineering-mathematical logic and formulas.
* `dist/`: Pre-compiled executable JAR file.
* `assets/`: Application's screenshots.


-----


## Installation & Execution
### Prerequisites
* **Java Runtime Environment (JRE) 8** or higher must be installed.
### How to Run
1. **Download** the app or clone the repository.
2. Navigate to the `dist/` folder.
3. Open a terminal, Command Prompt or Windows Powershell and run:
   ```bash
   java -jar PFC-Calculator.jar


---
---


## Developer
**Dimitrios Poulos** <br>
*Electrical \& Computer Engineering Student*
* **Release Date:** February 2026
