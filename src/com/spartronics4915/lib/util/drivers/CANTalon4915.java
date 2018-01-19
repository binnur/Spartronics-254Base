package com.spartronics4915.lib.util.drivers;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.spartronics4915.lib.util.CANProbe;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.MotorSafetyHelper;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.hal.HAL;

/* CANTalonPhoenix is a portability interface intended to facilitate
 * porting from CTRE CANTalon 2017 libs to CTRE Phoenix 2018.
 * This abstraction is useful as a buffer between all the
 * bad/weird stuff that CTRE does. It allows us to document
 * better, and even implement new features (e.g Talon
 * compatibility with Synthesis).
 * 
 * NB: we should endeavor to constrain the interface to those
 *   methods that we feel are useful.
 */

// From Phoenix docs:
// What are the units of my sensor?
// Position units are in the natural units of the sensor.
//  Quad (US Digital) 1024 CPR -> 4096
//  CTRE Quad relative  -> 4096
//  CTRE Quad absolute -> 4096
//  Pulse-width encoded position -> 4096 is 100%
//  AndyMark CIMcode    ->  80  (20 pulse -> 80 edges)
//
// Velocity is measured in sensor units per 100ms. This ensures
//  sufficient resolution regardless of the sensing strategy. For 
//  example, when using the CTRE Magnetic Encoder, 1u velocity 
//  represents 1/4096 of a rotation every 100ms. Generally you 
//  can multiply the velocity units by 600/UnitsPerRotation to obtain RPM.
//
// Tachometer velocity measurement is unique in that it measures 
//  time directly. As a result, the reported velocity is calculated 
//  where 1024 represents a full "rotation". This means that a velocity 
//  measurement of 1 represents 1/1024 of a rotation every 100ms

public class CANTalon4915 implements Sendable, MotorSafety
{

    static final int sDefaultTimeoutMS = 0; // 0 for no blocking. This is like the old behavior (I think).
    static final int sPidIdx = 0; // We're unsure about what this does.
    static final int sDefaultOrdinal = 0; // This probably does something specific on certain ParamEnums

    ControlMode mControlMode = ControlMode.Disabled;
    ControlMode mLastControlMode = null;
    NeutralMode mNeutralMode;
    int mDeviceId;
    double mLastSetpoint = 0;
    int mPidSlot;
    int mMaxVolts = 12;
    int mCodesPerRevolution; // Encoder codes per revolution
    TalonSRX mTalon = null;

    public CANTalon4915(int deviceNumber)
    {
        mDeviceId = deviceNumber;
        CANProbe canProbe = CANProbe.getInstance();
        if (canProbe.validateSRXId(deviceNumber))
        {
            mTalon = new TalonSRX(deviceNumber);
            //        this.changeMotionControlFramePeriod(config.MOTION_CONTROL_FRAME_PERIOD_MS);
            //        this.clearIAccum();
            //        this.clearMotionProfileHasUnderrun();
            //        this.clearMotionProfileTrajectories();
            //        this.clearStickyFaults();
            //        this.configFwdLimitSwitchNormallyOpen(config.LIMIT_SWITCH_NORMALLY_OPEN);
            //        this.configMaxOutputVoltage(config.MAX_OUTPUT_VOLTAGE);
            //        this.configNominalOutputVoltage(config.NOMINAL_VOLTAGE, -config.NOMINAL_VOLTAGE);
            //        this.configPeakOutputVoltage(config.PEAK_VOLTAGE, -config.PEAK_VOLTAGE);
            //        this.configRevLimitSwitchNormallyOpen(config.LIMIT_SWITCH_NORMALLY_OPEN);
            //        this.enableBrakeMode(config.ENABLE_BRAKE);
            //        this.enableCurrentLimit(config.ENABLE_CURRENT_LIMIT);
            //        this.enableForwardSoftLimit(config.ENABLE_SOFT_LIMIT);
            //        this.enableLimitSwitch(config.ENABLE_LIMIT_SWITCH, config.ENABLE_LIMIT_SWITCH);
            //        this.enableReverseSoftLimit(config.ENABLE_SOFT_LIMIT);
            //        this.enableZeroSensorPositionOnForwardLimit(false);
            //        this.enableZeroSensorPositionOnIndex(false, false);
            //        this.enableZeroSensorPositionOnReverseLimit(false);
            //        this.reverseOutput(false);
            //        this.reverseSensor(false);
            //        this.setAnalogPosition(0);
            //        this.setCurrentLimit(config.CURRENT_LIMIT);
            //        this.setExpiration(config.EXPIRATION_TIMEOUT_SECONDS);
            //        this.setForwardSoftLimit(config.FORWARD_SOFT_LIMIT);
            //        this.reverseOutput(config.INVERTED);
            //        this.setNominalClosedLoopVoltage(config.NOMINAL_CLOSED_LOOP_VOLTAGE);
            //        this.setPosition(0);
            //        this.setProfile(0);
            //        this.setPulseWidthPosition(0);
            //        this.setReverseSoftLimit(config.REVERSE_SOFT_LIMIT);
            //        this.setSafetyEnabled(config.SAFETY_ENABLED);
            //        this.setVelocityMeasurementPeriod(config.VELOCITY_MEASUREMENT_PERIOD);
            //        this.setVelocityMeasurementWindow(config.VELOCITY_MEASUREMENT_ROLLING_AVERAGE_WINDOW);
            //        this.setVoltageCompensationRampRate(config.VOLTAGE_COMPENSATION_RAMP_RATE);
            //        this.setVoltageRampRate(config.VOLTAGE_RAMP_RATE);
            //
            //        this.setStatusFrameRateMs(StatusFrameEnhanced.Status_1_General, config.GENERAL_STATUS_FRAME_RATE_MS);
            //        this.setStatusFrameRateMs(StatusFrameEnhanced.Status_2_Feedback0, config.FEEDBACK_STATUS_FRAME_RATE_MS); // XXX: was Feedback
            //        this.setStatusFrameRateMs(StatusFrameEnhanced.Status_3_Quadrature, config.QUAD_ENCODER_STATUS_FRAME_RATE_MS);
            //        this.setStatusFrameRateMs(StatusFrameEnhanced.Status_4_AinTempVbat,
            //                config.ANALOG_TEMP_VBAT_STATUS_FRAME_RATE_MS);
        }
    }
    
