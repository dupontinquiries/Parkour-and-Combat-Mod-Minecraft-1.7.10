package media.kitchen.kitchenparkour.itemtype.token.capabilities;

public class ScoutCapability implements IParkourCapability {

    protected int num = 0;

    @Override
    public int getValue() {
        return num;
    }

    @Override
    public void setValue(int n) {
        num = n;
    }
}
