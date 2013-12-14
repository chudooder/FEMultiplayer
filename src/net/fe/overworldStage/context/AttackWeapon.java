package net.fe.overworldStage.context;

import java.util.ArrayList;

import net.fe.overworldStage.*;
import net.fe.unit.*;

public class AttackWeapon extends OverworldContext{
	private Unit attacker;
	private Unit defender;
	private BattlePreview preview;
	private ArrayList<Weapon> weapons;
	private int index;
	public AttackWeapon(OverworldStage s, OverworldContext prevContext, Unit a, Unit d) {
		super(s, prevContext);
		attacker = a;
		defender = d;
		//TODO Positioning
		preview = new BattlePreview(0, 150, a, d, Grid.getDistance(a, d));
		
	}
	
	public void startContext(){
		super.startContext();
		stage.addEntity(preview);
		weapons = attacker.equippableWeapons(Grid.getDistance(attacker, defender));
	}

	@Override
	public void onSelect() {
		stage.addCmd("Attack");
		stage.addCmd(new UnitIdentifier(defender));
		stage.send();
	
		attacker.moved();
		cursor.setXCoord(attacker.getXCoord());
		cursor.setYCoord(attacker.getYCoord());
		stage.returnToNeutral();
	}

	@Override
	public void onCancel() {
		stage.removeEntity(preview);
		prev.startContext();
	}

	@Override
	public void onUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeft() {
		index--;
		if(index < 0){
			index += weapons.size();
		}
		equip();
	}

	@Override
	public void onRight() {
		index = (index+1)%weapons.size();
		equip();
	}
	
	public void equip(){
		attacker.equip(weapons.get(index));
	}

}
