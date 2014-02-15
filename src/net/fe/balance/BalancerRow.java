package net.fe.balance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.*;

public class BalancerRow extends JPanel{
	private BalanceData data;
	private BalancerTab tab;
	private HashMap<String, JLabel> values;
	
	public BalancerRow(BalanceData d, BalancerTab tab){
		this.data = d;
		this.tab = tab;
		values = new HashMap<String, JLabel>();
		setVerticalAlignment(SwingConstants.TOP);
		add(new JLabel(d.name){{
			setPreferredSize(new Dimension(60,3*getPreferredSize().height));
			setHorizontalAlignment(SwingConstants.RIGHT);
			setVerticalAlignment(SwingConstants.TOP);
		}});
		JPanel allStats = new JPanel(new GridLayout(3, 8));
			for(String stat: BalanceData.ORDER){
				StatSpinner spinner = new StatSpinner(stat, d.bases.get(stat), 1);
				spinner.addChangeListener(new ChangeListener(){
					@Override
					public void stateChanged(ChangeEvent e) {
						StatSpinner source = (StatSpinner) e.getSource();
						data.bases.put(source.name, (Integer)source.getValue());
						refresh();
					}
				});
				allStats.add(spinner);
			}
			
			for(String stat: BalanceData.ORDER){
				StatSpinner spinner = new StatSpinner(stat, d.growths.get(stat), 5);
				spinner.addChangeListener(new ChangeListener(){
					@Override
					public void stateChanged(ChangeEvent e) {
						StatSpinner source = (StatSpinner) e.getSource();
						data.growths.put(source.name, (Integer)source.getValue());
						refresh();
					}
				});
				allStats.add(spinner);
			}

			for(String stat: BalanceData.ORDER){
				JLabel label = new JLabel();
				label.setHorizontalAlignment(SwingConstants.CENTER);
				values.put(stat, label);
				allStats.add(label);
			}
			
		add(allStats);
		refresh();
		setMaximumSize(getPreferredSize());
	}
	private void setVerticalAlignment(int top) {
		// TODO Auto-generated method stub
		
	}
	public void refresh(){
		int lv = tab.getLevel();
		for(String stat: BalanceData.ORDER){
			int value = data.bases.get(stat) + (lv - 1) * data.growths.get(stat) / 100;
			values.get(stat).setText(value + "");
		}
	}
	
	public String exportString(){
		return data.exportString();
	}
}
