package net.fe.network;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;

import net.fe.Player;
import net.fe.Session;
import net.fe.lobbystage.LobbyStage;
import net.fe.modifier.DivineIntervention;
import net.fe.modifier.MadeInChina;
import net.fe.modifier.Modifier;
import net.fe.modifier.SuddenDeath;
import net.fe.modifier.Treasury;
import net.fe.overworldStage.objective.Objective;
import net.fe.overworldStage.objective.Rout;
import net.fe.overworldStage.objective.Seize;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import chu.engine.Game;
import chu.engine.Stage;

/**
 * A game that does not render anything. Manages logic only
 * @author Shawn
 *
 */
public class FEServer extends Game {
	
	private static Server server;
	private static Stage currentStage;
	public static LobbyStage lobby;
	private static Map<String, Objective[]> maps;
	
	public static void main(String[] args) {
		final JFrame frame = new JFrame("FEServer");
		
		Rout rout = new Rout();
		Seize seize = new Seize();
		
		maps = new HashMap<String, Objective[]>();
		maps.put("town", new Objective[]{rout});
		maps.put("plains", new Objective[]{rout, seize});
		maps.put("fort", new Objective[]{rout, seize});
		maps.put("decay", new Objective[]{rout, seize});
		
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		DefaultListModel sModel = new DefaultListModel();
		// Modifiers
		DefaultListModel model = new DefaultListModel();
		model.addElement(new MadeInChina());
		model.addElement(new Treasury());
		model.addElement(new DivineIntervention());
		model.addElement(new SuddenDeath());
		
		final JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel mapPanel = new JPanel();
		mainPanel.add(mapPanel);
		mapPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel mapNameLabel = new JLabel("Map: ");
		mapPanel.add(mapNameLabel);
		
		JPanel objectivePanel = new JPanel();
		mainPanel.add(objectivePanel);
		
		JLabel objLabel = new JLabel("Objective: ");
		objectivePanel.add(objLabel);
		
		final JComboBox objComboBox = new JComboBox();
		objectivePanel.add(objComboBox);
		
		// populate list of maps
		final JComboBox mapSelectionBox = new JComboBox();
		mapSelectionBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				objComboBox.setModel(new DefaultComboBoxModel(maps.get(mapSelectionBox.getSelectedItem())));
			}
		});
		mapPanel.add(mapSelectionBox);
		mapSelectionBox.setModel(new DefaultComboBoxModel(maps.keySet().toArray()));
		
		// Objectives
		ComboBoxModel oModel = new DefaultComboBoxModel(maps.get(mapSelectionBox.getSelectedItem()));
		objComboBox.setModel(oModel);
		
		JPanel maxUnitsPanel = new JPanel();
		mainPanel.add(maxUnitsPanel);
		maxUnitsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel label = new JLabel("Max units: ");
		maxUnitsPanel.add(label);
		
		final JSpinner maxUnitsSpinner = new JSpinner();
		maxUnitsSpinner.setModel(new SpinnerNumberModel(8, 1, 8, 1));
		maxUnitsPanel.add(maxUnitsSpinner);
		
		JSeparator separator = new JSeparator();
		mainPanel.add(separator);
		
		JLabel modifiersLabel = new JLabel("Modifiers");
		modifiersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(modifiersLabel);
		
		JPanel modifiersPane = new JPanel();
		mainPanel.add(modifiersPane);
		modifiersPane.setLayout(new BoxLayout(modifiersPane, BoxLayout.X_AXIS));
		
		JScrollPane selectedModifiersScrollPane = new JScrollPane();
		selectedModifiersScrollPane.setPreferredSize(new Dimension(120,150));
		modifiersPane.add(selectedModifiersScrollPane);
		
		JScrollPane modifiersScrollPane = new JScrollPane();
		modifiersScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		modifiersScrollPane.setPreferredSize(new Dimension(120,150));
		
		final ModifierList modifiersList = new ModifierList();
		modifiersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		modifiersScrollPane.add(modifiersList);
		modifiersList.setModel(model);
		modifiersScrollPane.setViewportView(modifiersList);
		
		final ModifierList selectedModifiersList = new ModifierList();
		selectedModifiersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectedModifiersScrollPane.add(selectedModifiersList);
		selectedModifiersList.setModel(sModel);
		selectedModifiersScrollPane.setViewportView(selectedModifiersList);
		
		JPanel buttonsPanel = new JPanel();
		modifiersPane.add(buttonsPanel);
		
		JButton addModifierBtn = new JButton("<-- Add");
		addModifierBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = modifiersList.getSelectedIndex();
				if(index != -1) {
					Object o = modifiersList.getModel().getElementAt(index);
					((DefaultListModel)modifiersList.getModel()).remove(modifiersList.getSelectedIndex());
					((DefaultListModel)selectedModifiersList.getModel()).add(0,o);
				}
			}
		});
		buttonsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		buttonsPanel.add(addModifierBtn);
		
		JButton removeModifierBtn = new JButton("Remove -->");
		removeModifierBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = selectedModifiersList.getSelectedIndex();
				if(index != -1) {
					Object o = selectedModifiersList.getModel().getElementAt(index);
					((DefaultListModel)selectedModifiersList.getModel()).remove(selectedModifiersList.getSelectedIndex());
					((DefaultListModel)modifiersList.getModel()).add(0,o);
				}
			}
		});
		buttonsPanel.add(removeModifierBtn);
		
		modifiersPane.add(modifiersScrollPane);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		mainPanel.add(verticalStrut);
		
		final JButton startServer = new JButton("Start server");
		startServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					frame.getContentPane().add(new JLabel("Server IP: " + InetAddress.getLocalHost().getHostAddress()){
						private static final long serialVersionUID = 1L;
					{
						this.setFont(getFont().deriveFont(20f));
						this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
					}}, BorderLayout.NORTH);
					frame.remove(mainPanel);
					frame.remove(startServer);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				frame.pack();
				Thread serverThread = new Thread() {
					public void run() {
						FEServer feserver = new FEServer();
						try{
							Session s = FEServer.getServer().getSession();
							s.setMaxUnits((Integer)maxUnitsSpinner.getValue());
							for(int i=0; i< selectedModifiersList.getModel().getSize(); i++) {
								Modifier m = (Modifier) selectedModifiersList.getModel().getElementAt(i);
								s.addModifier(m);
							}
							s.setMap((String)mapSelectionBox.getSelectedItem());
							s.setObjective((Objective)objComboBox.getSelectedItem());
							feserver.init();
							feserver.loop();
						} catch (Exception e){
							System.err.println("Exception occurred, writing to logs...");
							e.printStackTrace();
							try{
								File errLog = new File("error_log_server" + System.currentTimeMillis()%100000000 + ".log");
								PrintWriter pw = new PrintWriter(errLog);
								e.printStackTrace(pw);
								pw.close();
							}catch (IOException e2){
								e2.printStackTrace();
							}
							System.exit(0);
						}
					}
				};
				serverThread.start();
			}
		});
		frame.getContentPane().add(startServer, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	protected Session getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	public FEServer() {
		server = new Server();
	}
	
	public void init() {
		messages = new ArrayList<Message>();
		Thread serverThread = new Thread() {
			public void run() {
				server.start(21255);
			}
		};
		lobby = new LobbyStage(server.getSession());
		currentStage = lobby;
		serverThread.start();
	}
	
	public static Unit getUnit(UnitIdentifier id){
		for(Player p: getPlayers().values()){
			if(!p.isSpectator() && p.getParty().getColor().equals(id.partyColor)){
				return p.getParty().search(id.name);
			}
		}
		return null;
	}
	
	@Override
	public void loop() {
		boolean yes = true;
		while(yes) {
			time = System.nanoTime();
			messages.clear();
			messages.addAll(server.messages);
			for(Message m : messages)
				server.messages.remove(m);
			currentStage.beginStep();
			currentStage.onStep();
			currentStage.endStep();
			timeDelta = System.nanoTime()-time;
		}
	}
	
	public static Stage getCurrentStage() {
		return currentStage;
	}
	
	public static void setCurrentStage(Stage stage) {
		currentStage = stage;
	}

	public static Server getServer() {
		return server;
	}
	
	public static HashMap<Byte, Player> getPlayers() {
		return server.getSession().getPlayerMap();
	}

	public static void resetToLobby() {
		for(Player p : getPlayers().values()) {
			p.ready = false;
		}
		currentStage = lobby;
	}

}

class ModifierList extends JList {
	
	private static final long serialVersionUID = 561574462354745569L;

	public String getToolTipText(MouseEvent event) {
		Point p = event.getPoint();
		int index = locationToIndex(p);
		String tip = ((Modifier) getModel().getElementAt(index)).getDescription();
		return tip;
	}
	
}
