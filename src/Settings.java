public record Settings() {

    public record screen() {

        static int[] screenSize = {1920, 1080};
        public static int[] getScreenSize() { return screenSize; }

    }

    public record Engine() {

        static int MAX_SPLIT = 10;
        //public static int getMaxSplit() { return  maxSplit; }

        static int TARGET_OBJECTS = 2;
        //public static int getTargetObjects() { return targetObjects; }

        static int AABB_FATTENING = 50;

    }

}
