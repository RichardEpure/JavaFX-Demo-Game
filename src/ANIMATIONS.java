public enum ANIMATIONS
{
    PLAYER_DOWN_IDLE(new String[]{"resources/player/down_idle/0.png", "resources/player/down_idle/1.png",
            "resources/player/down_idle/2.png", "resources/player/down_idle/3.png", "resources/player/down_idle/4.png",
            "resources/player/down_idle/5.png", "resources/player/down_idle/6.png", "resources/player/down_idle/7.png",
            "resources/player/down_idle/8.png", "resources/player/down_idle/9.png", "resources/player/down_idle/10.png"}),

    PLAYER_DOWN_WALK(new String[]{"resources/player/down_walk/0.png", "resources/player/down_walk/1.png",
            "resources/player/down_walk/2.png", "resources/player/down_walk/3.png", "resources/player/down_walk/4.png",
            "resources/player/down_walk/5.png", "resources/player/down_walk/6.png", "resources/player/down_walk/7.png",
            "resources/player/down_walk/8.png", "resources/player/down_walk/9.png", "resources/player/down_walk/10.png"}),

    PLAYER_LEFT_IDLE(new String[]{"resources/player/left_idle/0.png", "resources/player/left_idle/1.png",
            "resources/player/left_idle/2.png", "resources/player/left_idle/3.png", "resources/player/left_idle/4.png",
            "resources/player/left_idle/5.png", "resources/player/left_idle/6.png", "resources/player/left_idle/7.png",
            "resources/player/left_idle/8.png", "resources/player/left_idle/9.png", "resources/player/left_idle/10.png"}),

    PLAYER_LEFT_WALK(new String[]{"resources/player/left_walk/0.png", "resources/player/left_walk/1.png",
            "resources/player/left_walk/2.png", "resources/player/left_walk/3.png", "resources/player/left_walk/4.png",
            "resources/player/left_walk/5.png", "resources/player/left_walk/6.png", "resources/player/left_walk/7.png",
            "resources/player/left_walk/8.png", "resources/player/left_walk/9.png", "resources/player/left_walk/10.png"}),

    PLAYER_RIGHT_IDLE(new String[]{"resources/player/right_idle/0.png", "resources/player/right_idle/1.png",
            "resources/player/right_idle/2.png", "resources/player/right_idle/3.png", "resources/player/right_idle/4.png",
            "resources/player/right_idle/5.png", "resources/player/right_idle/6.png", "resources/player/right_idle/7.png",
            "resources/player/right_idle/8.png", "resources/player/right_idle/9.png", "resources/player/right_idle/10.png"}),

    PLAYER_RIGHT_WALK(new String[]{"resources/player/right_walk/0.png", "resources/player/right_walk/1.png",
            "resources/player/right_walk/2.png", "resources/player/right_walk/3.png", "resources/player/right_walk/4.png",
            "resources/player/right_walk/5.png", "resources/player/right_walk/6.png", "resources/player/right_walk/7.png",
            "resources/player/right_walk/8.png", "resources/player/right_walk/9.png", "resources/player/right_walk/10.png"}),

    PLAYER_UP_IDLE(new String[]{"resources/player/up_idle/0.png", "resources/player/up_idle/1.png",
            "resources/player/up_idle/2.png", "resources/player/up_idle/3.png", "resources/player/up_idle/4.png",
            "resources/player/up_idle/5.png", "resources/player/up_idle/6.png", "resources/player/up_idle/7.png",
            "resources/player/up_idle/8.png", "resources/player/up_idle/9.png", "resources/player/up_idle/10.png"}),

    PLAYER_UP_WALK(new String[]{"resources/player/up_walk/0.png", "resources/player/up_walk/1.png",
            "resources/player/up_walk/2.png", "resources/player/up_walk/3.png", "resources/player/up_walk/4.png",
            "resources/player/up_walk/5.png", "resources/player/up_walk/6.png", "resources/player/up_walk/7.png",
            "resources/player/up_walk/8.png", "resources/player/up_walk/9.png", "resources/player/up_walk/10.png"}),

    PLAYER_ATTACK_DOWN(new String[]{"resources/player/attack_down/0.png", "resources/player/attack_down/1.png",
            "resources/player/attack_down/2.png", "resources/player/attack_down/3.png", "resources/player/attack_down/4.png",
            "resources/player/attack_down/5.png", "resources/player/attack_down/6.png", "resources/player/attack_down/7.png",
            "resources/player/attack_down/8.png", "resources/player/attack_down/9.png", "resources/player/attack_down/10.png"}),

    PLAYER_ATTACK_LEFT(new String[]{"resources/player/attack_left/0.png", "resources/player/attack_left/1.png",
            "resources/player/attack_left/2.png", "resources/player/attack_left/3.png", "resources/player/attack_left/4.png",
            "resources/player/attack_left/5.png", "resources/player/attack_left/6.png", "resources/player/attack_left/7.png",
            "resources/player/attack_left/8.png", "resources/player/attack_left/9.png", "resources/player/attack_left/10.png"}),

    PLAYER_ATTACK_RIGHT(new String[]{"resources/player/attack_right/0.png", "resources/player/attack_right/1.png",
            "resources/player/attack_right/2.png", "resources/player/attack_right/3.png", "resources/player/attack_right/4.png",
            "resources/player/attack_right/5.png", "resources/player/attack_right/6.png", "resources/player/attack_right/7.png",
            "resources/player/attack_right/8.png", "resources/player/attack_right/9.png", "resources/player/attack_right/10.png"}),

    PLAYER_ATTACK_UP(new String[]{"resources/player/attack_up/0.png", "resources/player/attack_up/1.png",
            "resources/player/attack_up/2.png", "resources/player/attack_up/3.png", "resources/player/attack_up/4.png",
            "resources/player/attack_up/5.png", "resources/player/attack_up/6.png", "resources/player/attack_up/7.png",
            "resources/player/attack_up/8.png", "resources/player/attack_up/9.png", "resources/player/attack_up/10.png"});

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