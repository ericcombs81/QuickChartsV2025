package main;

public class CoordinateCalculator {

	public static Double xSteps;
	public static int[] yardLines = { 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 45, 40, 35, 30, 25, 20, 15, 10, 5, 0 };
	public static int fieldLength = 1000;
	public static Boolean isInside;
	public static Double coordinateX;
	public static Double coordinateY;
	public static Boolean isLeftSide;
	//Up Down
	public static Double ySteps;
	public static Boolean isBehind;
	public static Boolean home;

	// Field is from 0 to 1000, sideline to sideline
	// 1 Step = 6.25px
	// .5 step = 3.125px
	// 1/4 step = 1.5625px
	// 5 yards = 50 px
	// 1 yard = 10px

	// "3.75 steps to the outside/inside of the 35 yard line"
	// "On the 35 yard line"

	public static String getXString(Point point) {
		
		String string = "";
		coordinateX = point.getX();
		Double originalX = rounded(coordinateX);

		// Determine Right or Left

		if (originalX == 500) {
			string = "On 50 yd ln";
			return string;
		}

		if (coordinateX < 500) {
			isLeftSide = true;
		} else {
			isLeftSide = false;
		}
		
		if(coordinateX < 0) {
			coordinateX = Math.abs(coordinateX);
			xSteps = coordinateX / 6.25;
			xSteps = rounded(xSteps);
			string = "Left: " + xSteps + " steps outside 0 yd ln";
			if(string.equals("Left: 1.0 steps outside 0 yd ln")) {
				string = "Left: 1.0 step outside 0 yd ln";
			}
			return string;
		}
		if(coordinateX > 1000) {
			coordinateX = Math.abs(coordinateX);
			xSteps = (coordinateX - 1000) / 6.25;
			xSteps = rounded(xSteps);
			string = "Right: " + xSteps + " steps outside 0 yd ln";
			if(string.equals("Right: 1.0 steps outside 0 yd ln")) {
				string = "Right: 1.0 step outside 0 yd ln";
			}
			return string;
		}

		// if it is 0
		if (originalX == 0) {
			string = "Left: on 0 yd ln";
			return string;
		}

		if (originalX == 1000) {
			string = "Right: on 0 yd ln";
			return string;
		}
		// if it is on a yard line
		if (originalX % 50 == 0) {
			// find out which one
			Double yardLineIndex = coordinateX / 50;
			int yardLineNumber = yardLines[yardLineIndex.intValue()];
			String yardLineNumberString = String.valueOf(yardLineNumber);
			if (isLeftSide) {
				string = "Left: On " + yardLineNumberString + " yd ln";
			} else {
				string = "Right: On " + yardLineNumberString + " yd ln";
			}
			return string;

		}

		int closestYardLine = calculateClosestYardLine(coordinateX);
		String closestYardLineToString = String.valueOf(closestYardLine);
		
		String inOrOut = "";
		if (isInside) {
			inOrOut = "inside";
		} else {
			inOrOut = "outside";
		}
		xSteps = rounded(xSteps);

		// return string;
		if(isLeftSide) {
			string = "Left: " + xSteps + " steps " + inOrOut + " of the " + closestYardLineToString + " yd ln";
			if(string.equals("Left: 1.0 steps " + inOrOut + " of the " + closestYardLineToString + " yd ln")) {
				string = "Left: 1.0 step " + inOrOut + " of the " + closestYardLineToString + " yd ln";
			}
		} else {
			string = "Right: " + xSteps + " steps " + inOrOut + " of the " + closestYardLineToString + " yd ln";
			if(string.equals("Right: 1.0 steps " + inOrOut + " of the " + closestYardLineToString + " yd ln")) {
				string = "Right: 1.0 step " + inOrOut + " of the " + closestYardLineToString + " yd ln";
			}
		}
		
		return string;
	}
	
	public static int calculateClosestYardLine(Double x) {
		double remainder = x % 50;
		int yardLineIndex = (int)(x / 50);

		if (isLeftSide) {
			if (remainder < 25) {
				isInside = true;
				xSteps = (remainder / 6.25);
				return yardLines[yardLineIndex];
			} else {
				isInside = false;
				xSteps = ((50 - remainder) / 6.25);
				return yardLines[yardLineIndex + 1];
			}

		} else {// else it is right side of field
			if (remainder <= 25) {
				isInside = false;
				xSteps = (remainder / 6.25);
				return yardLines[yardLineIndex];
			} else {
				isInside = true;
				xSteps = ((50 - remainder) / 6.25);
				return yardLines[yardLineIndex + 1];
			}
		}
	}
	
	public static double rounded(double value) {
	    return Math.round(value * 10) / 10.0;
	}
	
