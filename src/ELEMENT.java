public enum ELEMENT
{
    FLOOR("X", false),
    NORTHWALL("V", true),
    SOUTHWALL("^", true),
    SIDEWALL("|", true);

    private String symbol;
    private boolean collidable;

    ELEMENT(String symbol, boolean collidable)
    {
        this.symbol = symbol;
        this.collidable = collidable;
    }

    public boolean isCollidable()
    {
        return collidable;
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
