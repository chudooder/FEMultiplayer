package net.fe.balance;

import java.awt.Dimension;

import javax.swing.*;

public class StatSpinner extends JSpinner {
	public final String name;
	public StatSpinner(String name, int initial, int step){
		super(new SpinnerNumberModel(initial,0,100,step));
		this.name = name;
	}
}
