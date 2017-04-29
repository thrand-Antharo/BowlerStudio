package com.neuronrobotics.bowlerstudio.creature;

import com.neuronrobotics.sdk.addons.kinematics.math.RotationNR;
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class TransformWidget extends GridPane implements IOnEngineeringUnitsChange, EventHandler<ActionEvent> {

    private IOnTransformChange onChange;
    //EngineeringUnitsSliderWidget rw;
    private EngineeringUnitsSliderWidget tilt;
    private EngineeringUnitsSliderWidget elevation;
    private EngineeringUnitsSliderWidget azimeth;
    private EngineeringUnitsSliderWidget tx;
    private EngineeringUnitsSliderWidget ty;
    private EngineeringUnitsSliderWidget tz;
    //	private TextField tx;
//	private TextField ty;
//	private TextField tz;
    private TransformNR initialState;


    public TransformWidget(String title, TransformNR is, IOnTransformChange onChange) {
        this.initialState = is;
        this.onChange = onChange;
//		tx = new TextField(CreatureLab.getFormatted(initialState.getX()));
//		ty = new TextField(CreatureLab.getFormatted(initialState.getY()));
//		tz = new TextField(CreatureLab.getFormatted(initialState.getZ()));
//		tx.setOnAction(this);
//		ty.setOnAction(this);
//		tz.setOnAction(this);
        tx = new EngineeringUnitsSliderWidget(this, -200, 200, initialState.getX(), 100, "mm");
        ty = new EngineeringUnitsSliderWidget(this, -200, 200, initialState.getY(), 100, "mm");
        tz = new EngineeringUnitsSliderWidget(this, -200, 200, initialState.getZ(), 100, "mm");

        RotationNR rot = initialState.getRotation();
        double t = 0;
        try {
            t = Math.toDegrees(rot.getRotationTilt());
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        double e = 0;
        try {
            e = Math.toDegrees(rot.getRotationElevation());
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        double a = 0;
        try {
            a = Math.toDegrees(rot.getRotationAzimuth());
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        tilt = new EngineeringUnitsSliderWidget(this, -179.99, 179.99, t, 100, "degrees");
        elevation = new EngineeringUnitsSliderWidget(this, -89.99, 89.99, e, 50, "degrees");
        azimeth = new EngineeringUnitsSliderWidget(this, -179.99, 179.99, a, 100, "degrees");
        getColumnConstraints().add(new ColumnConstraints(40)); // translate text
        getColumnConstraints().add(new ColumnConstraints(160)); // translate values
        getColumnConstraints().add(new ColumnConstraints(50)); // units
        getColumnConstraints().add(new ColumnConstraints(40)); // rotate text
        setHgap(20);// gab between elements


        add(new Text(title),
            1, 0);
//	    add(	new Text("(r)W"), 
//	    		3,  0);
//	    add(	rw, 
//	    		4,  0);
        // These all seem out of order here, but it is because the
        // screen is rotating the orenation of this interface from BowlerStudio3dEngine.getOffsetforvisualization()
        //X line
        add(new Text("X"),
            0, 1);
        add(tx,
            1, 1);

        add(new Text("Tilt"),
            3, 1);
        add(tilt,
            4, 1);
        //Y line
        add(new Text("Y"),
            0, 2);
        add(ty,
            1, 2);

        add(new Text("Elevation"),
            3, 2);
        add(elevation,
            4, 2);
        //Z line
        add(new Text("Z"),
            0, 3);
        add(tz,
            1, 3);

        add(new Text("Azimuth"),
            3, 3);
        add(azimeth,
            4, 3);
    }

    private TransformNR getCurrent() {
        TransformNR tmp = new TransformNR(
                tx.getValue(),
                ty.getValue(),
                tz.getValue(),
                new RotationNR(
                        tilt.getValue(),
                        azimeth.getValue(),
                        elevation.getValue()
                ));


        return tmp;
    }

    @Override
    public void onSliderMoving(EngineeringUnitsSliderWidget source, double newAngleDegrees) {
        onChange.onTransformChaging(getCurrent());
    }

    @Override
    public void onSliderDoneMoving(EngineeringUnitsSliderWidget source,
                                   double newAngleDegrees) {
        handle(null);
    }

    @Override
    public void handle(ActionEvent event) {
        onChange.onTransformChaging(getCurrent());
        onChange.onTransformFinished(getCurrent());
    }

    public void updatePose(TransformNR p) {
        Platform.runLater(() -> {
            tx.setValue(p.getX());
            ty.setValue(p.getY());
            tz.setValue(p.getZ());
        });

        RotationNR rot = p.getRotation();
        double t = 0;
        try {
            t = Math.toDegrees(rot.getRotationTilt());
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        double e = 0;
        try {
            e = Math.toDegrees(rot.getRotationElevation());
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        double a = 0;
        try {
            a = Math.toDegrees(rot.getRotationAzimuth());
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        tilt.setValue(t);
        elevation.setValue(e);
        azimeth.setValue(a);
    }

}
