import processing.data.JSONObject;

public class UICommand {

    public int faceId = -1;
    public String text;
    public float resetAfter = -1;

    public static UICommand from(JSONObject json) {
        UICommand c = new UICommand();

        if (json.hasKey("face")) {
            c.faceId = json.getInt("face");
        }

        if (json.hasKey("text")) {
            c.text = json.getString("text");
        }

        if (json.hasKey("reset_after")) {
            c.resetAfter = json.getFloat("reset_after");
        }

        return c;
    }
}
