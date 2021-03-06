package com.github.annasajkh;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

enum Type
{
	I,
	O,
	T,
	L,
	J,
	S,
	Z,
	E
}

public class Pieces
{
	Type type;
	Vector2 pivot;
	int counter = 0;
	
	List<Block> blocks = new ArrayList<>();
	static int[] angles = new int[] {0,90,180,270};
	int angle = angles[MathUtils.random(0,3)];
	boolean moving = true;
	
	public Pieces(Vector2 position,Type type)
	{
		this.type = type;
		Color color;
		
		switch(type)
		{
			case I:
				color = Color.CYAN;
				
				for (int i = 1; i < 5; i++)
				{
					blocks.add(new Block(new Vector2((i * Block.size.x + position.x) - Block.size.x * 2,position.y),color));
					
					if(i == 2)
					{
						pivot = new Vector2(position.x +  Block.halfSize.x,position.y - Block.halfSize.x);
					}
				}
				break;
			case O:
				color = Color.YELLOW;
				
				for (int i = 1; i < 3; i++)
				{
					blocks.add(new Block(new Vector2((i * Block.size.x + position.x) - Block.size.x,position.y),color));
				}
				
				for (int i = 1; i < 3; i++)
				{
					blocks.add(new Block(new Vector2((i * Block.size.x + position.x) - Block.size.x, position.y - Block.size.y),color));
				}
				break;
			case T:
				color = Color.PURPLE;
				for (int i = 1; i < 4; i++)
				{
					if(i == 2)
					{
						pivot =  new Vector2((i * Block.size.x + position.x) - Block.size.x,position.y);
					}
					blocks.add(new Block(new Vector2((i * Block.size.x + position.x) - Block.size.x,position.y),color));
				}
				

				blocks.add(new Block(new Vector2(pivot.x,pivot.y - Block.size.y),color));
				
				break;
			case L:
				color = Color.ORANGE;
				
				for (int i = 1; i < 4; i++)
				{
					if(i == 2)
					{
						pivot =  new Vector2((i * Block.size.x + position.x) - Block.size.x,position.y);
					}
					blocks.add(new Block(new Vector2((i * Block.size.x + position.x) - Block.size.x,position.y),color));
				}
				blocks.add(new Block(new Vector2(blocks.get(0).position.x,blocks.get(0).position.y - Block.size.y),color));
				break;
			case J:
				color = Color.BLUE;
				
				for (int i = 1; i < 4; i++)
				{
					if(i == 2)
					{
						pivot =  new Vector2((i * Block.size.x + position.x) - Block.size.x,position.y);
					}
					blocks.add(new Block(new Vector2((i * Block.size.x + position.x) - Block.size.x,position.y),color));
				}
				blocks.add(new Block(new Vector2(blocks.get(blocks.size() - 1).position.x,blocks.get(0).position.y - Block.size.y),color));
				break;
			case S:
				color = Color.LIME;
				
				for (int i = 1; i < 3; i++)
				{
					blocks.add(new Block(new Vector2(i * Block.size.x + position.x,position.y),color));
				}
				for (int i = 1; i < 3; i++)
				{
					blocks.add(new Block(new Vector2((i * Block.size.x + position.x) - Block.size.x,position.y - Block.size.y),color));
				}
				pivot =  blocks.get(blocks.size()-1).position.cpy();
				break;
			case Z:
				color = Color.RED;
				
				for (int i = 1; i < 3; i++)
				{
					blocks.add(new Block(new Vector2((i * Block.size.x + position.x) - Block.size.x,position.y - Block.size.y),color));
				}


				pivot =  blocks.get(blocks.size()-1).position.cpy();
				
				for (int i = 1; i < 3; i++)
				{
					blocks.add(new Block(new Vector2(i * Block.size.x + position.x,position.y),color));
				}
			case E:
		}
		rotate(angle);
	}
	
	private void rotate(float angle)
	{
		if(pivot != null)
		{
			for(Block block : blocks)
			{
				block.position.rotateAround(pivot,angle);
			}
		}
	}

