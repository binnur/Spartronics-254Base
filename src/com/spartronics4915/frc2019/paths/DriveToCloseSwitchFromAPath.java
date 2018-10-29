package com.spartronics4915.frc2019.paths;

import java.util.ArrayList;

import com.spartronics4915.frc2019.paths.PathBuilder.Waypoint;
import com.spartronics4915.lib.util.control.Path;
import com.spartronics4915.lib.util.math.RigidTransform2d;

public class DriveToCloseSwitchFromAPath extends DriveToCloseSwitchFromCPath
{
    @Override
    public Path buildPath()
    {
        return PathBuilder.buildPathFromWaypoints(PathTransformHelper.mirrorWaypointsAboutAxis((ArrayList<Waypoint>)super.getWaypoints(), false, true));
    }
    
    @Override
    public RigidTransform2d getStartPose()
    {
        return PathTransformHelper.mirrorRigidTransformAboutAxis(super.getStartPose(), false, true); 
    }
}