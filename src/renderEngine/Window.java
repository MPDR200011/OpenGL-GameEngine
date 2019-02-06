package renderEngine;

import utils.vector.Vector2f;

public class Window {

    private long ID;
    private String title;
    private int width;
    private int height;

    public Projection projection;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.projection = new Projection(this);
    }

    public void show() {
        this.ID = DisplayManager.createDisplay(this.title, this.width, this.height);
    }

    public long ID() {
        return this.ID;
    }

    public Vector2f getSize(){
        return new Vector2f(width, height);
    }

    public void destroy() {
        DisplayManager.destroyDisplay(this.ID);
    }
}
