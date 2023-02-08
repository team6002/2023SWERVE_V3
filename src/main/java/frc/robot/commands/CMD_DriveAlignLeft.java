// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.SUB_Drivetrain;
import frc.robot.subsystems.SUB_LimeLight;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class CMD_DriveAlignLeft extends SequentialCommandGroup {
  /** Creates a new CMD_DriveAlignLeft. */
  SUB_Drivetrain m_Drivetrain;
  SUB_LimeLight m_LimeLight;
  public CMD_DriveAlignLeft(SUB_Drivetrain p_Drivetrain, SUB_LimeLight p_LimeLight) {
    m_Drivetrain = p_Drivetrain;
    m_LimeLight = p_LimeLight;
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new CMD_DriveAlignTag(m_Drivetrain, m_LimeLight),
      new CMD_DriveAlignPosition(m_Drivetrain, 20, 20, 0)
      // new CMD_DriveAlignPosition(m_Drivetrain, 5, 0, 0)
    );
  }
}
