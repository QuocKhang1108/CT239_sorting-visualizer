import java.awt.*;

public class CustomCanvas extends Canvas {
    private CanvasListener listener;

    public CustomCanvas(CanvasListener listener){
        super();

        this.listener=listener;
    }

    public void paint(Graphics graphics){
        super.paint(graphics);
        clear(graphics);
        listener.onDrawArray();
    }

    public void clear(Graphics graphics){
        graphics.setColor(CustomColor.canvasBackground);
        graphics.fillRect(0,0,getWidth(),getHeight());
    }

    public interface CanvasListener{
        void onDrawArray();
    }
}
