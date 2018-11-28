/**
 * 
 */
/**
 * @author Clownvin
 *
 */
module com.git.clownvin.dsapi {
	requires com.git.clownvin.simplepacketframework;
	requires transitive com.git.clownvin.util;
	requires java.base;
	exports com.git.clownvin.dsapi.packet;
	exports com.git.clownvin.dsapi.entity;
	exports com.git.clownvin.dsapi.entity.character;
	exports com.git.clownvin.dsapi.entity.object;
	exports com.git.clownvin.dsapi.world;
	exports com.git.clownvin.dsapi.entity.projectile;
	exports com.git.clownvin.dsapi.config;
	exports com.git.clownvin.dsapi.item;
}