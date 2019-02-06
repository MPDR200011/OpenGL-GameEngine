package entities.components;

import entities.GameObject;
import renderEngine.Time;
import utils.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class RigibBody extends Component {

    public static List<RigibBody> rigidBodies = new ArrayList<>();

    public Vector3f velocity;
    public float mass;

    public RigibBody(GameObject gameObject, Vector3f initialVelocity, float mass){
        super(gameObject);
        this.velocity = initialVelocity;
        this.mass = mass;
        rigidBodies.add(this);
    }

    public void update(){
        Vector3f velocityAmount = new Vector3f();
        Vector3f.mul(velocity, Time.TimePerUpdate, velocityAmount);
        Vector3f.add(transform.position, velocityAmount, transform.position);
    }

    public void applyForce(Vector3f force){

        Vector3f.mul(force, 1/mass, force);
        Vector3f.add(velocity, force, velocity);

    }

}
