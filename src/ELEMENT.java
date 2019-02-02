public enum ELEMENT
{
    FLOOR("X", "resources/tiles/RockyFloor.png", false, 100),
    NORTHWALL("V", "resources/tiles/RockyWall(Horizontal).png", true, 100),
    SOUTHWALL("^", "resources/tiles/RockyWall(Horizontal-Hidden).png", true, 0),
    SIDEWALL("|", "resources/tiles/RockyWall(Up).png", true, 0);

    private String symbol;
    private String image;
    private boolean collidable;
    private int viewOrder;

    ELEMENT(String symbol, String image, boolean collidable, int viewOrder)
    {
        this.symbol = symbol;
        this.image = image;
        this.collidable = collidable;
        this.viewOrder = viewOrder;
    }

    public String getImage()
    {
        return image;
    }

    public boolean isCollidable()
    {
        return collidable;
    }

    public int getViewOrder()
    {
        return viewOrder;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public static ELEMENT getElementWithSymbol(String symbol)
    {
        for(ELEMENT element : ELEMENT.values())
        {
            if(element.getSymbol().equals(symbol))
            {
                return element;
            }
        }
        return null;
    }
}
