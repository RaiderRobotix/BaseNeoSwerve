# BaseNeoSwerve </br>

**Basic Swerve Code for a Swerve Module using Neo Motors, a CTRE CANCoder, and a NavX Gyro** </br>
This version was designed for Swerve Drive Specialties MK4 modules.</br>

**Setting Constants**
----
The following things must be adjusted to your robot and module's specific constants in the Constants.java file (all distance units must be in meters, and rotation units in radians):</br>
These instructions are mostly followable from Step 

1. ```trackWidth```: Center to Center distance of left and right modules in meters.
2. ```wheelBase```: Center to Center distance of front and rear module wheels in meters.
3. Setting Offsets
    * For finding the offsets, use a piece of 1x1 metal that is straight against the forks of the front and back modules (on the left and right side) to ensure that the modules are straight. 
    * Point the bevel gears of all the wheels in the same direction (either facing left or right), where a postive input to the drive motor drives the robot forward (you can use phoenix tuner to test this). If for some reason you set the offsets with the wheels backwards, you can change the ```driveMotorInvert``` value to fix.
    * Open smartdashboard (or shuffleboard and go to the smartdashboard tab), you will see 4 printouts called "Mod 0 Cancoder", "Mod 1 Cancoder", etc. 
    <br>If you have already straightened the modules, copy those 4 numbers exactly (to 2 decimal places) to their respective ```angleOffset``` variable in constants.
    <br><b>Note:</b> The CANcoder values printed to smartdashboard are in degrees, when copying the values to ```angleOffset``` you must use ```Rotation2d.fromDegrees("copied value")```.

**Controller Mappings**
----
This code is natively setup to use a xbox controller to control the swerve drive. </br>
* Left Stick: Translation Control (forwards and sideways movement)
* Right Stick: Rotation Control </br>
* Y button: Zero Gyro (useful if the gyro drifts mid match, just rotate the robot forwards, and press Y to rezero)
* Left Trigger: Enables Brake Mode while held (20% Max Speed)
* Right Trigger: Enables Boost Mode while held (100% Max Speed)
<b> The Default Max Speed is 60% </b> <br></br>
**Code Taken From Team 364 BaseFalconSwerve at https://github.com/Team364/BaseFalconSwerve**