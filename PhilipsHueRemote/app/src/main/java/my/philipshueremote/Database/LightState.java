package my.philipshueremote.Database;

public abstract class LightState {
    private boolean on;
    private boolean reachable;

    public LightState(boolean on, boolean reachable) {
        this.on = on;
        this.reachable = reachable;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }
}
