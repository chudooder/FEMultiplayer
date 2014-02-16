package net.fe.balance;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import javax.swing.*;

import net.fe.unit.*;

public class StatBalancer extends JFrame {
	private static final long serialVersionUID = 1L;
	private TreeMap<String, BalancerTab> classTabs;
	
	
	public StatBalancer(List<Unit> units){
		super("FE:Multiplayer Stat Balancer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		classTabs = new TreeMap<String, BalancerTab>();
		
		JTabbedPane panel = new JTabbedPane();
		
		HashMap<String, ArrayList<BalanceData>> data = 
				new HashMap<String, ArrayList<BalanceData>>();
		for(Unit u: units){
			String clazz = u.getTheClass().name;
			if(!data.containsKey(clazz)){
				
				data.put(clazz, new ArrayList<BalanceData>());
			}
			data.get(clazz).add(new BalanceData(u));
		}
		
		for(String clazz: data.keySet()){
			
			BalancerTab tab = new BalancerTab(data.get(clazz));
			classTabs.put(clazz,tab);
			panel.addTab(clazz, tab);
		}
		
		add(panel);
		
		JButton button = new JButton("Export");
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				int ret = chooser.showSaveDialog(StatBalancer.this);
				if(ret == JFileChooser.APPROVE_OPTION){
					export(chooser.getSelectedFile());
				}
			}
		});
		add(button, BorderLayout.SOUTH);
		
		pack();
	}
	
	private void export(File f){
		try {
			PrintWriter pw = new PrintWriter(f);
			String head = "#Name\t\tClass\t\t\tLv\t";
			for(String stat: BalanceData.ORDER){
				head += stat + "\t";
			}
			head += "Con\tMov\t\t";
			for(String stat: BalanceData.ORDER){
				head += stat + "\t";
			}
			head += "\tGender";
			pw.println(head);
			for(String clazz: classTabs.keySet()){
				pw.println(classTabs.get(clazz).exportString());
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				WeaponFactory.loadWeapons();
				UnitFactory.loadUnits();
				StatBalancer balancer = new StatBalancer(UnitFactory.getAllUnits());
				balancer.setLocationRelativeTo(null);
				balancer.setVisible(true);
			}
		});
	}
}
