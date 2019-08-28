package com.example.menudemocontentresolver.bean;

public class Menu {
    private Integer menuId;
    private String menuName;
    private String type;


    public Menu() {
    }

    public Menu(Integer menuId, String menuName,String type) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.type=type;

    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuId=" + menuId +
                ", menuName='" + menuName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
