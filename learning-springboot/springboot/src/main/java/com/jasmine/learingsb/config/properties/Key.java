package com.jasmine.learingsb.config.properties;

/**
 * @author : jasmineXz
 */
public class Key {
    private boolean enabled;
    private Integer id;

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

    @Override
    public String toString() {
        return "Key{" +
                "enabled=" + enabled +
                ", id=" + id +
                '}';
    }
}
