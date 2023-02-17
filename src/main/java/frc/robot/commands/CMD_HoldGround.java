// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.GlobalVariables;
import frc.robot.Constants.ElbowConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.subsystems.SUB_Elbow;
import frc.robot.subsystems.SUB_Elevator;
import frc.robot.subsystems.SUB_FiniteStateMachine;
import frc.robot.subsystems.SUB_Intake;
import frc.robot.subsystems.SUB_FiniteStateMachine.RobotState;


public class CMD_HoldGround extends SequentialCommandGroup {
  public CMD_HoldGround(SUB_Intake p_intake, SUB_Elbow p_elbow, SUB_Elevator p_elevator,
    SUB_FiniteStateMachine p_finiteStateMachine, GlobalVariables p_variables
    ) {
    addCommands(
      new CMD_setState(p_finiteStateMachine, RobotState.STOW),
      new CMD_IntakeHold(p_intake, p_variables),
      new ParallelCommandGroup(
        new CMD_ElevatorSetPosition(p_elevator, ElevatorConstants.kElevatorStow),
        new CMD_ElbowSetPosition(p_elbow, ElbowConstants.kElbowStow)
      )
    );
  }
}
