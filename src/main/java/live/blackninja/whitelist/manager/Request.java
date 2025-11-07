package live.blackninja.whitelist.manager;

public class Request {

    protected final String player;
    protected final long requestTime;

    public Request(String player, long requestTime) {
        this.player = player;
        this.requestTime = requestTime;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public String getPlayer() {
        return player;
    }
}
