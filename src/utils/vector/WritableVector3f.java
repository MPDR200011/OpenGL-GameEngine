package utils.vector;

/**
 * Writable interface to Vector3fs
 * @author $author$
 * @version $revision$
 * $Id$
 */
public interface WritableVector3f extends WritableVector2f {

	/**
	 * Set the Z value
	 * @param z
	 */
	void setZ(float z);

	/**
	 * Set the X,Y,Z values
	 * @param x
   * @param y
   * @param z
	 */
	void set(float x, float y, float z);

}