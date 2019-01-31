public enum ELEMENT
{
    FLOOR("resources/tiles/RockyFloor.png", false, 100),
    NORTHWALL("resources/tiles/RockyWall(Horizontal).png", true, 100),
    SOUTHWALL("resources/tiles/RockyWall(Horizontal-Hidden).png", true, 0),
    SIDEWALL("resources/tiles/RockyWall(Up).png", true, 0);

    private String image;
    private boolean collidable;
    private int viewOrder;

    ELEMENT(String image, boolean collidable, int viewOrder)
    {
        this.image = image;
        this.collidable = collidable;
        this.viewOrder = viewOrder;
    }

    public String getImage()
    {
        return this.image;
    }

    public boolean isCollidable()
    {
        return this.collidable;
    }

    public int getViewOrder()
    {
        return viewOrder;
    }
}
