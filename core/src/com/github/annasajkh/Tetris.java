package com.github.annasajkh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Tetris extends ApplicationAdapter 
{
	
	ShapeRenderer shapeRenderer;
	static List<Pieces> pieces;
	static Type[] types = {Type.I,Type.J,Type.L,Type.O,Type.S,Type.T,Type.Z};
	
	@Override
	public void create()
	{
		pieces = new ArrayList<>();
		pieces.add(new Pieces(new Vector2(Gdx.graphics.getWidth() * 0.5f,585),types[MathUtils.random(6)]));
		shapeRenderer = new ShapeRenderer();
	}
	

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Map<Float,Integer> map = new HashMap<>();
		
		for(Pieces p : pieces)
		{
			if(!p.moving)
			{
				for(Block block : p.blocks)
				{
					if(map.containsKey(block.position.y))
					{
						map.put(block.position.y,map.get(block.position.y) + 1);
					}
					else
					{
						map.put(block.position.y,1);
					}
				}
			}
		}
		
		float largestRemovePos = Float.MIN_VALUE;
		
		for(Float key : map.keySet())
		{
			if(map.get(key) >= 13)
			{
				if(key > largestRemovePos)
				{
					largestRemovePos = key;
				}
				for(Pieces p : pieces)
				{
					
					if(!p.moving)
					{
						for(int j = p.blocks.size() - 1; j >= 0; j--)
						{
							if(p.blocks.get(j).position.y == key)
							{
								p.blocks.remove(j);
							}
						}
					}
				}
			}
		}
		
		for(int i = pieces.size() - 1; i >= 0; i--)
		{
			Pieces p = pieces.get(i);
			
			if(p.blocks.isEmpty())
			{
				pieces.remove(p);
			}
		}
		
		if(largestRemovePos != Float.MIN_VALUE)
		{
			Pieces chunk = new Pieces(Vector2.Zero,Type.E);
			
			for(Pieces p : pieces)
			{
				if(!p.moving)
				{
					for(Block block : p.blocks)
					{
						if(block.position.y > largestRemovePos)
						{
							chunk.blocks.add(block);
						}
					}
				}
			}
			
			if(!chunk.blocks.isEmpty())
			{
				while(!chunk.collideChunk() && !chunk.collideFloorFuture())
				{
					chunk.move(0,-Block.size.y);
				}
			}
		}
		
		
		for(int i = pieces.size()-1;i >= 0; i--)
		{
			pieces.get(i).update();
		}
		
		shapeRenderer.begin(ShapeType.Filled);
		for(Pieces p : pieces)
		{
			p.draw(shapeRenderer);
		}
		shapeRenderer.end();
		
		for(int i = pieces.size() - 1;i >= 0; i--)
		{
			Pieces p = pieces.get(i);
			
			if(!p.moving && p.containsPosY(585))
			{
				create();
				return;
			}
		}

		try
		{
			TimeUnit.MILLISECONDS.sleep(50);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void dispose() 
	{
		shapeRenderer.dispose();
	}
}
