package com.stefensharkey.cah.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JScrollPane;

public class ScrollPaneLayout extends javax.swing.ScrollPaneLayout
{
    public Dimension preferredLayoutSize(Container parent)
    {
        Dimension dim =  super.preferredLayoutSize(parent);
        JScrollPane pane = (JScrollPane) parent;
        Component comp = pane.getViewport().getView();
        Dimension viewPref = comp.getPreferredSize();
        Dimension port = pane.getViewport().getExtentSize();
        
        if (port.height < viewPref.height)
            dim.width += pane.getVerticalScrollBar().getPreferredSize().width;
        return dim;
    }
}