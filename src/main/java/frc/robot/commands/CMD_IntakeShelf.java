// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.GlobalVariables;
import frc.robot.Constants.ElbowConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.subsystems.SUB_Elbow;
import frc.robot.subsystems.SUB_Elevator;
import frc.robot.subsystems.SUB_FiniteStateMachine;
import frc.robot.subsystems.SUB_FiniteStateMachine.RobotState;
import frc.robot.subsystems.SUB_Intake;


public class CMD_IntakeShelf extends SequentialCommandGroup {
  public CMD_IntakeShelf(SUB_Elbow p_elbow, SUB_Elevator p_elevator, SUB_Intake p_intake,
   SUB_FiniteStateMachine p_finiteStateMachine, GlobalVariables p_variables
   ) {
    addCommands(
      new CMD_setState(p_finiteStateMachine, RobotState.INTAKE),
      new SequentialCommandGroup(
        new CMD_ElevatorSetPosition(p_elevator, ElevatorConstants.kElevatorShelf),
        new CMD_CheckElbowSafe(p_elevator),
        new CMD_ElbowSetPosition(p_elbow, ElbowConstants.kElbowForwards)
      ),
      new CMD_IntakeOn(p_intake, p_variables),
      new CMD_IntakeCheck(p_intake),
      new CMD_Hold(p_intake, p_elbow, p_elevator, p_finiteStateMachine, p_variables)
    );
  }
}
