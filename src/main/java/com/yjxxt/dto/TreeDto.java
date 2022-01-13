package com.yjxxt.dto;

public class TreeDto {
    private int id;
    private String name;
    private int pId;
    private Boolean checked;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public TreeDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    @Override
    public String toString() {
        return "TreeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pId=" + pId +
                ", checked=" + checked +
                '}';
    }
}
