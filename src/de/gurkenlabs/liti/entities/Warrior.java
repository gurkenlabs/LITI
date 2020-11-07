package de.gurkenlabs.liti.entities;

import de.gurkenlabs.litiengine.entities.*;

@EntityInfo(width = 18, height = 22)
@CollisionInfo(collision = true, collisionBoxWidth = 7, collisionBoxHeight = 12)
@CombatInfo(hitpoints = 100)
@MovementInfo(velocity = 50)
public class Warrior extends Character {
}
