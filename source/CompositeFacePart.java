// CompositeFacePart.java
import java.util.*;

class CompositeFacePart implements UIComponent {
  private final List<UIComponent> children = new ArrayList<>();
  void add(UIComponent c){ children.add(c); }
  public void update(){ for(UIComponent c:children) c.update(); }
  public void draw(){ for(UIComponent c:children) c.draw(); }
}