	public static String getYString(Point point) {
		//NOTE: front hash is at 3.55.33333333px
		
		coordinateY = point.getY();
		Double originalY = rounded(coordinateY);
		
		String string = "";
		
		//The high school grids all have the same hashes.  Lets do them first.
		if(Frame.field.gridStyle.equals("hsm1") || Frame.field.gridStyle.equals("hsm2") || Frame.field.gridStyle.equals("hsm3")) {
			if(originalY == 0) {
				string = "On Visitor Sideline";
				return string;
			}
			
			if(originalY == 553) {
				string = "On Home Sideline";
				return string;
			}
			
			if(coordinateY < 0) {
				//Behind Visitor Sideline
				ySteps = Math.abs(coordinateY) / 6.25;
				ySteps = rounded(ySteps);
				string = ySteps + " steps behind Visitor side line";
				if(string.equals("0.0 steps behind Visitor side line")) {
					string = "On the Visitor sideline";
				}
				if(string.equals("1.0 steps behind Visitor side line")) {
					string = "1.0 step behind Visitor sideline";
				}
				return string;
				
			}
			if(coordinateY < 88.833 && coordinateY > 0) {
				//in front of Visitor Sideline
				ySteps = coordinateY / 6.25;
				ySteps = rounded(ySteps);
				string = ySteps + " steps in front of Visitor side line";
				if(string.equals("0.0 steps in front of Visitor side line")) {
					string = "On visitor side line";
				}
				if(string.equals("1.0 steps in front of Visitor side line")) {
					string = "1.0 step in front of Visitor side line";
				}
				return string;
			}
			if(coordinateY <= 177.667 && coordinateY >= 88.833) {
				//Behind Visitor Hash
				ySteps = 177.667 - coordinateY;
				ySteps = ySteps / 6.25;
				ySteps = rounded(ySteps);
				
				string = ySteps + " steps behind Visitor hash";
				if(string.equals("1.0 steps behind Visitor hash")) {
					string = "1.0 step behind Visitor hash";
					return string;
				}
				if(string.equals("0.0 steps behind Visitor hash")) {
					string = "On Visitor hash";
				}
				return string;
			}
			if(coordinateY < 266.5 && coordinateY > 177.667) {
				//in front of Visitor Hash
				ySteps = Math.abs(266.5 - coordinateY - 88.833);
				ySteps = ySteps / 6.25;
				ySteps = rounded(ySteps);
				
				string = ySteps + " steps in front of Visitor hash";
				if(string.equals("1.0 steps in front of Visitor hash")) {
					string = "1.0 step in front of Visitor hash";
					return string;
				}
				if(string.equals("0.0 steps in front of Visitor hash")) {
					string = "On visitor hash";
				}
			}
			if(coordinateY >= 266.5 && coordinateY < 355.333) {
				//behind Home Hash
				ySteps = 355.333 - coordinateY;
				ySteps = ySteps / 6.25;
				ySteps = rounded(ySteps);
				
				string = ySteps + " steps behind Home hash";
				if(string.equals("1.0 steps behind Home hash")) {
					string = "1.0 step behind Home hash";
					return string;
				}
				if(string.equals("0.0 steps behind Home hash")) {
					string = "On Home hash";
				}
				
			}
			if(coordinateY >= 355.333 && coordinateY <= 444.167) {
				//in front of Home Hash
				ySteps = Math.abs(444.167 - coordinateY-88.833);
				ySteps = ySteps / 6.25;
				ySteps = rounded(ySteps);
				
				string = ySteps + " steps in front of Home hash";
				if(string.equals("1.0 steps in front of Home hash")) {
					string = "1.0 step in front of Home hash";
					return string;
				}
				if(string.equals("0.0 steps in front of Home hash")) {
					string = "On Home hash";
				}
			}
			if(coordinateY > 444.167 && coordinateY < 533) {
				//behind Home Sideline
				ySteps = 533 - coordinateY;
				ySteps = ySteps / 6.25;
				ySteps = rounded(ySteps);
				string = ySteps + " steps behind Home side line";
				if(string.equals("1.0 steps behind Home side line")) {
					string = "1.0 step behind Home side line";
					return string;
				}
				if(string.equals("0.0 steps behind Home side line")) {
					string = "On Home hash";
				}
			}
			if(coordinateY >= 533) {
				//in front of Home Sideline
				ySteps = coordinateY - 533;
				ySteps = rounded(ySteps);
				string = ySteps + " steps in front of Home side line";
				if(string.equals("1.0 steps in front of Home side line")) {
					string = "1.0 step in front of Home side line";
					return string;
				}
				if(string.equals("0.0 steps in front of Home side line")) {
					string = "On Home side line";
				}
			}
			
		}
		
		
		return string;
	}


}
