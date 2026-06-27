package managment;

public abstract class ScriptComponent implements Component {

    public abstract void start();
    public abstract void update();

    @Override
    public void attach(int id) {

    }

    @Override
    public void detach() {

    }
}
