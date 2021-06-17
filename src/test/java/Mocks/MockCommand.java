package Mocks;

import Commands.utils.Command;

public class MockCommand extends Command {
    /**
     * Generates the Command for a specific Key
     *
     * @param key  the key to run the command
     * @param desc the mock description
     */
    public MockCommand(String key, String desc) {
        super(key, desc);
    }
}