    public boolean isValid()
    {
        return mTalon != null;
    }
    
    public TalonSRX getTalon()
    {
        return mTalon;
    }

    public String dumpState()
    {
        if (mTalon != null)
        {
            @SuppressWarnings("deprecation") // Things that don't work are deprecated
            StringBuilder sb = new StringBuilder().append("isRevLimitSwitchClosed = ")
                    .append(this.isRevLimitSwitchClosed()).append("\n").append("getBusVoltage = ")
                    .append(mTalon.getBusVoltage()).append("\n")
                    .append("isForwardSoftLimitEnabled = ")
                    .append(this.isForwardSoftLimitEnabled()).append("\n")
                    .append("getFaultRevSoftLim = ")
                    .append(this.getFaultRevSoftLim()).append("\n")
                    .append("getStickyFaultOverTemp = ")
                    .append(this.getStickyFaultOverTemp()).append("\n")
                    .append("isZeroSensorPosOnFwdLimitEnabled = ")
                    .append(this.isZeroSensorPosOnFwdLimitEnabled()).append("\n")
                    .append("getMotionProfileTopLevelBufferCount = ")
                    .append(mTalon.getMotionProfileTopLevelBufferCount())
                    .append("\n").append("getNumberOfQuadIdxRises = ")
                    .append(this.getNumberOfQuadIdxRises()).append("\n")
                    .append("getInverted = ").append(mTalon.getInverted()).append("\n")
                    .append("getPulseWidthRiseToRiseUs = ").append(this.getPulseWidthRiseToRiseUs())
                    .append("\n")
                    .append("getError = ").append(this.getError()).append("\n")
                    .append("isSensorPresent = ")
                    .append(this.isSensorPresent(FeedbackDevice.CTRE_MagEncoder_Relative))
                    .append("\n")
                    .append("isControlEnabled = ").append(this.isControlEnabled()).append("\n")
                    .append("getTable = ")
                    //.append(this.getTable()).append("\n") // XXX: Sendable::getTable method has disappeared
                    .append("isEnabled = ").append(this.isEnabled()).append("\n")
                    .append("isZeroSensorPosOnRevLimitEnabled = ")
                    .append(this.isZeroSensorPosOnRevLimitEnabled())
                    .append("\n").append("isSafetyEnabled = ").append(this.isSafetyEnabled())
                    .append("\n")
                    .append("getOutputVoltage = ").append(this.getOutputVoltage()).append("\n")
                    .append("getTemperature = ")
                    .append(mTalon.getTemperature()).append("\n")
                    // .append("getSmartDashboardType = ")
                    // .append(this.getSmartDashboardType()).append("\n") // XXX: Sendable::getSmartDashboardType has disappeared
                    .append("getPulseWidthPosition = ")
                    .append(this.getPulseWidthPosition()).append("\n").append("getOutputCurrent = ")
                    .append(mTalon.getOutputCurrent()).append("\n")
                    .append("\n")
                    .append("isZeroSensorPosOnIndexEnabled = ")
                    .append(this.isZeroSensorPosOnIndexEnabled()).append("\n")
                    .append("getMotionMagicCruiseVelocity = ")
                    .append(this.getMotionMagicCruiseVelocity()).append("\n")
                    .append("getStickyFaultRevSoftLim = ").append(this.getStickyFaultRevSoftLim())
                    .append("\n")
                    .append("getFaultRevLim = ").append(this.getFaultRevLim()).append("\n")
                    .append("getEncPosition = ")
                    .append(this.getEncPosition()).append("\n").append("getIZone = ")
                    .append(this.getIZone()).append("\n")
                    .append("getAnalogInPosition = ").append(this.getAnalogInPosition())
                    .append("\n")
                    .append("getFaultUnderVoltage = ").append(this.getFaultUnderVoltage())
                    .append("\n")
                    .append("getCloseLoopRampRate = ").append(this.getCloseLoopRampRate())
                    .append("\n")
                    .append("toString = ").append(this.toString()).append("\n")
                    // .append("getMotionMagicActTrajPosition =
                    // ").append(this.getMotionMagicActTrajPosition()).append("\n")
                    .append("getF = ").append(this.getF()).append("\n").append("getClass = ")
                    .append(this.getClass())
                    .append("\n").append("getAnalogInVelocity = ")
                    .append(this.getAnalogInVelocity())
                    .append("\n")
                    .append("getI = ").append(this.getI()).append("\n")
                    .append("isReverseSoftLimitEnabled = ")
                    .append(this.isReverseSoftLimitEnabled()).append("\n")
                    // .append("getPIDSourceType = ").append(this.getPIDSourceType()).append("\n") // XXX Sendable change
                    .append("getEncVelocity = ")
                    .append(this.getEncVelocity()).append("\n")
                    .append("GetVelocityMeasurementPeriod = ")
                    .append(this.getVelocityMeasurementPeriod()).append("\n").append("getP = ")
                    .append(this.getP())
                    .append("\n").append("GetVelocityMeasurementWindow = ")
                    .append(this.getVelocityMeasurementWindow())
                    .append("\n").append("getDeviceID = ").append(mTalon.getDeviceID()).append("\n")
                    .append("getStickyFaultRevLim = ").append(this.getStickyFaultRevLim())
                    .append("\n")
                    // .append("getMotionMagicActTrajVelocity =
                    // ").append(this.getMotionMagicActTrajVelocity()).append("\n")
                    .append("getReverseSoftLimit = ").append(this.getReverseSoftLimit())
                    .append("\n")
                    .append("getD = ")
                    .append(this.getD()).append("\n").append("getFaultOverTemp = ")
                    .append(this.getFaultOverTemp())
                    .append("\n").append("getForwardSoftLimit = ")
                    .append(this.getForwardSoftLimit())
                    .append("\n")
                    .append("GetFirmwareVersion = ").append(mTalon.getFirmwareVersion())
                    .append("\n")
                    .append("getLastError = ").append(mTalon.getLastError()).append("\n")
                    .append("isAlive = ")
                    .append(this.isAlive()).append("\n").append("getPinStateQuadIdx = ")
                    .append(this.getPinStateQuadIdx())
                    .append("\n").append("getAnalogInRaw = ").append(this.getAnalogInRaw())
                    .append("\n")
                    .append("getFaultForLim = ").append(this.getFaultForLim()).append("\n")
                    .append("getSpeed = ")
                    .append(this.getSpeed()).append("\n").append("getStickyFaultForLim = ")
                    .append(this.getStickyFaultForLim()).append("\n")
                    .append("getFaultForSoftLim = ")
                    .append(this.getFaultForSoftLim()).append("\n")
                    .append("getStickyFaultForSoftLim = ")
                    .append(this.getStickyFaultForSoftLim()).append("\n")
                    .append("getClosedLoopError = ")
                    .append(this.getClosedLoopError()).append("\n").append("getSetpoint = ")
                    .append(this.getSetpoint())
                    .append("\n").append("isMotionProfileTopLevelBufferFull = ")
                    .append(mTalon.isMotionProfileTopLevelBufferFull()).append("\n")
                    .append("getDescription = ")
                    .append(this.getDescription()).append("\n").append("hashCode = ")
                    .append(this.hashCode()).append("\n")
                    .append("isFwdLimitSwitchClosed = ").append(this.isFwdLimitSwitchClosed())
                    .append("\n")
                    .append("getPinStateQuadA = ").append(this.getPinStateQuadA()).append("\n")
                    .append("getPinStateQuadB = ").append(this.getPinStateQuadB()).append("\n")
                    .append("GetIaccum = ")
                    .append(this.getIaccum()).append("\n").append("getFaultHardwareFailure = ")
                    .append(this.getFaultHardwareFailure()).append("\n").append("pidGet = ")
                    .append(this.pidGet())
                    .append("\n").append("getBrakeEnableDuringNeutral = ")
                    .append(this.getBrakeEnableDuringNeutral())
                    .append("\n").append("getStickyFaultUnderVoltage = ")
                    .append(this.getStickyFaultUnderVoltage())
                    .append("\n").append("getPulseWidthVelocity = ")
                    .append(this.getPulseWidthVelocity()).append("\n")
                    .append("GetNominalClosedLoopVoltage = ")
                    .append(this.getNominalClosedLoopVoltage())
                    .append("\n")
                    .append("getPosition = ").append(this.getPosition()).append("\n")
                    .append("getExpiration = ")
                    .append(this.getExpiration()).append("\n")
                    .append("getPulseWidthRiseToFallUs = ")
                    .append(this.getPulseWidthRiseToFallUs()).append("\n")
                    // .append("createTableListener = ").append(this.createTableListener()).append("\n")
                    .append("getControlMode = ").append(mTalon.getControlMode()).append("\n")
                    .append("getMotionMagicAcceleration = ")
                    .append(this.getMotionMagicAcceleration())
                    .append("\n")
                    .append("getControlMode = ").append(mTalon.getControlMode());
            return sb.toString();
        }
        else
        {
            return "Talon " + mDeviceId + " was not found on the CAN bus";
        }
    }

