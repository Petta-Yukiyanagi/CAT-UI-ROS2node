// FacePart.java
import processing.core.*;

abstract class FacePart implements UIComponent {
  protected final PApplet app;
  protected final float s; // global scale
  protected float centerX, centerY;
  protected float animProgress = 0f;

  FacePart(PApplet app, float s) {
    this.app = app;
    this.s = s;
    this.centerX = app.width / 2f;
    this.centerY = app.height / 2f;
  }

  public void update() {}
  public void draw() {}

  public void setAnimProgress(float p) {
    animProgress = PApplet.constrain(p, 0f, 1f);
  }

  protected void refreshCenter() {
    centerX = app.width / 2f;
    centerY = app.height / 2f;
  }
}
