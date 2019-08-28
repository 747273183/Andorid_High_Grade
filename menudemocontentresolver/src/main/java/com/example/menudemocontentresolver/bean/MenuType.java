package com.example.menudemocontentresolver.bean;

import java.util.List;

public class MenuType {

    private String typeName;
    private List<Menu> menus;

    public MenuType() {
    }

    public MenuType(String typeName, List<Menu> menus) {

        this.typeName = typeName;
        this.menus = menus;
    }



    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "MenuType{" +
                "typeName='" + typeName + '\'' +
                ", menus=" + menus +
                '}';
    }
}
