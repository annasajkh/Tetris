package com.github.annasajkh;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Block
{
	Vector2 position;
	final static Vector2 size = new Vector2(30,30);
	final static Vector2 halfSize = size.cpy().scl(0.5f);
	Color color;
	
	
	public Block(Vector2 position,Color color)
	{
		this.position = position;
		this.color = color;
	}
	
	public void draw(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.setColor(color);
		shapeRenderer.rect(position.x - halfSize.x, position.y - halfSize.y, size.x, size.y);
	}
}
