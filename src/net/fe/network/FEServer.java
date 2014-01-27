package net.fe.network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;

import net.fe.Player;
import net.fe.Session;
import net.fe.lobbystage.LobbyStage;
import net.fe.modifier.MadeInChina;
import net.fe.modifier.Modifier;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;
import chu.engine.Game;
import chu.engine.Stage;
import java.awt.GridLayout;

/**
 * A game that does not render anything. Manages logic only
 * @author Shawn
 *
 */
public class FEServer extends Game {
	
	private static Server server;
	private static Stage currentStage;
	public static LobbyStage lobby;
	
	public static void main(String[] args) {
		final JFrame frame = new JFrame("FEServer");
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		DefaultListModel sModel = new DefaultListModel();
		DefaultListModel model = new DefaultListModel();
		model.addElement(new MadeInChina());
		
		final JPanel panel_4 = new JPanel();
		frame.getContentPane().add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		panel_4.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel mapNameLabel = new JLabel("Map: ");
		panel.add(mapNameLabel);
		
		// populate list of maps
		final JComboBox mapSelectionBox = new JComboBox();
		panel.add(mapSelectionBox);
		mapSelectionBox.setModel(new DefaultComboBoxModel(new String[]{"town", "plains"}));
		
		JPanel panel_1 = new JPanel();
		panel_4.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel label = new JLabel("Max units: ");
		panel_1.add(label);
		
		final JSpinner maxUnitsSpinner = new JSpinner();
		maxUnitsSpinner.setModel(new SpinnerNumberModel(8, 1, 8, 1));
		panel_1.add(maxUnitsSpinner);
		
		JPanel panel_2 = new JPanel();
		panel_4.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		final JList selectedModifiersList = new JList();
		selectedModifiersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel_2.add(selectedModifiersList);
		selectedModifiersList.setBorder(new LineBorder(new Color(0, 0, 0)));
		selectedModifiersList.setModel(sModel);
		selectedModifiersList.setPreferredSize(new Dimension(100,15));
		
		JPanel buttonsPanel = new JPanel();
		panel_2.add(buttonsPanel);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		final JList modifiersList = new JList();
		modifiersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel_2.add(modifiersList);
		modifiersList.setBorder(new LineBorder(new Color(0, 0, 0)));
		modifiersList.setModel(model);
		modifiersList.setPreferredSize(new Dimension(100,15));
		
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
					frame.remove(panel_4);
					frame.remove(startServer);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				frame.pack();
				Thread serverThread = new Thread() {
					public void run() {
						FEServer feserver = new FEServer();
						Session s = FEServer.getServer().getSession();
						s.setMaxUnits((Integer)maxUnitsSpinner.getValue());
						for(int i=0; i< selectedModifiersList.getModel().getSize(); i++) {
							Modifier m = (Modifier) selectedModifiersList.getModel().getElementAt(i);
							s.addModifier(m);
						}
						s.setMap((String)mapSelectionBox.getSelectedItem());
						feserver.init();
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
