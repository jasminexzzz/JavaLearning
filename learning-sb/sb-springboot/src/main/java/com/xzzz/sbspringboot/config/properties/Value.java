package com.xzzz.sbspringboot.config.properties;

/**
 * @author : jasmineXz
 */
public class Value {
    private boolean enabled;
    private Integer id;
    private String name;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Value{" +
                "enabled=" + enabled +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
