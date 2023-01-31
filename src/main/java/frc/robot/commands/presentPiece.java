package frc.robot.commands;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.ArmPoses;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;

public class presentPiece extends CommandBase
    {
        private Intake INTAKE;
        private Arm ARM;
        private boolean finished = false;

        public presentPiece(Intake intake, Arm arm)
        {
            INTAKE = intake;
            ARM = arm;
            addRequirements(INTAKE);
            addRequirements(ARM);
        }

        @Override
        public void initialize()
        {
            //ARM.setPosition(ArmPoses.home).alongWith(
            INTAKE.setPresenter(true);
            finished = true;
        }
        @Override
        public void execute()
        {

        }
        @Override
        public void end(boolean inturrupted)
        {

        }
        @Override
        public boolean isFinished()
        {
            return finished;
        }

    }