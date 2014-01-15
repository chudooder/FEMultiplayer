package net.fe.overworldStage;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.RunesBg;
import net.fe.fightStage.FightStage;
import net.fe.unit.MapAnimation;
import net.fe.unit.Unit;
import net.fe.unit.UnitIcon;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class EndGameStage extends Stage {
	
	private ArrayList<Player> players;
	
	private static final int X0 = 5;
	private static final int Y0 = 100;
	private static final int X_SPACING = 235;
	private static final int Y_SPACING = 24;
	
	public EndGameStage(ArrayList<Player> players) {
		this.players = players;
		addEntity(new RunesBg(new Color(0xd2b48c)));
		for(int x=0; x<players.size(); x++) {
			Player p = players.get(x);
			for(int i=0; i<p.getParty().size(); i++) {
				addEntity(new UnitIcon(p.getParty().getUnit(i), X0+x*X_SPACING, Y0+i*Y_SPACING, 0.5f));
			}
		}
		processAddStack();
	}

	@Override
	public void beginStep() {
		for(Entity e : entities) {
			e.beginStep();
		}
		for(KeyboardEvent key : Game.getKeys()) {
			if(key.state && key.key == Keyboard.KEY_RETURN) {
				FEMultiplayer.setCurrentStage(FEMultiplayer.lobby);
			}
		}
	}

	@Override
	public void onStep() {
		MapAnimation.updateAll();
		for(Entity e : entities) {
			e.onStep();
		}
	}

	@Override
	public void endStep() {
		for(Entity e : entities) {
			e.endStep();
		}
	}
	
	public void render() {
		super.render();
		Renderer.drawString("default_med", "Press Enter to return to lobby...", 200, 5, 0.5f);
		String[] stats = {"Kills", "Assists", "Damage", "Healing"};
		for(int i=0; i<players.size(); i++) {
			Player p = players.get(i);
			Renderer.drawBorderedRectangle(X0+X_SPACING*i, Y0-3, X0+X_SPACING*(i+1), Y0+Y_SPACING*p.getParty().size(), 0.9f, 
					 FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
			Renderer.drawBorderedRectangle(X0+X_SPACING*i, Y0-28, X0+X_SPACING*(i+1), Y0-8, 0.9f, 
					 FightStage.NEUTRAL, FightStage.BORDER_LIGHT, FightStage.BORDER_DARK);
			for(int k=0; k<stats.length; k++) {
				Renderer.drawString("default_med", stats[k], X0+70+40*k+X_SPACING*i, Y0-25, 0.5f);
			}
			Renderer.drawString("default_med", "Name", X0+20+X_SPACING*i, Y0-25, 0.5f);
			for(int j=0; j<p.getParty().size(); j++) {
				Unit u = p.getParty().getUnit(j);
				Renderer.drawString("default_med", u.name, X0+20+X_SPACING*i, Y0+j*Y_SPACING, 0.5f);
				for(int k=0; k<stats.length; k++) {
					Renderer.drawString("default_med", u.getBattleStat(stats[k]), X0+75+40*k+X_SPACING*i, Y0+j*Y_SPACING, 0.5f);
				}
			}	
		}
	}

}
