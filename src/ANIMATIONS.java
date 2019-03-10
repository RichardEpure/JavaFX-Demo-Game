public enum ANIMATIONS
{
    PLAYER_DOWN_IDLE(new String[]{"resources/player/down_idle/down_stand.png"}),
    PLAYER_DOWN_WALK(new String[]{"resources/player/down_walk/down_walk1.png", "resources/player/down_walk/down_walk2.png"}),
    PLAYER_LEFT_IDLE(new String[]{"resources/player/left_idle/left_stand.png"}),
    PLAYER_LEFT_WALK(new String[]{"resources/player/left_walk/left_walk1.png", "resources/player/left_walk/left_walk2.png"}),
    PLAYER_RIGHT_IDLE(new String[]{"resources/player/right_idle/right_stand.png"}),
    PLAYER_RIGHT_WALK(new String[]{"resources/player/right_walk/right_walk1.png", "resources/player/right_walk/right_walk2.png"}),
    PLAYER_UP_IDLE(new String[]{"resources/player/up_idle/up_stand.png"}),
    PLAYER_UP_WALK(new String[]{"resources/player/up_walk/up_walk1.png", "resources/player/up_walk/up_walk2.png"});

    private String[] animation;

    ANIMATIONS(String[] animation)
    {
        this.animation = animation;
    }

    public String[] getFrames()
    {
        return animation;
    }
}