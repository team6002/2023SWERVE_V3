// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import java.util.function.BooleanSupplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.SUB_FiniteStateMachine.RobotState;
import frc.robot.AUTO.*;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final GlobalVariables m_variable = new GlobalVariables();
  private final SUB_Elbow m_elbow = new SUB_Elbow();
  private final SUB_Elevator m_elevator = new SUB_Elevator();
  private final SUB_Blinkin m_blinkin = new SUB_Blinkin();
  private final SUB_FiniteStateMachine m_finiteStateMachine = new SUB_FiniteStateMachine();
  private final SUB_LimeLight m_limeLight = new SUB_LimeLight(m_blinkin, m_finiteStateMachine);
  public final SUB_Drivetrain m_robotDrive = new SUB_Drivetrain(m_blinkin, m_finiteStateMachine, m_limeLight);
  private final SUB_Intake m_intake = new SUB_Intake();
  private final AUTO_Trajectory m_trajectory = new AUTO_Trajectory(m_robotDrive);
  private final BooleanSupplier IntakeToggle = () -> m_finiteStateMachine.getState() == RobotState.INTAKE;
  private final BooleanSupplier DropGround = () -> m_variable.getDropLevel() == 1;
  // The driver's controller
  CommandXboxController m_operatorController = new CommandXboxController(1);
  CommandXboxController m_driverControllerTrigger = new CommandXboxController(0);
  Trigger xButton = m_driverControllerTrigger.leftBumper();
  Pose2d m_reset = new Pose2d(0, 0, Rotation2d.fromDegrees(0));

  /**
   * The container for the robot. Contains subsystems, IO devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    m_robotDrive.setDefaultCommand(new CMD_DriveCommand(m_robotDrive, m_driverControllerTrigger));
    // m_elevator.setDefaultCommand(new CMD_ElevatorSetPower(m_elevator, m_driverControllerTrigger));
  }
  boolean pressed = false;
  private void configureButtonBindings() {

    m_driverControllerTrigger.povUp().onTrue(new InstantCommand(() -> zeroGyroAngle()));
    m_driverControllerTrigger.povDown().onTrue(new InstantCommand(() -> m_robotDrive.resetOdometry(m_reset)));

    // m_driverControllerTrigger.y().onTrue(new CMD_DriveAlignTag(m_robotDrive, m_limeLight));
    // m_driverControllerTrigger.x().onTrue(new CMD_DriveAlignLeft(m_robotDrive, m_limeLight));
    // m_driverControllerTrigger.b().onTrue(new CMD_DriveAlignRight(m_robotDrive, m_limeLight));

    m_driverControllerTrigger.leftBumper().onTrue(new ConditionalCommand((
      new CMD_IntakeShelf(m_elbow, m_elevator, m_intake, m_finiteStateMachine, m_variable)
        .until(m_driverControllerTrigger.back().onTrue(new CMD_Hold(m_intake, m_elbow, m_elevator, m_finiteStateMachine, m_variable)))),
      new CMD_IntakeGround(m_elbow, m_elevator, m_intake, m_finiteStateMachine, m_variable)
        .until(m_driverControllerTrigger.back().onTrue(new CMD_Hold(m_intake, m_elbow, m_elevator, m_finiteStateMachine, m_variable)))
      ,IntakeToggle));
    
    m_driverControllerTrigger.x().onTrue(new ConditionalCommand(
      new CMD_PlaceGround(m_elevator, m_intake, m_elbow, m_finiteStateMachine, m_variable),
      new CMD_Place(m_elevator, m_intake, m_elbow, m_finiteStateMachine, m_variable),
      DropGround
    ));
    m_driverControllerTrigger.a().onTrue(new CMD_ToggleDropLevel(m_variable));

    m_driverControllerTrigger.b().onTrue(new CMD_ToggleIntakeState(m_variable));
  }

    public void zeroGyroAngle() {
      m_robotDrive.zeroAngle();
    }

}