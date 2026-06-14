package core.physics.objects;

import core.physics.broadphase.AABB;
import core.physics.shapes.Polygon2d;
import core.physics.space.Vector2d;

public class StaticPolygon2d extends Polygon2d implements StaticBody2d {

    protected float restitution = 1;
    @Override
    public float getRestitution() {
        return restitution;
    }

    public StaticPolygon2d(int id, Vector2d[] vertices) {
        super(id, vertices);
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
