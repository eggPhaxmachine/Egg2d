package game;

import physics.space.Vector2d;

public record Settings() {

    public record screen() {

        static int[] screenSize = {1920, 1080};
        public static int[] getScreenSize() { return screenSize; }

        public static int fps = 60;
        public static int getFps() {
            return fps;
        }
    }

    public record Engine() {

        static int MAX_SPLIT = 10;
        //public static int getMaxSplit() { return  maxSplit; }

        static int TARGET_OBJECTS = 2;
        //public static int getTargetObjects() { return targetObjects; }
        public static int AABB_FATTENING = 50;

        static Vector2d INITIAL_VECTOR = new Vector2d(1, 0);

    }

}
