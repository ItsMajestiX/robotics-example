// RobotBuilder Version: 3.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package frc.robot.subsystems;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANEncoder;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.*;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class TurretSubsystem extends SubsystemBase {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // _BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private CANSparkMax hoodMotor;
    // private Encoder quadratureEncoder1;
    private CANEncoder canEncoder;
    private PIDController hoodPID;
    private boolean closedLoop = true;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    /**
    *
    */
    public TurretSubsystem() {
        // _BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        hoodMotor = new CANSparkMax(5, MotorType.kBrushless);

        hoodMotor.restoreFactoryDefaults();
        hoodMotor.setInverted(false);
        hoodMotor.setIdleMode(IdleMode.kCoast);
        hoodMotor.setSmartCurrentLimit(30);

        /* quadratureEncoder1 = new Encoder(1, 2, false, EncodingType.k4X);
        addChild("Quadrature Encoder 1", quadratureEncoder1);
        quadratureEncoder1.setDistancePerPulse(1.0);
        quadratureEncoder1.setPIDSourceType(PIDSourceType.kRate); */

        canEncoder = hoodMotor.getAlternateEncoder(AlternateEncoderType.kQuadrature, 4096);
        canEncoder.setInverted(true);
        canEncoder.setPositionConversionFactor(16.822429906542056074766355140187);

        hoodPID = new PIDController(1.0, 0.0, 0.0, 0.02);
        addChild("Hood PID", hoodPID);
        hoodPID.setTolerance(0.01);

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Hood Encoder", canEncoder.getPosition());
        if (closedLoop) {
            if (!hoodPID.atSetpoint()) {
                double pidValue = hoodPID.calculate(canEncoder.getPosition(), hoodPID.getSetpoint());
                double feedforward = Math.signum(pidValue) * 0.9;
                hoodMotor.set(pidValue + feedforward);
            }
            else {
                hoodMotor.set(0);
            }
        }
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run when in simulation

    }

    public void setSetpoint(double setpoint) {
        closedLoop = true;
        hoodPID.setSetpoint(setpoint);
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public boolean isAtSetpoint() {
        return hoodPID.atSetpoint();
    }

    public void setPower(double power) {
        closedLoop = false;
        SmartDashboard.putNumber("Motor Power", power);
        hoodMotor.set(power);
    }
}
