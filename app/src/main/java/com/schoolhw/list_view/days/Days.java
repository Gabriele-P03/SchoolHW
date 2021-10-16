package com.schoolhw.list_view.days;

public enum Days{

    MONDAY("Mon", 1), TUESDAY("Tue", 2), WEDNESDAY("Wed", 3),
    THURSDAY("Thu", 4), FRIDAY("Fri", 5), SATURDAY("Sat", 6), SUNDAY("Sun", 7);

    int index;
    String name;

    Days(String name, int index){
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
