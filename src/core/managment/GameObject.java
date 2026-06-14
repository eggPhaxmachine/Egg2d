package core.managment;

public abstract class GameObject {

    public final int id;
    public int getId() {
        return id;
    }

    public GameObject(int id) {
        this.id = id;
        GameManager.getCurrentScene().addGameObject(this);
    }

    public void destroy() {
        GameManager.getCurrentScene().removeGameObject(id);
    }

}
