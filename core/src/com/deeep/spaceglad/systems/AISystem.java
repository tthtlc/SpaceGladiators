package com.deeep.spaceglad.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.deeep.spaceglad.Logger;
import com.deeep.spaceglad.components.*;

/**
 * Created by Andreas on 8/5/2015.
 */
public class AISystem extends EntitySystem implements EntityListener {
    private ImmutableArray<Entity> entities;
    private Entity player;

    ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    ComponentMapper<CharacterComponent> cm = ComponentMapper.getFor(CharacterComponent.class);

    @Override
    public void addedToEngine(Engine e) {
        entities = e.getEntitiesFor(Family.all(ModelComponent.class, RotationComponent.class, PositionComponent.class, VelocityComponent.class, AIComponent.class, StatusComponent.class, CharacterComponent.class).get());
        e.addEntityListener(Family.one(PlayerComponent.class).get(), this);
    }

    public void update(float delta) {
        for (int i = 0; i < entities.size(); i++) {
            StatusComponent sta = entities.get(i).getComponent(StatusComponent.class);
            //if(!sta.enabled) continue;
            RotationComponent rot = entities.get(i).getComponent(RotationComponent.class);
            ModelComponent mod = entities.get(i).getComponent(ModelComponent.class);
            AIComponent aic = entities.get(i).getComponent(AIComponent.class);
            CharacterComponent cha = entities.get(i).getComponent(CharacterComponent.class);

            /*
            PositionComponent playerPositionComponent = player.getComponent(PositionComponent.class);

            float dX = playerPositionComponent.position.x - pm.get(e).position.x;
            float dZ = playerPositionComponent.position.z - pm.get(e).position.z;

            rot.yaw = (float) (Math.atan2(dX, dZ));

            //mod.instance.transform.setFromEulerAngles((float) Math.toDegrees(rot.yaw), rot.pitch, rot.roll);


            */

            ModelComponent playerModelInformation = player.getComponent(ModelComponent.class);

            Vector3 playerPosition = new Vector3();
            Vector3 enemyPosition = new Vector3();
            Quaternion enemyRotation = new Quaternion();

            playerPosition = playerModelInformation.transform.getTranslation(playerPosition);
            enemyPosition = mod.transform.getTranslation(playerPosition);
            enemyRotation = mod.transform.getRotation(enemyRotation);

            float dX = playerPosition.x - enemyPosition.x;
            float dZ = playerPosition.z - enemyPosition.z;

            Logger.log(Logger.ANDREAS, Logger.INFO, dX + ", " + dZ);

            float theta = (float) (Math.atan2(dX, dZ));
            /**
             * rotate (yaw pitch roll) x y z
             * This function operates with degrees and the Quaternion object enemyRotation seems to already operate in degrees,
             * however our theta value is in radians as it comes form the Math library and should thus be converted to degrees.
             */

            mod.transform.setFromEulerAngles((float) Math.toDegrees(theta), enemyRotation.getPitch(), enemyRotation.getRoll());


            /**if(aic.state != AIComponent.STATE.IDLE && !sta.frozen){
             float speedX =vel.velocity.x +  (float) Math.sin(rot.yaw) * 0.5f;
             speedX = (speedX < -10)? -10 : speedX;
             speedX = (speedX > 10)? 10 : speedX;
             float speedZ =vel.velocity.z +  (float) Math.cos(rot.yaw) * 0.5f;
             speedZ = (speedZ < -10)? -10 : speedZ;
             speedZ = (speedZ > 10)? 10 : speedZ;
             vel.velocity.x = speedX;
             vel.velocity.z = speedZ;
             }*/
            //TODO: Redo this

        }
    }


    @Override
    public void entityAdded(Entity entity) {
        player = entity;
    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}