    // MotorSafety Interface { -------------------------------------------------------------
    @Override
    public String getDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getExpiration()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isAlive()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSafetyEnabled()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setExpiration(double arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSafetyEnabled(boolean arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopMotor()
    {
        // TODO Auto-generated method stub

    }
    // } MotorSafety

    // Sendable interface {
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSubsystem()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void initSendable(SendableBuilder arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setName(String arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSubsystem(String arg0)
    {
        // TODO Auto-generated method stub

    }

    // } Sendable Interface
    
    
    /* TalonSRX dispatch -------------------------------------------------------------------*/
    public double getOutputCurrent()
    {
        if(mTalon != null)
            return mTalon.getOutputCurrent();
        else
            return 0.0;
    }

    /**
     * Converts RPM to Native Unit velocity.
     * RPM is rotations per minute.
     * Native velocity is Native Units per 100 milliseconds.
     * <b>configEncoderCodesPerRev must have been called for
     * this to work!</b>
     * 
     * @param Rotations per minute
     * @return Native Units per 100 milliseconds
     */
    private int rpmToNativeVelocity(double rpm)
    {
        if (mCodesPerRevolution == 0)
            return (int) Math.round(rpm);
        double rotationsPer100MS = (rpm / 60) / 10;
        return (int) Math.round(rotationsPer100MS * mCodesPerRevolution);
    }

    /**
     * Converts Native Unit velocity to RPM.
     * RPM is rotations per minute.
     * Native velocity is Native Units per 100 milliseconds.
     * <b>configEncoderCodesPerRev must have been called for
     * this to work!</b>
     * 
     * @param Native unit velocity
     * @return Rotations per minute
     */
    private int nativeVelocityToRpm(double nativeVelocity)
    {
        if (mCodesPerRevolution == 0)
            return (int) Math.round(nativeVelocity);
        double nativeUnitsPerMinute = nativeVelocity * 10 * 60;
        return (int) Math.round(nativeUnitsPerMinute / mCodesPerRevolution);
    }

    /**
     * Converts encoder codes (encoder specific)
     * to wheel rotations.
     * <b>configEncoderCodesPerRev must have been called for
     * this to work!</b>
     * 
     * @param Absolute encoder codes (use {@link nativeVelocityToRpm} for
     *        non-absolute units)
     * @return Absolute wheel rotations
     */
    private double encoderCodesToRotations(double codes)
    {
        if (mCodesPerRevolution == 0)
            return codes;
        return codes / mCodesPerRevolution;
    }

    /**
     * Sets the output on the Talon, depending on the mode you're in.
     * This is basically the only thing the new API does better,
     * IMO. You need to maintain state with the old API, and this
     * preserves that behavior.
     * 
     * @param The output depending on the ControlMode you've set the motor to.
     */
    public void set(double value)
    {
        if (mTalon != null)
        {
            // We've integrated LazyCANTalon into here
            if (value != mLastSetpoint || mControlMode != mLastControlMode)
            {
                mTalon.set(mControlMode, value);
                mLastSetpoint = value;
                mLastControlMode = mControlMode;
            }
        }
    }

    /**
     * Sets the output on the Talon, with the mode specified explicitly.
     * This overrides the new-style method, so that we can maintain
     * state in the wrapper subsystem.
     *
     * @param A ControlMode dependent setpoint.
     * @see set(double)
     */
    public void set(ControlMode m, double value)
    {
        if (mTalon != null)
        {
            mControlMode = m;
            this.set(value);
        }
    }

    public void changeControlMode(ControlMode m)
    {
        this.mControlMode = m; // in SRX mode, set() requires controlmode
    }

    public void setControlMode(ControlMode m)
    {
        this.mControlMode = m;
    }

    public void setFeedbackDevice(FeedbackDevice d)
    {
        if (mTalon != null)
        {
            mTalon.configSelectedFeedbackSensor(d, sPidIdx, sDefaultTimeoutMS);
            mTalon.setSensorPhase(false);
        }
    }

    public void configEncoderCodesPerRev(int cpr)
    {
        mCodesPerRevolution = cpr;
    }

    public void setEncPosition(int p)
    {
        if (mTalon != null)
        {
            mTalon.getSensorCollection().setQuadraturePosition(p, sDefaultTimeoutMS);
        }
    }

    public void setPID(double p, double i, double d, double f, int izone, double closeLoopRampRate,
            int profile)
    {
        if (mTalon != null)
        {
            mTalon.config_kP(profile, p, sDefaultTimeoutMS);
            mTalon.config_kI(profile, i, sDefaultTimeoutMS);
            mTalon.config_kD(profile, d, sDefaultTimeoutMS);
            mTalon.config_kF(profile, f, sDefaultTimeoutMS);
            mTalon.config_IntegralZone(profile, izone, sDefaultTimeoutMS);
            double newRampRate = mMaxVolts / closeLoopRampRate;
            mTalon.configClosedloopRamp(newRampRate, sDefaultTimeoutMS);
            mTalon.configOpenloopRamp(newRampRate, sDefaultTimeoutMS);
        }
    }

    public void setMotionMagicAcceleration(double motMagicAccel)
    {
        if (mTalon != null)
        {
            mTalon.configMotionAcceleration(rpmToNativeVelocity(motMagicAccel), sDefaultTimeoutMS);
        }
    }

    public void setMotionMagicCruiseVelocity(double kDriveLowGearMaxVelocity)
    {
        if (mTalon != null)
        {
            mTalon.configMotionCruiseVelocity(rpmToNativeVelocity(kDriveLowGearMaxVelocity),
                    sDefaultTimeoutMS);
        }
    }

    public void clearIAccum()
    {
        if (mTalon != null)
            mTalon.setIntegralAccumulator(0, sPidIdx, sDefaultTimeoutMS);
    }

    public void clearMotionProfileHasUnderrun()
    {
        if (mTalon != null)
            mTalon.clearMotionProfileHasUnderrun(sDefaultTimeoutMS);
    }

    public void clearStickyFaults()
    {
        if (mTalon != null)
            mTalon.clearStickyFaults(sDefaultTimeoutMS);
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return void
     */
    public void configMaxOutputVoltage(double maxV)
    {
        // XXX: Really? Is configPeakOutputVoltage equivalent?
    }

    /**
     * Configure the nominal output voltage allowed.
     * <b>Because of how the new api works, min is ignored!</b>
     * 
     * @param max
     * @param <b>min (completely ignored)</b>
     */
    public void configNominalOutputVoltage(double max, double min)
    {
        if (mTalon != null)
        {
            // XXX: This was changed to use percentages, and just negate the percentage for the min.
            // That means that min doesn't do anything.
            mTalon.configNominalOutputForward(max / mMaxVolts, sDefaultTimeoutMS);
            mTalon.configNominalOutputReverse(max / mMaxVolts, sDefaultTimeoutMS);
        }
    }

    /**
     * Configure the peak output voltage allowed.
     * <b>Because of how the new api works, min is ignored!</b>
     * 
     * @param max
     * @param <b>min (completely ignored)</b>
     */
    public void configPeakOutputVoltage(double max, double min)
    {
        if (mTalon != null)
        {
            // XXX: This was changed to use percentages, and just negate the percentage for the min.
            // That means that min doesn't do anything.
            mTalon.configPeakOutputForward(max / mMaxVolts, sDefaultTimeoutMS);
            mTalon.configPeakOutputReverse(max / mMaxVolts, sDefaultTimeoutMS);
        }
    }

    public void enableBrakeMode(boolean s)
    {
        if (mTalon != null && s)
            mTalon.neutralOutput();
    }

    public void setCurrentLimit(int amps)
    {
        if (mTalon != null)
            mTalon.configPeakCurrentLimit(amps, sDefaultTimeoutMS);
    }

    /**
     * Reverses motor output.
     * 
     * @param Is inverted or not.
     */
    public void reverseOutput(boolean s)
    {
        if (mTalon != null)
        {
            mTalon.setInverted(s);
        }
    }

    /**
     * Configures the soft limit threshold on the forward sensor.
     * 
     * <b>Not backwards compatible</b>
     * 
     * @param l Limit in raw sensor units.
     */
    public void setForwardSoftLimit(int l)
    {
        if (mTalon != null)
        {
            mTalon.configForwardSoftLimitThreshold(l, sDefaultTimeoutMS);
        }
    }

    /**
     * Configures the soft limit threshold on the reverse sensor.
     * 
     * <b>Not backwards compatible</b>
     * 
     * @param l Limit in raw sensor units.
     */
    public void setReverseSoftLimit(int l)
    {
        if (mTalon != null)
        {
            mTalon.configReverseSoftLimitThreshold(l, sDefaultTimeoutMS);
        }
    }

    public void setNominalClosedLoopVoltage(double v)
    {
        if (mTalon != null)
        {
            // XXX: These are now in percentages, not volts... And there's no closed-loop only method.
            mTalon.configNominalOutputForward(v / mMaxVolts, sDefaultTimeoutMS);
            mTalon.configNominalOutputReverse(v / mMaxVolts, sDefaultTimeoutMS);
        }
    }

    public void setPosition(int d)
    {
        if (mTalon != null)
        {
            mTalon.getSensorCollection().setAnalogPosition(d, sDefaultTimeoutMS);
        }
    }

    public void setProfile(int p)
    {
        // Select which closed loop profile to use, and uses whatever PIDF gains and the such that are already there.
        mPidSlot = p;
        if (mTalon != null)
        {
            mTalon.selectProfileSlot(p, sPidIdx);
        }
    }

    public void setPulseWidthPosition(int p)
    {
        if (mTalon != null)
        {
            mTalon.getSensorCollection().setPulseWidthPosition(p, sDefaultTimeoutMS);
        }
    }

    public void setVelocityMeasurementPeriod(VelocityMeasPeriod p)
    {
        if (mTalon != null)
        {
            mTalon.configVelocityMeasurementPeriod(p, sDefaultTimeoutMS);
        }
    }

    public void setVelocityMeasurementWindow(int w)
    {
        if (mTalon != null)
        {
            mTalon.configVelocityMeasurementWindow(w, sDefaultTimeoutMS);
        }
    }

    public void setVoltageCompensationRampRate(double rampRate)
    {
        if (mTalon != null)
        {
            mTalon.configVoltageCompSaturation(rampRate, sDefaultTimeoutMS); // XXX: I have no idea if this is these are the right units.
        }
    }

    /**
     * Set the voltage ramp rate.
     * <b>This is no longer in volts/second, now it's the minimum
     * desired time to go from neutral to full throttle</b>
     * 
     * @param rampRate
     */
    public void setVoltageRampRate(double rampRate)
    {
        if (mTalon != null)
        {
            double newRampRate = mMaxVolts / rampRate;
            mTalon.configClosedloopRamp(newRampRate, sDefaultTimeoutMS);
            mTalon.configOpenloopRamp(newRampRate, sDefaultTimeoutMS);
        }
    }

    public void setStatusFrameRateMs(StatusFrameEnhanced statFrame, int rate)
    {
        if (mTalon != null)
        {
            mTalon.setStatusFramePeriod(statFrame, rate, sDefaultTimeoutMS);
        }
    }

    public void reverseSensor(boolean s)
    {
        if (mTalon != null)
        {
            mTalon.setSensorPhase(s);
        }
    }

    public void setAnalogPosition(int pos)
    {
        if (mTalon != null)
        {
            mTalon.getSensorCollection().setAnalogPosition(pos, sDefaultTimeoutMS);
        }
    }

    /**
     * Don't trust this method. It doesn't seem
     * to have a clear-cut equivalent in the new
     * API, so I'm using something similar.
     * There's also an int cast, so the mantissa
     * in the double is lost.
     * 
     * @param Current limit in amps.
     */
    public void setCurrentLimit(double l)
    {
        if (mTalon != null)
        {
            mTalon.configPeakCurrentLimit((int) l, sDefaultTimeoutMS); // XXX: Is this actually equivalent?
        }
    }

    public void enableForwardSoftLimit(boolean s)
    {
        if (mTalon != null)
        {
            mTalon.configForwardSoftLimitEnable(s, sDefaultTimeoutMS);
        }
    }

    /**
     * There is no equivalent in the new api for this.
     * You have to enable all or none, so you
     * should use those methods.
     * 
     * @deprecated
     * @return void
     */
    public void enableLimitSwitch(boolean fwd, boolean rev)
    {
    }

    public void enableReverseSoftLimit(boolean s)
    {
        if (mTalon != null)
        {
            mTalon.configReverseSoftLimitEnable(s, sDefaultTimeoutMS);
        }
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return void
     */
    public void enableZeroSensorPositionOnForwardLimit(boolean s)
    {
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return void
     */
    public void enableZeroSensorPositionOnIndex(boolean fwd, boolean rev)
    {
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return void
     */
    public void enableZeroSensorPositionOnReverseLimit(boolean s)
    {
    }

    public void configFwdLimitSwitchNormallyOpen(boolean s)
    {
        if (mTalon != null)
        {
            mTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, /*
                                                                                        * XXX:
                                                                                        * LimitSwitchSource
                                                                                        */
                    s ? LimitSwitchNormal.NormallyOpen : LimitSwitchNormal.NormallyClosed,
                    sDefaultTimeoutMS);
        }
    }

    public void configRevLimitSwitchNormallyOpen(boolean s)
    {
        if (mTalon != null)
        {
            mTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, /*
                                                                                        * XXX:
                                                                                        * LimitSwitchSource
                                                                                        */
                    s ? LimitSwitchNormal.NormallyOpen : LimitSwitchNormal.NormallyClosed,
                    sDefaultTimeoutMS);
        }
    }

    public boolean isRevLimitSwitchClosed()
    {
        if (mTalon != null)
        {
            return mTalon.getSensorCollection().isRevLimitSwitchClosed();
        }
        else
            return true;
    }

    public boolean isForwardSoftLimitEnabled()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eForwardSoftLimitEnable, sDefaultOrdinal,
                    sDefaultTimeoutMS) == 1 ? true : false;
        else
            return true;
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return void
     */
    public int getStickyFaultOverTemp()
    {
        return 0;
    }

    public boolean isZeroSensorPosOnFwdLimitEnabled()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eClearPositionOnLimitF, sDefaultOrdinal,
                    sDefaultTimeoutMS) == 1 ? true : false;
        else
            return false;
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return void
     */
    public int getNumberOfQuadIdxRises()
    {
        return 0;
    }

    public int getPulseWidthRiseToRiseUs()
    {
        if (mTalon != null)
            return mTalon.getSensorCollection().getPulseWidthRiseToRiseUs();
        else
            return 0;
    }

    public double getError()
    {
        if (mTalon != null)
            return mTalon.getClosedLoopError(sPidIdx);
        else
            return 0.0;
    }

    // FIXME: I can't find how to do this in the new API.
    //  transition notes suggest something to do with getPulseWidthRiseToRiseUs()
    public boolean isSensorPresent(FeedbackDevice d)
    {
        return true;
    }

    // FIXME: What's the difference between isControlEnabled and isEnabled?
    public boolean isControlEnabled()
    {
        if (mTalon != null)
            return mTalon.getControlMode() != ControlMode.Disabled ? true : false;
        else
            return false;
    }

    public boolean isEnabled()
    {
        if (mTalon != null)
            return mTalon.getControlMode() != ControlMode.Disabled ? true : false;
        else
            return false;
    }

    public boolean isZeroSensorPosOnRevLimitEnabled()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eClearPositionOnLimitR, sDefaultOrdinal,
                    sDefaultTimeoutMS) == 1 ? true : false;
        else
            return false;
    }

    public double getOutputVoltage()
    {
        if (mTalon != null)
            return mTalon.getMotorOutputVoltage();
        else
            return 0.0;
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return void
     */
    public void getSmartDashboardType()
    {
    }

    public int getPulseWidthPosition()
    {
        if (mTalon != null)
            return mTalon.getSensorCollection().getPulseWidthPosition();
        else
            return 0;
    }

    public boolean isZeroSensorPosOnIndexEnabled()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eClearPositionOnIdx, sDefaultOrdinal,
                    sDefaultTimeoutMS) == 1 ? true : false;
        else
            return false;
    }

    /**
     * Get motion magic cruise velocity.
     * 
     * @return Velocity native units.
     */
    public double getMotionMagicCruiseVelocity()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eMotMag_VelCruise, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    /**
     * Check if there's a reverse limit switch issue.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is there limit switch issue.
     */
    public boolean getFaultRevSoftLim()
    {
        if (mTalon != null)
        {
            Faults faults = new Faults();
            mTalon.getFaults(faults);
            return faults.ReverseSoftLimit;
        }
        else
            return false;
    }

    /**
     * Check if there's a reverse limit switch issue.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is there limit switch issue.
     */
    public boolean getFaultRevLim()
    {
        if (mTalon != null)
        {
            Faults faults = new Faults();
            mTalon.getFaults(faults);
            return faults.ReverseLimitSwitch;
        }
        else
            return false;
    }

    /**
     * Check if there's a reverse limit switch issue.
     * A sticky fault is just one that persists.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is there a limit switch issue.
     */
    public boolean getStickyFaultRevLim()
    {
        if (mTalon != null)
        {
            StickyFaults faults = new StickyFaults();
            mTalon.getStickyFaults(faults);
            return faults.ReverseLimitSwitch;
        }
        else
            return false;
    }

    /**
     * Check if there's a reverse limit switch issue.
     * A sticky fault is just one that persists.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is there a limit switch issue.
     */
    public boolean getStickyFaultRevSoftLim()
    {
        if (mTalon != null)
        {
            StickyFaults faults = new StickyFaults();
            mTalon.getStickyFaults(faults);
            return faults.ReverseSoftLimit;
        }
        else
            return false;
    }

    /**
     * Get the current encoder position in encoder native units.
     * 
     * @return Encoder position.
     */
    public int getEncPosition()
    {
        if (mTalon != null)
        {
            return mTalon.getSelectedSensorPosition(sPidIdx);
        }
        else
            return 0;
    }

    /**
     * Get the analog position of the encoder, in volts?
     * 
     * <b>Not backwards compatible</b>
     * 
     * @return Analog position (probably volts?)
     */
    public double getAnalogInPosition()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eAnalogPosition, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    /**
     * Check if the voltage in went under 6.5V.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is there a hardware failure.
     */
    public boolean getFaultUnderVoltage()
    {
        if (mTalon != null)
        {
            Faults faults = new Faults();
            mTalon.getFaults(faults);
            return faults.UnderVoltage;
        }
        else
            return false;
    }

    /**
     * The current ramp rate in closed loop mode.
     * 
     * @return Closed loop ramp rate.
     */
    public double getCloseLoopRampRate()
    {
        if (mTalon != null)
        {
            return mTalon.configGetParameter(ParamEnum.eClosedloopRamp, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        }
        else
            return 0.0;
    }

    /**
     * Get the position of the active trajectory.
     * 
     * @return Position of the active trajectory.
     */
    public double getMotionMagicActTrajPosition()
    {
        if (mTalon != null)
            return mTalon.getActiveTrajectoryPosition();
        else
            return 0.0;
    }

    /**
     * Get the currently set proportional of the PID.
     * 
     * @return Current proportional of the PID.
     */
    public double getP()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eProfileParamSlot_P, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    /**
     * Get the currently set feedforward of the PID.
     * 
     * @return Current proportional of the PID.
     */
    public double getF()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eProfileParamSlot_F, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    public int getAnalogInVelocity()
    {
        if (mTalon != null)
            return mTalon.getSensorCollection().getAnalogInVel();
        else
            return 0;
    }

    /**
     * Get the currently set integral of the PID.
     * 
     * @return Current derivative of the PID.
     */
    public double getI()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eProfileParamSlot_I, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    /**
     * Sets the integral zone. Who knows what that is.
     * 
     * @return The integral zone.
     */
    public double getIZone()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eProfileParamSlot_IZone, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    /**
     * This checks if the soft limit (switch) that's
     * reversed is enabled? I don't know.
     * 
     * @return Is the reverse(d?) soft limit (switch?) enabled
     */
    public boolean isReverseSoftLimitEnabled()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eReverseSoftLimitEnable, sDefaultOrdinal,
                    sDefaultTimeoutMS) == 1 ? true : false;
        else
            return false;
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return void
     */
    public void getPIDSourceType()
    {
        // not implemented - not available in 2018
    }

    public int getEncVelocity()
    {
        if (mTalon != null)
            return mTalon.getSelectedSensorVelocity(sPidIdx);
        else
            return 0;
    }

    /**
     * I think this is how fast we can get velocity measurements, but that's
     * just because they use simmilar terminology in their old documentation
     * when referring to pwm pulses. There's very little to work off here.
     * 
     * @return Velocity measurement speed?
     */
    public double getVelocityMeasurementPeriod()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eSampleVelocityPeriod, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    /**
     * This appears to set the amount of time that the Talon is allowed to take
     * to make velocity measurements?
     * 
     * @return
     */
    public double getVelocityMeasurementWindow()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eSampleVelocityWindow, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    /**
     * This <i>currently</i> gets if reverse soft limit is enabled.
     * <b>I don't know if that's what it did in the
     * old API</b>, because the documentation of the
     * old API was crap.
     * 
     * @return Is the forward soft limit (switch?) enabled
     */
    public double getReverseSoftLimit()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eReverseSoftLimitEnable, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    /**
     * Get the currently set derivative of the PID.
     * 
     * @return Current derivative of the PID.
     */
    public double getD()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eProfileParamSlot_D, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return 0
     */
    public boolean getFaultOverTemp()
    {
        return false;
    }

    /**
     * This gets if forward soft limit is enabled.
     * I don't know if that's what it did in the
     * old API, because the documentation of the
     * old API was crap.
     * 
     * @return Is the forward soft limit (switch?) enabled
     */
    public double getForwardSoftLimit()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eForwardSoftLimitEnable, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

    /**
     * <b>Not backwards compatible.</b>
     */
    public boolean getPinStateQuadIdx()
    {
        if (mTalon != null)
            return mTalon.getSensorCollection().getPinStateQuadIdx();
        else
            return false;
    }

    public int getAnalogInRaw()
    {
        if (mTalon != null)
            return mTalon.getSensorCollection().getAnalogInRaw();
        else
            return 0;
    }

    /**
     * Get sensor speed/velocity.
     * 
     * The speed units will be in the sensor's native ticks per 100ms.
     * For analog sensors, 3.3V corresponds to 1023 units. So a speed
     * of 200 equates to ~0.645 dV per 100ms or 6.451 dV per second.
     * If this is an analog encoder, that likely means 1.9548 rotations
     * per sec. For quadrature encoders, each unit corresponds a quadrature
     * edge (4X). So a 250 count encoder will produce 1000 edge events
     * per rotation. An example speed of 200 would then equate to 20%
     * of a rotation per 100ms, or 10 rotations per second.
     * 
     * @return Sensor speed in native units per 100ms.
     */
    public double getSpeed()
    {
        if (mTalon != null)
            return nativeVelocityToRpm(mTalon.getSelectedSensorVelocity(sPidIdx));
        else
            return 0;
    }

    /**
     * Check if there's a forward limit switch issue.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is there a hardware failure.
     */
    public boolean getFaultForLim()
    {
        if (mTalon != null)
        {
            Faults faults = new Faults();
            mTalon.getFaults(faults);
            return faults.ForwardLimitSwitch;
        }
        else
            return false;
    }

    /**
     * Check if there's a forward limit switch issue.
     * A sticky fault is just one that persists.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is there a limit switch issue.
     */
    public boolean getStickyFaultForLim()
    {
        if (mTalon != null)
        {
            StickyFaults faults = new StickyFaults();
            mTalon.getStickyFaults(faults);
            return faults.ForwardLimitSwitch;
        }
        else
            return false;
    }

    /**
     * Check if there's a forward soft limit issue. I think forward soft limit
     * refers to a limit switch, but CTRE docs are not exactly good
     * or comprehensive.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is there a hardware failure.
     */
    public boolean getFaultForSoftLim()
    {
        if (mTalon != null)
        {
            Faults faults = new Faults();
            mTalon.getFaults(faults);
            return faults.ForwardSoftLimit;
        }
        else
            return false;
    }

    /**
     * Check if there's a forward soft limit issue. I think forward soft limit
     * refers to a limit switch, but CTRE docs are not exactly good
     * or comprehensive.
     * A sticky fault is just one that persists.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is there a limit switch issue.
     */
    public boolean getStickyFaultForSoftLim()
    {
        if (mTalon != null)
        {
            StickyFaults faults = new StickyFaults();
            mTalon.getStickyFaults(faults);
            return faults.ForwardSoftLimit;
        }
        else
            return false;
    }

    /**
     * Get the PID error, if we're in a closed loop control mode.
     * 
     * @return The current PID error.
     */
    public int getClosedLoopError()
    {
        if (mTalon != null)
        {
            // This method appears to be mis-documented. The 
            // @param part of the javadoc says slotIdx (PID
            // gain slot), which makes the most sense, so
            // I'm going with that.
            return mTalon.getClosedLoopError(mPidSlot);
        }
        else
            return 0;
    }

    /**
     * Get the last value passed to the {@link set} method.
     * 
     * @return The last value passed to set.
     */
    public double getSetpoint()
    {
        return mLastSetpoint;
    }

    /**
     * Get the state of the forward limit switch.
     * <b>This might be buggy.</b>
     * 
     * @return State of the forward limit switch.
     */
    public boolean isFwdLimitSwitchClosed()
    {
        if (mTalon != null)
        {
            return mTalon.getSensorCollection().isFwdLimitSwitchClosed();
        }
        else
            return false;
    }

    /**
     * <b>Not backwards compatible.</b>
     */
    public boolean getPinStateQuadA()
    {
        if (mTalon != null)
        {
            return mTalon.getSensorCollection().getPinStateQuadA();
        }
        else
            return false;
    }

    /**
     * <b>Not backwards compatible.</b>
     */
    public boolean getPinStateQuadB()
    {
        if (mTalon != null)
            return mTalon.getSensorCollection().getPinStateQuadB();
        else
            return false;
    }

    /**
     * Gets the integral accumulation of the motor controller.
     * 
     * @return Integral accumulation.
     */
    public double getIaccum()
    {
        if (mTalon != null)
            return mTalon.getIntegralAccumulator(sPidIdx);
        else
            return 0.0;
    }

    /**
     * Check if there's a hardware failure.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is there a hardware failure.
     */
    public boolean getFaultHardwareFailure()
    {
        if (mTalon != null)
        {
            Faults faults = new Faults();
            mTalon.getFaults(faults);
            return faults.HardwareFailure;
        }
        else
            return false;
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return 0
     */
    public double pidGet() // wpilib PIDSource
    {
        return 0.;
    }

    /**
     * Get if the brake is enabled when the motor
     * becomes neutral.
     * 
     * @return If we brake during neutral.
     */
    public boolean getBrakeEnableDuringNeutral()
    {
        return mNeutralMode == NeutralMode.Brake ? true : false;
    }

    /**
     * Check if voltage dropped below 6.5V. A sticky fault is
     * just one that persists.
     * 
     * In the old api this returned an int, but the new one only
     * returns a boolean. I suspect this is just more bad CTRE code, and
     * they should have used a boolean. It's also possible that
     * it meant something else before.
     * <b>Not backwards compatible</b>
     * 
     * @return Is under voltage or not.
     */
    public boolean getStickyFaultUnderVoltage()
    {
        if (mTalon != null)
        {
            StickyFaults faults = new StickyFaults();
            mTalon.getStickyFaults(faults);
            return faults.UnderVoltage;
        }
        else
            return false;
    }

    public int getPulseWidthVelocity()
    {
        if (mTalon != null)
            return mTalon.getSensorCollection().getPulseWidthVelocity();
        else
            return 0;
    }

    /**
     * There is no equivalent in the new api for this.
     * 
     * @deprecated
     * @return 0
     */
    public double getNominalClosedLoopVoltage()
    {
        // the currently selected nominal closed loop voltage. Zero (Default) means feature is disabled.
        return 0.;
    }

    /**
     * Gets position. If {@link configEncoderCodesPerRev} has been called,
     * then this will return position in absolute wheel rotations. If that
     * hasn't been called, this will return absolute native units (see below).
     * 
     * When using analog sensors, 0 units corresponds to 0V, 1023 units
     * corresponds to 3.3V
     * When using an analog encoder (wrapping around 1023 to 0 is possible) the
     * units are still
     * 3.3V per 1023 units.
     * 
     * @return Absolute position in raw sensor units or wheel rotations.
     */
    public double getPosition()
    {
        // When using analog sensors, 0 units corresponds to 0V, 1023 units corresponds to 3.3V 
        // When using an analog encoder (wrapping around 1023 to 0 is possible) the units are still 
        // 3.3V per 1023 units.
        if (mTalon != null)
        {
            return encoderCodesToRotations(mTalon.getSelectedSensorPosition(sPidIdx));
        }
        else
            return 0.0;
    }

    public int getPulseWidthRiseToFallUs()
    {
        if (mTalon != null)
            return mTalon.getSensorCollection().getPulseWidthRiseToFallUs();
        else
            return 0;
    }

    /**
     * Get the acceleration of the motion magic controller.
     * If units are configured its in RPM per second,
     * otherwise it's native units.
     * 
     * @return Acceleration of the motion magic controller.
     */
    public double getMotionMagicAcceleration()
    {
        if (mTalon != null)
            return mTalon.configGetParameter(ParamEnum.eMotMag_Accel, sDefaultOrdinal,
                    sDefaultTimeoutMS);
        else
            return 0.0;
    }

}
