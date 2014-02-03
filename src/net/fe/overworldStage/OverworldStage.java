package net.fe.overworldStage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.fe.Party;
import net.fe.Player;
import net.fe.Session;
import net.fe.editor.Level;
import net.fe.editor.SpawnPoint;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.HealCalculator;
import net.fe.modifier.Modifier;
import net.fe.network.Chat;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.message.ChatMessage;
import net.fe.network.message.CommandMessage;
import net.fe.network.message.EndGame;
import net.fe.network.message.EndTurn;
import net.fe.overworldStage.objective.Objective;
import net.fe.unit.Item;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

import org.newdawn.slick.Color;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;
import chu.engine.Stage;

public class OverworldStage extends Stage {
	public Grid grid;
	protected Chat chat;
	protected Session session;
	private ArrayList<Player> turnOrder;
	private int currentPlayer;
	private int turnCount;

	public OverworldStage(Session s) {
		super("overworld");
		this.session = s;
		System.out.println(session.getObjective().getDescription());
		chat = new Chat();
		turnOrder = new ArrayList<Player>();
		for(Player p : session.getPlayers()) {
			if(!p.isSpectator()) turnOrder.add(p);
		}
		Collections.sort(turnOrder, new Comparator<Player>() {
			@Override
			public int compare(Player arg0, Player arg1) {
				return arg0.getID() - arg1.getID();
			}
		});
		currentPlayer = 0;
		turnCount = 1;
		loadLevel(session.getMap());
		for(Modifier m : session.getModifiers()) {
			m.initOverworld(this);
		}
		processAddStack();
	}
	
	public Player getPlayerByID(int id) {
		for(Player p : session.getPlayers()) {
			if(p.getID() == id) {
				return p;
			}
		}
		return null;
	}
	
	public Player getCurrentPlayer() {
		return turnOrder.get(currentPlayer);
	}
	
	public Player getNextPlayer() {
		int i = currentPlayer + 1;
		if(i >= turnOrder.size()) i = 0;
		return turnOrder.get(i);
	}

	public Terrain getTerrain(int x, int y) {
		return grid.getTerrain(x, y);
	}

	public Unit getUnit(int x, int y) {
		return grid.getUnit(x, y);
	}

	public boolean addUnit(Unit u, int x, int y) {
		if (grid.addUnit(u, x, y)) {
			this.addEntity(u);
			return true;
		} else {
			return false;
		}
	}

	public Unit removeUnit(int x, int y) {
		Unit u = grid.removeUnit(x, y);
		if (u != null) {
			this.removeEntity(u);
		}
		return u;
	}
	
	public void removeUnit(Unit u) {
		grid.removeUnit(u.getXCoord(), u.getYCoord());
		this.removeEntity(u);
	}
	
