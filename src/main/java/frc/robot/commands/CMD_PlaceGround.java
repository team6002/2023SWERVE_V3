// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.GlobalVariables;
import frc.robot.Constants.ElbowConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.subsystems.SUB_Elbow;
import frc.robot.subsystems.SUB_Elevator;
import frc.robot.subsystems.SUB_FiniteStateMachine;
import frc.robot.subsystems.SUB_Intake;
import frc.robot.subsystems.SUB_FiniteStateMachine.RobotState;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class CMD_PlaceGround extends SequentialCommandGroup {
  /** Creates a new CMD_PlaceGround. */
  /** Creates a new CMD_PlaceThirdLevel. */
  SUB_Elevator m_elevator;
  SUB_Intake m_intake;
  SUB_Elbow m_elbow;
  GlobalVariables m_variables;
  SUB_FiniteStateMachine m_finiteStateMachine;
  public CMD_PlaceGround(SUB_Elevator p_elevator, SUB_Intake p_intake, SUB_Elbow p_elbow, 
  SUB_FiniteStateMachine p_finiteStateMachine, GlobalVariables p_variables
  ) {
    m_elevator = p_elevator;
    m_intake = p_intake;
    m_elbow = p_elbow;
    m_variables = p_variables;
    m_finiteStateMachine = p_finiteStateMachine;
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new CMD_setState(p_finiteStateMachine, RobotState.SCORING),
      new ParallelCommandGroup(
        new CMD_ElevatorSetPosition(p_elevator, ElevatorConstants.kElevatorPrep),
        new SequentialCommandGroup(
          new CMD_CheckElbowSafe(p_elevator),
          new CMD_ElbowSetPosition(p_elbow, ElbowConstants.kElbowForwards)
        )
      ),
      new CMD_ElevatorSetLevel(p_elevator, p_variables),
      new CMD_IntakeDrop(m_intake, m_variables),
      new WaitCommand(1),
      new CMD_ElevatorSetPosition(p_elevator, ElevatorConstants.kElevatorPrep),
      new CMD_Stow(m_elevator, m_intake, m_elbow, m_finiteStateMachine)
    );
  }
}
