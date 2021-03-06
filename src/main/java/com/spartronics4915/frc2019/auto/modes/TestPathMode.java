package com.spartronics4915.frc2019.auto.modes;

import com.spartronics4915.frc2019.auto.AutoModeBase;
import com.spartronics4915.frc2019.auto.AutoModeEndedException;
import com.spartronics4915.frc2019.auto.actions.DrivePathAction;
import com.spartronics4915.frc2019.auto.actions.ResetPoseFromPathAction;
import com.spartronics4915.frc2019.auto.actions.TurnToHeadingAction;
import com.spartronics4915.frc2019.paths.PathContainer;
import com.spartronics4915.frc2019.paths.TestPath;
import com.spartronics4915.lib.math.Rotation2d;

public class TestPathMode extends AutoModeBase
{

    @Override
    protected void routine() throws AutoModeEndedException
    {
        PathContainer path = new TestPath();
        runAction(new ResetPoseFromPathAction(path));
        runAction(new DrivePathAction(path));
       runAction(new TurnToHeadingAction(Rotation2d.fromDegrees(120)));
    }

}
