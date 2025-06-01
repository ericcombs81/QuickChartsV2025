package main;


public class Point {

	public Double x, y;

	Point(Double x, Double y) {
		if(FullScreenView.isFullScreen) {
			this.x = x  / (Frame.field.scale / 10);
			this.y = y  / (Frame.field.scale / 10);
		} else {
		
		this.x = x;
		this.y = y;
		}
	}

	Point() {
		this.x = 0.0;
		this.y = 0.0;
	}

	public Double getX() {
		if(FullScreenView.isFullScreen) {
			return this.x  * (Frame.field.scale / 10);
		}
		return this.x;
	}

	public Double getY() {
		
		if(FullScreenView.isFullScreen) {
			return this.y  * (Frame.field.scale / 10);
		} else {
		return this.y;
		}
	}

	public void setLocation(Double x, double y) {
		
		if(FullScreenView.isFullScreen) {
			this.x = x /  (Frame.field.scale / 10);
			this.y = y /  (Frame.field.scale / 10);
			
		} else {
		
		this.x = x;
		this.y = y;
		}
	}
}
