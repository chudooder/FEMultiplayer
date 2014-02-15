package net.fe.balance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BalancerTab extends JPanel {
	private List<BalancerRow> rows;
	private int lv;
	public BalancerTab(List<BalanceData> data){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lv = 1;
		rows = new ArrayList<BalancerRow>();
		
		JComponent labels = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		labels.add(Box.createRigidArea(new Dimension(65,0)));
		for(String stat: BalanceData.ORDER){
			labels.add(new JLabel(stat){{
				this.setHorizontalAlignment(CENTER);
				setPreferredSize(new Dimension(53,getPreferredSize().height));
			}});
		}
		labels.setMaximumSize(labels.getPreferredSize());
		add(labels);
		
		for(BalanceData d: data){
			BalancerRow row = new BalancerRow(d, this);
			rows.add(row);
			this.add(row);
		}
		
		add(Box.createVerticalGlue());
		
		Box lev = Box.createHorizontalBox();
		lev.add(Box.createHorizontalGlue());
		lev.add(new JLabel("Level: "));
		JSpinner spin = new JSpinner(new SpinnerNumberModel(1,1,20,1));
		spin.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				lv = (Integer)((JSpinner) e.getSource()).getValue();
				refresh();
			}
		});
		spin.setMaximumSize(spin.getPreferredSize());
		lev.add(spin);
		add(lev);
	}
	
	public void refresh(){
		for(BalancerRow row: rows){
			row.refresh();
		}
		repaint();
	}
	
	public int getLevel(){
		return lv;
	}
	
	public String exportString(){
		StringBuilder ans = new StringBuilder();
		for(BalancerRow row: rows){
			ans.append(row.exportString());
		}
		return ans.toString();
	}
}
