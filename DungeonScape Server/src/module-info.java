/**
 * 
 */
/**
 * @author Clownvin
 *
 */
module FinalMMOProject {
	requires com.git.clownvin.simpleserverframework;
	requires transitive com.git.clownvin.simplepacketframework;
	requires transitive com.git.clownvin.dsapi;
	requires transitive com.git.clownvin.simpleuserframework;
	requires com.git.clownvin.util;
	requires java.desktop;
	//Exports so that dependancies can do their jobs
	exports com.git.clownvin.dsserver.connection;
	exports com.git.clownvin.dsserver.user;
	exports com.git.clownvin.dsserver.entity.character;
	exports com.git.clownvin.dsserver.entity.object;
	exports com.git.clownvin.dsserver.world;
	exports com.git.clownvin.dsserver.entity;
}