	public boolean collideChunk()
	{
		for(Pieces p : Tetris.pieces)
		{
			for(Block otherBlock : p.blocks)
			{
				for(Block block : blocks)
				{
					Vector2 futurePos = block.position.cpy();
					futurePos.y -= Block.size.y;
						
					if(futurePos.equals(otherBlock.position) && !blocks.contains(otherBlock))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean collideFuture(float xAdd,float yAdd)
	{
		for(Pieces p : Tetris.pieces)
		{
			if(!p.equals(this))
			{
				for(Block block : blocks)
				{
					for(Block otherBlock : p.blocks)
					{
						Vector2 futurePos = block.position.cpy();
						futurePos.x += xAdd;
						futurePos.y += yAdd;
					
						if(futurePos.equals(otherBlock.position))
						{
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}

	public void move(float x, float y)
	{
		for(Block block : blocks)
		{
			block.position.x += x;	
			block.position.y += y;	
		}
		if(pivot != null)
		{
			pivot.x += x;
			pivot.y += y;
		}
	}
	
	public boolean collideFloorFuture()
	{
		for(Block block : blocks)
		{
			if(block.position.y - Block.size.y < 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean collideWallRightFuture()
	{
		for(Block block : blocks)
		{
			if(block.position.x + Block.size.x > Gdx.graphics.getWidth())
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean collideWallLeftFuture()
	{
		for(Block block : blocks)
		{
			if(block.position.x - Block.size.x < 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean containsPosY(float y)
	{
		for (Block block : blocks)
		{
			if(block.position.y == y)
			{
				return true;
			}
		}
		return false;
	}
	
	public void resolveCollision()
	{
		while(collideFuture(-Block.size.x,0) || collideWallLeftFuture())
		{
			move(Block.size.x,0);
		}
		
		while(collideFuture(Block.size.x,0) || collideWallRightFuture())
		{
			move(-Block.size.x,0);
		}
		
		while(collideFuture(0,-Block.size.y))
		{
			move(0,Block.size.y);
		}
		
		while(collideFloorFuture())
		{
			move(0,Block.size.y);
		}
	}
	
	public void update()
	{
		if(moving)
		{
			if(Gdx.input.isKeyJustPressed(Keys.LEFT))
			{
				resolveCollision();
				rotate(90);
			}
			else if(Gdx.input.isKeyJustPressed(Keys.RIGHT))
			{
				resolveCollision();
				rotate(-90);
			}
			
			if(Gdx.input.isKeyJustPressed(Keys.SPACE))
			{
				while(!collideFloorFuture() && !collideFuture(0,-Block.size.y))
				{
					move(0,-Block.size.y);
				}
				moving = false;
				Tetris.pieces.add(new Pieces(new Vector2(Gdx.graphics.getWidth() * 0.5f,585),Tetris.types[MathUtils.random(6)]));
				return;
			}
			
			if(Gdx.input.isKeyPressed(Keys.A))
			{
				if(!collideFuture(-Block.size.x,0) && !collideWallLeftFuture())
				{
					for(Block block : blocks)
					{
						block.position.x -= Block.size.x;			
					}
					if(type != Type.O)
					{			
						pivot.x -= Block.size.x;
					}
				}
			}
			else if(Gdx.input.isKeyPressed(Keys.D))
			{
				if(!collideFuture(Block.size.x,0) && !collideWallRightFuture())
				{
					for(Block block : blocks)
					{
						block.position.x += Block.size.x;			
					}
					if(type != Type.O)
					{			
						pivot.x += Block.size.x;
					}
				}
			}
			
			if(collideFuture(0,-Block.size.y))
			{
				moving = false;
				Tetris.pieces.add(new Pieces(new Vector2(Gdx.graphics.getWidth() * 0.5f,585),Tetris.types[MathUtils.random(6)]));
				return;
			}
			
			if(collideFloorFuture())
			{
				moving = false;
				Tetris.pieces.add(new Pieces(new Vector2(Gdx.graphics.getWidth() * 0.5f,585),Tetris.types[MathUtils.random(6)]));
				return;
			}
			if(counter > 100)
			{
				move(0,-Block.size.y);
				counter = 0;
			}
			counter += 400 * Gdx.graphics.getDeltaTime();
		}
	}
	
	public void draw(ShapeRenderer shapeRenderer)
	{
		for(Block block : blocks)
		{
			block.draw(shapeRenderer);
		}
	}
	
}
