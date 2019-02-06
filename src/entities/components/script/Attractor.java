package entities.components.script;

import entities.GameObject;
import entities.components.RigibBody;
import utils.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Attractor extends Script {

    private float mass;

    private static float G = 0.0000667F;
    private static List<Attractor> attractors = new ArrayList<>();

    public Attractor(GameObject gameObject, float mass){
        super(gameObject);
        this.mass = mass;
        attractors.add(this);
    }

    @Override
    public void fixedUpdate(){
        for (Attractor attractor: attractors){
            if (attractor != this){
                attract(attractor);
            }
        }
    }

    private void attract(Attractor attractor){

        RigibBody rb = attractor.gameObject.getComponent(RigibBody.class);

        Vector3f direction = new Vector3f();
        Vector3f.sub(transform.position, attractor.transform.position, direction);

        float force = G * this.mass * attractor.mass / (float) Math.pow(direction.length(), 2);

        Vector3f forceVector = new Vector3f();
        direction.normalise(forceVector);
        Vector3f.mul(forceVector, force, forceVector);

        rb.applyForce(forceVector);

    }

}
