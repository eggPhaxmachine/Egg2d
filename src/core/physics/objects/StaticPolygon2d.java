package physics.objects;

import managment.GameManager;
import physics.broadphase.AABB;
import physics.shapes.Polygon2d;
import physics.space.Vector2d;

public class StaticPolygon2d extends Polygon2d implements StaticBody2d {

    protected float restitution = 1;
    @Override
    public float getRestitution() {
        return restitution;
    }

    public StaticPolygon2d(Vector2d[] vertices) {
        super(GameManager.getId(), vertices);
    }

    @Override
    public boolean AABBCheck(AABB aabb) {
        return AABB.AABBCheck(vertices, aabb);
    }

    @Override
    public AABB fitAABB() {
        return AABB.fitBoundingBox(vertices, 0);
    }

}
