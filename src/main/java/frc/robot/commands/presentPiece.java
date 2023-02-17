package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.lib.util.States.LinkableState;
import frc.lib.util.States.StateMachine;
import frc.robot.Arm.Arm;
import frc.robot.Arm.ArmCommand;
import frc.robot.Arm.NamedPose;
import frc.robot.Arm.PoseList;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeState;

public class PresentPiece extends CommandBase
{
     
        private final Intake INTAKE;

        // doing this definitely feels like I'm subverting the purpose of the command system,
        // but I'm very happy with the state machine thing I made and wanted to use it more.
        private final Arm ARM;
        private PoseList poses;
        private Command subordinateCommand;

       
        private StateMachine stateMachine;
        private Timer timer;
        private boolean finished = false;
        

        
        /**
         * 
         * @param intake
         * @param arm
         * @param waitOn a command which must finish executing before the intake will present.
         */
        public PresentPiece(Intake intake, Arm arm, PoseList poses)
        {
            INTAKE = intake;
            ARM = arm;
            addRequirements(INTAKE);
            this.poses = poses;

            subordinateCommand = null;
            
            stateMachine = new StateMachine(this::begin);
            stateMachine.execute();

            timer = new Timer();
         
        }
        

        @Override
        public void initialize()
        {
           
            
        }

        @Override
        public void execute()
        {
            
            finished = stateMachine.execute();

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



        // The following methods are state machine states

        private LinkableState begin()
        {
            // TODO set correct arm pose
            subordinateCommand = ArmCommand.PlotPath((NamedPose.FloorPick), ARM);

             // cones do not need to execute the prePresent stage, since they do not deal with the vaccum.
            if(INTAKE.getState() == IntakeState.getCube)
            {
                return this::startVaccum;
            }

           
            return this::waitToPresent;
        }


        
        // turn the vaccum on for a specified amount of time.
        private LinkableState startVaccum()
        {
            timer.start();
            INTAKE.setVaccum(true);

            // TODO minimize time
            if(timer.hasElapsed(.5))
            {
                timer.stop();
                return this::waitToPresent;
            }

            
            return this::startVaccum;
        }

        // if the command we are waiting on (for all intents and purposes, the arm) isn't finished, wait for it to finish before we begin.
        private LinkableState waitToPresent()
        {
            if(subordinateCommand == null || subordinateCommand.isFinished())
            {
                return this::present;
            }

            return this::waitToPresent;
        }

        // set the presenter up, and wait a specified amount of time.
        private LinkableState present()
        {
            timer.start();
            INTAKE.setPresenter(true);

            // TODO set correct arm pose
            ARM.setClaw(true);

            //TODO minimize time
            if(timer.hasElapsed(.5))
            {
              
                timer.stop();
                return this::waitForArm;
            }

            return this::present;
        }

        // wait for the arm to grab the gamepiece before we reset the intake.
        private LinkableState waitForArm()
        {
            if(subordinateCommand.isFinished())
            {
                INTAKE.setVaccum(false);
                INTAKE.setPresenter(false);
                return null;
            }

            return this::waitForArm;

        }

       

    }