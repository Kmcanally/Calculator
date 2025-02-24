package edu.jsu.mcis.cs408.calculator;

import java.beans.PropertyChangeEvent;

public interface AbstractView {
    public abstract void modelPropertyChange(final PropertyChangeEvent evt);

}