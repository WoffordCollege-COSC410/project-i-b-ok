package edu.wofford.wocoin;

public interface UIController {
    public void updateDisplay(AccessController.Result actionResult, AccessController.AccessOptions[] userOptions);
    public void updateDisplay(AccessController.Result actionResult, AccessController.AccessOptions[] userOptions, String[] args);
}
