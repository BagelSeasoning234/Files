package model;

import com.candle.fileexplorer.model.observer.DataListener;

public class DummyListener implements DataListener
{
    private boolean hasBeenNotified = false;

    public boolean getHasBeenNotified()
    {
        return hasBeenNotified;
    }

    @Override
    public void currentDirectoryChanged()
    {
        hasBeenNotified = true;
    }
}
