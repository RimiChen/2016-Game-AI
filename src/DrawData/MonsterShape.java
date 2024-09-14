package DrawData;

import BasicStructures.ColorVectorRGB;
import BasicStructures.Vector2;
import processing.core.PApplet;

public class MonsterShape {
	//Decided when create
	PApplet parent;
	//this point is for reference, should be (0, 0)
	private Vector2 originalPoint;
	private float triangleSize;
	private float circleSize;

	
	//change according to program change
	private Vector2 currentPosition;
	private float currentOrientation;
	private ColorVectorRGB shapeColor;
	
	public MonsterShape(
			PApplet P,
			float CircleSize,
			float TriangleSize,			
			Vector2 OriginalPoint,
			Vector2 CurrentPosition,
			float CurrentOrientation,
			ColorVectorRGB Color
	){
		this.parent = P;
		this.circleSize = CircleSize;
		this.triangleSize = TriangleSize;
		this.originalPoint = OriginalPoint;
		
		//this.currentPosition = CurrentPosition;
		updatePosition(CurrentPosition);
		updateOrientation(CurrentOrientation);
		this.shapeColor = Color;

	}
	
	//update position
	public void updatePosition(Vector2 CurrentPosition){
		updatePosition(CurrentPosition.getX(), CurrentPosition.getY());
	}
	public void updatePosition(float New_X, float New_Y){
		currentPosition = new Vector2(New_X, New_Y);
	}
	//update orientation
	public void updateOrientation(float CurrentOrientation){
		currentOrientation = CurrentOrientation;
	}
	
	//update color
	public void updateColor(ColorVectorRGB CurrentColor){
		updateColor(CurrentColor.getR(), CurrentColor.getG(), CurrentColor.getB());
	}
	public void updateColor(float New_R, float New_G, float New_B){
		shapeColor = new ColorVectorRGB(New_R, New_G, New_B);
	}

	
	public void display(){
		parent.pushMatrix();
		parent.translate(currentPosition.getX(), currentPosition.getY());
		parent.rotate(currentOrientation);
		parent.stroke(0,0,0, 0);
		parent.fill(shapeColor.getR(), shapeColor.getG(), shapeColor.getB());
			parent.pushMatrix();		
			parent.stroke(0,0,0, 255);
			parent.triangle(
					originalPoint.getX() -circleSize/2.0f, originalPoint.getY(),
					originalPoint.getX() -circleSize*2/3, originalPoint.getY()-circleSize*2/3,
					originalPoint.getX()-5 , originalPoint.getY()- circleSize/2+3
			);
			parent.triangle(
					originalPoint.getX() +circleSize/2.0f, originalPoint.getY(),
					originalPoint.getX() +circleSize*2/3, originalPoint.getY()-circleSize*2/3,
					originalPoint.getX() +5 , originalPoint.getY()- circleSize/2+3
			);
			parent.ellipse(originalPoint.getX(), originalPoint.getY(), circleSize, circleSize);
				parent.pushMatrix();
				parent.stroke(0,0,0, 255);
				parent.fill(0,0,0, 255);
				parent.triangle(
						originalPoint.getX() -circleSize*1/8, originalPoint.getY()-circleSize/16.0f,
						originalPoint.getX() -circleSize*2/8, originalPoint.getY(),
						originalPoint.getX() -circleSize*3/8, originalPoint.getY()-circleSize*3/16.0f
				);
				parent.triangle(
						originalPoint.getX() +circleSize*1/8, originalPoint.getY()-circleSize/16.0f,
						originalPoint.getX() +circleSize*2/8, originalPoint.getY(),
						originalPoint.getX() +circleSize*3/8, originalPoint.getY()-circleSize*3/16.0f
				);
				parent.popMatrix();
			parent.popMatrix();
		parent.popMatrix();
	}	
}
