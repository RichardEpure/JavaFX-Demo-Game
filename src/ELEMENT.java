public enum ELEMENT
{
    FLOOR("resources/tiles/RockyFloor.png", false),
    NORTHWALL("resources/tiles/RockyWall(Horizontal).png", true),
    SOUTHWALL("resources/tiles/RockyWall(Horizontal-Hidden).png", true),
    SIDEWALL("resources/tiles/RockyWall(Up).png", true);

    private String image;
    private boolean collidable;

    ELEMENT(String image, boolean collidable)
    {
        this.image = image;
        this.collidable = collidable;
    }

    public String getImage()
    {
        return this.image;
    }

    public boolean isCollidable()
    {
        return this.collidable;
    }
}