	public void loadLevel(String levelName) {
        try {
        	InputStream in = ResourceLoader.getResourceAsStream("levels/"+levelName+".lvl");
            ObjectInputStream ois = new ObjectInputStream(in);
            Level level = (Level) ois.readObject();
            Set<SpawnPoint> spawns = level.spawns;
            grid = new Grid(level.width, level.height, Terrain.NONE);
            for(int i=0; i<level.tiles.length; i++) {
            	for(int j=0; j<level.tiles[0].length; j++) {
            		grid.setTerrain(j, i, Tile.getTerrainFromID(level.tiles[i][j]));
            		if(Tile.getTerrainFromID(level.tiles[i][j]) == Terrain.THRONE) {
            			int blue = 0;
            			int red = 0;
            			for(SpawnPoint sp : spawns) {
                    		if(sp.team.equals(Party.TEAM_BLUE)) {
                				blue += Math.abs(sp.x-j) + Math.abs(sp.y-i);
                    		} else {
                    			red += Math.abs(sp.x-j) + Math.abs(sp.y-i);
                    		}
                    	}
            			if(blue < red) {
            				System.out.println(blue + " "+ red);
            				grid.setThronePos(Party.TEAM_BLUE, j, i);
            			} else {
            				System.out.println(blue + " "+ red);
            				grid.setThronePos(Party.TEAM_RED, j, i);
            			}
            		}
            	}
            }
            
            // Add units
            for(Player p : session.getPlayers()) {
            	Color team = p.getParty().getColor();
    			for(int i=0; i<p.getParty().size(); i++) {
    				SpawnPoint remove = null;
                	for(SpawnPoint sp : spawns) {
                		if(sp.team.equals(team)) {
            				Unit u = p.getParty().getUnit(i);
            				addUnit(u, sp.x, sp.y);
            				remove = sp;
            				break;
                		}
                	}
                	spawns.remove(remove);
    			}
            }
            ois.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void beginStep() {
		for(Message message : Game.getMessages()) {
			if(message instanceof CommandMessage) {
				processCommands((CommandMessage)message);
			}
			else if(message instanceof ChatMessage) {
				ChatMessage chatMsg = (ChatMessage)message;
				for(Player p : session.getPlayers())
					if(p.getID() == chatMsg.origin)
						chat.add(p, chatMsg.text);
			}
			else if(message instanceof EndTurn) {
				doEndTurn(message.origin);
				currentPlayer++;
				if(currentPlayer >= turnOrder.size()) {
					currentPlayer = 0;
				}
				doStartTurn(message.origin);
			}
		}
	}
	
	protected void doEndTurn(int playerID) {
		for(int x = 0; x < grid.width; x++){
			for(int y = 0; y < grid.height; y++){
				for(TerrainTrigger t: grid.getTerrain(x, y).getTriggers()){
					if(t.attempt(this, x, y, getCurrentPlayer()) && !t.start){
						beforeTriggerHook(t, x, y);
						t.endOfTurn(this, x, y);
					}
				}
			}
		}
		
		for(Player p : session.getPlayers()) {
			for(Unit u : p.getParty()) {
				u.setMoved(false);
			}
		}
		for(Modifier m : session.getModifiers()) {
			m.endOfTurn(this);
		}
		turnCount++;
		checkEndGame();
	}
	
	protected void doStartTurn(int playerID) {
		for(int x = 0; x < grid.width; x++){
			for(int y = 0; y < grid.height; y++){
				for(TerrainTrigger t: grid.getTerrain(x, y).getTriggers()){
					if(t.attempt(this, x, y, getCurrentPlayer()) && t.start){
						beforeTriggerHook(t, x, y);
						t.startOfTurn(this, x, y);
					}
				}
			}
		}
	}
	
	protected void beforeTriggerHook(TerrainTrigger t, int x, int y){
		
	}

	public void processCommands(CommandMessage message) {
		CommandMessage cmds = (CommandMessage) message;
		//TODO: command validation
		// After validation, update the unit position
		// Move it instantly since this is the server stage
		final Unit unit = (cmds.unit == null ? null : getUnit(cmds.unit));
		if(unit != null) {
			grid.move(unit, unit.getXCoord()+cmds.moveX, unit.getYCoord()+cmds.moveY, false);
			unit.setMoved(true);
		}
		// Parse commands
		
		for(int i=0; i<cmds.commands.length; i++) {
			Object obj = cmds.commands[i];
			if(obj.equals("EQUIP")) {
				Unit other = getUnit((UnitIdentifier) cmds.commands[++i]);
				other.equip((Integer) cmds.commands[++i]);
			}
			else if(obj.equals("TRADE")) {
				Unit u1 = getUnit((UnitIdentifier) cmds.commands[++i]);
				int i1 = (Integer)cmds.commands[++i];
				Unit u2 = getUnit((UnitIdentifier) cmds.commands[++i]);
				int i2 = (Integer)cmds.commands[++i];
				//Swap the two items
				Item temp = u1.getInventory().get(i1);
				u1.getInventory().set(i1, u2.getInventory().get(i2));
				u2.getInventory().set(i2, temp);
			}
			else if(obj.equals("USE")) {
				int index = (Integer)cmds.commands[++i];
				unit.use(index);
			}
			else if(obj.equals("RESCUE")) {
				Unit rescuee = getUnit((UnitIdentifier) cmds.commands[++i]);
				unit.rescue(rescuee);
			}
			else if(obj.equals("TAKE")) {
				Unit other = getUnit((UnitIdentifier) cmds.commands[++i]);
				other.give(unit);
			}
			else if(obj.equals("DROP")) {
				int dropX = (Integer) cmds.commands[++i];
				int dropY = (Integer) cmds.commands[++i];
				unit.drop(dropX, dropY);
			}
			else if(obj.equals("ATTACK")) {
				//This updates HP so we're ok
				CombatCalculator calc = new CombatCalculator(cmds.unit, (UnitIdentifier) cmds.commands[++i], false);
				cmds.attackRecords = calc.getAttackQueue();
			}
			else if(obj.equals("HEAL")) {
				//This updates HP so we're ok
				HealCalculator calc = new HealCalculator(cmds.unit, (UnitIdentifier) cmds.commands[++i]);
				cmds.attackRecords = calc.getAttackQueue();
			}
		}
		FEServer.getServer().broadcastMessage(message);
		checkEndGame();
	}

	public void checkEndGame() {
		// Objective evaluation
		int winner = session.getObjective().evaluate(this);
		if(winner > 0 && FEServer.getServer() != null) {
			FEServer.getServer().broadcastMessage(new EndGame((byte) 0, winner));
			FEServer.resetToLobby();
		}
	}

	protected Unit getUnit(UnitIdentifier id) {
		for(Player p: session.getPlayers()){
			if(!p.isSpectator() && p.getParty().getColor().equals(id.partyColor)){
				return p.getParty().search(id.name);
			}
		}
		return null;
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endStep() {
		
	}

	public Player[] getPlayers() {
		return session.getPlayers();
	}
	
	public ArrayList<Player> getNonSpectators() {
		ArrayList<Player> ans = new ArrayList<Player>();
		for(Player p : session.getPlayers()) {
			if(!p.isSpectator()) ans.add(p);
		}
		return ans;
	}
	
	public Objective getObjective(){
		return session.getObjective();
	}
	
	public int getTurnCount(){
		return turnCount;
	}

	public List<Unit> getAllUnits() {
		List<Unit> units = new ArrayList<Unit>();
		for(Player p : session.getPlayers()) {
			for(int i=0; i<p.getParty().size(); i++) {
				units.add(p.getParty().getUnit(i));
			}
		}
		return units;
	}

}
