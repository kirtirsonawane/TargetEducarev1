package com.targeteducare.Adapter;

import com.targeteducare.Classes.Item;

/**
 * Created by bpncool on 2/23/2016.
 */
public class Section {
    // private final String name;
    public boolean isExpanded;
    Item item;

    public Section(Item name) {
        this.item = name;
        isExpanded = true;

    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
/*public String getName() {
        return name;
    }*/
}
